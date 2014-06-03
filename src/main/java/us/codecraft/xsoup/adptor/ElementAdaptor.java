package us.codecraft.xsoup.adptor;

import org.jsoup.nodes.Attribute;
import org.w3c.dom.*;

/**
 * @author code4crafer@gmail.com
 */
public class ElementAdaptor extends NodeAdaptor implements Element {

    private org.jsoup.nodes.Element element;

    public ElementAdaptor(org.jsoup.nodes.Element element) {
        this.element = element;
    }

    @Override
    public String getTagName() {
        return element.tagName();
    }

    @Override
    public String getAttribute(String name) {
        return element.attr(name);
    }

    @Override
    public Attr getAttributeNode(String name) {
        if (element.attr(name) == null) {
            return null;
        }
        return new AttributeAdaptor(new Attribute(name, element.attr(name)), element);
    }

    @Override
    public NodeList getElementsByTagName(String name) {
        return new NodeListAdaptor(element.getElementsByTag(name));
    }

    @Override
    public boolean hasAttribute(String name) {
        return element.hasAttr(name);
    }

    @Override
    public TypeInfo getSchemaTypeInfo() {
        return DummyTypeInfo.getInstance();
    }

    @Override
    public String getNodeName() {
        return element.nodeName();
    }

    @Override
    public String getNodeValue() throws DOMException {
        return element.val();
    }

    @Override
    public short getNodeType() {
        return ELEMENT_NODE;
    }

    @Override
    public Node getParentNode() {
        return new ElementAdaptor(element.parent());
    }

    @Override
    public NodeList getChildNodes() {
        return new NodeListAdaptor(element.childNodes());
    }

    @Override
    public Node getFirstChild() {
        return NodeAdaptorFactory.getNode(element.child(0));
    }

    @Override
    public Node getLastChild() {
        return NodeAdaptorFactory.getNode(element.child(element.childNodeSize()));
    }

    @Override
    public Node getPreviousSibling() {
        return NodeAdaptorFactory.getNode(element.previousSibling());
    }

    @Override
    public Node getNextSibling() {
        return NodeAdaptorFactory.getNode(element.nextSibling());
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
        return null;
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

    /*----------------------------- update - not support-------------*/
    @Override
    public void setAttribute(String name, String value) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttribute(String name) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIdAttribute(String name, boolean isId) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        throw new UnsupportedOperationException();
    }

    /*--------------------- NS not supported ----------------*/

    @Override
    public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException();
    }

}
