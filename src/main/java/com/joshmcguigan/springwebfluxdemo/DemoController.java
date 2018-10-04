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
        return "hello";
    }

}
