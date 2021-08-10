package markup;

import markup.interfaces.Markup;

import java.util.List;

public abstract class AbstractMarkupCollection implements Markup {

    final List<Markup> childes;

    public AbstractMarkupCollection(List<Markup> child) {
        this.childes = child;
    }

    @Override
    public void toTex(StringBuilder sb) {

        sb.append(getLeftTexBorder());

        for (var elem : childes) {
            elem.toTex(sb);
        }

        sb.append(getRightTexBorder());
    }

    @Override
    public void toMarkdown(StringBuilder sb) {

        sb.append(getMarkdownBorder());

        for (var elem : childes) {
            elem.toMarkdown(sb);
        }

        sb.append(getMarkdownBorder());
    }

    abstract String getLeftTexBorder();
    abstract String getRightTexBorder();
    abstract String getMarkdownBorder();
}
