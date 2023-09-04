package com.green.babymeal.cate;




import com.green.babymeal.cate.model.Cate;
import com.green.babymeal.cate.model.CateSelList;
import com.green.babymeal.cate.model.CateSelVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cate")
@Tag(name = "카테고리")
public class CateController {


  private final CateService service;

  @GetMapping("/all")
  private List selCate(){
     return service.selCate();
  }

    @GetMapping("/list")
    private List<CateSelVo> selCateList( CateSelList cateSelList){
        return service.selCateList(cateSelList);
    }


}
