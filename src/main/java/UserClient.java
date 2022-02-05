import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {

    public final static String API_URL = "/auth/";

    @Step("Create User")
    public Response createUser(User user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(API_URL+"register/");

    }

    @Step("Create User Without Email")
    public Response createUserWithOutEmail(User user){
        String body = "{\"password\":\"" + user.password + "\","
                + "\"name\":\"" + user.name + "\"}";

        return  given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post(API_URL+"register/");
    }

    @Step("Create User Without Password")
    public Response createUserWithOutPassword(User user){
        String body = "{\"email\":\"" + user.email + "\","
                + "\"name\":\"" + user.name + "\"}";
        return given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post(API_URL+"register/");
    }

    @Step("Create WithOut Name")
    public Response createUserWithOutName(User user){
        String body = "{\"email\":\"" + user.email + "\","
                + "\"password\":\"" + user.password + "\"}";
        return given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post(API_URL+"register/");
    }

    @Step("User Authorization")
    public Response Authorization(UserData userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post(API_URL+"login/");
    }

    @Step("User Authorization With Incorrect Email And Password")
    public Response userAuthorizationWithIncorrectEmailAndPassword(UserData userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post(API_URL+"login/");
    }

    @Step("Get User Access Token")
    public String getUserAccessToken(UserData userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post(API_URL + "/login/")
                .then()
                .extract()
                .path("accessToken")
                .toString()
                .substring(7);
    }

    @Step("Registration And Get Access Token")
    public String registrationAndGetAccessToken(User user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(API_URL+"register/")
                .then()
                .extract()
                .path("accessToken")
                .toString()
                .substring(7);
    }

    @Step("Edit User Data With Authorization")
    public Response editUserDataWithAuthorization(User newUser, String accessToken) {
        String body = "{ \"email\":\"" + newUser.email + "\","
                + "\"name\":\"" + newUser.name + "\"}";
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .patch(API_URL + "user/");
    }


    @Step("Edit User Data Without Authorization")
    public Response editUserDataWithoutAuthorization(UserData user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(API_URL + "user/");
    }
}
