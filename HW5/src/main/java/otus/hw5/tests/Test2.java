package otus.hw5.tests;

import junit.framework.Assert;
import otus.hw5.MyAfter;
import otus.hw5.MyBefore;
import otus.hw5.MyTest;

public class Test2 {
    private int a = 4;

    @MyBefore
    public void before() {
        a++;
    }

    @MyTest
    public void test1() {
        Assert.assertEquals(2, a);
    }

    @MyTest
    public void test2() {
        Assert.assertEquals(6, a);
    }
}
