import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.AuthClient;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.UserCredentials;

import static org.junit.Assert.assertEquals;

public class UserAuthTest {

    private Response responseRegister;
    private Response responseAuth;
    private AuthClient client;
    private Faker faker = new Faker();
    private User user;
    private User userWithEmailAndPassword = new User();
    private String accessToken;

    @Before
    public void setUp() {
        client = new AuthClient();
        user = User.getRandom();
        responseRegister = client.register(user);
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
    }

    @After
    public void tearDown()
    {
        accessToken = responseRegister.path("accessToken");
        client.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Пользователь может авторизоваться")
    public void userAuthSuccessTest() {
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = client.auth(UserCredentials.from(userWithEmailAndPassword));
        assertEquals("Status code incorrect", 200, responseAuth.statusCode());
        assertEquals("Auth error", true, responseAuth.then().extract().path("success"));

    }

    @Test
    @DisplayName("Авторизация пользователя с неверным email")
    public void userAuthWithIncorrectEmailFailTest() {
        userWithEmailAndPassword.setEmail(RandomStringUtils.randomAlphabetic(10) + "@" + RandomStringUtils.randomAlphabetic(10) + ".ru");
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = client.auth(UserCredentials.from(userWithEmailAndPassword));
        assertEquals("Status code incorrect", 401, responseAuth.statusCode());
        assertEquals("Email or password are incorrect", false, responseAuth.then().extract().path("success"));

    }

    @Test
    @DisplayName("Авторизация пользователя с неверным password")
    public void userAuthWithIncorrectPasswordFailTest() {
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(RandomStringUtils.randomAlphabetic(10));
        responseAuth = client.auth(UserCredentials.from(userWithEmailAndPassword));
        assertEquals("Status code incorrect", 401, responseAuth.statusCode());
        assertEquals("Email or password are incorrect", false, responseAuth.then().extract().path("success"));

    }

}
