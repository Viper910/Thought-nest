package io.microservices.authentication.serviceTemplates;

import io.microservices.authentication.dto.request.UserCreationRequest;
import io.microservices.authentication.dto.request.UserLoginRequest;
import io.microservices.authentication.model.User;

public interface AuthenticationServiceTemplate {
    public User saveUser(UserCreationRequest userCreationRequest);

    public String loginUser(UserLoginRequest userLoginRequest);

    public User updateUser(String id, UserCreationRequest userCreationRequest);

    public String deleteUser(String id);
}
