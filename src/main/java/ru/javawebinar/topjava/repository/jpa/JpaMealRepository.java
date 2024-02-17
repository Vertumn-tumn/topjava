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

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User user = entityManager.find(User.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("User with id " + userId + " not found");
            }
            meal.setUser(user);
            entityManager.persist(meal);
            return meal;
        } else {
            User user = entityManager.find(User.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("User with id " + userId + " not found");
            }
            meal.setUser(user);
            return entityManager.merge(meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return entityManager.createNamedQuery(Meal.DELETE, Meal.class)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return entityManager.createNamedQuery(Meal.GET_BY_ID_AND_USER_ID, Meal.class)
                .setParameter("id", id)
                .setParameter("User_id", userId)
                .getSingleResult();
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createNamedQuery(Meal.GET_ALL_MEAL, Meal.class)
                .setParameter("user_id", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return entityManager.createNamedQuery(Meal.GET_IN_INTERVAL, Meal.class)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .setParameter("userId", userId)
                .getResultList();
    }
}