package markup;

import markup.interfaces.InParagraph;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInParagraphElement extends AbstractMarkupCollection implements InParagraph {

    public AbstractInParagraphElement(List<InParagraph> l) {
        super(new ArrayList<>(l));
    }

    @Override
    String getLeftTexBorder() {
        return String.format("\\%s{", getTexName());
    }

    @Override
    String getRightTexBorder() {
        return "}";
    }

    protected abstract String getTexName();
}
