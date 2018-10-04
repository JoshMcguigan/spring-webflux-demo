package com.joshmcguigan.springwebfluxdemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class DemoController {
    @GetMapping("/greeting")
    @ResponseBody
    public String greeting() {
        return "greetings";
    }

    @GetMapping("/single")
    @ResponseBody
    public Mono<HttpBinResponse> single() {
        WebClient webClient = WebClient.create();

        Mono<HttpBinResponse> response = webClient.get().uri("https://httpbin.org/delay/2").retrieve().bodyToMono(HttpBinResponse.class);

        return response;
    }

    @GetMapping("/parallel")
    @ResponseBody
    public Flux<HttpBinResponse> serial() {
        WebClient webClient = WebClient.create();

        List<Mono<HttpBinResponse>> responses = new LinkedList<>();

        for (int i = 1; i <= 3; i++) {
            String url = String.format("https://httpbin.org/delay/%d", i);

            Mono<HttpBinResponse> response = webClient.get().uri(url).retrieve().bodyToMono(HttpBinResponse.class);
            responses.add(response);
        }


        return Flux.merge(responses);
    }

    @GetMapping("/parallel-many")
    @ResponseBody
    public Flux<HttpBinResponse> parallelMany() {
        WebClient webClient = WebClient.create();

        List<Mono<HttpBinResponse>> responses = new LinkedList<>();

        for (int i = 1; i <= 3; i++) {
            String url = String.format("https://httpbin.org/delay/%d", i);
            for (int j = 0; j < 4; j++) {
                Mono<HttpBinResponse> response = webClient.get().uri(url).retrieve().bodyToMono(HttpBinResponse.class);
                responses.add(response);
            }
        }

        return Flux.merge(responses);
    }

    @GetMapping("/parallel-overload")
    @ResponseBody
    public Flux<HttpBinResponse> parallelOverload() throws ExecutionException, InterruptedException {
        WebClient webClient = WebClient.create();

        List<Mono<HttpBinResponse>> responses = new LinkedList<>();

        for (int i = 1; i <= 3; i++) {
            String url = String.format("https://httpbin.org/delay/%d", i);
            for (int j = 0; j < 50; j++) {
                Mono<HttpBinResponse> response = webClient.get().uri(url).retrieve().bodyToMono(HttpBinResponse.class);
                responses.add(response);
            }
        }

        return Flux.merge(responses);
    }

    @GetMapping("/alot")
    @ResponseBody
    public Flux<HttpBinResponse> alot() throws ExecutionException, InterruptedException {
        WebClient webClient = WebClient.create();

        List<Mono<HttpBinResponse>> responses = new LinkedList<>();

        for (int i = 1; i <= 3; i++) {
            String url = String.format("https://httpbin.org/delay/%d", i);
            for (int j = 0; j < 500; j++) {
                Mono<HttpBinResponse> response = webClient.get().uri(url).retrieve().bodyToMono(HttpBinResponse.class);
                responses.add(response);
            }
        }

        return Flux.merge(responses);
    }
}
