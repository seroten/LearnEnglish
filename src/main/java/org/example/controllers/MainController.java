package org.example.controllers;

import org.example.entities.Word;
import org.example.repositories.UserProgressRepo;
import org.example.repositories.UserRepo;
import org.example.repositories.WordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private WordRepo wordRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserProgressRepo userProgressRepo;
    private List<Word> words;
    private int libraryId;
    private int wordNumber = 0;

    @GetMapping("/")
    public String main(Model model) {
        return "main";
    }

    @GetMapping("/exercises")
    public String learn(@RequestParam(name = "wordsNumber", required = false) String wordsNumber,
                        Model model, HttpServletRequest request) {
        if (wordsNumber != null && !wordsNumber.equals("")) {
            if (Integer.parseInt(wordsNumber) >= 0) {
                wordNumber = Integer.parseInt(wordsNumber);
            }
        }
        String username = request.getRemoteUser();
        model.addAttribute("word", words.get(wordNumber));
        model.addAttribute("libraryName", words.get(0).getLibraryName(libraryId));
        model.addAttribute("total", wordRepo.findByLibraryNumber(libraryId).size());
        model.addAttribute("repeated", wordRepo.findByRepeatedIsTrueAndLibraryNumberIs(libraryId, username));
        model.addAttribute("wordNumber", wordNumber++);
        if (wordNumber >= words.size() - 1 || wordNumber < 0) {
            wordNumber = 0;
        }
        return "exercises";
    }

    @GetMapping("/exercises/{id}")
    public String selectLibrary(@PathVariable String id,
                                @RequestParam String format,
                                @RequestParam String direction,
                                HttpServletRequest request) {
        if (words != null && words.size() > 0) {
            words.clear();
            wordNumber = 0;
        }
        libraryId = Integer.parseInt(id);
        String username = request.getRemoteUser();
        if (direction.equals("direct")) {
            words = wordRepo.findByRepeatedIsFalseAndLibraryNumberIs(libraryId, username);
        } else if (direction.equals("reverse")) {
            words = wordRepo.findByRepeatedIsFalseAndLibraryNumberIsSortDesc(libraryId, username);
        } else {
            words = wordRepo.findByRepeatedIsFalseAndLibraryNumberIs(libraryId, username);
            Collections.shuffle(words);
        }
        return "redirect:/exercises";
    }

    @GetMapping("/option/{id}")
    public String option(@PathVariable String id, Model model) {
        model.addAttribute("libraryId", id);
        return "option";
    }

    @GetMapping("/repeated/{wordId}")
    public String saveRepeated(@PathVariable String wordId, HttpServletRequest request) {
        int userId = userRepo.findByUsername(request.getRemoteUser()).getId().intValue();
        int wordIdInt = Integer.parseInt(wordId);
        if(userProgressRepo.isExist(wordIdInt, userId)) {
            userProgressRepo.save(wordIdInt, userId, true);
        } else {
            userProgressRepo.insert(wordIdInt, userId);
        }
        return "redirect:/exercises";
    }

    @GetMapping("/unrepeated/{wordId}")
    public String saveUnlearned(@PathVariable String wordId,
                                @RequestParam(name = "wordsNumber", required = false) String wordsNumber,
                                HttpServletRequest request) {
        int userId = userRepo.findByUsername(request.getRemoteUser()).getId().intValue();
        int wordIdInt = Integer.parseInt(wordId);
        userProgressRepo.save(wordIdInt, userId, false);
        wordNumber = Integer.parseInt(wordsNumber) + 1;
        return "redirect:/exercises";
    }

    @GetMapping("/option2")
    public String resetForm() {
        return "reset";
    }

    @GetMapping("/reset")
    public String reset(@RequestParam String libraryNumber, HttpServletRequest request) {
        String username = request.getRemoteUser();
        userProgressRepo.reset(Integer.parseInt(libraryNumber), username);
        return "redirect:/";
    }
}