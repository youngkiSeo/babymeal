package com.green.babymeal.main;

import com.green.babymeal.main.model.MainSelPaging;
import com.green.babymeal.main.model.MainSelVo;
import com.green.babymeal.main.model.SelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final MainMapper mapper;


    public MainSelPaging mainSel(SelDto dto){
        if(dto.getCheck()==1){
            int selMaxPageCount = mapper.SelMainCount();
            int maxPage=(int)Math.ceil((double)selMaxPageCount/dto.getRow());
            List<MainSelVo> mainSelVos = mapper.mainSel();
            MainSelPaging mainSelPaging=new MainSelPaging();
            mainSelPaging.setMaxPage(maxPage);
            mainSelPaging.setMaxCount(selMaxPageCount);
            mainSelPaging.setList(mainSelVos);
            return mainSelPaging;
        }
        return null;
    }
}
