package com.green.babymeal.admin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AdminThumbnailDelDto {
    private Long productId;
    private String thumbnailFullName;
}
