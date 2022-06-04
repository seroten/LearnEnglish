package org.example.repositories;

import org.example.entities.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserProgressRepo extends JpaRepository<UserProgress, Long> {

    @Modifying
    @Transactional
    @Query(value = "update user_progress up set repeated = ?3, learned = ?4, date = default" +
            " where up.word_id = ?1 and up.user_id = ?2",
            nativeQuery = true)
    void save(int wordId, int userId, boolean repeated, int learned);

    @Modifying
    @Transactional
    @Query(value = "insert into user_progress(word_id, user_id, date, repeated, learned)" +
            "values (?1, ?2, default, ?3, ?4)",
            nativeQuery = true)
    void insert(int wordId, int userId, boolean repeated, int learned);

    @Modifying
    @Transactional
    @Query(value = "update user_progress up set repeated = false, date = default" +
            "    from word, usr" +
            "    where word.library_number = ?1 and usr.username = ?2 and up.repeated = true",
            nativeQuery = true)
    void reset(int libraryNumber, String username);

    @Query(value = "select exists(select w.id from word w" +
            "        inner join user_progress up on w.id = up.word_id" +
            "        inner join usr u on up.user_id = u.id" +
            "        where w.id = ?1 and u.id = ?2)",
            nativeQuery = true)
    boolean isExist(int wordId, int userId);

    @Query(value = "select up.learned from user_progress up" +
            "        inner join word w on up.word_id = w.id" +
            "        inner join usr u on up.user_id = u.id" +
            "        where w.id = ?1 and u.id = ?2",
            nativeQuery = true)
    Integer getLearnedField(int wordId, int userId);

    @Query(value = "select up.repeated from user_progress up" +
            "        inner join word w on up.word_id = w.id" +
            "        inner join usr u on up.user_id = u.id" +
            "        where w.id = ?1 and u.id = ?2",
            nativeQuery = true)
    Boolean getRepeatedField(int wordId, int userId);
}
