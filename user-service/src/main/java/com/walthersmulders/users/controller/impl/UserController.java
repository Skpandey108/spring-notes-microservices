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

package com.walthersmulders.users.controller.impl;

import com.walthersmulders.users.controller.UserOperations;
import com.walthersmulders.users.mapstruct.dto.UserDto;
import com.walthersmulders.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
@Slf4j
@RequiredArgsConstructor
public class UserController implements UserOperations {
    private final UserService userService;

    @Override
    public ResponseEntity<UserDto> insertUser(UserDto userDto) {
        log.info("UserController | insertUser method");
        UserDto userObj = userService.insertUser(userDto);

        return userObj != null ? new ResponseEntity<>(userObj, HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<UserDto> updateUserByUserId(Long userId, UserDto userDto) {
        log.info("UserController | updateUserByUserId method");

        UserDto userObj = userService.updateUserByUserId(userId, userDto);

        return userObj != null ? new ResponseEntity<>(userObj, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<UserDto> getUserByUserId(Long userId) {
        log.info("UserController | getUserByUserId method");

        UserDto userObj = userService.getUserByUserId(userId);

        return userObj != null ? new ResponseEntity<>(userObj, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
