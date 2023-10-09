package com.example.todo.api.like;

import com.example.todo.domain.Response;
import com.example.todo.dto.like.response.PostLikeListResponseDto;
import com.example.todo.dto.like.response.PostLikeResponseDto;
import com.example.todo.facade.OptimisticLockPostLikeFacade;
import com.example.todo.service.like.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/team/{teamId}/post/{postId}/likes")
public class PostLikeApiController {

    private final PostLikeService postLikeService;
    private final OptimisticLockPostLikeFacade optimisticLockPostLikeFacade;

    @PostMapping
    public Response<PostLikeResponseDto> createPostLike(final Authentication authentication,
                                           @PathVariable final Long teamId,
                                           @PathVariable final Long postId) throws InterruptedException {
        Long userId = Long.parseLong(authentication.getName());

        PostLikeResponseDto resultDto = optimisticLockPostLikeFacade.createPostLike(userId, teamId, postId);
        if (resultDto == null){
            return Response.conflict(resultDto);
        } else {
            return Response.success(resultDto);
        }
    }

    @DeleteMapping
    public Response<String> deletePostLike(final Authentication authentication,
                                           @PathVariable final Long teamId,
                                           @PathVariable final Long postId) throws InterruptedException {
        Long userId = Long.parseLong(authentication.getName());
        optimisticLockPostLikeFacade.deletePostLike(userId, teamId, postId);
        return Response.success("좋아요 취소");
    }
    @GetMapping
    public Response<Page<PostLikeListResponseDto>> readAllPostLike(final Authentication authentication,
                                                                   @PathVariable final Long teamId,
                                                                   @PathVariable final Long postId,
                                                                   @RequestParam(value = "offset", defaultValue = "1") final Integer offset){
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(postLikeService.readAllPostLikes(userId, teamId, postId, offset));
    }

}
