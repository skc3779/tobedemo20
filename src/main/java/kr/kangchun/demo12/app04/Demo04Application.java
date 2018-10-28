package kr.kangchun.demo12.app04;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * Tobe12
 * Reactive Programming - WebFlux - SpringBoot 2.0 M1, SpringFramework 5.0 RC1
 */
@Slf4j
@SpringBootApplication
public class Demo04Application {

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
         * 비동기 논블록킹 방ㅎ식으로 동작하는 지 확인함.
         * @param idx
         * @return Mono and Flux 로 리턴
         */
       @GetMapping(name="/rest")
        public Mono<String> rest(int idx) {

            // 2개의 API를 비동기로 호출하고 추가로 비동기 메소드를 호출
            return  client.get().uri(REMOTE_URL, idx).exchange()                                // Mono<ClientResponse>
                    .flatMap(cr->cr.bodyToMono(String.class))                                   // Mono<String>
                    .doOnNext(c->log.info("1:{}",c))
                    .flatMap((String res1) -> client.get().uri(REMOTE2_URL, res1).exchange())   // Mono<ClientResponse>
                    .flatMap(cr->cr.bodyToMono(String.class))                                   // Mono<String>
                    .doOnNext(c->log.info("2:{}",c))
                    .flatMap((String res2) -> Mono.fromCompletionStage(myService.work(res2)))  // CompletableFuture -> Mono<String> 변환하기 위해서 Mono.fromCompletionStage 사용.
                    .doOnNext(c->log.info("3:{}",c));
       }
    }

    /**
     * MyService는 이제 비동기로 작업시킬 필요가 없음.
     * CompletableFuture의 thenApplyAsync 메소드 사용.
     */
    @Service
    public static class MyService {
        @Async
        public CompletableFuture<String> work(String req) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
            return CompletableFuture.completedFuture(req + "/asyncwork3");
        }
    }

    @Bean
    public ThreadPoolTaskExecutor myThreadPool() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(10);
        te.setMaxPoolSize(100);
        te.initialize();
        return te;
    }

    public static void main(String[] args) {
        System.setProperty("reactor.ipc.netty.workerCount", "100");
        System.setProperty("reactor.ipc.netty.pool.maxConnections", "2000");
        SpringApplication.run(Demo04Application.class, args);
    }
}
