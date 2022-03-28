import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.model.Order;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.UserCredentials;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest {

    private Response responseGetIngredient;
    private Response responseCreateOrder;
    private Response responseAuth;
    private Response responseRegister;
    private Order order;
    private Client client;
    private User userWithEmailAndPassword = new User();
    private User user;
    private String accessToken;


    @Before
    public void setUp() {
        client = new Client();
        order = new Order();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            accessToken = responseRegister.path("accessToken");
            client.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем c ингредиентами")
    public void createOrderUserWithoutAuthTest() {
        responseGetIngredient = client.getIngredients();
        order.setIngredients(order.listIngredient(responseGetIngredient));
        responseCreateOrder = client.createOrder(order);
        assertEquals("Error create order", true, responseCreateOrder.path("success"));
        assertEquals("Inccorect status code", 200, responseCreateOrder.statusCode());

    }


    @Test
    @DisplayName("Создание заказа авторизованным пользователем c ингредиентами")
    public void createOrderUserAuthTest() {
        user = User.getRandom();
        responseRegister = client.register(user);
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = client.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        responseGetIngredient = client.getIngredientsWithAuthToken(accessToken);
        order.setIngredients(order.listIngredient(responseGetIngredient));
        responseCreateOrder = client.createOrderAuthUser(order, accessToken);
        assertEquals("Error create order", true, responseCreateOrder.path("success"));
        assertEquals("Inccorect status code", 200, responseCreateOrder.statusCode());

    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем без ингредиентов")
    public void createOrderUserAuthWithoutIngredientsTest() {
        user = User.getRandom();
        responseRegister = client.register(user);
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = client.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        ArrayList<String> listIngredient = new ArrayList<>();
        order.setIngredients(listIngredient);
        responseCreateOrder = client.createOrderAuthUser(order, accessToken);
        assertEquals("Error create order", false, responseCreateOrder.path("success"));
        assertEquals("Inccorect status code", 400, responseCreateOrder.statusCode());

    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем с неверным хеш ингридиентов")
    public void createOrderUserAuthWithIncorrectHashIngredientsTest() {
        user = User.getRandom();
        responseRegister = client.register(user);
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = client.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        ArrayList<String> listIngredient = new ArrayList<>();
        listIngredient.add("12345");
        order.setIngredients(listIngredient);
        responseCreateOrder = client.createOrderAuthUser(order, accessToken);
        assertEquals("Inccorect status code", 500, responseCreateOrder.statusCode());

    }

}
