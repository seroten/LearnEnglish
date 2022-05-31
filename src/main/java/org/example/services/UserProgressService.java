package org.example.services;

import org.example.entities.Word;
import org.example.repositories.UserProgressRepo;
import org.example.repositories.WordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProgressService {
    @Autowired
    private UserProgressRepo userProgressRepo;
    @Autowired
    private WordRepo wordRepo;

    public void fillProgressTable(int userId) {
        List<Word> words = wordRepo.findAll();
        for (Word word : words) {
            userProgressRepo.fillProgressTable(word.getId(), userId);
        }
    }
}
