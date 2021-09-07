package org.sid.services;

import org.sid.entities.AppRole;
import org.sid.entities.AppUser;

import java.util.Collection;
import java.util.List;

public interface AccountService {

    public AppUser saveUser(String username, String password , String confirmedPassword);
    public AppRole save(AppRole role);
    public AppUser loadUserByUsername( String username);
    public void addRoleToUser (String username , String rolename);
    public Collection<AppRole> getAllRolleByuser(String username);
}
