package com.green.babymeal.search;

import com.green.babymeal.search.model.SearchPopularVo;
import com.green.babymeal.search.model.SearchRes;
import com.green.babymeal.search.model.SearchSelRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Tag(name = "검색")
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService service;

    @PostMapping("/popular")
    @Operation(summary = "인기검색어", description = "30초뒤에 count값 감소됨")
    public Double searchRankList(@RequestParam String product){
        return service.search(product);
    }

    @GetMapping("/popular")
    @Operation(summary = "인기검색어", description = "")
    public List<SearchPopularVo> searchRankList(){
        return service.list();
    }
    @GetMapping("/filter")
    @Operation(summary = "필터",description = ""+
            " sorter: 0이면 판매량 많은 순서 1이면 판매량 적은 순서 <br> "+
            "sorter: 2이면 가격 높은 순서 3이면 가격 낮은 순서<br>")
    public SearchSelRes filterAllergy(@RequestParam String product,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int row,
                                      @RequestParam int sorter,
                                      @RequestParam List<String>filter){
        SearchSelRes selfilter = service.selfilter(product,page,row,sorter,filter);
        return selfilter;
    }
}
