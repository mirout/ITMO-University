package markup;

import markup.interfaces.InParagraph;

import java.util.ArrayList;
import java.util.List;

public class Strikeout extends AbstractInParagraphElement {

    public Strikeout(List<InParagraph> l) {
        super(new ArrayList<>(l));
    }

    @Override
    protected String getMarkdownBorder() {
        return "~";
    }

    @Override
    protected String getTexName() {
        return "textst";
    }

}
