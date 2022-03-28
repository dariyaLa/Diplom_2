import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
    public void tearDown()
    {
        client.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Получение заказов пользователя")
    public void getOrdersUserTest() {
        user = User.getRandom();
        responseRegister = client.register(user);
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = client.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        responseGetIngredient = client.getIngredientsWithAuthToken(accessToken);
        order.setIngredients(order.listIngredient(responseGetIngredient));
        responseCreateOrder = client.createOrderAuthUser(order, accessToken);
        responseGetOrders = client.getOrdersUser(accessToken);
        assertEquals("Error create order", true, responseCreateOrder.path("success"));
        assertEquals("Incorrect status code", 200, responseCreateOrder.statusCode());

    }
}
