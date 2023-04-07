package com.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Borrow> borrows;

    // Default constructor is required by JPA, so it must be declared
    public Book() {
    }

    public Book(String isbn, String name, Integer amount) {
        this.isbn = isbn;
        this.name = name;
        this.amount = amount;
        this.borrows = new ArrayList<>();
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
