package com.green.babymeal.cate;


import com.green.babymeal.cate.model.CateSelList;
import com.green.babymeal.cate.model.CateSelSel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cate")
public class CateController {


    private final CateService service;

    @GetMapping("/all")
    private List selCate(){
       return service.selCate();
    }

    @PostMapping("/list")
    private List<CateSelSel> selCateList(@RequestBody CateSelList cateSelList){
        return service.selCateList(cateSelList);
    }

}
