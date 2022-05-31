package org.example.repositories;

import org.example.entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WordRepo extends JpaRepository<Word, Long> {
    Word findById(Integer id);

    List<Word> findByLibraryNumber(int number);

    @Query(value = "select w.id, w.word, w.translate, w.library_number, w.wordsBlock from word w" +
            "    inner join user_progress up on w.id = up.word_id" +
            "    inner join usr u on up.user_id = u.id" +
            "    where w.id = ?1 and up.repeated = false and username = ?2" +
            "    order by w.id asc",
            nativeQuery = true)
    List<Word> findByLearnedIsFalseAndLibraryNumberIs(int libraryId, String username);

//    @Query(value = "select * from word w where w.learned = false and w.library_number = ?1 order by w.id desc",
//            nativeQuery = true)
//    List<Word> findByLearnedIsTrueAndLibraryNumberIsSortDesc(int number);

    @Query(value = "select w.id, w.word, w.translate, w.library_number, w.wordsBlock from word w" +
            "    inner join user_progress up on w.id = up.word_id" +
            "    inner join usr u on up.user_id = u.id" +
            "    where w.id = ?1 and up.repeated = false and username = ?2" +
            "    order by w.id desc",
            nativeQuery = true)
    List<Word> findByRepeatedIsFalseAndLibraryNumberIsSortDesc(int libraryId, String username);

    @Query(value = "select count(*) from word w" +
            "    inner join user_progress up on w.id = up.word_id" +
            "    inner join usr u on up.user_id = u.id" +
            "    where w.library_number = ?1 and up.repeated = true and username = ?2",
            nativeQuery = true)
    int findByRepeatedIsTrueAndLibraryNumberIs(int libraryId, String username);

//    @Modifying
//    @Transactional
//    @Query(value = "update word set learned = false where learned = true and library_number = ?1",
//            nativeQuery = true)
//    void reset(int libraryNumber);
}
