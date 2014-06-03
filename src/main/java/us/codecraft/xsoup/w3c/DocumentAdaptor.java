package us.codecraft.xsoup.w3c;

import org.w3c.dom.*;

import java.nio.charset.Charset;

/**
 * @author code4crafer@gmail.com
 */
public class DocumentAdaptor extends ElementAdaptor implements Document {

    private org.jsoup.nodes.Document document;

    public DocumentAdaptor(org.jsoup.nodes.Document document) {
        super(document);
        this.document = document;
    }

    @Override
    public DocumentType getDoctype() {
        return new HtmlDocumentType(document);
    }

    @Override
    public DOMImplementation getImplementation() {
        return null;
    }

    @Override
    public short getNodeType() {
        return DOCUMENT_NODE;
    }

    @Override
    public Element getDocumentElement() {
        return this;
    }

    @Override
    public Element getElementById(String elementId) {
        return NodeAdaptors.getElement(document.getElementById(elementId));
    }

    @Override
    public String getInputEncoding() {
        return Charset.defaultCharset().name();
    }

    @Override
    public String getXmlEncoding() {
        return Charset.defaultCharset().name();
    }

    @Override
    public boolean getXmlStandalone() {
        return false;
    }

    @Override
    public String getXmlVersion() {
        //TODO
        return null;
    }

    @Override
    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setXmlVersion(String xmlVersion) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getStrictErrorChecking() {
        return false;
    }

    @Override
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDocumentURI() {
        return document.baseUri();
    }

    @Override
    public void setDocumentURI(String documentURI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node adoptNode(Node source) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DOMConfiguration getDomConfig() {
        return null;
    }

    @Override
    public void normalizeDocument() {

    }

    @Override
    public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element createElement(String tagName) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DocumentFragment createDocumentFragment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Text createTextNode(String data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comment createComment(String data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CDATASection createCDATASection(String data) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Attr createAttribute(String name) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityReference createEntityReference(String name) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        throw new UnsupportedOperationException();
    }
}
