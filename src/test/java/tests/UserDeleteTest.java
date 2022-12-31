package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test tries to delete user, who can't be deleted")
    @DisplayName("Test negative delete user")
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
}
