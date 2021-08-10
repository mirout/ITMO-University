import java.io.*;
import java.nio.charset.StandardCharsets;

class TestChecker implements Checker {
    public boolean isWordCharacter(char c) {
        return !Character.isWhitespace(c);
    }
}
public class Test {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in, new TestChecker());
            sc.readNewToken();
            System.out.println(sc.next());
            sc.readNewToken();
            System.out.println(sc.nextInt());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}