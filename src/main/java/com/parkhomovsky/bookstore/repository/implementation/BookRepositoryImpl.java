package com.parkhomovsky.bookstore.repository.implementation;

import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public BookRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Book save(Book book) {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can`t insert book in database: " + book, e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Book> allBooksQuery = entityManager.createQuery("FROM Book", Book.class);
            return allBooksQuery.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Failed during gathering all books", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            return Optional.ofNullable(entityManager.find(Book.class, id));
        } catch (Exception e) {
            if (entityManager != null) {
                entityManager.close();
            }
            throw new RuntimeException("Error due searching book with id: " + id);
        }
    }
}
