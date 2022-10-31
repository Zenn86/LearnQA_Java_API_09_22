package Exersizes;

import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex12Test extends BaseTestCase {
    private String url = "https://playground.learnqa.ru/api/homework_header";
    String expectedHeader = "x-secret-homework-header";
    String expectedValue = "Some secret value";

    @Test
    public void testHeader() {
        Response response = getResponse(url);
        assertEquals(expectedValue, getHeader(response, expectedHeader), "Header search error");
    }

}
