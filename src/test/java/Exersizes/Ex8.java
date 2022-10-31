package Exersizes;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex8 {
    @Test
    public void testToken() {
        JsonPath response1 = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = response1.get("token");
        int seconds = response1.get("seconds");
        String errorMessage = "Job is NOT ready";
        String okMessage = "Job is ready";

        JsonPath response2 = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        assertEquals(errorMessage, response2.get("status"));
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JsonPath response3 = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        assertEquals(okMessage, response3.get("status"));
        assertNotNull(response3.get("result"), "Something wrong!");

        System.out.println("Result is " + response3.get("result"));

    }
}
