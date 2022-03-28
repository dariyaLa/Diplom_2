import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.model.User;

import static org.junit.Assert.assertEquals;

public class UserRegisterTest {

    private Response responseRegister;
    private Client client;
    private User user;
    private String accessToken;


    @Before
    public void setUp() {
        client = new Client();
        user = User.getRandom();
        responseRegister = client.register(user);

    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            accessToken = responseRegister.path("accessToken");
            client.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Пользователя можно зарегистрировать")
    public void userRegisterSuccessTest() {
        assertEquals("User is not create", true, responseRegister.then().extract().path("success"));
        assertEquals("Status code incorrect", 200, responseRegister.statusCode());

    }

    @Test
    @DisplayName("Пользователя нельзя зарегистрировать повторно")
    public void doubleUserRegisterFailTest() {
        User doubleUser = new User(user.email, user.password, user.name);
        responseRegister = client.register(doubleUser);
        assertEquals("Unexpected error", false, responseRegister.then().extract().path("success"));
        assertEquals("Status code incorrect", 403, responseRegister.statusCode());

    }

    @Test
    @DisplayName("Пользователя нельзя зарегистрировать без поля email")
    public void userRegisterWithoutEmailFailTest() {
        User userWithoutEmail = new User();
        userWithoutEmail.setPassword(user.password);
        userWithoutEmail.setName(user.name);
        responseRegister = client.register(userWithoutEmail);
        assertEquals("Unexpected error", false, responseRegister.then().extract().path("success"));
        assertEquals("Status code incorrect", 403, responseRegister.statusCode());

    }

}
