Xsoup
----
XPath selector for Jsoup.

>Under developing yet!

## Get started:

```java
    @Test
    public void testSelect() {
        String html = "<html><div><a href='https://github.com'>github.com</a></div></html>";

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "//a/@href").get();
        Assert.assertEquals("https://github.com", result);

        result = Xsoup.compile("//a/@href").evaluate(document).get();
        Assert.assertEquals("https://github.com", result);
    }
```

## Why:

Compare with another XPath selector for HTML [**`HtmlCleaner`**](http://htmlcleaner.sourceforge.net/),Jsoup is much faster:

	Normal HTML, size 44KB
	XPath: "//a"	| 	CSS Selector: "a"
	Run for 2000 times

	Environment：Mac Air MD231CH/A 
	CPU: 1.8Ghz Intel Core i5

<table>
    <tr>
        <td width="100">Operation</td>
        <td width="100">Jsoup</td>
        <td>HtmlCleaner</td>
    </tr>
    <tr>
        <td>parse</td>
        <td>3,207(ms)</td>
        <td>7,999(ms)</td>
    </tr>
    <tr>
        <td>select</td>
        <td>99(ms)</td>
        <td>380(ms)</td>
    </tr>
</table>

So I try to apply XPath selector to Jsoup for better performance!