package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Delete user cases")
@Feature("Removal")
public class UserDeleteTest {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test tries to delete user, who can't be deleted")
    @DisplayName("Test negative delete user, who can't be deleted")
    public void testDeleteUserWithUserId2() {
        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //DELETE
        Response responseDeleteUserToFail = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/2",
                        responseGetAuth.getHeader("x-csrf-token"), responseGetAuth.getCookie("auth_sid"));
        Assertions.assertResponseCodeEquals(responseDeleteUserToFail, 400);
        Assertions.assertResponseTextEquals(responseDeleteUserToFail,
                "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("This test tries to delete just created user")
    @DisplayName("Test positive delete just created user")
    public void testDeleteJustCreatedUser() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Response responseGetAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login", userData);

        //DELETE
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId,
                        responseGetAuth.getHeader("x-csrf-token"), responseGetAuth.getCookie("auth_sid"));
        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //GET
        Response responseAfterDeleting = apiCoreRequests
                .makeGetRequestWithoutTokenAndCookie("https://playground.learnqa.ru/api/user/" + userId);

        Assertions.assertResponseCodeEquals(responseAfterDeleting, 404);
        Assertions.assertResponseTextEquals(responseAfterDeleting, "User not found");
    }

    @Test
    @Description("This test tries to delete user with another user authorization")
    @DisplayName("Test positive delete user with another user's authorization")
    public void testDeleteUserWithAnotherUserAuth() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");
        System.out.println(userId);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //DELETE
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId,
                        responseGetAuth.getHeader("x-csrf-token"), responseGetAuth.getCookie("auth_sid"));
        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser,
                "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

        //GET
        Response responseAfterDeleting = apiCoreRequests
                .makeGetRequestWithoutTokenAndCookie("https://playground.learnqa.ru/api/user/" + userId);

        Assertions.assertResponseCodeEquals(responseAfterDeleting, 200);
        Assertions.assertJsonByName(responseAfterDeleting, "username", userData.get("username"));
    }

}
