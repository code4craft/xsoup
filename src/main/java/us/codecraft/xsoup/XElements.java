package us.codecraft.xsoup;

import java.util.List;
import org.jsoup.select.Elements;

/**
 * @author code4crafter@gmail.com
 */
public interface XElements {

    String get();

    List<String> list();

    Elements getElements();
}
