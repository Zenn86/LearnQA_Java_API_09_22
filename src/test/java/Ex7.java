import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex7 {

    @Test
    public void testLongRedirect() {
        String path = "https://playground.learnqa.ru/api/long_redirect";
        int countOfRedirects = 0;
        boolean toContinue = true;

        while (toContinue) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(path)
                    .andReturn();

            path = response.getHeader("location");
            countOfRedirects++;
            if (path != null) {
                System.out.println("Redirect path №" + countOfRedirects + ": " + path);
            }

            if (response.getStatusCode() == 200) {
                toContinue = false;
                countOfRedirects--;
            }
        }

        System.out.println("Quantity of redirects: " + countOfRedirects);
    }
}
