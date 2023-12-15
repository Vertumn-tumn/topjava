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
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        //карта для хранения калорий за день
        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();

        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            int calories = meal.getCalories();

            if (caloriesByDate.containsKey(date)) {
                // Если да, прибавляем к существующей сумме калорий новую порцию
                int updateCalories = caloriesByDate.get(date) + calories;
                caloriesByDate.put(date, updateCalories);
            } else {
                //если нет, то создаём новую карту и устанавливаем сумму калорий
                caloriesByDate.put(date, calories);
            }
        }
        //список с флагами excess
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();

        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            //если сумма калорий за текущий день больше лимита, то создаём UserMealWithExcess.excess=true и кладём в список
            if (caloriesByDate.get(date) > caloriesPerDay) {
                mealsWithExcess.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
            } else {
                //то же, но с false
                mealsWithExcess.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false));
            }
        }

        //отфильтрованный список
        List<UserMealWithExcess> filteredMealsWithExcess = new ArrayList<>();
        for (UserMealWithExcess meal : mealsWithExcess) {
            if (TimeUtil.isBetweenHalfOpenInc(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredMealsWithExcess.add(meal);
            }
        }
        return filteredMealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        //карта для хранения калорий за день
        Map<LocalDate, Integer> caloriesSumPerDay = meals.stream()
                .collect(Collectors.groupingBy(
                        UserMeal::getDate, // группировка по дате
                        Collectors.summingInt(UserMeal::getCalories) // суммирование калорий для каждой даты
                ));

        //отфильтрованный список с флагами excess
        return meals.stream()
                .map(userMeal -> {
                    boolean excess = caloriesSumPerDay.get(userMeal.getDate()) > caloriesPerDay;
                    return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess);
                }).filter(userMealWithExcess -> TimeUtil.isBetweenHalfOpenInc(userMealWithExcess.getDateTime().toLocalTime(), startTime, endTime)).collect(Collectors.toList());
    }
}
