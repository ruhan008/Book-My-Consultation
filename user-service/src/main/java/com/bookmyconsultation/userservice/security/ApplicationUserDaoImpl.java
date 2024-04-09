package com.bookmyconsultation.userservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ApplicationUserDaoImpl implements ApplicationUserDao{

    @Autowired
    PasswordEncoder encoder;

    @Override
    public ApplicationUser loadUserByUsername(String username) {
        return loadAllUsers()
                .stream()
                .filter(u->u.getUsername().equalsIgnoreCase(username))
                .findFirst().orElseThrow(()-> new UsernameNotFoundException(username));
    }

    private List<ApplicationUser> loadAllUsers(){
        return Arrays.asList(
                ApplicationUser
                        .builder()
                        .username("Elon Musk")
                        .password(encoder.encode("bmc-password-1"))
                        .authorities(ApplicationRole.USER.getAuthorities())
                        .build(),
                ApplicationUser
                        .builder()
                        .username("Mukesh Ambani")
                        .password(encoder.encode("bmc-password-2"))
                        .authorities(ApplicationRole.USER.getAuthorities())
                        .build(),
                ApplicationUser
                        .builder()
                        .username("Dwyane Johnson")
                        .password(encoder.encode("bmc-password-3"))
                        .authorities(ApplicationRole.USER.getAuthorities())
                        .build(),
                ApplicationUser
                        .builder()
                        .username("Syed Sadiq Umar")
                        .password(encoder.encode("bmc-password-4"))
                        .authorities(ApplicationRole.USER.getAuthorities())
                        .build(),
                ApplicationUser
                        .builder()
                        .username("Saumya Verma")
                        .password(encoder.encode("bmc-password-5"))
                        .authorities(ApplicationRole.USER.getAuthorities())
                        .build(),
                ApplicationUser
                        .builder()
                        .username("Sundar Pichai")
                        .password(encoder.encode("bmc-password-6"))
                        .authorities(ApplicationRole.ADMIN.getAuthorities())
                        .build(),
                ApplicationUser
                        .builder()
                        .username("Syed Ruhan")
                        .password(encoder.encode("bmc-password-7"))
                        .authorities(ApplicationRole.ADMIN.getAuthorities())
                        .build()
        );
    }
}
