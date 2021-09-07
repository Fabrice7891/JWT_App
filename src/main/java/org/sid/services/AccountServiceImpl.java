package org.sid.services;

import org.sid.dao.AppRoleRepository;
import org.sid.dao.AppUserRepository;
import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {


    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public AppUser saveUser(String username, String password, String confirmedPassword) {
        AppUser user= appUserRepository.findByUsername(username);
        if(user!=null) throw  new RuntimeException("User already exist");
        if(!password.equals(confirmedPassword)) throw new  RuntimeException("Please confirm your password");
        AppUser appUser=new AppUser();
        appUser.setUsername(username);
        appUser.setActived(true);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        appUserRepository.save(appUser);
        addRoleToUser(username, "USER");
        return appUser;
    }

    @Override
    public AppRole save(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {

        return appUserRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
       AppUser appUser=appUserRepository.findByUsername(username);
       AppRole appRole=appRoleRepository.findByRoleName(rolename);
       appUser.getRoles().add(appRole);
    }

    @Override
    public Collection<AppRole> getAllRolleByuser(String username) {
        AppUser appUser= loadUserByUsername(username);
        if(appUser==null) throw  new RuntimeException("Utilisateur introuvable");
        return appUser.getRoles();
    }


}
