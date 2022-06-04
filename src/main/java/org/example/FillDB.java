package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.sql.*;


public class FillDB {
    static Document document;
    static int libraryNumber = 4;
    public static void main(String[] args) throws IOException {
        File input = new File("src/main/resources/" + libraryNumber + ".html");
        document = Jsoup.parse(input, "UTF-8");
        Elements elementsWordTranslate = document.select("div.word-item > p");
        Elements elementsTranscription = document.select("span.transcription-audio");
        Elements elementsImg = document.select("div.instruments > img");
        Elements elementsMp3 = document.select("div.sound-item > a");

        try {
            saveDB(elementsImg, elementsMp3, elementsTranscription, elementsWordTranslate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveDB(Elements... elements) throws SQLException {
        String word;
        String translate;
        String transcription;
        int count = 0;
        int countFor30 = 0;
        int wordsBlock = 1;

        Connection conn = connect();
        Statement statement = null;
        for (int i = 0; i < elements[0].size(); i++) {
            countFor30++;
            if(countFor30 > 30) {
                countFor30 = 1;
                wordsBlock++;
            }
            word = elements[3].get(count++).text().toLowerCase();
            translate = elements[3].get(count++).text().toLowerCase();
            transcription = elements[2].get(i).text();
            if(transcription.contains("\'")) {
                StringBuilder sb = new StringBuilder();
                String[] temp = transcription.split("\'");
                sb.append(temp[0].concat("\'\'").concat(temp[1]));
                transcription = sb.toString();
            }
            if(word.contains("\'")) {
                StringBuilder sb = new StringBuilder();
                String[] temp = word.split("\'");
                sb.append(temp[0].concat("\'\'").concat(temp[1]));
                word = sb.toString();
            }
            statement = conn.createStatement();
            String sql = "insert into word(id, image, library_number, mp3, transcription, translate, word, words_block)" +
                    " values (" + "default" + ", " +
                                "'" + elements[0].get(i).attr("src") + "', " +
                                libraryNumber + ", " +
                                "'" + elements[1].get(i).attr("href") + "', " +
                                "'" + transcription + "', " +
                                "'" + translate + "', " +
                                "'" + word + "', " +
                                wordsBlock + ");";
            System.out.println(countFor30 + " " + sql);
            statement.executeUpdate(sql);

        }
        statement.close();
        conn.close();
    }

    public static Connection connect() {
        String url = "jdbc:postgresql://localhost:5432/translate_db";
        String user = "postgres";
        String password = "4321";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
}