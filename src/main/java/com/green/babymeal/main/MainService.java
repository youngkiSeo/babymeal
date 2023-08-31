package com.green.babymeal.main;

import com.green.babymeal.main.model.MainSelPaging;
import com.green.babymeal.main.model.SelDto;


public interface MainService {

     MainSelPaging mainSel(SelDto dto);

}
