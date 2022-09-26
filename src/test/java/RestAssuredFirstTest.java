import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class RestAssuredFirstTest {

    @Test
    public void testRestAssured() {

        Response response = RestAssured
                .given()
                .queryParam("param1", "value1")
                .queryParam("param2", "value2")
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        response.print();
    }

}
