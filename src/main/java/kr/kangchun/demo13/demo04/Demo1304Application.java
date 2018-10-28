package kr.kangchun.demo13.demo04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Created by skc37 on 2017-06-05.
 */
@Slf4j
@SpringBootApplication
@RestController
public class Demo1304Application {

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
        String msg = generateHello();
        Mono<String> m = Mono.just(msg).doOnNext(log::info).log();
        String msg2 = m.block();

        log.info("pos2 : " + msg2);

        return m;
    }

    private String generateHello() {
        log.info("method generateHello");
        return "Hello Mono";
    }

    public static void main(String[] args) {
        SpringApplication.run(Demo1304Application.class, args);
    }

}
