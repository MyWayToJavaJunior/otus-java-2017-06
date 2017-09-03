package context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Context {
    private static ApplicationContext applicationContext;

    private Context() {}

    public static ApplicationContext instance() {
        if (applicationContext == null) applicationContext = new AnnotationConfigApplicationContext(BeanConfig.class);
        return applicationContext;
    }
}
