package com.ecommerce.user.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ecommerce.user.payloads.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ecommerce.user.dto.UserDTO;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserRole;
import com.ecommerce.user.exception.UserAlreadyExistException;
import com.ecommerce.user.exception.UserNotFoundException;
import com.ecommerce.user.repository.RoleRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommonResponse saveUser(UserDTO userDTO) {
		log.info("Inside userservice save method ");
		Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
		if (userOptional.isPresent()) {
			throw new UserAlreadyExistException("user wih email : " + userDTO.getEmail() + " already exist");
		}
		User user = modelMapper.map(userDTO, User.class);
		final Integer role_id   = user.getRole().getId();
		UserRole userRole = roleRepository.findById(role_id)
				.orElseThrow(() -> new RuntimeException("role not found with id :"+role_id ));
		userRole = roleRepository.save(userRole);
		user.setRole(userRole);
		user = userRepository.save(user);
		log.info("user after getting saved to db {}",user);
		userDTO = modelMapper.map(user, UserDTO.class);
		return CommonResponse.builder()
					.message("User created successfully!")
					.data(userDTO)
					.httpStatus(HttpStatus.CREATED)
					.build();
	}

	@Override
	public CommonResponse getAllUser() {
		List<User> all = userRepository.findAll();
		List<UserDTO> userDTOS = Arrays.asList(modelMapper.map(all, UserDTO[].class));
		return CommonResponse.builder()
						.message("All users")
						.data(userDTOS)
						.httpStatus(HttpStatus.OK)
						.build();
	}

	@Override
	public CommonResponse getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("user with id : " + id + " not found"));
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		return CommonResponse.builder()
					.message("User with id : "+id)
					.data(userDTO)
					.httpStatus(HttpStatus.OK)
					.build();
	}

}
