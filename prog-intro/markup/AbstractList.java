package markup;

import markup.interfaces.InList;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractList extends AbstractMarkupCollection implements InList {

    public AbstractList(List<ListItem> l) {
        super(new ArrayList<>(l));
    }

    @Override
    public void toMarkdown(StringBuilder s) {
        throw new UnsupportedOperationException("This class don't support toMarkdown()");
    }

    @Override
    String getLeftTexBorder() {
        return String.format("\\begin{%s}", getTexName());
    }

    @Override
    String getRightTexBorder() {
        return String.format("\\end{%s}", getTexName());
    }

    @Override
    String getMarkdownBorder() {
        throw new UnsupportedOperationException("This class don't support getMarkdownBorder");
    }

    protected abstract String getTexName();

}
