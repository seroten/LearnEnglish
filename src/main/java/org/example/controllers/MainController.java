package org.example.controllers;

import org.example.entities.Word;
import org.example.repositories.WordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private WordRepo wordRepo;
    private Word word;
    private List<Word> words;
    private String libraryName;
    private int libraryNumber;
    private int wordNumber = 0;

    @GetMapping("/")
    public String main(Model model) {
        word = null;
        libraryName = null;
        model.addAttribute("word", word);
        return "main";
    }

    @GetMapping("/exercises")
    public String learn(@RequestParam(name = "wordsNumber", required = false) String wordsNumber, Model model) {
        System.out.println(wordsNumber);
        if (wordsNumber != null && !wordsNumber.equals("")) {
            if(Integer.parseInt(wordsNumber) >= 0) {
                wordNumber = Integer.parseInt(wordsNumber);
            }
        }
        model.addAttribute("word", words.get(wordNumber));
        model.addAttribute("libraryName", libraryName);
        model.addAttribute("total", wordRepo.findAll().size());
        model.addAttribute("learned", wordRepo.findByLearnedIsTrueAndLibraryNumberIs(libraryNumber));
        model.addAttribute("wordNumber", wordNumber++);
        if (wordNumber >= words.size() - 1 || wordNumber < 0) {
            wordNumber = 0;
        }
        return "exercises";
    }

    @GetMapping("/exercises/{id}")
    public String selectLibrary(@PathVariable String id,
                                @RequestParam String format,
                                @RequestParam String direction) {
        int libraryId = Integer.parseInt(id);
        if (words != null && words.size() > 0) {
            words.clear();
        }
        if (direction.equals("direct")) {
            words = wordRepo.findByLearnedIsFalseAndLibraryNumberIs(libraryId);
        } else if (direction.equals("reverse")) {
            words = wordRepo.findByLearnedIsTrueAndLibraryNumberIsSortDesc(libraryId);
        } else {
            words = wordRepo.findByLearnedIsFalseAndLibraryNumberIs(libraryId);
            Collections.shuffle(words);
        }

        setLibraryName(libraryId);
        return "redirect:/exercises?wordNumber";
    }

    @GetMapping("/option/{id}")
    public String option(@PathVariable String id, Model model) {
        model.addAttribute("libraryId", id);
        return "option";
    }

    @GetMapping("/learned/{wordId}")
    public String saveLearned(@PathVariable String wordId) {
        word = wordRepo.findById(Integer.parseInt(wordId));
        word.setLearned(true);
        wordRepo.save(word);
        return "redirect:/exercises";
    }

    @GetMapping("/unlearned/{wordId}")
    public String saveUnlearned(@PathVariable String wordId,
                                @RequestParam(name = "wordsNumber", required = false) String wordsNumber) {
        word = wordRepo.findById(Integer.parseInt(wordId));
        word.setLearned(false);
        wordRepo.save(word);
        wordNumber = Integer.parseInt(wordsNumber) + 1;
        return "redirect:/exercises";
    }

    private void setLibraryName(int libraryId) {
        //TODO перенести в базу данных
        libraryNumber = libraryId;
        switch (libraryId) {
            case 1:
                libraryName = "Beginner";
                break;
            case 2:
                libraryName = "Pre-Intermediate";
                break;
            case 3:
                libraryName = "Intermediate";
                break;
            case 4:
                libraryName = "Irregular verbs";
        }
    }
}
