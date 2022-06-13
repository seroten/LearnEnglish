package org.example.controllers;

import org.example.entities.Word;
import org.example.repositories.UserProgressRepo;
import org.example.repositories.UserRepo;
import org.example.repositories.WordRepo;
import org.example.services.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/learn")
public class LearnController {
    @Autowired
    private WordRepo wordRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserProgressRepo userProgressRepo;
    @Autowired
    private UserProgressService userProgressService;
    private List<Word> words;
    private int libraryId;
    private int wordNumber = 0;
    private List<Word> wordForLearnAndTwoShuffleWords;
    private Integer wordLearnedField;
    private Boolean wordRepeatedField;
    private Integer score;
    private Integer total;

    /*
        Word`s status
    0 or null - choose from three words
    1 - spell
    2 - choose from three words by ear
    3 - write a word
    4 - word is learned
     */

    @GetMapping("/")
    public String learn(@RequestParam(required = false) String wordsNumber,
                        Model model, HttpServletRequest request) {
        String username = request.getRemoteUser();
        int countLearnedWordsInWordsBlock = wordRepo
                .findCountByLearnedAndWordsBlockIsAndLibraryNumberIs(libraryId, words.get(wordNumber).getWordsBlock(), username);
        score = wordRepo.findCountByLearnedAndLibraryNumberIs(libraryId, username);
        total = wordRepo.findByLibraryNumber(libraryId).size();
        if (total == score) {
            model.addAttribute("libraryIsLearned", "Library is learned");
            return "learn";
        }
        if (countLearnedWordsInWordsBlock == 30) {
            model.addAttribute("wordsBlockIsLearned", "Block is learned");
            model.addAttribute("wordsBlock", words.get(0).getWordsBlock());
            model.addAttribute("libraryId", libraryId);
            return "learn";
        }
        if (wordsNumber != null && !wordsNumber.equals("")) {
            int wordsNumberInt = Integer.parseInt(wordsNumber);
            if (wordsNumberInt >= 0) {
                wordNumber = wordsNumberInt;
            }
        }
        wordForLearnAndTwoShuffleWords = userProgressService.getThreeWords(words.get(wordNumber), words);
        wordLearnedField = userProgressRepo.getLearnedField(
                words.get(wordNumber).getId(), userRepo.findByUsername(username).getId().intValue());
        wordRepeatedField = userProgressRepo
                .getRepeatedField(words.get(wordNumber).getId(), userRepo.findByUsername(username).getId().intValue());
        model.addAttribute("word", words.get(wordNumber));
        model.addAttribute("learnedField", wordLearnedField == null ? 0 : wordLearnedField);
        model.addAttribute("repeatedField", wordRepeatedField == null ? false : wordRepeatedField);
        model.addAttribute("threeWords", wordForLearnAndTwoShuffleWords);
        model.addAttribute("score", score);
        model.addAttribute("total", total);
        model.addAttribute("libraryName", words.get(0).getLibraryName(libraryId));
        model.addAttribute("wordNumber", wordNumber++);
        model.addAttribute("countWord", "Learned");
        model.addAttribute("countLearnedWordsInWordsBlock", countLearnedWordsInWordsBlock);
        model.addAttribute("remoteUser", username.toUpperCase().substring(0, 1));
        if (wordNumber >= words.size() - 1 || wordNumber < 0) {
            wordNumber = 0;
        }
        return "learn";
    }

    @GetMapping("/{id}")
    public String selectLibrary(@PathVariable String id,
                                HttpServletRequest request) {
        if (words != null && words.size() > 0) {
            words.clear();
            wordNumber = 0;
        }
        libraryId = Integer.parseInt(id);
        String username = request.getRemoteUser();
        int wordsBlock = wordRepo.findFirstUnlearnedWordsBlockNumber(libraryId, username);
        words = wordRepo.findByLibraryNumberAndNotLearnedAndWordsBlockIsAndShuffle(libraryId, username, wordsBlock);
        return "redirect:/learn/";
    }

    @GetMapping("/save/{wordId}")
    public String saveLearned(@PathVariable String wordId,
                              @RequestParam String learnedField,
                              @RequestParam Boolean repeatedField,
                              @RequestParam String wrongChoice,
                              HttpServletRequest request) {
        int userId = userRepo.findByUsername(request.getRemoteUser()).getId().intValue();
        int wordIdInt = Integer.parseInt(wordId);
        int learnedFieldInt = Integer.parseInt(learnedField);
        if (!wrongChoice.equals("wrongChoice")) {
            if (userProgressRepo.isExist(wordIdInt, userId)) {
                userProgressRepo.save(wordIdInt, userId, repeatedField, learnedFieldInt);
            } else {
                userProgressRepo.insert(wordIdInt, userId, repeatedField, learnedFieldInt);
            }
        }
        return "redirect:/learn/";
    }
}
