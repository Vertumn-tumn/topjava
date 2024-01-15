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
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
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
                mealService.create(new Meal(null, DUPLICATE, "дубль", 500), USER_ID));
    }

    @Test
    public void get() {
        Meal knowNotInBase = MealTestData.getNew();
        MealTestData.assertMealEquality(mealService.get(knowNotInBase.getId(), USER_ID), MealTestData.KNOW_IN_BASE);
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_IN_BASE, USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(IN_BASE, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(IN_BASE, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> betweenInclusive = mealService.getBetweenInclusive(START_DATE.toLocalDate(), END_DATE.toLocalDate(), USER_ID);
        MealTestData.assertMealListSize(betweenInclusive);
    }

    @Test
    public void getAll() {
        List<Meal> all = mealService.getAll(USER_ID);
        MealTestData.assertMealListSize(all);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdatedMeal();
        mealService.update(updated, USER_ID);
        MealTestData.assertMealEquality(mealService.get(updated.getId(), USER_ID), updated);
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_IN_BASE, USER_ID));
    }

    @Test
    public void create() {
        Meal newbie = MealTestData.getNew();
        mealService.create(newbie, USER_ID);
        MealTestData.assertMealListContains(mealService.getAll(USER_ID), newbie);
    }
}