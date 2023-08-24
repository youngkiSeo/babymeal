package com.green.babymeal.cate;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cate")
public class CateController {


    private final CateService service;

    @GetMapping("/all")
    private List selCateList(){
       return service.selCateList();
    }

}
