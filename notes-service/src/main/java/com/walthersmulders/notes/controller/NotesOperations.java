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

package com.walthersmulders.notes.controller;

import com.walthersmulders.notes.mapstruct.dto.NotesDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/notes/")
public interface NotesOperations {
    @PostMapping(value = "user/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<NotesDto> insertNoteByUserId(@PathVariable(name = "userId") Long userId, @RequestBody NotesDto notesDto);

    @PutMapping(value = "{noteId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<NotesDto> updateNoteByNoteId(@PathVariable(name = "noteId") Long noteId, @RequestBody NotesDto notesDto);

    @GetMapping(value = "{noteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<NotesDto> getNoteByNoteId(@PathVariable(name = "noteId") Long noteId);

    @GetMapping(value = "user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<NotesDto>> getAllNotesByUserId(@PathVariable(name = "userId") Long userId);
}
