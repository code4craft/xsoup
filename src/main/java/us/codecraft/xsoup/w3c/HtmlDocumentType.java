package us.codecraft.xsoup.w3c;

import org.w3c.dom.*;

/**
 * @author code4crafer@gmail.com
 */
public class HtmlDocumentType extends NodeAdaptor implements DocumentType{

    private org.jsoup.nodes.Document document;

    public HtmlDocumentType(org.jsoup.nodes.Document document) {
        this.document = document;
    }

    @Override
    public String getNodeName() {
        return "html";
    }

    @Override
    public String getNodeValue() throws DOMException {
        return null;
    }

    @Override
    public short getNodeType() {
        return DOCUMENT_TYPE_NODE;
    }

    @Override
    public Node getParentNode() {
        return null;
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
        return NodeAdaptors.getDocument(document);
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
    public String getTextContent() throws DOMException {
        return document.text();
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

    @Override
    public String getName() {
        return "html";
    }

    @Override
    public NamedNodeMap getEntities() {
        return null;
    }

    @Override
    public NamedNodeMap getNotations() {
        return null;
    }

    @Override
    public String getPublicId() {
        return null;
    }

    @Override
    public String getSystemId() {
        return null;
    }

    @Override
    public String getInternalSubset() {
        return null;
    }
}
