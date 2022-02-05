import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class OrderClient extends RestAssuredClient{
    public final static String API_URL = "/orders/";

    @Step("Make New Order With Authorization")
    public Response makeNewOrderWithAuthorization(String ingredient, String accessToken){
        String orderRequest = "{\"ingredients\": \"" + ingredient + "\"}";
        return given()
                .spec(getBaseSpec())
                .headers("authorization", accessToken)
                .body(orderRequest)
                .when()
                .post(API_URL);
    }

    @Step ("Make New Order Without Authorization")
    public Response makeNewOrderWithoutAuthorization(String ingredient){
        String orderRequest = "{\"ingredients\": \"" + ingredient + "\"}";
        return given()
                .spec(getBaseSpec())
                .body(orderRequest)
                .when()
                .post(API_URL);
    }

    @Step ("Make New Order Without Ingredients")
    public Response makeNewOrderWithoutIngredients(String accessToken){
        return given()
                .spec(getBaseSpec())
                .headers("Authorization", accessToken)
                .when()
                .post(API_URL);
    }

    @Step ("Get Orders List")
    public Response getOrdersList (String accessToken) {
        return given()
                .spec(getBaseSpec())
                .headers("Authorization", accessToken)
                .when()
                .get(API_URL+ "all");
    }
    @Step ("Get Orders List Without User Authorization")
    public Response getOrdersListWithoutUserAuthorization () {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(API_URL);
    }
}
