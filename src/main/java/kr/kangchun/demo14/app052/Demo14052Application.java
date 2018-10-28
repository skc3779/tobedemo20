package kr.kangchun.demo14.app052;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Created by skc37 on 2017-06-05.
 */
@Slf4j
@SpringBootApplication
@RestController
public class Demo14052Application {

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

    /**
     *  produces = MediaType.TEXT_EVENT_STREAM_VALUE 사용 시
     *  컬랙션을 이용해 한번에 주는 것이 아니라
     *  Stream 방식으로 제공
     *  fromStream을 이용해 10개의 take을 제공
     *
     *  Flux.generate 초기의 상태값은 1을 입력하고,
     *  다음 상태값은 초기의 상태 값에 1을 더하도록 함.
     * @return
     */
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> event() {
        return Flux.<Event, Long>generate(()->1L, (id, sink)-> {
            sink.next(new Event(id, "value:" + id));
            return id+1;
        })
        .delayElements(Duration.ofSeconds(1))
        .take(10);
    }

    public static void main(String[] args) {
        SpringApplication.run(Demo14052Application.class, args);
    }

    @Data @AllArgsConstructor
    public static class Event {
        long id;
        String value;
    }
}
