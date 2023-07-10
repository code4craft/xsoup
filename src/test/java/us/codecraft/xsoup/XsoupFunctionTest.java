package us.codecraft.xsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 函数测试类
 * @author liu xw
 * @date 2023 07-10
 */
public class XsoupFunctionTest {

    private String html = "<html>" +
        "<body>" +
        " <div id='test'>aaa</div>" +
        " <div id='test'>aaa" +
        "  <div>" +
        "   <a href=\"https://github.com\">github.com</a>" +
        "   <a href=\"https://github.com\">github.com</a>" +
        "   <a href=\"https://github2.com\">github2.com</a>" +
        "   <a href=\"https://github3.com\">github3.com</a>" +
        "  </div>" +
        " </div>" +
        "</body>" +
        "</html>";

    /**
     * 包含测试
     */
    @Test
    public void contains$testOne(){
        Document document = Jsoup.parse(html);
        String result = Xsoup.compile("//a[contains(:text:one,'github2.com')]/@href").evaluate(document).get();
        assertThat(result).isEqualTo("https://github2.com");
        System.out.println(result);
    }

    /**
     * 包含测试
     */
    @Test
    public void contains$testAll(){
        Document document = Jsoup.parse(html);
        List<String> list = Xsoup.compile("//a[contains(:text:all,'github')]/@href").evaluate(document).list();
        assertThat(list).hasSize(2);
        System.out.println(list);
    }

    /**
     * 包含测试
     */
    @Test
    public void contains$attribute(){
        Document document = Jsoup.parse(html);
        List<String> list = Xsoup.compile("//a[contains(:@href,'https:github.com')]/@href").evaluate(document).list();
        assertThat(list).hasSize(2);
        System.out.println(list);
    }

    /**
     * 包含测试
     */
    @Test
    public void contains$attributeNot(){
        Document document = Jsoup.parse(html);
        List<String> list = Xsoup.compile("//a[contains(:not:@href,'https://github.com')]/@href").evaluate(document).list();
        assertThat(list).hasSize(2);
        System.out.println(list);
    }

    /**
     * 包含测试
     */
    @Test
    public void contains$attributeStart(){
        Document document = Jsoup.parse(html);
        List<String> list = Xsoup.compile("//a[contains(:start:@href,'https://github2')]/@href").evaluate(document).list();
        assertThat(list).hasSize(1);
        System.out.println(list);

        List<String> list2 = Xsoup.compile("//a[contains(:start:@href,'https://github')]/@href").evaluate(document).list();
        assertThat(list2).hasSize(4);
        System.out.println(list2);
    }
}
