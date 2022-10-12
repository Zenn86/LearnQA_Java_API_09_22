package lib;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
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

    protected JsonPath getJsonPath(String url) {
        JsonPath response = RestAssured
                .get(url)
                .jsonPath();
        return response;
    }

    protected JsonPath getJsonPathWithUserAgent(String url, String name) {
        Map<String, String> headerUserAgent = new HashMap<>();
        headerUserAgent.put("User-Agent", name);

        JsonPath response = RestAssured
                .given()
                .headers(headerUserAgent)
                .when()
                .get(url)
                .jsonPath();
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

    protected String getJsonValue(JsonPath response, String key) {
        String value = response.get(key);
        assertFalse(value.equals(null));
        return value;
    }
}
