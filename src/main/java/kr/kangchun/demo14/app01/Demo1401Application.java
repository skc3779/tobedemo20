package kr.kangchun.demo14.app01;

import kr.kangchun.demo12.app02.Demo02Application;
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
public class Demo1401Application {

    @Bean
    NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
        NettyReactiveWebServerFactory nettyReactiveWebServerFactory = new NettyReactiveWebServerFactory();
        nettyReactiveWebServerFactory.setPort(8080);
        return nettyReactiveWebServerFactory;
    }

    @GetMapping("/event/{id}")
    Mono<List<Event>> event(@PathVariable(name = "id") long id) {
        return Mono.just(Arrays.asList(new Event(id, "event : " + id),new Event(id+1, "event : " + (id+1l))));
    }

    @GetMapping("/events")
    Flux<Event> event() {
        return Flux.just(new Event(1L, "event : 1"), new Event(2L, "event : 2"));
    }

    public static void main(String[] args) {
        SpringApplication.run(Demo1401Application.class, args);
    }

    @Data @AllArgsConstructor
    public static class Event {
        long id;
        String value;
    }
}
