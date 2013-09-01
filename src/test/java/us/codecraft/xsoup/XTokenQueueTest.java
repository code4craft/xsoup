package us.codecraft.xsoup;

import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author code4crafter@gmail.com
 */
public class XTokenQueueTest {

    @Test
    public void testParseFuncionParams(){
        List<String> list = XTokenQueue.parseFuncionParams("a,b,c");
        Assert.assertTrue(list.size()==3);

        list = XTokenQueue.parseFuncionParams("'a,b',c");
        Assert.assertTrue(list.size()==2);

        list = XTokenQueue.parseFuncionParams("'a,\\'b',c");
        Assert.assertTrue(list.size()==2);

        list = XTokenQueue.parseFuncionParams("@a,1,c");
        Assert.assertTrue(list.size()==3);

    }
}
