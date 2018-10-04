package com.joshmcguigan.springwebfluxdemo;

import lombok.Data;

@Data
public class HttpBinResponse {
    private String origin;
    private String url;
    private String data;
}
