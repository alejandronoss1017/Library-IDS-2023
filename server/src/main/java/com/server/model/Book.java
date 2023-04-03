package com.server.model;

public class Book {
    private  String ISBN;
    private String name;
    private int amount;

    public Book(String ISBN, String name, int amount) {
        this.ISBN = ISBN;
        this.name = name;
        this.amount = amount;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
