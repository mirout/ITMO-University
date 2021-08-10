package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FileSource {

    private final BufferedReader reader;
    private String line = "";
    private boolean isParagraphSaved = false;
    private StringBuilder paragraph;

    public FileSource(final BufferedReader reader) throws IOException {
        this.reader = reader;
    }

    public boolean hasNextParagraph() throws IOException {
        readParagraph();
        return isParagraphSaved;
    }

    private String nextLine() throws IOException {
        return line = reader.readLine();
    }

    public String nextParagraph() throws IOException {
        readParagraph();
        if (isParagraphSaved) {
            isParagraphSaved = false;
            return paragraph.toString();
        }
        throw new NoSuchElementException("Don't have next paragraph");
    }

    private void readParagraph() throws IOException {
        if (isParagraphSaved || line == null) {
            return;
        }

        while (line != null && line.isEmpty()) {
            nextLine();
        }

        paragraph = new StringBuilder();

        paragraph.append(line);

        while (nextLine() != null && !line.isEmpty()) {
            paragraph.append("\n");
            paragraph.append(line);
        }

        isParagraphSaved = true;
    }
}
