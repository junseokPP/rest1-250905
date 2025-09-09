package com.rest1.global.rsData;

import com.rest1.domain.post.comment.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RsData<T> {

    private String resultCode;
    private String msg;
    private T data;

    public RsData(String resultCode, String msg) {
        this.resultCode = resultCode;
        this.msg = msg;
        this.data = null;
    }


}
