import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Ex11Test extends BaseTestCase {
    String url = "https://playground.learnqa.ru/api/homework_cookie";
    @Test
    public void testCookie() {
        Response response = getResponce(url);
        assertFalse(response.getCookies().isEmpty(), "There is no cookies");
        Map<String, String> cookies = response.getCookies();
        Response response2 = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        assertFalse(response2.getCookies().isEmpty(), "There is no cookies");
        assertEquals(cookies, response2.getCookies(), "Cookies don't match");
        System.out.println(response.getCookies());
    }
}
