package otus.hw5;

import otus.hw5.tests.Test1;
import otus.hw5.tests.Test2;
import otus.hw5.tests.subtests.Test3;

public class Main {
    public static void main(String[] args) {
        Class<?>[] clazzes = {Test1.class, Test3.class};
        TestFramework.run(clazzes);

        System.out.println("\n\n-------------------------------------------------------------------\n");

        TestFramework.run("otus.hw5.tests");
    }
}
