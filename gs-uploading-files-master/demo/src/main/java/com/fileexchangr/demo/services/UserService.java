package com.fileexchangr.demo.services;


import com.fileexchangr.demo.configs.StringConstants;
import com.fileexchangr.demo.entitys.Role;
import com.fileexchangr.demo.entitys.User;
import com.fileexchangr.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public void setLimit(User user){
        Set<Role> roles = user.getRoles();
        if(roles.contains(Role.REGULAR_USER)){
            user.setDownloadLimit(100000L);
        }else if(roles.contains(Role.PREMIUM_USER)){
            user.setDownloadLimit(10000000L);
        }
    }

    public Boolean addUser(User user){
        User user1 = userRepository.findByUsername(user.getUsername());

        if (user1 != null){
            return false;
        }
        user.setRoles(Collections.singleton(Role.REGULAR_USER));
        this.setLimit(user);
        user.setBlocked(false);
        userRepository.saveAndFlush(user);
        return true;
    }

    public void saveUser(User user, String username, Map<String, String> form){
        user.setUsername((username));
        this.setLimit(user);
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();
        for(String key : form.keySet()){
            if(roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public void justSave(User user){
        userRepository.saveAndFlush(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);

        if (user == null){
            System.out.println(StringConstants.exception);
            throw new UsernameNotFoundException(StringConstants.userNotFound);
        }
        return user;
    }
}

