package markup;


import markup.interfaces.InParagraph;

import java.util.ArrayList;
import java.util.List;

public class Strong extends AbstractInParagraphElement {

    public Strong(List<InParagraph> l) {
        super(new ArrayList<>(l));
    }

    @Override
    public String getMarkdownBorder() {
        return "__";
    }

    @Override
    protected String getTexName() {
        return "textbf";
    }

}
