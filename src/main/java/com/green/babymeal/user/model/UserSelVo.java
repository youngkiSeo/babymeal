package com.green.babymeal.user.model;

import com.green.babymeal.common.entity.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class UserSelVo {
    private int page;
    private int count;
    private int maxPage;
    private List list;
}
