package tests;

import io.qameta.allure.Description;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("This test tries to edit just created user")
    @DisplayName("Test positive edit user")
    public void testEditJustCreated() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = userData;

        Response responseGetAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        String header = getHeader(responseGetAuth, "x-csrf-token");
        String cookie = getCookie(responseGetAuth,"auth_sid");

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId, editData,
                        header, cookie);

        //GET
        Response responseAfterEditing = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, header, cookie);

        Assertions.assertJsonByName(responseAfterEditing, "firstName", newName);
    }

    @Test
    @Description("This test tries to edit a user with no authorization")
    @DisplayName("Test negative edit user unauthorized")
    public void testEditUserNotAuth() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData)
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithoutHeaderAndCookie("https://playground.learnqa.ru/api/user/" + userId,
                        editData);

        Assertions.assertResponseCodeEquals(responseEditUser,400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
    }

    @Test
    @Description("This test tries to edit a user with another user authorization")
    @DisplayName("Test negative edit one user with another user's authorization")
    public void testEditUserWithAnotherAuth() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData)
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        String header = getHeader(responseGetAuth, "x-csrf-token");
        String cookie = getCookie(responseGetAuth,"auth_sid");

        Response responseEditUser = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                editData, header, cookie);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Please, do not edit test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("This test tries to edit email just created user with a not valid email value")
    @DisplayName("Test negative edit user with invalid email")
    public void testEditJustCreatedUserWithInvalidEmail() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = userData;

        Response responseGetAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String invalidEmail = DataGenerator.getRandomEmail().replace('@', '.');
        Map<String, String> editData = new HashMap<>();
        editData.put("email", invalidEmail);
        String header = getHeader(responseGetAuth, "x-csrf-token");
        String cookie = getCookie(responseGetAuth,"auth_sid");

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId, editData,
                        header, cookie);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser,"Invalid email format");
    }

    @Test
    @Description("This test tries to edit userName just created user with a not valid value")
    @DisplayName("Test negative edit userName with one symbol name")
    public void testEditJustCreatedUserWithOneSymbolUserName() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = userData;

        Response responseGetAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        Map<String, String> editData = new HashMap<>();
        editData.put("username", "I");
        String header = getHeader(responseGetAuth, "x-csrf-token");
        String cookie = getCookie(responseGetAuth,"auth_sid");

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId, editData,
                        header, cookie);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "Too short value for field username");
    }
}
