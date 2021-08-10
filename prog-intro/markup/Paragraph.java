package markup;

import markup.interfaces.InList;
import markup.interfaces.InParagraph;

import java.util.ArrayList;
import java.util.List;

public class Paragraph extends AbstractMarkupCollection implements InList {

    public Paragraph(List<InParagraph> l) {
        super(new ArrayList<>(l));
    }

    @Override
    String getLeftTexBorder() {
        return "";
    }

    @Override
    String getRightTexBorder() {
        return "";
    }

    @Override
    String getMarkdownBorder() {
        return "";
    }

}
