package com.green.babymeal.admin.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserVo {
    private Long iuser;
    private String name;
}
