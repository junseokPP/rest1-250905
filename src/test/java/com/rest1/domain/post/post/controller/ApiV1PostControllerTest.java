package com.rest1.domain.post.post.controller;

import com.rest1.domain.post.post.entity.Post;
import com.rest1.domain.post.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ApiV1PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("글 다건 조회")
    void t1() throws Exception {

        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/posts")
                )
                .andDo(print());

        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("글 단건 조회")
    void t2() throws Exception {

        long targetId = 1;

        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/posts/%d".formatted(targetId))
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("getItem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.createDate").exists())
                .andExpect(jsonPath("$.modifyDate").exists())
                .andExpect(jsonPath("$.title").value("제목1"))
                .andExpect(jsonPath("$.content").value("내용1"));
    }

    @Test
    @DisplayName("글 생성")
    void t3() throws Exception {
        String title = "제목입니다";
        String content = "내용입니다";

        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "%s",
                                            "content": "%s"
                                        }
                                        """.formatted(title, content))
                )
                .andDo(print());

        resultActions
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("글 수정")
    void t4() throws Exception {
        long targetId = 1;
        String title = "제목 수정";
        String content = "내용 수정";

        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/posts/%d".formatted(targetId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "%s",
                                            "content": "%s"
                                        }
                                        """.formatted(title, content))
                )
                .andDo(print());

        // 필수 검증
        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("modifyItem"))
                .andExpect(status().isOk());

        // 선택적 검증
        Post post = postRepository.findById(targetId).get();

        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
    }
}