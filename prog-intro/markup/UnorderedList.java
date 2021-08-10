package markup;

import java.util.ArrayList;
import java.util.List;

public class UnorderedList extends  AbstractList {
    public UnorderedList(List<ListItem> l) {
        super(new ArrayList<>(l));
    }

    @Override
    protected String getTexName() {
        return "itemize";
    }
}
