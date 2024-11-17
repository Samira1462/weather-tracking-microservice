package com.codechallenge.weathertracking.mapper;

import com.codechallenge.weathertracking.model.UserEntity;

import java.time.LocalDateTime;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserEntity createUserEntity(String username, String postalCode) {
        var entity = new UserEntity();
        entity.setUsername(username);
        entity.setPostalCode(postalCode);
        entity.setRequestTime(LocalDateTime.now());
        return entity;
    }

}
