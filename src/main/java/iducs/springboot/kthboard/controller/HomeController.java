package iducs.springboot.kthboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 데이터(처리결과)를 view에 전달
// @RestController  // 자원의 상태(응답)를 Client에게 전달
// Restful(Representational State Transfer). OpenAPIs
@RequestMapping("/")
public class HomeController {
    @GetMapping("")
    public String getHome() {
        return "index";
    }
}