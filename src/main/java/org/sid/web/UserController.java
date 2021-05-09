package org.sid.web;

import lombok.Data;
import org.sid.entities.AppUser;
import org.sid.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
private AccountService accountService;

    @PostMapping("/register")
    public AppUser register(@RequestBody Userform userform){
    return accountService.saveUser(userform.getUsername(),userform.getPassword(),userform.getConfirmedPassword());
}

@Data
 static class  Userform{
        private String username;
        private String password;
        private String ConfirmedPassword;
}
}
