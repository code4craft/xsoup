package us.codecraft.xsoup.w3c;

import org.jsoup.nodes.*;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author code4crafer@gmail.com
 */
public class AttributeAdaptor extends NodeAdaptor implements Attr {

    private Attribute attribute;

    private org.jsoup.nodes.Element element;

    public AttributeAdaptor(Attribute attribute, org.jsoup.nodes.Element element) {
        this.attribute = attribute;
        this.element = element;
    }

    @Override
    public String getName() {
        return attribute.getKey();
    }

    @Override
    public boolean getSpecified() {
        return false;
    }

    @Override
    public String getValue() {
        return attribute.getValue();
    }

    @Override
    public void setValue(String value) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element getOwnerElement() {
        return NodeAdaptors.getElement(element);
    }

    @Override
    public TypeInfo getSchemaTypeInfo() {
        return new DummyTypeInfo();
    }

    @Override
    public boolean isId() {
        return false;
    }

    @Override
    public String getNodeName() {
        return attribute.getKey();
    }

    @Override
    public String getNodeValue() throws DOMException {
        return attribute.getValue();
    }

    @Override
    public short getNodeType() {
        return ATTRIBUTE_NODE;
    }

    @Override
    public Node getParentNode() {
        return new ElementAdaptor(element);
    }

    @Override
    public NodeList getChildNodes() {
        return null;
    }

    @Override
    public Node getFirstChild() {
        return null;
    }

    @Override
    public Node getLastChild() {
        return null;
    }

    @Override
    public Node getPreviousSibling() {
        return null;
    }

    @Override
    public Node getNextSibling() {
        return null;
    }

    @Override
    public NamedNodeMap getAttributes() {
        return null;
    }

    @Override
    public Document getOwnerDocument() {
        return new DocumentAdaptor(element.ownerDocument());
    }

    @Override
    public boolean hasChildNodes() {
        return false;
    }

    @Override
    public Node cloneNode(boolean deep) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAttributes() {
        return false;
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return 0;
    }

    @Override
    public String getTextContent() throws DOMException {
        return attribute.getValue();
    }

    @Override
    public boolean isSameNode(Node other) {
        return false;
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return false;
    }

    @Override
    public Object getUserData(String key) {
        return null;
    }
}
