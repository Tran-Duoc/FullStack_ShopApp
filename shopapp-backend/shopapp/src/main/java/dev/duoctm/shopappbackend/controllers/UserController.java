package dev.duoctm.shopappbackend.controllers;


import dev.duoctm.shopappbackend.dtos.UserDTO;
import dev.duoctm.shopappbackend.dtos.UserLoginDTO;
import dev.duoctm.shopappbackend.models.User;
import dev.duoctm.shopappbackend.responses.LoginResponse;
import dev.duoctm.shopappbackend.services.IUserService;
import dev.duoctm.shopappbackend.services.UserService;
import dev.duoctm.shopappbackend.utils.LocalizationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final UserService userService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors()){
                List<String> errors = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return  ResponseEntity.badRequest().body(errors);
            }

            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password does not match");

            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

    @PostMapping("/login")
    public  ResponseEntity<LoginResponse> loginUser(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletRequest request
    ){
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
             ;
            return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.builder()
                            .message(localizationUtils.getLocalizeMessage("user.login.login_successfully"))
                            .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LoginResponse.builder()
                    .message(e.getMessage())
                    .build());
        }

    }
}
