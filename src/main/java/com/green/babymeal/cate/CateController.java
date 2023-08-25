package com.green.babymeal.cate;




import com.green.babymeal.cate.model.CateSelList;
import lombok.RequiredArgsConstructor;


import org.springdoc.core.converters.models.Pageable;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cate")
public class CateController {


    private final CateService service;

    @GetMapping("/all")
    private List selCate(){
       return service.selCateList();
    }

    @GetMapping("/list")
    private List selCateList(@RequestBody CateSelList cateSelList){
        return ;
    }

}
