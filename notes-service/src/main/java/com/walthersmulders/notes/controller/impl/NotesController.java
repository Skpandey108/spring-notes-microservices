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

package com.walthersmulders.notes.controller.impl;

import com.walthersmulders.notes.controller.NotesOperations;
import com.walthersmulders.notes.mapstruct.dto.NotesDto;
import com.walthersmulders.notes.service.NotesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notes/")
@Slf4j
@RequiredArgsConstructor
public class NotesController implements NotesOperations {
    private final NotesService notesService;

    @Override
    public ResponseEntity<NotesDto> insertNoteByUserId(Long userId, NotesDto notesDto) {
        log.info("NotesController | insertNote method");
        NotesDto noteObj = notesService.insertNoteByUserId(userId, notesDto);

        return noteObj != null ? new ResponseEntity<>(noteObj, HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<NotesDto> updateNoteByNoteId(Long noteId, NotesDto notesDto) {
        log.info("NotesController | updateNoteByNoteId method");
        NotesDto noteObj = notesService.updateNoteByNoteId(noteId, notesDto);

        return noteObj != null ? new ResponseEntity<>(noteObj, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<NotesDto> getNoteByNoteId(Long noteId) {
        log.info("NotesController | getNoteById method");
        NotesDto notesDto = notesService.getNoteByNoteId(noteId);

        return notesDto != null ? new ResponseEntity<>(notesDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<NotesDto>> getAllNotesByUserId(Long userId) {
        log.info("NotesController | getAllNotesByUserId method");
        List<NotesDto> notesDtoList = notesService.getAllNotesByUserId(userId);

        return notesDtoList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(notesDtoList, HttpStatus.OK);
    }
}
