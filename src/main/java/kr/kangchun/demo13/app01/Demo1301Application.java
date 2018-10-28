package kr.kangchun.demo13.app01;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * Created by skc37 on 2017-06-05.
 */
@Slf4j
@SpringBootApplication
@RestController
public class Demo1301Application {

    @Bean
    NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
        NettyReactiveWebServerFactory nettyReactiveWebServerFactory = new NettyReactiveWebServerFactory();
        nettyReactiveWebServerFactory.setPort(8080);
        return nettyReactiveWebServerFactory;
    }

    @GetMapping("/")
    Mono<String> hello() {
        log.info("pos1");
        Mono m = Mono.just("Hello WebFlux").doOnNext(c->log.info(c)).log();
        log.info("pos2");
        return m;
    }

    public static void main(String[] args) {
        SpringApplication.run(Demo1301Application.class, args);
    }

}
