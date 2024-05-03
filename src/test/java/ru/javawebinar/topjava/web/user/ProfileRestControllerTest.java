package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.user.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.getAll(), admin, guest);
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), updated);
    }

    @Test
    void getWithMeals() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/with-meals"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user))
                .andExpect(content().string(containsString("""
                        {"id":100003,"dateTime":"2020-01-30T10:00:00","description":"Завтрак","calories":500}""")))
                .andExpect(content().string(containsString("""
                        {"id":100004,"dateTime":"2020-01-30T13:00:00","description":"Обед","calories":1000}""")))
                .andExpect(content().string(containsString("""
                        {"id":100005,"dateTime":"2020-01-30T20:00:00","description":"Ужин","calories":500}""")))
                .andExpect(content().string(containsString("""
                        {"id":100006,"dateTime":"2020-01-31T00:00:00","description":"Еда на граничное значение","calories":100}""")))
                .andExpect(content().string(containsString("""
                        {"id":100007,"dateTime":"2020-01-31T10:00:00","description":"Завтрак","calories":500}""")))
                .andExpect(content().string(containsString("""
                        {"id":100008,"dateTime":"2020-01-31T13:00:00","description":"Обед","calories":1000}""")))
                .andExpect(content().string(containsString("""
                        {"id":100009,"dateTime":"2020-01-31T20:00:00","description":"Ужин","calories":510}""")));
    }
}