package org.example.controllers;

import org.example.entities.Word;
import org.example.repositories.WordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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

    @GetMapping("/")
    public String main(Model model) {
        word = null;
        libraryName = null;
        model.addAttribute("word", word);
        return "main";
    }

    @GetMapping("/exercises")
    public String learn(Model model) {
        //TODO если список слов закончился
        model.addAttribute("word", words.remove(0));
        model.addAttribute("libraryName", libraryName);
        model.addAttribute("total", wordRepo.findAll().size());
        model.addAttribute("learned", wordRepo.findByLearnedIsTrueAndLibraryNumberIs(libraryNumber));
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
        if(direction.equals("direct")) {
            words = wordRepo.findByLearnedIsFalseAndLibraryNumberIs(libraryId);
        } else if(direction.equals("reverse")) {
            words = wordRepo.findByLearnedIsTrueAndLibraryNumberIsSortDesc(libraryId);
        } else {
            words = wordRepo.findByLearnedIsFalseAndLibraryNumberIs(libraryId);
            Collections.shuffle(words);
        }

        setLibraryName(libraryId);
        return "redirect:/exercises";
    }

    @GetMapping("/option/{id}")
    public String option(@PathVariable String id, Model model) {
        model.addAttribute("libraryId", id);
        return "option";
    }

    @GetMapping("/learned/{wordId}")
    public String save(@PathVariable String wordId) {
        word = wordRepo.findById(Integer.parseInt(wordId));
        word.setLearned(true);
        wordRepo.save(word);
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
