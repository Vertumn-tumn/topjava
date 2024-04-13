package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);
    private int userId = SecurityUtil.authUserId();
    private int authCalories = SecurityUtil.authUserCaloriesPerDay();
    private final MealService mealService;

    public JspMealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping
    public String getMethod(HttpServletRequest request) {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                handleDelete(request, userId, authCalories);
                break;
            case "create", "update":
                handleCreateOrUpdate(request, userId);
                return "mealForm";
            case "filter":
                handleFilter(request, userId, authCalories);
                break;
            default:
                handleDefault(request, userId, authCalories);
                break;
        }

        return "meals";
    }

    @PostMapping
    public String postMethod(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        int userId = SecurityUtil.authUserId();

        if (StringUtils.hasLength(request.getParameter("id"))) {
            handleUpdate(request, userId);
        } else {
            handleCreate(request, userId);
        }
        return "redirect:meals";
    }

    private void handleDelete(HttpServletRequest request, int userId, int authCalories) {
        int id = getId(request);
        log.info("delete meal {} for user {}", id, userId);
        mealService.delete(id, userId);
        request.setAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), authCalories));
    }

    private void handleCreateOrUpdate(HttpServletRequest request, int userId) {
        log.info("redirect to mealForm {}", userId);
        Meal meal = "create".equals(request.getParameter("action")) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                mealService.get(getId(request), userId);
        request.setAttribute("meal", meal);

    }

    private void handleFilter(HttpServletRequest request, int userId, int authCalories) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(startDate, endDate, userId);
        List<MealTo> filteredTos = MealsUtil.getFilteredTos(mealsDateFiltered, authCalories, startTime, endTime);
        request.setAttribute("meals", filteredTos);
    }

    private void handleDefault(HttpServletRequest request, int userId, int authCalories) {
        log.info("getAll for user {}", userId);
        request.setAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), authCalories));
    }

    private void handleCreate(HttpServletRequest request, int userId) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        mealService.create(meal, userId);
    }

    private void handleUpdate(HttpServletRequest request, int userId) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        meal.setId(Integer.parseInt(request.getParameter("id")));
        mealService.update(meal, userId);
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
