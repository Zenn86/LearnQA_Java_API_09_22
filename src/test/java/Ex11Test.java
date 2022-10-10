import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex11Test {

    @Test
    public void testCookie() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        assertTrue(!response.getCookies().isEmpty());
        Map<String, String> cookies = response.getCookies();
        Response response2 = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        assertTrue(!response2.getCookies().isEmpty());
        assertEquals(cookies, response2.getCookies(), "Cookies don't match");
        System.out.println(response.getCookies());
    }
}
