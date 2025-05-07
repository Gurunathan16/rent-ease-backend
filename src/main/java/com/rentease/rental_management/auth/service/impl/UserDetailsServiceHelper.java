package com.rentease.rental_management.auth.service.impl;

import com.rentease.rental_management.auth.entity.UserPrincipal;
import com.rentease.rental_management.auth.entity.Users;
import com.rentease.rental_management.auth.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceHelper implements UserDetailsService
{
    private final UsersRepository usersRepository;

    public UserDetailsServiceHelper(UsersRepository usersRepository)
    {

        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
    {
        Users users = usersRepository.findByUsername(username);

        if(users == null)
            throw new UsernameNotFoundException("User not found!");

        return new UserPrincipal(users);
    }

}
