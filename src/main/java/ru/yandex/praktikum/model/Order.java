package ru.yandex.praktikum.model;

import com.github.javafaker.Faker;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class Order {

    public List<String> ingredients;

    public Order() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> listIngredient(Response response) {

        Faker faker = new Faker();
        ArrayList<String> listIngredient = new ArrayList<>();
        int countIngredient = faker.random().nextInt(1, 10);
        for (int i = 1; i < countIngredient; i++) {
            listIngredient.add(response.path("data[" + faker.random().nextInt(1, 10) + "]._id"));
        }
        return listIngredient;
    }
}
