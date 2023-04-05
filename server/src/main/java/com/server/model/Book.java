package com.server.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @Column(name = "isbn_book")
    private String isbn;

    @Column(name = "book_name")
    private String name;

    @Column(name = "book_amount")
    private Integer amount;

    public Book(String isbn, String name, Integer amount) {
        this.isbn = isbn;
        this.name = name;
        this.amount = amount;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
