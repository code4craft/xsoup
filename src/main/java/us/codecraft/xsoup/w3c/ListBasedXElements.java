package us.codecraft.xsoup.w3c;

import us.codecraft.xsoup.XElements;

import java.util.List;

/**
 * @author code4crafer@gmail.com
 */
public class ListBasedXElements implements XElements {

    private final List<String> list;

    public ListBasedXElements(List<String> list) {
        this.list = list;
    }

    @Override
    public String get() {
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<String> list() {
        return list;
    }
}
