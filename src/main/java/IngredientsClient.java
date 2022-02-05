import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends RestAssuredClient {
    public final static String APIURL = "/ingredients/";
    public final static String Type = "Content-type";
    public final static String APP = "application/json";

    @Step("Get List Of Ingredients")
    public String getListOfIngredients(int massiveId) {
        return given()
                .spec(getBaseSpec())
                .get(APIURL)
                .then()
                .extract()
                .path("data["+ massiveId + "]._id");

    }

}
