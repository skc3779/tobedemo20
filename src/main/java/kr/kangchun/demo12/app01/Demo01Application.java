package kr.kangchun.demo12.app01;

import io.netty.channel.nio.NioEventLoopGroup;
import jdk.nashorn.internal.ir.annotations.Ignore;
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
public class Demo01Application {

    @RestController
    public static class HelloController {

        @RequestMapping(name="/hello", method= RequestMethod.GET)
        public Mono<String> hello() {
            log.info("log 1");
            //Mono m = Mono.just("Hello WebFlux").log();
            Mono m = Mono.just("Hello WebFlux").doOnNext(c->log.info(c)).log();
            log.info("log 2");
            return m;
        }

    }

    @RestController
    public static class MyController {

        AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
        private String REMOTE_URL = "http://localhost:8081/demo09?req={req}";
        private String REMOTE2_URL = "http://localhost:8082/service2?req={req}";

        @Bean
        NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
            NettyReactiveWebServerFactory nettyReactiveWebServerFactory = new NettyReactiveWebServerFactory();
            nettyReactiveWebServerFactory.setPort(8080);
            return nettyReactiveWebServerFactory;
        }

        @Autowired
        MyService myService;

        WebClient client = WebClient.create();

        /**
         * Mono 파라메터를 한꺼번에 던질때 활용
         * @param idx
         * @return Mono and Flux 로 리턴
         */
        @RequestMapping(name="/rest", method= RequestMethod.GET)
        //@GetMapping(name="/rest")
        public Mono<String> rest(int idx) {
            // List<String> : 리스트라는 컨테이너에 스트링값을 넣은 것
            // Mono<String> m = Mono.just("Hello "+ idx);
            // return m;

            // 해당 라인만으로는 API 호출되지 않는다.
            Mono<ClientResponse> exchange = client.get().uri(REMOTE_URL, idx).exchange();

            // flatMap을 이용해서 Mono Type으로 리턴 받는다.
            // Mono<String> stringMono = exchange.flatMap(cr -> cr.bodyToMono(String.class));
            // return stringMono;

            // Mono 변수에 담지 않고 바로 리턴한다.
            return exchange.flatMap(cr->cr.bodyToMono(String.class));
        }
    }

    /**
     * MyService는 이제 비동기로 작업시킬 필요가 없음.
     * CompletableFuture의 thenApplyAsync 메소드 사용.
     */
    @Service
    public static class MyService {
        public String work(String req) {
            return req + "/asyncwork3";
        }
    }

    @Bean
    public ThreadPoolTaskExecutor myThreadPool() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(1);
        te.setMaxPoolSize(10);
        te.initialize();
        return te;
    }

    public static void main(String[] args) {
        System.setProperty("reactor.ipc.netty.workerCount", "2");
        System.setProperty("reactor.ipc.netty.pool.maxConnections", "2000");
        SpringApplication.run(Demo01Application.class, args);
    }
}
