package org.example.services;

import org.example.entities.Word;
import org.example.repositories.WordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserProgressService {
    @Autowired
    WordRepo wordRepo;
    private final int countWordsBlock = 25;

    public int getFirstUnlearnedWordsBlockNumber(int libraryId, String username) {
        int temp;
        for (int i = 1; i <= countWordsBlock; i++) {
            temp = wordRepo.findFirstWordsBlockByUnlearnedAndAndLibraryNumberIs(libraryId, username, i);
            if(temp != 30) {
                return i;
            }
        }
        return -1;
    }

    public List<Word> getThreeWords(Word word, List<Word> words) {
        List<Word> list = new ArrayList<>();
        List<Word> temp = new ArrayList<>(words);
        Collections.shuffle(temp);
        list.add(word);
        for (int i = 0; i < temp.size(); i++) {
            if(!temp.get(i).equals(word)) {
                list.add(temp.get(i));
            }
            if(list.size() == 3) {
                Collections.shuffle(list);
                return list;
            }
        }
        return list;
    }
}
