package kr.kangchun.demo13.app03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * Created by skc37 on 2017-06-05.
 */
@Slf4j
@SpringBootApplication
@RestController
public class Demo1303Application {

    @Bean
    NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
        NettyReactiveWebServerFactory nettyReactiveWebServerFactory = new NettyReactiveWebServerFactory();
        nettyReactiveWebServerFactory.setPort(8080);
        return nettyReactiveWebServerFactory;
    }

    /**
     * 실행 순서를 확인
     *
     * 1. pos1
     * 2. generateHello
     * 3. pos2
     *
     * 2. Mono가 (호출)리턴하는 시점에 함수를 호출하고 싶다면.
     *    함수형 방식으로 처리함.
     * @return
     */
    @GetMapping("/")
    Mono<String> hello() {
        log.info("pos1");
//  아래와 같은 방식의 람다식으로 변환.
//        Mono m = Mono.fromSupplier(new Supplier<String>() {
//            @Override
//            public String get() {
//                return generateHello();
//            }
//        });
//      Mono m = Mono.fromSupplier(()->generateHello()).doOnNext(c->log.info(c)).log();
        Mono m = Mono.fromSupplier(this::generateHello).doOnNext(log::info).log();
        log.info("pos2");
        return m;
    }

    private String generateHello() {
        log.info("method generateHello");
        return "Hello Mono";
    }

    public static void main(String[] args) {
        SpringApplication.run(Demo1303Application.class, args);
    }

}
