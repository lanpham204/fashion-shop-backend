package com.shop.services;

import com.shop.dtos.OrderDetailDTO;
import com.shop.dtos.UserDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.OrderDetail;
import com.shop.models.Role;
import com.shop.models.User;
import com.shop.repositories.RoleRepository;
import com.shop.repositories.UserRepository;
import com.shop.response.UserResponse;
import com.shop.services.interfaces.IUserService;
import com.shop.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    @Override
    public UserResponse createUser(UserDTO userDTO) throws DataNotFoundException {
        Role role = roleRepository.findById(2)
                .orElseThrow(() -> new DataNotFoundException("Cannot found role with id: "+userDTO.getRoleId()));
        modelMapper.typeMap(UserDTO.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setId));
        User user = modelMapper.map(userDTO, User.class);
        user.setActive(true);
        user.setRole(role);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userRepository.save(user);
        return modelMapper.map(user,UserResponse.class);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(user -> modelMapper.map(user,UserResponse.class)).toList();
    }

    @Override
    public UserResponse getUserDetailsFromToken(String token) throws Exception {
        if(jwtTokenUtils.isTokenExpired(token)) {
            throw new Exception("Token is Expired");
        }
        String email = jwtTokenUtils.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Cannot found user"));
        return modelMapper.map(user,UserResponse.class);
    }

    @Override
    public String[] login(String email, String password) throws DataNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Invalid email or password"));
        if(!passwordEncoder.matches(password,user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        UsernamePasswordAuthenticationToken
                authenticationToken = new UsernamePasswordAuthenticationToken(email,password,user.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        String[] result = new String[2];
        result[0] = jwtTokenUtils.generateToken(user);
        return result;
    }

    @Override
    public UserResponse updateUser(UserDTO userDTO, int userId) throws DataNotFoundException {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Cannot found user with id: " + userId));
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found role with id: "+userDTO.getRoleId()));
        if(!userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already exist");
        }
        modelMapper.typeMap(UserDTO.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setId));
        modelMapper.map(userDTO,existingUser);
        existingUser.setRole(role);
        userRepository.save(existingUser);
        return modelMapper.map(existingUser,UserResponse.class);
    }

    @Override
    public void deleteUser(int userId) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Cannot found user with id: " + userId));
        existingUser.setActive(false);
        userRepository.save(existingUser);
    }
}
