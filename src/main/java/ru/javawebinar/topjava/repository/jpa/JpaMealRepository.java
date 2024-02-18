package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User user = entityManager.getReference(User.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("User with id " + userId + " not found");
            }
            meal.setUser(user);
            entityManager.persist(meal);
            return meal;
        } else {
            return get(meal.id(), userId) == (null) ? null : entityManager.merge(meal);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) {
        return entityManager.createQuery("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return entityManager.createQuery("SELECT m from Meal m WHERE m.id=:id AND m.user.id=:userId", Meal.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createQuery("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY dateTime DESC ", Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return entityManager.createQuery("SELECT m FROM Meal m WHERE m.user.id=:userId AND dateTime>=:startDateTime AND dateTime<=:endDateTime ORDER BY dateTime DESC ", Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}