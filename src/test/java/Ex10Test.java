import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Ex10Test {

    @ParameterizedTest
    @ValueSource(strings = {"It's a good phrase for this test", "Test to fail", "And this is another one good phrase for this test"})
    public void testPhrase(String phrase) {
        assertTrue(phrase.length() > 15, phrase + ". <--Length of this phrase is under 15 symbols");
    }
}
