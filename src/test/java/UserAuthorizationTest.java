import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;


public class UserAuthorizationTest {
    private User user;
    private UserClient userClient;

    @Before
    public void setUp(){
        user = User.getRandom();
        userClient = new UserClient();
    }
    @Test
    @DisplayName("Authorization Registered User")
    public void authorizationRegisteredUser(){
        userClient.createUser(user);
        userClient.Authorization(UserData.from(user))
        .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);


    }

    @Test
    @DisplayName("Authorization with Incorrect User Data ")
    public void authorizationWithIncorrectEmailAndPassword(){
        userClient.userAuthorizationWithIncorrectEmailAndPassword(UserData.from(user))
                .then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
}
