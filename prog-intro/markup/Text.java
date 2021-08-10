package markup;

import markup.interfaces.InParagraph;

public class Text implements InParagraph {
    private final String text;

    public Text(String s) {
        text = s;
    }
    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(text);
    }
    @Override
    public void toTex(StringBuilder sb) {
        sb.append(text);
    }
}
