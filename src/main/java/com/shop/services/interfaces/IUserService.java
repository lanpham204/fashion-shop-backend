package com.shop.services.interfaces;

import com.shop.dtos.UserDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.response.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse createUser(UserDTO userDTO) throws DataNotFoundException;
    List<UserResponse> getAll();
    UserResponse getUserDetailsFromToken(String token) throws Exception;
    String[] login(String email,String password) throws DataNotFoundException;
    UserResponse updateUser(UserDTO userDTO, int userId) throws DataNotFoundException;
    void deleteUser(int userId);
    long getCountUsers();
}
