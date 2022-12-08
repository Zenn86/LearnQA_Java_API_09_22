package Exersizes;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Ex9 {
    @Test
    public void testPassword() {

        List<String> passwords = new ArrayList<>();
//        try (FileReader f = new FileReader("src/test/resources/passwords.txt")) {
//            StringBuffer sb = new StringBuffer();
//            while (f.ready()) {
//                char c = (char) f.read();
//                if (c == '\n') {
//                    passwords.add(sb.toString());
//                    sb = new StringBuffer();
//                } else {
//                    sb.append(c);
//                }
//            }
//            if (sb.length() > 0) {
//                passwords.add(sb.toString());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        passwords.add("princess");
        passwords.add("welcome");
        passwords.add("admin");

        Map<String, String> authData1 = new HashMap<>();
        authData1.put("login", "super_admin");

        for (int i = 0; i < passwords.size(); i++) {
            authData1.put("password", passwords.get(i));
            Response response = RestAssured
                    .given()
                    .queryParams(authData1)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String auth_cookie = response.getCookie("auth_cookie");
            System.out.println("password №" + (i + 1) + ": " + passwords.get(i));
            if (testCookie(auth_cookie)) {
                System.out.println("Right cookie: " + auth_cookie);
                break;
            }
        }

    }

    public boolean testCookie(String inputCookie) {
        String rightAnswer = "You are authorized";
        Response responseForCookieCheck = RestAssured
                .given()
                .cookie("auth_cookie", inputCookie)
                .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                .andReturn();

        String answer = responseForCookieCheck.body().asString();
        System.out.println(answer);
        return answer.equals(rightAnswer);
    }

}
