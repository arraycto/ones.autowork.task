package ones.autowork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by liYueYang on 2020/6/11.
 */
@SpringBootApplication
//@ImportResource(locations = "file:./config/spring.xml")
@ImportResource(locations = "spring.xml")
@EnableScheduling
public class Start extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Start.class);
    }

}
