package com.springboot.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

// 페이지네이션 구현 타입은 제네릭

@Getter
public class MultiResponseDto<T> {
    private List<T> data;
    private PageInfo pageInfo;

    public MultiResponseDto(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber() + 1,
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
