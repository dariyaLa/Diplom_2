import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.AuthClient;
import ru.yandex.praktikum.client.IngredientsClient;
import ru.yandex.praktikum.client.OrdersClient;
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
    private AuthClient authClient;
    private IngredientsClient ingredientsClient;
    private OrdersClient ordersClient;
    private User userWithEmailAndPassword = new User();
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        authClient = new AuthClient();
        ingredientsClient = new IngredientsClient();
        ordersClient = new OrdersClient();
        order = new Order();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            accessToken = responseRegister.path("accessToken");
            authClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем c ингредиентами")
    public void createOrderUserWithoutAuthTest() {
        responseGetIngredient = ingredientsClient.getIngredients();
        order.setIngredients(order.listIngredient(responseGetIngredient));
        responseCreateOrder = ordersClient.createOrder(order);
        assertEquals("Inccorect status code", 200, responseCreateOrder.statusCode());
        assertEquals("Error create order", true, responseCreateOrder.path("success"));

    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем c ингредиентами")
    public void createOrderUserAuthTest() {
        user = User.getRandom();
        responseRegister = authClient.register(user);
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = authClient.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        responseGetIngredient = ingredientsClient.getIngredientsWithAuthToken(accessToken);
        order.setIngredients(order.listIngredient(responseGetIngredient));
        responseCreateOrder = ordersClient.createOrderAuthUser(order, accessToken);
        assertEquals("Inccorect status code", 200, responseCreateOrder.statusCode());
        assertEquals("Error create order", true, responseCreateOrder.path("success"));

    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем без ингредиентов")
    public void createOrderUserAuthWithoutIngredientsTest() {
        user = User.getRandom();
        responseRegister = authClient.register(user);
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = authClient.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        ArrayList<String> listIngredient = new ArrayList<>();
        order.setIngredients(listIngredient);
        responseCreateOrder = ordersClient.createOrderAuthUser(order, accessToken);
        assertEquals("Inccorect status code", 400, responseCreateOrder.statusCode());
        assertEquals("Error create order", false, responseCreateOrder.path("success"));

    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем с неверным хеш ингридиентов")
    public void createOrderUserAuthWithIncorrectHashIngredientsTest() {
        user = User.getRandom();
        responseRegister = authClient.register(user);
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = authClient.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        ArrayList<String> listIngredient = new ArrayList<>();
        listIngredient.add("12345");
        order.setIngredients(listIngredient);
        responseCreateOrder = ordersClient.createOrderAuthUser(order, accessToken);
        assertEquals("Inccorect status code", 500, responseCreateOrder.statusCode());

    }

}
