package com.green.babymeal.main;

import com.green.babymeal.main.model.MainSelPaging;
import com.green.babymeal.main.model.SelDto;
import org.springframework.data.domain.Pageable;


public interface MainService {

     MainSelPaging mainSel(SelDto dto);

}
