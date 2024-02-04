package dev.duoctm.shopappbackend.services;

import dev.duoctm.shopappbackend.dtos.UserDTO;
import dev.duoctm.shopappbackend.exceptions.DataNotFoundException;
import dev.duoctm.shopappbackend.models.User;
import org.springframework.stereotype.Service;



public interface IUserService  {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password) throws Exception;
}
