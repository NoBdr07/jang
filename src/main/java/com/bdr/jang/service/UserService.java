package com.bdr.jang.service;

import com.bdr.jang.dto.UserDTO;
import com.bdr.jang.model.CreateUserRequest;

public interface UserService {

    UserDTO register(CreateUserRequest req);

}
