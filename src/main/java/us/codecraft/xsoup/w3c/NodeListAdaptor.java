package us.codecraft.xsoup.w3c;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * @author code4crafer@gmail.com
 */
public class NodeListAdaptor implements NodeList {

    private List<? extends org.jsoup.nodes.Node> nodes;

    public NodeListAdaptor(List<? extends org.jsoup.nodes.Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public Node item(int index) {
        return NodeAdaptors.getNode(nodes.get(index));
    }

    @Override
    public int getLength() {
        return nodes.size();
    }
}
