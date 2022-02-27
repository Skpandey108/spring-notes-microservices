/*
 * MIT License
 *
 * Copyright (c) 2022 Walther Smulders
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.walthersmulders.users.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.walthersmulders.users.entity.UserEntity;
import com.walthersmulders.users.mapstruct.dto.UserDto;
import com.walthersmulders.users.mapstruct.mappers.UserMapper;
import com.walthersmulders.users.producer.UserUpdateProducer;
import com.walthersmulders.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository     userRepository;
    private final UserMapper         userMapper;
    private final UserUpdateProducer userUpdateProducer;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public UserDto insertUser(UserDto userDto) {
        userDto.setDateCreated(LocalDateTime.now());
        userDto.setDateUpdated(LocalDateTime.now());

        log.info("Insert new User");

        return userMapper.userEntityToDto(userRepository.save(userMapper.userDtoToEntity(userDto)));
    }

    public UserDto updateUserByUserId(Long userId, UserDto userDto) {
        log.info("Update User By User ID : {}", userId);

        int result = userRepository.updateUserByUserId(
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmailAddress(),
                LocalDateTime.now(),
                userId
        );

        log.info("Update result : {}", result);

        if (result == 1) {
            UserDto userDtoUpdated = userMapper.userEntityToDto(userRepository.getUserByUserId(userId));

            try {
                log.info("Sending message to queue");
                userUpdateProducer.produceMessage(objectMapper.writeValueAsString(userDtoUpdated));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                return null;
            }

            return userDtoUpdated;
        } else {
            return null;
        }
    }

    public UserDto getUserByUserId(Long userId) {
        log.info("Get User By User ID : {}", userId);

        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userRepository.getUserByUserId(userId));

        return userEntityOptional.map(userMapper::userEntityToDto).orElse(null);
    }
}
