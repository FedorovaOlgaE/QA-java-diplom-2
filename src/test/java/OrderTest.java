import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class OrderTest extends RestAssuredClient {
    private UserClient userClient;
    private OrderClient orderClient;
    private IngredientsClient ingredientClient;
    private User user;
    public String accessToken;

    public String getAccessToken() {
        {
            return accessToken;
        }
    }


    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        ingredientClient = new IngredientsClient();
        user = User.getRandom();
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
    @DisplayName("Create Order With Authorized User")
    public void createOrderWithAuthorizedUserTest() {
        userClient.createUser(user);
        String accessToken = userClient.getUserAccessToken(UserData.from(user));
        String ingredient = ingredientClient.getListOfIngredients(1);
        orderClient.makeNewOrderWithAuthorization(ingredient, accessToken)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    //при выполнении теста ниже всегда баг
    @Test
    @DisplayName("Create Order without Authorized User")
    public void createOrderWithoutAuthorizedUserTest() {
        String ingredientId = ingredientClient.getListOfIngredients(1);
        orderClient.makeNewOrderWithoutAuthorization(ingredientId)
                .then().assertThat()
                .body("success", equalTo(false))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create Order Without Ingredient And With Authorization")
    public void createOrderWithoutIngredientAndWithAuthorizationTest() {
        userClient.createUser(user);
        String accessToken = userClient.getUserAccessToken(UserData.from(user));
        orderClient.makeNewOrderWithoutIngredients(accessToken)
                .then().assertThat()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create Order With Wrong Ingredient And With Authorization")
    public void createOrderWithWrongIngredientAndWithAuthorizationTest() {
        userClient.createUser(user);
        String accessToken = userClient.getUserAccessToken(UserData.from(user));
        String ingredientId = "incorrectId";
        orderClient.makeNewOrderWithAuthorization(ingredientId, accessToken)
                .then().assertThat()
                .statusCode(500);

    }

    @Test
    @DisplayName("Get Order With Authorization")
    public void getOrderWithAuthorizationTest() {
        userClient.createUser(user);
        String accessToken = userClient.getUserAccessToken(UserData.from(user));
        String ingredient = ingredientClient.getListOfIngredients(1);
        orderClient.makeNewOrderWithAuthorization(ingredient, accessToken);
        orderClient.getOrdersList(accessToken)
                .then().assertThat()
                .body("total", notNullValue())
                .and()
                .body("orders", notNullValue())
                .and()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Get Order Without Authorization")
    public void getOrderWithoutAuthorizationTest() {
        orderClient.getOrdersListWithoutUserAuthorization()
                .then().assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);

    }
}
