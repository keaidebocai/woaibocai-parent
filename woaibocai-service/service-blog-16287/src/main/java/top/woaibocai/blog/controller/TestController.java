package top.woaibocai.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/aa")
    public String test(){
        return "hello world! aa";
    }
    @GetMapping("/bb")
    public String test1(){
        return "hello world! bb";
    }
}
