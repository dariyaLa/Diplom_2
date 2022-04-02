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

public class ChangesUserTest {

    private Response responseRegister;
    private Response responseAuth;
    private Response responseGetInfoUser;
    private Response responseChangeInfoUser;
    private AuthClient client;
    private User user;
    private User userWithEmailAndPassword = new User();
    private User userChanges = new User();
    private String accessToken;

    @Before
    public void setUp() {
        client = new AuthClient();
        user = User.getRandom();
        responseRegister = client.register(user);

    }

    @After
    public void tearDown() {

        client.deleteUser(accessToken);

    }

    @Test
    @DisplayName("Авторизованный пользователь может изменить имя")
    public void authUserEditNameSuccessTest() {
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = client.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        responseGetInfoUser = client.getInfoUser(accessToken);
        userChanges.setName(RandomStringUtils.randomAlphabetic(10));
        responseChangeInfoUser = client.change(userChanges, accessToken);
        assertEquals("Incorrect status code", 200, responseChangeInfoUser.statusCode());
        assertEquals("Error change name", userChanges.getName(), responseChangeInfoUser.path("user.name"));

    }

    @Test
    @DisplayName("Авторизванный пользователь может изменить пароль")
    public void authUserEditPasswordSuccessTest() {
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = client.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        responseGetInfoUser = client.getInfoUser(accessToken);
        userChanges.setPassword(RandomStringUtils.randomAlphabetic(10));
        responseChangeInfoUser = client.change(userChanges, accessToken);
        assertEquals("Incorrect status code", 200, responseChangeInfoUser.statusCode());
        assertEquals("Error change password", true, responseChangeInfoUser.path("success"));

    }

    @Test
    @DisplayName("Авторизованный пользователь может изменить почту")
    public void authUserEditEmailSuccessTest() {
        userWithEmailAndPassword.setEmail(user.email);
        userWithEmailAndPassword.setPassword(user.password);
        responseAuth = client.auth(UserCredentials.from(userWithEmailAndPassword));
        accessToken = responseAuth.path("accessToken");
        responseGetInfoUser = client.getInfoUser(accessToken);
        userChanges.setEmail(RandomStringUtils.randomAlphabetic(10) + "@" + RandomStringUtils.randomAlphabetic(10) + ".ru");
        responseChangeInfoUser = client.change(userChanges, accessToken);
        assertEquals("Incorrect status code", 200, responseChangeInfoUser.statusCode());
        assertEquals("Error change email", true, responseChangeInfoUser.path("success"));

    }

    @Test
    @DisplayName("Неавторизованный пользователь может изменить имя")
    public void userEditNameSuccessTest() {
        accessToken = responseRegister.path("accessToken");
        userChanges.setName(RandomStringUtils.randomAlphabetic(10));
        responseChangeInfoUser = client.change(userChanges, accessToken);
        assertEquals("Incorrect status code", 200, responseChangeInfoUser.statusCode());
        assertEquals("Error change name", userChanges.getName(), responseChangeInfoUser.path("user.name"));

    }

    @Test
    @DisplayName("Неавторизованный пользователь может изменить пароль")
    public void userEditPasswordSuccessTest() {
        accessToken = responseRegister.path("accessToken");
        userChanges.setPassword(RandomStringUtils.randomAlphabetic(10));
        responseChangeInfoUser = client.change(userChanges, accessToken);
        assertEquals("Incorrect status code", 200, responseChangeInfoUser.statusCode());
        assertEquals("Error change password", true, responseChangeInfoUser.path("success"));

    }

    @Test
    @DisplayName("Неавторизованный пользователь может изменить почту")
    public void userEditEmailSuccessTest() {
        accessToken = responseRegister.path("accessToken");
        userChanges.setPassword(RandomStringUtils.randomAlphabetic(10));
        userChanges.setEmail(RandomStringUtils.randomAlphabetic(10) + "@" + RandomStringUtils.randomAlphabetic(10) + ".ru");
        responseChangeInfoUser = client.change(userChanges, accessToken);
        assertEquals("Incorrect status code", 200, responseChangeInfoUser.statusCode());
        assertEquals("Error change email", true, responseChangeInfoUser.path("success"));

    }

    @Test
    @DisplayName("Изменение данных без авторизационного токена вызывает ошибку")
    public void requestChangeWithoutAuthTokenFailTest() {
        accessToken = responseRegister.path("accessToken");
        userChanges.setName(RandomStringUtils.randomAlphabetic(10));
        responseChangeInfoUser = client.changeWithoutAuth(userChanges);
        assertEquals("Incorrect status code", 401, responseChangeInfoUser.statusCode());
        assertEquals("Unexpected error", false, responseChangeInfoUser.path("success"));

    }

}
