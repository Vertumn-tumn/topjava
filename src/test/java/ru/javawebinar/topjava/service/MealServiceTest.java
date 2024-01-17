package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(null, DUPLICATE_DATE_TIME, "дубль", 500), USER_ID));
    }

    @Test
    public void get() {
        MealTestData.assertMealEquality(mealService.get(knowInBase.getId(), USER_ID), MealTestData.knowInBase);
    }

    @Test
    public void delete() {
        mealService.delete(USER_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(USER_MEAL_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> betweenInclusive = mealService.getBetweenInclusive(START_DATE.toLocalDate(), END_DATE.toLocalDate(), USER_ID);
        MealTestData.assertMealListContent(betweenInclusive, IN_BEETWEEN_LIST);
    }

    @Test
    public void getAll() {
        List<Meal> all = mealService.getAll(USER_ID);
        MealTestData.assertMealListContent(all, MealsUtil.meals);
    }

    @Test
    public void update() {
        Meal updated = getUpdatedMeal();
        mealService.update(updated, USER_ID);
        MealTestData.assertMealEquality(mealService.get(updated.getId(), USER_ID), getUpdatedMeal());
    }

    @Test
    public void create() {
        Meal created = mealService.create(getNewMeal(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNewMeal();
        newMeal.setId(newId);
        assertMealEquality(created, newMeal);
        assertMealEquality(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void getMealNotBelongToUser() {
        assertThrows(NotFoundException.class, () -> mealService.get(knowInBase.getId(), ADMIN_ID));
    }

    @Test
    public void updateMealNotBelongToUser() {
        Meal updated = getUpdatedMeal();
        updated.setId(9);
        assertThrows(NotFoundException.class, () -> mealService.update(updated, USER_ID));
    }

    @Test
    public void deleteMealNotBelongToUser() {
        Meal updated = getUpdatedMeal();
        updated.setId(9);
        assertThrows(NotFoundException.class, () -> mealService.delete(updated.getId(), USER_ID));
    }

    @Test
    public void deleteNonExistMeal() {
        assertThrows(NotFoundException.class, () -> mealService.delete(NON_EXIST, USER_ID));
    }

    @Test
    public void getBetweenWithNullValues() {
        List<Meal> betweenInclusive = mealService.getBetweenInclusive(null, null, USER_ID);
        MealTestData.assertMealListContent(betweenInclusive, MealsUtil.meals);
    }
}