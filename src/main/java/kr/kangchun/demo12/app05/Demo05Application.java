package kr.kangchun.demo12.app05;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Tobe12
 * Reactive Programming - WebFlux - SpringBoot 2.0 M1, SpringFramework 5.0 RC1
 */
@Slf4j
@SpringBootApplication
public class Demo05Application {
    @Bean
    NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
        NettyReactiveWebServerFactory nettyReactiveWebServerFactory = new NettyReactiveWebServerFactory();
        nettyReactiveWebServerFactory.setPort(8080);
        return nettyReactiveWebServerFactory;
    }

    @RestController
    public static class HelloController {

        @RequestMapping(name="/hello", method= RequestMethod.GET)
        public Mono<String> hello() {
            log.info("log 1");

            //Mono m = Mono.just("Hello WebFlux").log();

            //Mono m = Mono.just("Hello WebFlux").doOnNext(c->log.info(c)).log();

            //String msg = generateHello();
            //Mono m = Mono.just(msg).doOnNext(c->log.info(c)).log();


            Mono m = Mono.fromSupplier(()->generateHello()).doOnNext(c->log.info(c)).log();
            log.info("log 2");

            return m;
        }

        private String generateHello() {
            log.info("generate Hello");
            return "Hello WebFlux";
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(Demo05Application.class, args);
    }
}
