package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Md2Html {

    public static void main (String... args) throws IOException {
        String result;
        try (var fr = new FileReader(args[0], StandardCharsets.UTF_8);
                var br = new BufferedReader(fr) ) {

            var fs = new FileSource(br);
            Md2HtmlParser parser = new Md2HtmlParser(fs);
            result = parser.parse();
        } catch (IOException | Md2HtmlParserException e) {
            System.out.println(e.getMessage());
            return;
        }

        try (var fs = new FileWriter(args[1], StandardCharsets.UTF_8);
                var writer = new BufferedWriter(fs)) {
            writer.write(result);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
