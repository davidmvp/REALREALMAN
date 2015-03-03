package com.example.davidmvp23.realrealflash;

public class Card{

    private String catergoty;
    private String question;
    private String answer;
    public Card(String c, String q, String a) {
        this.catergoty = c;
        this.question = q;
        this.answer = a;

    }
    public String toString() {
        return this.catergoty + " " + this.question + " " + this.answer;
    }
    public String getCategory() {
        return this.catergoty;
    }
    public String getQuestion() {
        return this.question;
    }
    public String getAnswer() {
        return this.answer;
    }
}
