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

import static org.junit.Assert.assertEquals;

public class GetOrdersTest {

    private Response responseGetIngredient;
    private Response responseCreateOrder;
    private Response responseAuth;
    private Response responseRegister;
    private Response responseGetOrders;
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
    public void tearDown()
    {
        authClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Получение заказов пользователя")
    public void getOrdersUserTest() {
        user = User.getRandom();
        responseRegister = authClient.register(user);
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = authClient.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        responseGetIngredient = ingredientsClient.getIngredientsWithAuthToken(accessToken);
        order.setIngredients(order.listIngredient(responseGetIngredient));
        responseCreateOrder = ordersClient.createOrderAuthUser(order, accessToken);
        responseGetOrders = ordersClient.getOrdersUser(accessToken);
        assertEquals("Incorrect status code", 200, responseCreateOrder.statusCode());
        assertEquals("Error create order", true, responseCreateOrder.path("success"));

    }
}
