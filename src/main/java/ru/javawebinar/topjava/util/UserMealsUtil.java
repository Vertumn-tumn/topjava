package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 510),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 400)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(17, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(17, 0), 2000));
        System.out.println(filteredByOne(meals, LocalTime.of(7, 0), LocalTime.of(17, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();
        meals.forEach(userMeal -> {
            LocalDate date = userMeal.getDate();
            int calories = userMeal.getCalories();
            caloriesByDate.merge(date, calories, Integer::sum);
        });

        List<UserMeal> filteredMeals = new ArrayList<>();
        meals.forEach(userMeal -> {
            if (TimeUtil.isBetween(userMeal.getTime(), startTime, endTime)) {
                filteredMeals.add(userMeal);
            }
        });

        List<UserMealWithExcess> filteredMealsWithExcess = new ArrayList<>();
        filteredMeals.forEach(filteredMeal -> {
            LocalDateTime dateTime = filteredMeal.getDateTime();
            String desc = filteredMeal.getDescription();
            int calories = filteredMeal.getCalories();
            boolean excess = caloriesByDate.get(filteredMeal.getDate()) > caloriesPerDay;
            filteredMealsWithExcess.add(new UserMealWithExcess(dateTime, desc, calories, excess));
        });
        return filteredMealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesSumPerDay = meals.stream()
                .collect(Collectors.groupingBy(
                        UserMeal::getDate,
                        Collectors.summingInt(UserMeal::getCalories)
                ));

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetween(userMeal.getTime(), startTime, endTime))
                .map(userMeal -> {
                    boolean excess = caloriesSumPerDay.get(userMeal.getDate()) > caloriesPerDay;
                    return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess);
                }).collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByOne(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesSumPerDay = new HashMap<>();
        fillCaloriesSumPerDay(meals, caloriesSumPerDay, 0);

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetween(userMeal.getTime(), startTime, endTime))
                .map(userMeal -> {
                    boolean excess = caloriesSumPerDay.get(userMeal.getDate()) > caloriesPerDay;
                    return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess);
                }).collect(Collectors.toList());
    }

    public static void fillCaloriesSumPerDay(List<UserMeal> meals, Map<LocalDate, Integer> caloriesSumPerDay, int index) {
        if (index == meals.size()) {
            return;
        }

        UserMeal meal = meals.get(index);
        LocalDate date = meal.getDate();
        int calories = meal.getCalories();

        caloriesSumPerDay.merge(date, calories, Integer::sum);

        fillCaloriesSumPerDay(meals, caloriesSumPerDay, index + 1);
    }
}
