package com.joshmcguigan.springwebfluxdemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Controller
public class DemoController {

    @GetMapping("/greeting")
    @ResponseBody
    public String greeting() {
        return "greetings";
    }

    @GetMapping("/single")
    @ResponseBody
    public HttpBinResponse single() {
        RestTemplate restTemplate = new RestTemplate();

        HttpBinResponse response = restTemplate.getForObject("https://httpbin.org/delay/2", HttpBinResponse.class);

        return response;
    }

    @GetMapping("/serial")
    @ResponseBody
    public List<HttpBinResponse> serial() {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpBinResponse> responses = new LinkedList<>();

        for (int i = 1; i <= 3; i++) {
            String url = String.format("https://httpbin.org/delay/%d", i);

            HttpBinResponse response = restTemplate.getForObject(url, HttpBinResponse.class);
            responses.add(response);
        }


        return responses;
    }

    @GetMapping("/parallel")
    @ResponseBody
    public List<HttpBinResponse> parallel() {
        RestTemplate restTemplate = new RestTemplate();

        List<String> urls = new LinkedList<>();

        for (int i = 1; i <= 3; i++) {
            String url = String.format("https://httpbin.org/delay/%d", i);
            urls.add(url);
        }

        List<HttpBinResponse> responses = urls.parallelStream() // this could be stream() instead of parallel stream
                .map(url -> restTemplate.getForObject(url, HttpBinResponse.class))
                .collect(Collectors.toList());

        return responses;
    }

    @GetMapping("/parallel-many")
    @ResponseBody
    public List<HttpBinResponse> parallelMany() {
        RestTemplate restTemplate = new RestTemplate();

        List<String> urls = new LinkedList<>();

        // more urls than I have cores
        for (int i = 1; i <= 3; i++) {
            String url = String.format("https://httpbin.org/delay/%d", i);
            urls.add(url);
            urls.add(url);
            urls.add(url);
            urls.add(url);
        }

        List<HttpBinResponse> responses = urls.parallelStream()
                .map(url -> restTemplate.getForObject(url, HttpBinResponse.class))
                .collect(Collectors.toList());

        return responses;
    }

    @GetMapping("/parallel-custom")
    @ResponseBody
    public List<HttpBinResponse> parallelCustom() throws ExecutionException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        List<String> urls = new LinkedList<>();

        // more urls than I have cores
        for (int i = 1; i <= 3; i++) {
            String url = String.format("https://httpbin.org/delay/%d", i);
            urls.add(url);
            urls.add(url);
            urls.add(url);
            urls.add(url);
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool(12);
        List<HttpBinResponse> responses = forkJoinPool.submit(() ->
                urls.parallelStream()
                        .map(url -> restTemplate.getForObject(url, HttpBinResponse.class))
                        .collect(Collectors.toList())
        ).get();

        return responses;
    }

    @GetMapping("/parallel-overload")
    @ResponseBody
    public List<HttpBinResponse> parallelOverload() throws ExecutionException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        List<String> urls = new LinkedList<>();

        // more urls than I have cores
        for (int i = 1; i <= 3; i++) {
            String url = String.format("https://httpbin.org/delay/%d", i);
            for (int j = 0; j < 50; j++) {
                urls.add(url);
            }
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool(150);
        List<HttpBinResponse> responses = forkJoinPool.submit(() ->
                urls.parallelStream()
                        .map(url -> restTemplate.getForObject(url, HttpBinResponse.class))
                        .collect(Collectors.toList())
        ).get();


        return responses;
    }

}
