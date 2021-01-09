package us.codecraft.xsoup.w3c;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author code4crafer@gmail.com
 */
public class NamedNodeMapAdaptor implements NamedNodeMap {

    private List<? extends Node> nodeList;
    private Map<String, Node> nodeMap;

    public NamedNodeMapAdaptor(List<? extends Node> nodeList) {
        this.nodeList = nodeList;
        nodeMap = new HashMap<String, Node>(nodeList.size());
        for (Node node : nodeList) {
            nodeMap.put(node.getNodeName(), node);
        }
    }

    @Override
    public Node getNamedItem(String name) {
        return nodeMap.get(name);
    }

    @Override
    public Node setNamedItem(Node arg) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node removeNamedItem(String name) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node item(int index) {
        return nodeList.get(index);
    }

    @Override
    public int getLength() {
        return nodeList.size();
    }

    @Override
    public Node getNamedItemNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node setNamedItemNS(Node arg) throws DOMException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException();
    }
}
