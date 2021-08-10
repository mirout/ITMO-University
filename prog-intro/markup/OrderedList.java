package markup;

import markup.interfaces.InList;
import markup.interfaces.Markup;

import java.util.ArrayList;
import java.util.List;

public class OrderedList extends AbstractList {

    public OrderedList(List<ListItem> l) {
        super(new ArrayList<>(l));
    }

    @Override
    protected String getTexName() {
        return "enumerate";
    }
}
