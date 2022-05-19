package org.example.entities;

import javafx.print.Collation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String word;
    private String translate;
    private String transcription;
    private String image;
    private String mp3;
    private Integer libraryNumber;
    private Boolean learned;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Word(String word, String translate, String transcription) {
        this.word = word;
        this.translate = translate;
        this.transcription = transcription;
    }

    public String getLibraryName(int number) {
        switch (number) {
            case 1: return "Beginner";
            case 2: return "Pre-Intermediate";
            case 3: return "Intermediate";
            case 4: return "Irregular verbs";
            default: return "Learn English";
        }
    }
}
