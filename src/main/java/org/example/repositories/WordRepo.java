package org.example.repositories;

import org.example.entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordRepo extends JpaRepository<Word, Long> {

    List<Word> findByLibraryNumber(int number);

    @Query(value = "select w.id, w.word, w.translate, w.library_number, w.words_block," +
            " w.image, w.mp3, w.transcription from word w" +
            " left join (select up.word_id, up.repeated, up.learned from user_progress up" +
            " inner join usr u on up.user_id = u.id" +
            " inner join word w on w.id = up.word_id" +
            " where up.repeated = true and u.username = ?2) nt on nt.word_id = w.id" +
            " where w.library_number = ?1 and (nt.repeated = false or nt.repeated is null)" +
            " order by w.id asc",
            nativeQuery = true)
    List<Word> findByRepeatedIsFalseAndLibraryNumberIs(int libraryId, String username);

    @Query(value = "select w.id, w.word, w.translate, w.library_number, w.words_block," +
            " w.image, w.mp3, w.transcription from word w" +
            " left join (select up.word_id, up.repeated, up.learned from user_progress up" +
            " inner join usr u on up.user_id = u.id" +
            " inner join word w on w.id = up.word_id" +
            " where up.repeated = true and u.username = ?2) nt on nt.word_id = w.id" +
            " where w.library_number = ?1 and (nt.repeated = false or nt.repeated is null)" +
            " order by w.id desc",
            nativeQuery = true)
    List<Word> findByRepeatedIsFalseAndLibraryNumberIsSortDesc(int libraryId, String username);

    @Query(value = "select count(*) from word w" +
            " inner join user_progress up on w.id = up.word_id" +
            " inner join usr u on up.user_id = u.id" +
            " where w.library_number = ?1 and up.repeated = true and u.username = ?2",
            nativeQuery = true)
    int findCountByRepeatedIsTrueAndLibraryNumberIs(int libraryId, String username);

    @Query(value = "select count(*) from word w" +
            " inner join user_progress up on w.id = up.word_id" +
            " inner join usr u on up.user_id = u.id" +
            " where w.library_number = ?1 and up.learned = 4 and u.username = ?2",
            nativeQuery = true)
    int findCountByLearnedAndLibraryNumberIs(int libraryId, String username);

    @Query(value = "select count(*) from word w" +
            " inner join user_progress up on w.id = up.word_id" +
            " inner join usr u on up.user_id = u.id" +
            " where w.library_number = ?1 and w.words_block = ?2 and up.learned = 4 and u.username = ?3",
            nativeQuery = true)
    int findCountByLearnedAndWordsBlockIsAndLibraryNumberIs(int libraryId, int wordsBlock, String username);

    @Query(value = "select w.id, w.word, w.translate, w.library_number, w.words_block," +
            " w.image, w.mp3, w.transcription from word w" +
            " left join (select up.word_id, up.repeated, up.learned from user_progress up" +
            " inner join usr u on up.user_id = u.id" +
            " inner join word w on w.id = up.word_id" +
            " where u.username = ?2) nt on nt.word_id = w.id" + //4 - final status of word`s learning
            " where w.library_number = ?1 and w.words_block = ?3 and (nt.learned != 4 or nt.learned is null)" +
            " order by random()" +
            "limit 10",
            nativeQuery = true)
    List<Word> findByLibraryNumberAndNotLearnedAndWordsBlockIsAndShuffle(int libraryId, String username, int wordsBlock);

    @Query(value = "select min(w.words_block) from word w" +
            " left join user_progress up on w.id = up.word_id" +
            " left join usr u on up.user_id = u.id" +
            " where w.library_number = ?1 and (u.username = ?2 or u.username is null)" +
            " and (up.learned != 4 or up.learned is null)",
            nativeQuery = true)
    int findFirstUnlearnedWordsBlockNumber(int libraryId, String username);
}
