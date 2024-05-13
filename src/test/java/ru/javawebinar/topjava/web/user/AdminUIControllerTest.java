package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.*;

class AdminUIControllerTest extends AbstractControllerTest {
    private static final String URL = AdminUIController.URL;

    @Autowired
    private UserService service;

    @Test
    void changeEnabled() throws Exception {
        User fromTestData = admin;
        fromTestData.setEnabled(false);

        perform(MockMvcRequestBuilders.post(URL + "/{id}", fromTestData.getId())
                .param("condition", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(service.get(ADMIN_ID), fromTestData);
    }
}