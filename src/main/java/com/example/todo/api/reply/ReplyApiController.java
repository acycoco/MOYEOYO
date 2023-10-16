package com.example.todo.api.reply;

import com.example.todo.domain.Response;
import com.example.todo.dto.comment.request.CommentRequestDto;
import com.example.todo.dto.comment.response.*;
import com.example.todo.dto.reply.request.ReplyRequestDto;
import com.example.todo.dto.reply.response.*;
import com.example.todo.service.comment.CommentService;
import com.example.todo.service.reply.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{teamId}/post/{postId}/comment/{commentId}/reply")
public class ReplyApiController {

    private final ReplyService replyService;

    @PostMapping
    public Response<ReplyCreateResponseDto> createReply(@RequestBody final ReplyRequestDto createDto,
                                                        final Authentication authentication,
                                                        @PathVariable final Long teamId,
                                                        @PathVariable final Long postId,
                                                        @PathVariable final Long commentId) {
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(replyService.createReply(createDto, userId, teamId, postId, commentId));
    }

    @GetMapping
    public Response<Page<ReplyListResponseDto>> readAllReply(final Authentication authentication,
                                                             @PathVariable final Long teamId,
                                                             @PathVariable final Long postId,
                                                             @PathVariable final Long commentId,
                                                             @RequestParam(value = "offset", defaultValue = "1") final Integer offset){
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(replyService.readAllReply(userId, teamId, postId, commentId, offset));
    }

    @GetMapping("/{replyId}")
    public Response<ReplyOneResponseDto> readOneReply(final Authentication authentication,
                                                      @PathVariable final Long teamId,
                                                      @PathVariable final Long postId,
                                                      @PathVariable final Long commentId,
                                                      @PathVariable final Long replyId){
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(replyService.readOneReply(userId, teamId, postId, commentId, replyId));
    }

    @PutMapping("/{replyId}")
    public Response<ReplyUpdateResponseDto> updateReply(@RequestBody final ReplyRequestDto updateDto,
                                                        final Authentication authentication,
                                                        @PathVariable final Long teamId,
                                                        @PathVariable final Long postId,
                                                        @PathVariable final Long commentId,
                                                        @PathVariable final Long replyId) {
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(replyService.updateReply(updateDto, userId, teamId, postId, commentId, replyId));
    }

    @DeleteMapping("/{replyId}")
    public Response<ReplyDeleteResponseDto> deleteReply(final Authentication authentication,
                                                            @PathVariable final Long teamId,
                                                            @PathVariable final Long postId,
                                                            @PathVariable final Long commentId,
                                                            @PathVariable final Long replyId) {
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(replyService.deleteReply(userId, teamId, postId, commentId, replyId));
    }
}
