package ru.javawebinar.topjava;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.meal.MealRestController;

public class SpringMain {
    private ClassPathXmlApplicationContext springContext;
    private MealRestController mealController;

    public void init() {
        springContext = new ClassPathXmlApplicationContext();
        springContext.setConfigLocations("spring/spring-app.xml", "spring/spring-db.xml");
        springContext.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile(), Profiles.REPOSITORY_IMPLEMENTATION);
        springContext.refresh();
        mealController = springContext.getBean(MealRestController.class);
    }

    public void destroy() {
        springContext.close();
    }

    public static void main(String[] args) {
        SpringMain main = new SpringMain();
        main.init();
        System.out.println(main.mealController.getAll());
        main.springContext.close();
    }
}
