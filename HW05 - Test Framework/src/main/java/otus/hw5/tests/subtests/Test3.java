package otus.hw5.tests.subtests;

import junit.framework.Assert;
import otus.hw5.MyAfter;
import otus.hw5.MyBefore;
import otus.hw5.MyTest;

public class Test3 {
    private int a = 1;

    @MyTest
    public void test1() {
        Assert.assertEquals(a, a);
    }

    @MyTest
    public void test2() {
        Assert.assertEquals(2, a);
    }

    @MyTest
    public void test3() {
        Assert.assertEquals(3, a);
    }
}
