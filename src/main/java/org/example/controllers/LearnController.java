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
    private int wordsCount;
    private int wordNumber = 0;
    private List<Word> wordForLearnAndTwoRandomWords;
    private Integer countWordLearnedField;
    private Boolean countWordRepeatedField;
    private Integer score;
    private Integer total;
    private String username;
    private int countLearnedWordsInWordsBlock;
    private int wordsBlock;

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
        username = request.getRemoteUser();
        score = wordRepo.findCountByLearnedAndLibraryNumberIs(libraryId, username);
        total = wordRepo.findByLibraryNumber(libraryId).size();
        countLearnedWordsInWordsBlock = wordRepo
                .findCountByLearnedAndWordsBlockIsAndLibraryNumberIs(libraryId, words.get(0).getWordsBlock(), username);
        if (wordsNumber != null && !wordsNumber.equals("")) {
            int wordsNumberInt = Integer.parseInt(wordsNumber);
            if (wordsNumberInt >= 0) {
                wordNumber = wordsNumberInt;
            }
        }
        if (total == score) {
            model.addAttribute("libraryIsLearned");
            model.addAttribute("libraryName", words.get(0).getLibraryName(libraryId));
            return "learn";
        }
//        System.out.println("countLearnedWordsInWordsBlock " + countLearnedWordsInWordsBlock);
        if (countLearnedWordsInWordsBlock == 30) {
            model.addAttribute("wordsBlockIsLearned", "wordsBlockIsLearned");
            model.addAttribute("wordsBlock", words.get(0).getWordsBlock());
            model.addAttribute("libraryId", libraryId);
            return "learn";
        }
        if (wordNumber > words.size() - 1 || wordNumber < 0) {
            wordNumber = 0;
            System.out.println("wordNumber > words.size() - 1");
            return "redirect:/learn/" + libraryId;
        }
        wordForLearnAndTwoRandomWords = userProgressService.getThreeWords(words.get(wordNumber),
                wordRepo.findByLibraryNumberAndWordsBlock(libraryId, wordsBlock));
        countWordLearnedField = userProgressRepo.getLearnedField(
                words.get(wordNumber).getId(), userRepo.findByUsername(username).getId().intValue());
        countWordRepeatedField = userProgressRepo
                .getRepeatedField(words.get(wordNumber).getId(), userRepo.findByUsername(username).getId().intValue());
        model.addAttribute("word", words.get(wordNumber));
        model.addAttribute("learnedField", countWordLearnedField == null ? 0 : countWordLearnedField);
        model.addAttribute("repeatedField", countWordRepeatedField == null ? false : countWordRepeatedField);
        model.addAttribute("threeWords", wordForLearnAndTwoRandomWords);
        model.addAttribute("score", score);
        model.addAttribute("total", total);
        model.addAttribute("libraryName", words.get(0).getLibraryName(libraryId));
        model.addAttribute("wordNumber", wordNumber);
        model.addAttribute("countWord", "Learned");
        model.addAttribute("countLearnedWordsInWordsBlock", countLearnedWordsInWordsBlock);
        model.addAttribute("remoteUser", username.toUpperCase().substring(0, 1));

//        System.out.println("wordNumber is " + wordNumber + " " + words.get(wordNumber).getWord());
        wordNumber++;
        return "learn";
    }

    @GetMapping("/{id}/{count}")
    public String selectLibrary(@PathVariable String id,
                                @PathVariable String count,
                                HttpServletRequest request) {
        if (words != null && words.size() > 0) {
            words.clear();
            wordNumber = 0;
        }
        libraryId = Integer.parseInt(id);
        wordsCount = Integer.parseInt(count);
        String username = request.getRemoteUser();
//        wordsBlock = wordRepo.findFirstUnlearnedWordsBlockNumber(libraryId, username);
//        words = wordRepo.findByLibraryNumberAndNotLearnedAndWordsBlockIsAndShuffle(libraryId, username, wordsBlock);
        words = wordRepo.findByLibraryNumberAndNotLearnedAndWordsCountAndShuffle(libraryId, username, wordsCount);
        return "redirect:/learn/";
    }

    @GetMapping("/save/{wordId}")
    public String saveLearned(@PathVariable String wordId,
                              @RequestParam String learnedField,
                              @RequestParam Boolean repeatedField,
                              @RequestParam String choiceStatus,
                              @RequestParam String checkedInput,
                              HttpServletRequest request) {
        int userId = userRepo.findByUsername(request.getRemoteUser()).getId().intValue();
        int wordIdInt = Integer.parseInt(wordId);
        int learnedFieldInt = Integer.parseInt(learnedField);
        if ((choiceStatus.equals("rightChoice") || checkedInput.equals("rightChoice")) ||
                (choiceStatus.equals("choice") & checkedInput.equals("choice"))) {
            if (userProgressRepo.isExist(wordIdInt, userId)) {
                userProgressRepo.update(wordIdInt, userId, repeatedField, learnedFieldInt);
            } else {
                userProgressRepo.create(wordIdInt, userId, repeatedField, learnedFieldInt);
            }
        }
        return "redirect:/learn/";
    }
}
//TODO reset progress method
