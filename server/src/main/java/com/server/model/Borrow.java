package com.server.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = "borrow")
public class Borrow implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_borrow")
    private Integer id;

    @Column(name = "borrow_initial_date")
    private LocalDate initialDate;

    @Column(name = "borrow_final_date")
    private LocalDate finalDate;

    @Column(name = "borrow_return", columnDefinition = "TINYINT(1)")
    private boolean returned;

    @Column(name = "borrow_return_date")
    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn_book", nullable = false)
    private Book book;

    // Default constructor is required by JPA, so it must be declared
    public Borrow() {
    }

    public Borrow(LocalDate initialDate, LocalDate finalDate, boolean returned, LocalDate returnDate) {
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.returned = returned;
        this.returnDate = returnDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    public boolean getReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Book getBook() {
    return book;
    }

    public void setBook(Book book) {
    this.book = book;
    }

}
