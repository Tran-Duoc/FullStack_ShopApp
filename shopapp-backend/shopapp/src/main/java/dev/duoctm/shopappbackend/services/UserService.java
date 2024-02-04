package dev.duoctm.shopappbackend.services;

import dev.duoctm.shopappbackend.components.JwtTokenUtil;
import dev.duoctm.shopappbackend.dtos.UserDTO;
import dev.duoctm.shopappbackend.exceptions.DataNotFoundException;
import dev.duoctm.shopappbackend.exceptions.PermissionDenyException;
import dev.duoctm.shopappbackend.models.Role;
import dev.duoctm.shopappbackend.models.User;
import dev.duoctm.shopappbackend.repositories.RoleRepository;
import dev.duoctm.shopappbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager  authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("User already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("Role not found"));
        if(role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("You cant register an admin account");
        }

        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        newUser.setRole(role);
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            String password = userDTO.getPassword();
            String encryptedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encryptedPassword);
        }

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {

        Optional<User> existingUser = userRepository.findByPhoneNumber(phoneNumber);
        if(existingUser.isEmpty()) {
            throw new DataNotFoundException("Invalid phoneNumber or password");
        }
        if(existingUser.get().getFacebookAccountId()
                == 0 && existingUser.get().getGoogleAccountId() == 0){
            if(!passwordEncoder.matches(password, existingUser.get().getPassword())){
                throw new BadCredentialsException("wrong phoneNumber or password");
            }
        }
        UsernamePasswordAuthenticationToken  authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password
        );
        authenticationManager.authenticate(authenticationToken);
        return  jwtTokenUtil.generateToken(existingUser.get());
    }
}
