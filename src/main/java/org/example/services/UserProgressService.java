package org.example.services;

import org.example.entities.Word;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserProgressService {

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
