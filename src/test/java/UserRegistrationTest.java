import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserRegistrationTest {
    private User user;
    private UserClient userClient;

    @Before
    public void setUp(){
        user = User.getRandom();
        userClient = new UserClient();

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
