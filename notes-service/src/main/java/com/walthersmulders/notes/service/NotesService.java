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

package com.walthersmulders.notes.service;

import com.walthersmulders.notes.entity.NotesEntity;
import com.walthersmulders.notes.mapstruct.dto.NotesDto;
import com.walthersmulders.notes.mapstruct.mappers.NotesMapper;
import com.walthersmulders.notes.repository.NotesRepository;
import com.walthersmulders.notes.valueobjects.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotesService {
    private final NotesRepository repository;
    private final NotesMapper     notesMapper;
    private final RestTemplate    restTemplate;

    public NotesDto insertNoteByUserId(Long userId, NotesDto notesDto) {
        notesDto.setDateCreated(LocalDateTime.now());
        notesDto.setDateUpdated(LocalDateTime.now());

        ResponseEntity<UserVO> getUserByUserIdResponse = restTemplate.getForEntity("http://user-service/user/" + userId, UserVO.class);

        if (getUserByUserIdResponse.getStatusCode() == HttpStatus.OK) {
            log.info("User response : OK");
            notesDto.setUserFirstName(Objects.requireNonNull(getUserByUserIdResponse.getBody()).getFirstName());
            notesDto.setUserLastName(Objects.requireNonNull(getUserByUserIdResponse.getBody()).getLastName());
            notesDto.setUserId(userId);

            log.info("Insert New Note");

            return notesMapper.notesEntityToDto(repository.save(notesMapper.notesDtoToEntity(notesDto)));
        } else {
            log.error("Could not retrieve user from user-service : {}", getUserByUserIdResponse.getStatusCode());

            return null;
        }
    }

    public List<NotesDto> getAllNotesByUserId(Long userId) {
        log.info("Get All Notes By User ID : {}", userId);

        return Optional.ofNullable(repository.getAllNotesByUserId(userId))
                       .orElseGet(Collections::emptyList)
                       .stream()
                       .map(notesMapper::notesEntityToDto)
                       .collect(Collectors.toList());
    }

    public NotesDto getNoteByNoteId(Long noteId) {
        log.info("Get Note By Note ID : {}", noteId);

        Optional<NotesEntity> notesEntityOptional = Optional.ofNullable(repository.getNoteByNoteId(noteId));

        return notesEntityOptional.map(notesMapper::notesEntityToDto).orElse(null);
    }

    public NotesDto updateNoteByNoteId(Long noteId, NotesDto notesDto) {
        log.info("Update Note By Note ID");

        int result = repository.updateNoteByNoteId(
                notesDto.getTitle(),
                notesDto.getContent(),
                notesDto.getUserFirstName(),
                notesDto.getUserLastName(),
                notesDto.getUserId(),
                LocalDateTime.now(),
                noteId
        );

        log.info("Update result : {}", result);

        return result == 1 ? notesMapper.notesEntityToDto(repository.getNoteByNoteId(noteId)) : null;
    }

    public void refreshUserDataByUserId(UserVO userVO) {
        log.info("Refresh User Data By User ID : {}", userVO.getUserId());

        int count = repository.countNotesByUserId(userVO.getUserId());
        log.info("Note Count for User ID : {}", count);

        if (count > 0) {
            int result = repository.refreshUserDataByUserId(
                    userVO.getFirstName(),
                    userVO.getLastName(),
                    LocalDateTime.now(),
                    userVO.getUserId()
            );

            log.info("Update result : {}", result);
        }
    }
}
