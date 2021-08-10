package markup;

import markup.interfaces.InParagraph;
import markup.interfaces.Markup;

import java.util.ArrayList;
import java.util.List;

public class Emphasis extends AbstractInParagraphElement {

    public Emphasis(List<InParagraph> l) {
        super(new ArrayList<>(l));
    }

    @Override
    public String getMarkdownBorder() {
        return "*";
    }

    @Override
    protected String getTexName() {
        return "emph";
    }

}
