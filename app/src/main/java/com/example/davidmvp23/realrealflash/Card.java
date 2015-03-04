package com.example.davidmvp23.realrealflash;

public class Card{

    private String subject;
    private String question;
    private String answer;
    public Card(String c, String q, String a) {
        this.subject = c;
        this.question = q;
        this.answer = a;

    }
    public String toString() {
        return this.subject + " " + this.question + " " + this.answer;
    }
    public String getCategory() {
        return this.subject;
    }
    public String getQuestion() {
        return this.question;
    }
    public String getAnswer() {
        return this.answer;
    }
}
