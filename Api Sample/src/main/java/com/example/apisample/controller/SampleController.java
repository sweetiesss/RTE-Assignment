package com.example.apisample.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sample")
@CrossOrigin(origins = "*")
public class SampleController {

    @GetMapping()
    public static String sample() {
        return "hello, this is sample";
    }
}
