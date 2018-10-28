package kr.kangchun.demo13.app02;

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
public class Demo1302Application {

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
     * 2. generateHello()는 publisher 할 데이터가 미리 준비되어 있음.
     * @return
     */
    @GetMapping("/")
    Mono<String> hello() {
        log.info("pos1");
        String msg = generateHello();
        Mono m = Mono.just(msg).doOnNext(c->log.info(c)).log();
        log.info("pos2");
        return m;
    }

    private String generateHello() {
        log.info("method generateHello");
        return "Hello Mono";
    }

    public static void main(String[] args) {
        SpringApplication.run(Demo1302Application.class, args);
    }

}
