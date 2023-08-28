package com.green.babymeal.main;

import com.green.babymeal.main.model.MainSelPaging;
import com.green.babymeal.main.model.SelDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
@Tag(name = "메인 페이지")
public class MainController2 {

    private final MainService service;


  @GetMapping
  @Operation(summary = "메인페이지 상품",description = ""+
  "check:1 (기본으로 보여줄 상품)<br>"+
  "check:2 (회원 자녀의 개월에 따라 상품 추천)<br>"+
  "check:3 (랜덤으로 상품 추천)<br>"+
  "check:4 (제일 많이 팔린 상품)<br>"+
  "check:5 (제일 많이 팔린 상품 더보기)<br>"+
  "page: 페이지 번호"+
  "row: 한페이지에 보일 상품의 갯수")
  public MainSelPaging mainSel(SelDto dto){
      return service.mainSel(dto);
  }



}
