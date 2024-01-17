package ru.javawebinar.topjava;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final Meal knowInBase = new Meal(1, LocalDateTime.of(2020, 1, 30, 10, 0), "Завтрак", 500);
    public static final int NON_EXIST = 10;
    public static final int USER_MEAL_ID = 1;
    public static final LocalDateTime START_DATE = LocalDateTime.of(2020, 1, 30, 0, 0);
    public static final LocalDateTime END_DATE = LocalDateTime.of(2020, 1, 30, 0, 0);
    public static final LocalDateTime DUPLICATE_DATE_TIME = LocalDateTime.of(2020, 1, 30, 13, 0);
    public static List<Meal> IN_BEETWEEN_LIST = new ArrayList<>();

    static {
        MealsUtil.meals.stream().filter(meal -> meal.getDate().equals(LocalDate.of(2020, 1, 30)))
                .forEach(meal -> IN_BEETWEEN_LIST.add(meal));
    }

    public static void assertMealListContent(List<Meal> actual, List<Meal> expected) {
        assertThat(actual).isSortedAccordingTo(Comparator.comparing(Meal::getDateTime));
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).extracting("dateTime", "description", "calories")
                    .contains(expected.get(i).getDateTime(), expected.get(i).getDescription(), expected.get(i).getCalories());
        }
    }

    public static void assertMealListContains(List<Meal> meals, Meal expectedMeal) {
        Assertions.assertThat(meals).contains(expectedMeal);
    }

    public static Meal getNewMeal() {
        return new Meal(null, LocalDateTime.of(2022, 1, 31, 20, 0), "Ужин", 1000);
    }

    public static void assertMealEquality(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static Meal getUpdatedMeal() {
        Meal meal = knowInBase;
        meal.setCalories(222);
        meal.setDescription("лебеди");
        meal.setDateTime(LocalDateTime.of(2023, 1, 31, 20, 0));
        return meal;
    }
}