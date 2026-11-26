package vn.iotstar.service;

import vn.iotstar.dto.LoginRequest;
import vn.iotstar.dto.RegisterRequest;
import vn.iotstar.dto.UserDto;

public interface UserService {

    UserDto register(RegisterRequest request);

    UserDto authenticate(LoginRequest request);

    long count();
}
