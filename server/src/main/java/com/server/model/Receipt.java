package com.server.model;

import java.time.LocalDateTime;
public class Receipt {
    private int reference;
    private String ISBN;
    private LocalDateTime deliveryDate;

    public Receipt(int reference, String ISBN, LocalDateTime deliveryDate) {
        this.reference = reference;
        this.ISBN = ISBN;
        this.deliveryDate = deliveryDate;
    }

    // Getters and setters for each attribute
    public int getReference() {
        return reference;
    }

    public void setReference(int reference) {
        this.reference = reference;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
