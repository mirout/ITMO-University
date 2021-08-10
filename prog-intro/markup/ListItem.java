package markup;

import markup.interfaces.InList;

import java.util.ArrayList;
import java.util.List;

public class ListItem extends AbstractMarkupCollection {

    public ListItem(List<InList> l) {
        super(new ArrayList<>(l));
    }

    @Override
    String getLeftTexBorder() {
        return "\\item ";
    }

    @Override
    String getRightTexBorder() {
        return "";
    }

    @Override
    String getMarkdownBorder() {
        throw new UnsupportedOperationException("This class don't support getMarkdownBorder()");
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        throw new UnsupportedOperationException("This class don't support toMarkdown()");
    }
}
