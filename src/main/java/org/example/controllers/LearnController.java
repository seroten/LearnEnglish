package org.example.controllers;

import org.example.repositories.UserProgressRepo;
import org.example.repositories.UserRepo;
import org.example.repositories.WordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/learn")
public class LearnController {
    @Autowired
    private WordRepo wordRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserProgressRepo userProgressRepo;

    @GetMapping("/choose")
    public String choose(@RequestParam(required = false) String libraryId,
                         HttpServletRequest request) {
        System.out.println(libraryId + " - " + request.getRemoteUser());
        return "learn";
    }
}
