package otus.hw5.tests;

import junit.framework.Assert;
import otus.hw5.MyAfter;
import otus.hw5.MyBefore;
import otus.hw5.MyTest;

public class Test1 {
    private int a = 1;

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
        Assert.assertEquals(3, 3);
    }

    @MyTest
    public void test3() {
        Assert.assertEquals(5, a);
    }


    @MyAfter
    public void after() {
        System.out.println("Hello from after :)");
    }
}
