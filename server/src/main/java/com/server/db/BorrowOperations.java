package com.server.db;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import java.time.LocalDate;

import com.server.model.Borrow;

public class BorrowOperations {

    public final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("borrowPersistenceUnit");

    public final static EntityManager em = emf.createEntityManager();

    /**
     * Inserts a new borrow into the database.
     *
     * @param borrow The borrow to be inserted.
     * @return true if the borrow was inserted successfully or false if wasn't.
     * @throws SQLIntegrityConstraintViolationException If the ID already exists.
     */
    public static boolean createBorrow(Borrow borrow) throws SQLIntegrityConstraintViolationException {

        em.getTransaction().begin();
        em.persist(borrow);
        em.getTransaction().commit();

        em.close();
        emf.close();
        return true;
    }

    /**
     * Reads a borrow from the database.
     *
     * @param id The ID of the borrow to be read.
     * @return The borrow with the given ID, if it exists. Otherwise, returns null.
     */
    public static Borrow readBorrow(Integer id) {
        Borrow borrow = em.find(Borrow.class, id);

        em.close();
        return borrow;
    }

    /**
     * Updates a borrow in the database. If the borrow doesn't exist, it will return
     * false.
     *
     * @param id            The ID of the borrow to be updated.
     * @param updatedBorrow The borrow with the updated information.
     * @return true if the borrow was updated successfully or false if wasn't.
     */
    public static boolean updateBorrow(Integer id, Borrow updatedBorrow) {
        em.getTransaction().begin();

        Borrow borrow = em.find(Borrow.class, id);

        if (borrow == null) {
            em.close();
            emf.close();
            return false;
        }

        borrow = updatedBorrow;

        if (borrow == null) {
            em.close();
            emf.close();
            return false;
        }

        em.getTransaction().commit();
        em.close();
        emf.close();
        return true;
    }

    /**
     * Deletes a borrow from the database. If the borrow doesn't exist, it will
     * return
     * false.
     *
     * @param id The ID of the borrow to be deleted.
     * @return true if the borrow was deleted successfully or false if wasn't.
     */
    public static boolean deleteBorrow(Integer id) {
        em.getTransaction().begin();

        Borrow borrow = em.find(Borrow.class, id);

        if (borrow == null) {
            em.close();
            emf.close();
            return false;
        }

        em.remove(borrow);
        em.getTransaction().commit();
        em.close();
        emf.close();
        return true;
    }

    public static Borrow findBorrowByIsbn(String isbn) {

        Query query = em.createQuery("SELECT p FROM Borrow p WHERE p.book.isbn = :isbn");
        query.setParameter("isbn", isbn);

        if (query.getResultList().size() > 0) {
            return (Borrow) query.getResultList().get(0);
        }
        return null;
    }

    public static boolean returnBorrow(Borrow borrow) {
        em.getTransaction().begin();

        LocalDate today = LocalDate.now();
        borrow.setReturnDate(today);
        borrow.setReturned(true);
        em.merge(borrow);

        em.getTransaction().commit();
        em.close();
        emf.close();
        return true;
    }

    public static boolean renewalFinalDate(Borrow borrow) {
        em.getTransaction().begin();

        borrow.setFinalDate(borrow.getFinalDate().plusDays(7));
        em.merge(borrow);

        em.getTransaction().commit();
        em.close();
        emf.close();
        return true;
    }
}
