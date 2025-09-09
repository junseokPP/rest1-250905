package com.rest1.domain.post.post.controller;

import com.rest1.domain.post.comment.entity.Comment;
import com.rest1.domain.post.post.dto.PostDto;
import com.rest1.domain.post.post.entity.Post;
import com.rest1.domain.post.post.service.PostService;
import com.rest1.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1PostController {

    private final PostService postService;

    @GetMapping
    @Transactional(readOnly = true)
    public List<PostDto> list() {
        return postService.findAll().stream()
                .map(post -> new PostDto(post))
                .toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PostDto getItem(
            @PathVariable Long id
    ) {
        Post post = postService.findById(id).get();
        return new PostDto(post);
    }

    @DeleteMapping("/{id}")
    public RsData<Void> deleteItem(
            @PathVariable Long id
    ) {
        Post post = postService.findById(id).get();
        postService.delete(post);

        return new RsData<Void>(
                "204-1",
                "%d번 게시물이 삭제되었습니다.".formatted(id)
        );
    }

    record PostWriteReqBody(
            @NotBlank
            @Size(min = 2, max = 10)
            String title,

            @NotBlank
            @Size(min = 2, max = 100)
            String content
    ) {
    }

    record PostWriteResBody(
            PostDto postDto,
            long totalCount
    ) {
    }

    @PostMapping
    @Transactional
    public ResponseEntity<RsData<PostWriteResBody>> createItem(
            @RequestBody @Valid PostWriteReqBody reqBody
    ) {
        Post post = postService.write(reqBody.title, reqBody.content);
        long totalCount = postService.count();

        RsData<PostWriteResBody> rsData = new RsData<>(
                "201-1",
                "%d번 게시물이 생성되었습니다.".formatted(post.getId()),
                new PostWriteResBody(
                        new PostDto(post),
                        totalCount
                )
        );

        return ResponseEntity.status(201).body(rsData);
    }
}
