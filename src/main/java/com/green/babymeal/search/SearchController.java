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


    @GetMapping("/popular")
    @Operation(summary = "인기 검색어", description = "")
    public List<SearchPopularVo> searchRankList(){
        return service.list();
    }

    @GetMapping("/recent")
    @Operation(summary = "최근 검색어", description = "")
    public List<String> recentSearch(){
        return service.GetRecentSearch();
    }

    @DeleteMapping("/recent/{product}")
    @Operation(summary = "최근 검색어 삭제", description = "삭제하고 싶은 상품")
    public Long removeRecent(@PathVariable String product){
        return service.deleteRecentSearch(product);
    }
    @DeleteMapping("/recent")
    @Operation(summary = "최근 검색어 전체 삭제", description = "삭제하고 싶은 상품")
    public Long removeRecent(){
        return service.deleteRecentSearchAll();
    }

    @GetMapping()
    @Operation(summary = "",description = ""+
            " sorter: 0이면 판매량 많은 순서 1이면 판매량 적은 순서 <br> "+
            "sorter: 2이면 가격 높은 순서 3이면 가격 낮은 순서<br>")
    public SearchSelRes filterAllergy(@RequestParam String product,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int row,
                                      @RequestParam (required = false)String sorter,
                                      @RequestParam (required = false)List<String>filter){
        SearchSelRes selfilter = service.selfilter(product,page,row,sorter,filter);
        return selfilter;
    }
}
