package org.example.repositories;

import org.example.entities.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserProgressRepo extends JpaRepository<UserProgress, Long> {
    @Modifying
    @Transactional
    @Query(value = "update user_progress set id = default, word_id = ?1, user_id = ?2, " +
            "learned = 0, repeated = false, date = default",
            nativeQuery = true)
    void fillProgressTable(int wordId, int userId);

    @Query(value = "select exists(select w.id from word w" +
            "        inner join user_progress up on w.id = up.word_id" +
            "        inner join usr u on up.user_id = u.id" +
            "        where username = ?1)",
            nativeQuery = true)
    boolean isExist(String userName);

    @Modifying
    @Transactional
    @Query(value = "update user_progress up set repeated = ?3, date = default " +
            "where up.word_id = ?1 and up.user_id = ?2",
            nativeQuery = true)
    void save(int wordId, int userId, boolean repeated);

    @Modifying
    @Transactional
    @Query(value = "update user_progress up set repeated = false, date = default" +
            "    from word, usr" +
            "    where word.library_number = ?1 and usr.username = ?2 and up.repeated = true",
            nativeQuery = true)
    void reset(int libraryNumber, String username);
}
