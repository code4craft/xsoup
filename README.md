xsoup
----
XPath selector for Jsoup.

Usage:

```java
    public void testSelect() {
        String html = "<html><div><a href='https://github.com'>github.com</a></div></html>";

        String result = Xsoup.select(Jsoup.parse(html), "//a/@href").get();
        Assert.assertEquals("https://github.com", result);

        result = Xsoup.compile("//a/@href").evaluate(html).get();
        Assert.assertEquals("https://github.com", result);
    }
```