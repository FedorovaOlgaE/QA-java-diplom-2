import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserRegistrationTest extends RestAssuredClient {
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
    @DisplayName("New User Registration")
    public void newUserRegistrationTest() {
        userClient.createUser(user)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);

    }

    @Test
    @DisplayName("Re-registration User")
    public void reRegistrationUserTest() {
        userClient.createUser(user);
        userClient.createUser(user)
                .then().assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("User without email registration")
    public void newUserWithoutEmailTest() {
        userClient.createUserWithOutEmail(user)
                .then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("User without password registration")
    public void newUserWithoutPasswordTest() {
        userClient.createUserWithOutPassword(user)
                .then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("User without name registration")
    public void newUserWithoutNameTest() {
        userClient.createUserWithOutName(user)
                .then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }
}
