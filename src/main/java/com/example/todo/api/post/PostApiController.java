package com.example.todo.api.post;

import com.example.todo.domain.Response;
import com.example.todo.dto.post.PostReadAllResponseDto;
import com.example.todo.dto.post.PostResponseDto;
import com.example.todo.service.post.PostService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{teamId}/post")
public class PostApiController {

    private final PostService postService;

    @PostMapping
    public Response<PostResponseDto> create(
            @PathVariable("teamId") Long teamId, @RequestParam("title") String title, @RequestParam("content") String content,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication

    ) {
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(postService.createPost(title, content, images, teamId, userId));
    }

    @GetMapping
    public Response<Page<PostReadAllResponseDto>> readAll(
            @PathVariable("teamId") Long teamId,
            @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ){
        return Response.success(postService.readPostAll(teamId, page, limit));
    }

    @GetMapping("/{postId}")
    public Response<PostResponseDto> read(
            @PathVariable("teamId") Long teamId, @PathVariable("postId") Long postId
    ){
        return Response.success(postService.readPost(teamId, postId));
    }

    @PutMapping("/{postId}")
    public Response<PostResponseDto> update(
            @PathVariable("teamId") Long teamId, @PathVariable("postId") Long postId,
            @RequestParam(value = "title", required = false) String title, @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(postService.updatePost(title, content, images, teamId, userId, postId));
    }

    @DeleteMapping("/{postId}")
    public Response<String> delete(
            @PathVariable("teamId") Long teamId, @PathVariable("postId") Long postId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        postService.deletePost(teamId, userId, postId);
        return Response.success("삭제가 완료되었습니다.");
    }
}
