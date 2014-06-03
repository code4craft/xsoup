package us.codecraft.xsoup.w3c;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafer@gmail.com
 */
public class AttributesAdaptor {

    private Attributes attributes;

    private org.jsoup.nodes.Element element;

    private List<Attr> attrList;

    public AttributesAdaptor(Attributes attributes, Element element) {
        this.attributes = attributes;
        this.element = element;
        attrList = new ArrayList<Attr>();
        for (Attribute attribute : attributes) {
            attrList.add(new AttributeAdaptor(attribute,element));
        }
    }

    public List<Attr> get(){
        return attrList;
    }
}
