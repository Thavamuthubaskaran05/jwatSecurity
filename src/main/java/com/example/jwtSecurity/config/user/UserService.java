package com.example.jwtSecurity.config.user;

import com.example.jwtSecurity.config.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Lazy
    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    private JwtTokenProvider tokenProvider;




    public boolean verifyUser(String email,String password){
    User user=userRepository.findByEmail(email).orElseThrow();
    return new BCryptPasswordEncoder().matches(password,user.getPassword());
}

    public boolean checkuserNameExists(String email){
    return userRepository.findByEmail(email).isPresent();
}

    public boolean createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
        return true;
    }

    public Optional<User> getIndividualUser(String email){
        return userRepository.findByEmail(email);
    }

    public String generateToken(String email,String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=tokenProvider.generateToken(authentication);
        return token;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(""));
    }
}
