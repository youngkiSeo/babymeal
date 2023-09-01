package com.green.babymeal.baby.model;

import lombok.Data;

import java.util.List;

@Data
public class BabyAllergyTotalVo {

   private BaByInfoVo baByInfoVo;
   private List<BabyAllergyInfoVo> BabyAllergyList;
}
