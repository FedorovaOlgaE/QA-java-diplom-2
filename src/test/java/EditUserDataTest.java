import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class EditUserDataTest {
    private User user;
    private UserClient userClient;
    private UserData userData;

    @Before
    public void setUp(){
        user = User.getRandom();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Edit User Data With Authorization")
    public void editUserDataWithAuthorization(){
        String accessToken = userClient.registrationAndGetAccessToken(user);
        userClient.Authorization(UserData.from(user));
        User newUser = User.getRandom();
        userClient.editUserDataWithAuthorization(newUser, accessToken)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Edit User Data Without Authorization")
    public void editUserDataWithoutAuthorization(){
        userClient.createUser(user);
        User newUser = User.getRandom();
        userClient.editUserDataWithoutAuthorization(UserData.from(newUser))
                .then().assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);

    }
}
