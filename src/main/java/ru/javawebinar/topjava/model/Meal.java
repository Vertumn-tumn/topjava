package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m WHERE m.id=:id and m.user.id=:user_id"),
        @NamedQuery(name = Meal.GET_BY_ID_AND_USER_ID, query = "SELECT m FROM Meal m WHERE m.id = :id AND m.user.id = :user_id"),
        @NamedQuery(name = Meal.GET_ALL_MEAL, query = "SELECT m FROM Meal m WHERE m.user.id = :userId"),
        @NamedQuery(name = Meal.GET_IN_INTERVAL, query = "SELECT m FROM Meal m WHERE m.dateTime >= :startDateTime AND m.dateTime < :endDateTime AND m.user.id = :userId")
})
@Entity
@Table(name = "meal", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "meals_unique_user_datetime_idx")})
public class Meal extends AbstractBaseEntity {
    public static final String DELETE = "Meal.delete";
    public static final String GET_BY_ID_AND_USER_ID = "Meal.getByIdAndUserId";
    public static final String GET_ALL_MEAL = "Meal.getAllMeal";
    public static final String GET_IN_INTERVAL = "Meal.getInInterval";

    @Column(name = "date_time", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private Date dateTime;


    @Column(name = "description")
    @NotBlank
    private String description;

    @Column(name = "calories")
    @NotNull
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Meal() {
    }

    public Meal(@NotNull Date dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, @NotNull Date dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return convertToLocalDateTime(dateTime);
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return convertToLocalDateTime(dateTime).toLocalDate();
    }

    public LocalTime getTime() {
        return convertToLocalDateTime(dateTime).toLocalTime();
    }

    public void setDateTime(@NotNull Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
