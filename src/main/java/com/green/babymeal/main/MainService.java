package com.green.babymeal.main;

import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.main.model.MainSelPaging;
import com.green.babymeal.main.model.MainSelVo;
import com.green.babymeal.main.model.SelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final MainMapper mapper;
    public final AuthenticationFacade USERPK;

    public MainSelPaging mainSel(SelDto dto){
        if(dto.getCheck()==1){
            int selMaxPageCount = mapper.SelMainCount();
            int maxPage=(int)Math.ceil((double)selMaxPageCount/dto.getRow());
            int startIdx=(dto.getPage()-1)*dto.getRow();
            List<MainSelVo> mainSelVos = mapper.SelMainVo(startIdx,dto.getRow());
            MainSelPaging mainSelPaging=new MainSelPaging();
            mainSelPaging.setMaxPage(maxPage);
            mainSelPaging.setMaxCount(selMaxPageCount);
            mainSelPaging.setList(mainSelVos);
            return mainSelPaging;
        }

        else if(dto.getCheck()==2){

            int month = mapper.birth(USERPK.getLoginUser().getIuser());
            log.info("{}:",month);
            int cate = 0;
            if (month <= 4) {
                return null;
            }
            if (month > 4 && month <= 6) {
                cate = 1;
            } else if (month > 6 && month <= 10) {
                cate = 2;
            } else if (month > 10 && month <= 13) {
                cate = 3;
            } else if (month > 13) {
                cate = 4;
            }
            log.info("{}:",USERPK.getLoginUser().getIuser());
            List<MainSelVo> mainSelVos = mapper.birthRecommendFilter(cate, dto.getRow());
            log.info("{}:",mainSelVos);
            MainSelPaging mainSelPaging=new MainSelPaging();
            mainSelPaging.setList(mainSelVos);
            return mainSelPaging;
        }

        return null;
    }
}
