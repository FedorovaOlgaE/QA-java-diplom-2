import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class UserAuthorizationTest extends RestAssuredClient {
    private User user;
    private UserClient userClient;
    public String accessToken;

    public String getAccessToken() {
        {
            return accessToken;
        }
    }

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
    }

    @After
    public void delete() {
        if (getAccessToken() == null) {
            return;
        }
        given()
                .spec(getBaseSpec())
                .auth().oauth2(getAccessToken().substring(7))
                .when()
                .delete("auth/user")
                .then()
                .statusCode(202);
    }

    @Test
    @DisplayName("Authorization Registered User")
    public void authorizationRegisteredUser() {
        userClient.createUser(user);
        userClient.Authorization(UserData.from(user))
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);


    }

    @Test
    @DisplayName("Authorization with Incorrect User Data ")
    public void authorizationWithIncorrectEmailAndPassword() {
        userClient.userAuthorizationWithIncorrectEmailAndPassword(UserData.from(user))
                .then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
}
