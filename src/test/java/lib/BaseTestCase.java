package lib;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BaseTestCase {

    protected Response getResponce(String url) {
        Response response = RestAssured
                .get(url)
                .andReturn();
        return response;
    }

    protected String getHeader(Response response, String name) {
        Headers headers = response.getHeaders();
        assertTrue(headers.hasHeaderWithName(name), name + ": This header isn't found");
        return headers.getValue(name);
    }

    protected String getCookie(Response response, String name) {
        Map<String, String> cookies = response.getCookies();
        assertTrue(cookies.containsKey(name), name + ": This cookie isn't found");
        return cookies.get(name);
    }
}
