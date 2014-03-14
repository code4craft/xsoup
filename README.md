Xsoup
----
[![Build Status](https://api.travis-ci.org/code4craft/xsoup.png?branch=master)](https://travis-ci.org/code4craft/xsoup)

XPath selector based on Jsoup.

## Get started:

```java
    @Test
    public void testSelect() {

        String html = "<html><div><a href='https://github.com'>github.com</a></div>" +
                "<table><tr><td>a</td><td>b</td></tr></table></html>";

        Document document = Jsoup.parse(html);

        String result = Xsoup.compile("//a/@href").evaluate(document).get();
        Assert.assertEquals("https://github.com", result);

        List<String> list = Xsoup.compile("//tr/td/text()").evaluate(document).list();
        Assert.assertEquals("a", list.get(0));
        Assert.assertEquals("b", list.get(1));
    }
```

## Performance:

Xsoup use Jsoup as HTML parser. 

Compare with another most used XPath selector for HTML - [**`HtmlCleaner`**](http://htmlcleaner.sourceforge.net/), Xsoup is much faster:

	Normal HTML, size 44KB
	XPath: "//a"	
	Run for 2000 times

	Environment：Mac Air MD231CH/A 
	CPU: 1.8Ghz Intel Core i5

<table>
    <tr>
        <td width="100">Operation</td>
        <td width="100">Xsoup</td>
        <td>HtmlCleaner</td>
    </tr>
    <tr>
        <td>parse</td>
        <td>3,207(ms)</td>
        <td>7,999(ms)</td>
    </tr>
    <tr>
        <td>select</td>
        <td>95(ms)</td>
        <td>380(ms)</td>
    </tr>
</table>

## Syntax supported:

### XPath1.0:

<table>
    <tr>
        <td width="100">Name</td>
        <td width="100">Expression</td>
        <td>Support</td>
    </tr>
    <tr>
        <td>nodename</td>
        <td>nodename</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>immediate parent</td>
        <td>/</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>parent</td>
        <td>//</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>attribute</td>
        <td>[@key=value]</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>nth child</td>
        <td>tag[n]</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>attribute</td>
        <td>/@key</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>wildcard in tagname</td>
        <td>/*</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>wildcard in attribute</td>
        <td>/[@*]</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>function</td>
        <td>function()</td>
        <td>part</td>
    </tr>
    <tr>
        <td>or</td>
        <td>a | b</td>
        <td>yes since 0.2.0</td>
    </tr>
    <tr>
        <td>parent in path</td>
        <td>. or ..</td>
        <td>no</td>
    </tr>
    <tr>
        <td>predicates</td>
        <td>price>35</td>
        <td>no</td>
    </tr>
    <tr>
        <td>predicates logic</td>
        <td>@class=a or @class=b</td>
        <td>yes since 0.2.0</td>
    </tr>
</table>

### Function supported:

In Xsoup, we use some function (maybe not in Standard XPath 1.0):

<table>
    <tr>
        <td width="100">Expression</td>
        <td width="100">Description</td>
        <td>Standard XPath</td>
    </tr>
    <tr>
        <td width="100">text(n)</td>
        <td width="100">nth text content of element(0 for all)</td>
        <td>text() only</td>
    </tr>
        <tr>
        <td width="100">allText()</td>
        <td width="100">text including children</td>
        <td>not support</td>
    </tr>
    </tr>
        <tr>
        <td width="100">tidyText()</td>
        <td width="100">text including children, well formatted</td>
        <td>not support</td>
    </tr>
    <tr>
        <td width="100">html()</td>
        <td width="100">innerhtml of element</td>
        <td>not support</td>
    </tr>
    <tr>
        <td width="100">outerHtml()</td>
        <td width="100">outerHtml of element</td>
        <td>not support</td>
    </tr>
    <tr>
        <td width="100">regex(@attr,expr,group)</td>
        <td width="100">use regex to extract content</td>
        <td>not support</td>
    </tr>
</table>

### Extended syntax supported:

These XPath syntax are extended only in Xsoup (for convenience in extracting HTML, refer to Jsoup CSS Selector):

<table>
    <tr>
        <td width="100">Name</td>
        <td width="100">Expression</td>
        <td>Support</td>
    </tr>
    <tr>
        <td>attribute value not equals</td>
        <td>[@key!=value]</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>attribute value start with</td>
        <td>[@key~=value]</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>attribute value end with</td>
        <td>[@key$=value]</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>attribute value contains</td>
        <td>[@key*=value]</td>
        <td>yes</td>
    </tr>
    <tr>
        <td>attribute value match regex</td>
        <td>[@key~=value]</td>
        <td>yes</td>
    </tr>
</table>

## License

MIT License, see file `LICENSE`

[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/code4craft/xsoup/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

