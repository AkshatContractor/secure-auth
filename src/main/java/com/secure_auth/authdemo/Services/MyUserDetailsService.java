package com.secure_auth.authdemo.Services;

import com.secure_auth.authdemo.Models.User;
import com.secure_auth.authdemo.Models.UserPrincipal;
import com.secure_auth.authdemo.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.getUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("user not found 404");
        }
        System.out.println(user);
        return new UserPrincipal(user);
    }
}
