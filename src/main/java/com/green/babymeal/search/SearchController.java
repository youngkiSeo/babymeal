package com.green.babymeal.search;

import com.green.babymeal.search.model.SearchPopularVo;
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
}
