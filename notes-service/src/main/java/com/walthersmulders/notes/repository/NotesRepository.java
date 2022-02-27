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

package com.walthersmulders.notes.repository;

import com.walthersmulders.notes.entity.NotesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<NotesEntity, Long> {
    @Query("SELECT n FROM NotesEntity n WHERE n.id = :noteId")
    NotesEntity getNoteByNoteId(@Param("noteId") Long noteId);

    @Query("SELECT n FROM NotesEntity n WHERE n.userId = :userId")
    List<NotesEntity> getAllNotesByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE NotesEntity n SET n.title = :title, n.content = :content, n.userFirstName = :userFirstName, n.userLastName = :userLastName, n.userId = :userId, n.dateUpdated = :dateUpdated WHERE n.id = :noteId")
    int updateNoteByNoteId(@Param("title") String title, @Param("content") String content, @Param("userFirstName") String userFirstName, @Param("userLastName") String userLastName,
                           @Param("userId") Long userId, @Param("dateUpdated") LocalDateTime dateUpdated, @Param("noteId") Long noteId
    );

    @Transactional
    @Modifying
    @Query("UPDATE NotesEntity n SET n.userFirstName = :userFirstName, n.userLastName = :userLastName, n.dateUpdated = :dateUpdated WHERE n.userId = :userId")
    int refreshUserDataByUserId(@Param("userFirstName") String userFirstName, @Param("userLastName") String userLastName, @Param("dateUpdated") LocalDateTime dateUpdated,
                                @Param("userId") Long userId
    );

    @Query("SELECT COUNT(all n.userId) FROM NotesEntity n WHERE n.userId = :userId")
    int countNotesByUserId(@Param("userId") Long userId);
}
