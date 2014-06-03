package us.codecraft.xsoup.adptor;

import org.jsoup.nodes.Element;
import org.w3c.dom.Node;

/**
 * @author code4crafer@gmail.com
 */
public class NodeAdaptorFactory {

    public static Node getNode(org.jsoup.nodes.Node node) {
        if (node == null) {
            return null;
        }
        if (node instanceof Element) {
            return new ElementAdaptor((Element) node);
        }
        return null;
    }

}
