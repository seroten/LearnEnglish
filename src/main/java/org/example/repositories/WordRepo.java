package org.example.repositories;

import org.example.entities.Word;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WordRepo extends CrudRepository<Word, Long>, JpaRepository<Word, Long> {
    Word findById(Integer id);

    List<Word> findByLibraryNumber(int number);

    @Query(value = "select * from word w where w.learned = false and w.library_number = ?1 order by w.id asc",
            nativeQuery = true)
    List<Word> findByLearnedIsFalseAndLibraryNumberIs(Integer number);

    @Query(value = "select * from word w where w.learned = false and w.library_number = ?1 order by w.id desc",
            nativeQuery = true)
    List<Word> findByLearnedIsTrueAndLibraryNumberIsSortDesc(int number);

    @Query(value = "select count(*) from word where learned = true and library_number = ?1",
            nativeQuery = true)
    int findByLearnedIsTrueAndLibraryNumberIs(int number);

    @Modifying
    @Transactional
    @Query(value = "update user_progress set learned = 1, repeated = true where word_id = ?1 and user_id = ?2",
            nativeQuery = true)
    void saveResult(int wordId, int userId);

    @Modifying
    @Transactional
    @Query(value = "update word set learned = false where learned = true and library_number = ?1",
            nativeQuery = true)
    void reset(int libraryNumber);
}
