package com.example.todo.api.comment;

import com.example.todo.domain.Response;
import com.example.todo.dto.comment.request.CommentCreateRequestDto;
import com.example.todo.dto.comment.response.*;
import com.example.todo.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{teamId}/post/{postId}/comment")
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping
    public Response<CommentCreateResponseDto> createComment(@RequestBody final CommentCreateRequestDto createDto,
                                                            final Authentication authentication,
                                                            @PathVariable final Long teamId,
                                                            @PathVariable final Long postId) {
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(commentService.createComment(createDto, userId, teamId, postId));
    }

    @GetMapping
    public Response<Page<CommentListResponseDto>> readAllComment(final Authentication authentication,
                                                                @PathVariable final Long teamId,
                                                                @PathVariable final Long postId,
                                                                @RequestParam(value = "offset", defaultValue = "1") final Integer offset){
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(commentService.readAllComment(userId, teamId, postId, offset));
    }

    @GetMapping("/{commentId}")
    public Response<CommentOneResponseDto> readOneComment(final Authentication authentication,
                                                          @PathVariable final Long teamId,
                                                          @PathVariable final Long postId,
                                                          @PathVariable final Long commentId){
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(commentService.readOneComment(userId, teamId, postId, commentId));
    }

    @PutMapping("/{commentId}")
    public Response<CommentUpdateResponseDto> updateComment(@RequestBody final CommentCreateRequestDto updateDto,
                                                            final Authentication authentication,
                                                            @PathVariable final Long teamId,
                                                            @PathVariable final Long postId,
                                                            @PathVariable final Long commentId) {
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(commentService.updateComment(updateDto, userId, teamId, postId, commentId));
    }

    @DeleteMapping("/{commentId}")
    public Response<CommentDeleteResponseDto> deleteComment(
                                                            final Authentication authentication,
                                                            @PathVariable final Long teamId,
                                                            @PathVariable final Long postId,
                                                            @PathVariable final Long commentId) {
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(commentService.deleteComment(userId, teamId, postId, commentId));
    }
}
