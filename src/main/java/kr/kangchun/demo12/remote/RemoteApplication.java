package kr.kangchun.demo12.remote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by skc37 on 2017-03-11.
 */
@SpringBootApplication
public class RemoteApplication {

    @Bean
    public TomcatReactiveWebServerFactory tomcatReactiveWebServerFactory() {
        TomcatReactiveWebServerFactory tomcatReactiveWebServerFactory = new TomcatReactiveWebServerFactory();
        tomcatReactiveWebServerFactory.setPort(8081);
        return tomcatReactiveWebServerFactory;
    }

    @RestController
    public static class My1Controller {
        @GetMapping(name="service")
        public String service1(String req) throws InterruptedException {
            Thread.sleep(1000);
            return req + "/service1";
        }
    }

    public static void main(String[] args) {
        System.setProperty("SERVER_PORT","8082");
        System.setProperty("server.tomcat.max-threads","200");
        SpringApplication.run(RemoteApplication.class, args);
    }

}
