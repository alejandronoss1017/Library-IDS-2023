package com.server.db;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.server.model.Book;

public class BookOperations {

    public final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("bookPersistenceUnit");

    public final static EntityManager em = emf.createEntityManager();

    /**
     * Inserts a new book into the database.
     *
     * @param book The book to be inserted.
     * @return true if the book was inserted successfully or false if wasn't.
     * @throws SQLIntegrityConstraintViolationException If the ID already exists.
     */
    public static boolean createBook(Book book) throws SQLIntegrityConstraintViolationException {

        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();

        em.close();
        emf.close();
        return true;

    }

    /**
     * Reads a book from the database.
     *
     * @param isbn The ISBN of the book to be read.
     * @return The book with the given ISBN, if it exists. Otherwise, returns null.
     */
    public static Book readBook(String isbn) {
        Book book = em.find(Book.class, isbn);

        em.close();
        return book;
    }

    /**
     * Updates a book in the database. If the book doesn't exist, it will return
     * false.
     *
     * @param isbn        The ISBN of the book to be updated.
     * @param updatedBook The book with the updated information.
     * @return true if the book was updated successfully or false if wasn't.
     */
    public static boolean updateBook(String isbn, Book updatedBook) {
        em.getTransaction().begin();

        Book book = em.find(Book.class, isbn);
        book = updatedBook;

        if (book == null) {
            em.close();
            emf.close();
            return false;
        }

        em.merge(book);

        em.getTransaction().commit();

        em.close();
        emf.close();
        return true;
    }

    /**
     * Deletes a book from the database. If the book doesn't exist, it will return
     * false.
     *
     * @param isbn The ISBN of the book to be deleted.
     * @return true if the book was deleted successfully or false if wasn't.
     */
    public static boolean deleteBook(String isbn) {
        em.getTransaction().begin();

        Book book = em.find(Book.class, isbn);

        if (book == null) {
            em.close();
            emf.close();
            return false;
        }

        em.remove(book);

        em.getTransaction().commit();

        em.close();
        emf.close();
        return true;
    }

    /**
     * Finds a book by its ISBN.
     *
     * @param isbn The ISBN of the book to be found.
     * @return The book with the given ISBN, if it exists. Otherwise, returns null.
     */
    public static Book findByIsbn(String isbn) {
        Book book = em.find(Book.class, isbn);
        return book;

    }
}
