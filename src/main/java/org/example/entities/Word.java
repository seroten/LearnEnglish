package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private Integer wordsBlock;

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
