package pl.manager.library;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.manager.library.configuration.AppConfiguration;
import pl.manager.library.core.Core;

public class App {
    public static void main(String args[]) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        Core core = context.getBean(Core.class);
        core.run();
    }
}