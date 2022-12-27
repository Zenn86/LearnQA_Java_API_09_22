package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    private final String uriToCreateUser = "https://playground.learnqa.ru/api/user/";
    @Test
    @Description("This test tries to create a new user with email, that was used before and owned by existing user")
    @DisplayName("Test negative create user")
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(uriToCreateUser)
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @Description("This test tries to create a new user and checks if it succeed")
    @DisplayName("Test positive create user")
    public void testCreateUserSuccessfully() {

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(uriToCreateUser)
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @Description("This test tries to create a new user with invalid email")
    @DisplayName("Test negative create user with invalid email")
    public void testCreateUserWithInvalidEmail() {
        String invalidEmail = DataGenerator.getRandomEmail().replace('@', '.');

        Map<String, String> userData = new HashMap<>();
        userData.put("email", invalidEmail);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUserWithInvalidEmail = apiCoreRequests
                .makePostRequest(uriToCreateUser, userData);

        Assertions.assertResponseCodeEquals(responseCreateUserWithInvalidEmail, 400);
        Assertions.assertResponseTextEquals(responseCreateUserWithInvalidEmail, "Invalid email format");
    }

    @ParameterizedTest
    @CsvSource(value = {
            "13245:BorJo:Boris:Johnson",
            " :Uncharted:Nathan:Drake",
            "131345: :John:Doe",
            "3131313:Maverick: :Gun",
            "829739:Ufo:Alien: "
    }, delimiter = ':')
    @Description("This test tries to create a new user with one empty parameter")
    @DisplayName("Test negative create user with one missing parameter")
    public void testCreateUserWithOneEmptyParameter(String password, String userName, String firstName, String lastName) {
        String email;
        if (password != null && userName != null && firstName != null && lastName != null) {
            email = null;
        } else {
            email = DataGenerator.getRandomEmail();
        }
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", password);
        userData.put("username", userName);
        userData.put("firstName", firstName);
        userData.put("lastName", lastName);

        Response responseWithOneEmptyParameter = apiCoreRequests.makePostRequest(uriToCreateUser, userData);
        Assertions.assertResponseCodeEquals(responseWithOneEmptyParameter, 400);
        Set<String> keys = userData.keySet();
        for (String key : keys) {
            if (userData.get(key) == null) {
                Assertions.assertResponseTextEquals(responseWithOneEmptyParameter,
                        "The following required params are missed: " + key);
            }
        }
    }
}
