package pl.manager.library.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@ComponentScan({
        "pl.manager.library.authentication",
        "pl.manager.library.database",
        "pl.manager.library.gui",
        "pl.manager.library.core"
})
public class AppConfiguration {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
}