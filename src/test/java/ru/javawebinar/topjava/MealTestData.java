package ru.javawebinar.topjava;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.UserTestData.*;

public class MealTestData {
    public static final Meal KNOW_IN_BASE = new Meal(1, LocalDateTime.of(2024, 1, 10, 8, 0), "Ужин", USER_ID);
    public static final int NOT_IN_BASE = 10;
    public static final int IN_BASE = 1;
    public static final LocalDateTime START_DATE=LocalDateTime.of(2024, 1,10,0,0);
    public static final LocalDateTime END_DATE=LocalDateTime.of(2024, 2,10,0,0);
    public static final LocalDateTime DUPLICATE=LocalDateTime.of(2024, 1,10,8,0);

    public MealTestData() {
    }

    public static void assertMealListSize(List<Meal> meals) {
        Assertions.assertThat(meals).hasSize(3);
    }

    public static void assertMealListContains(List<Meal> meals, Meal expectedMeal) {
        Assertions.assertThat(meals).contains(expectedMeal);
    }

    public static Meal getNew() {
        return new Meal(1, LocalDateTime.now(), "Завтрак", 1000);
    }

    public static void assertMealEquality(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static Meal getUpdatedMeal() {
        Meal meal = new Meal(1, LocalDateTime.of(2024, 1, 10, 8, 0), "завтрак", 500);
        meal.setCalories(222);
        meal.setDescription("лебеди");
        meal.setDateTime(LocalDateTime.now());
        return meal;
    }
}