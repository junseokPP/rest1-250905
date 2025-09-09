package com.rest1.domain.post.comment.controller;

import com.rest1.domain.post.comment.dto.CommentDto;
import com.rest1.domain.post.comment.entity.Comment;
import com.rest1.domain.post.post.controller.ApiV1PostController;
import com.rest1.domain.post.post.dto.PostDto;
import com.rest1.domain.post.post.entity.Post;
import com.rest1.domain.post.post.service.PostService;
import com.rest1.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1CommentController {

    private final PostService postService;

    @GetMapping("/{postId}/comments")
    public List<CommentDto> getItems(
            @PathVariable Long postId
    ) {
        Post post = postService.findById(postId).get();
        return post.getComments().stream()
                .map(CommentDto::new)
                .toList();
    }

    @GetMapping("/{postId}/comments/{commentId}")
    @Transactional(readOnly = true)
    public CommentDto getItem(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        Post post = postService.findById(postId).get();
        Comment comment = post.findCommentById(commentId).get();
        return new CommentDto(comment);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @Transactional
    public RsData<Void> deleteItem(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ){
        Post post = postService.findById(postId).get();

        postService.deleteComment(post, commentId);


        return  new RsData<>(
                "200-1",
                "%d번 댓글이 삭제되었습니다.".formatted(commentId)
        );

    }

    record CommentWriteReqBody(

            @NotBlank
            @Size(min = 2, max = 100)
            String content
    ) {
    }

    record CommentWriteResBody(
            CommentDto commentDto
    ) {
    }

    @PostMapping("/{postId}/comments")
    @Transactional
    public RsData<CommentWriteResBody> createItem(
            @PathVariable Long postId,
            @RequestBody @Valid CommentWriteReqBody reqBody
    ) {
        Post post = postService.findById(postId).get();
        Comment comment = postService.writeComment(post, reqBody.content);
        return new RsData<>(
                "201-1",
                "%d번 게시물이 생성되었습니다.".formatted(post.getId()),
                new CommentWriteResBody(
                        new CommentDto(comment)
                )
        );
    }


}