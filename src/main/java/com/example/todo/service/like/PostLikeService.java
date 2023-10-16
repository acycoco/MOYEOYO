package com.example.todo.service.like;

import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.like.PostLike;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.like.PostLikeRepository;
import com.example.todo.domain.repository.post.PostRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.like.response.PostLikeListResponseDto;
import com.example.todo.dto.like.response.PostLikeResponseDto;
import com.example.todo.exception.TodoAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.todo.exception.ErrorCode.*;
import static com.example.todo.exception.ErrorCode.NOT_FOUND_POST;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final TeamReposiotry teamReposiotry;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostLikeResponseDto createPostLike(final Long userId, final Long teamId, final Long postId) {
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.findByIdWithOptimisticLock(postId)
                .orElseThrow(() -> new TodoAppException(NOT_FOUND_POST, NOT_FOUND_POST.getMessage()));

        if (!post.validateTeam(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        memberRepository.existsByTeamAndUserOrThrow(team, user);

        if (postLikeRepository.existsByPostAndUser(post, user)){
            throw new TodoAppException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());
        }

        PostLike newPostlike = postLikeRepository.save(
                PostLike.builder()
                        .post(post)
                        .user(user)
                        .build());
        post.addLikeCount();

        return new PostLikeResponseDto(newPostlike);
    }


    @Transactional
    public void deletePostLike(final Long userId, final Long teamId, final Long postId) {
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.findByIdWithOptimisticLock(postId)
                .orElseThrow(() -> new TodoAppException(NOT_FOUND_POST, NOT_FOUND_POST.getMessage()));

        if (!post.validateTeam(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        memberRepository.existsByTeamAndUserOrThrow(team, user);

        Optional<PostLike> existingPostLike = postLikeRepository.findByPostAndUser(post, user);


        if (existingPostLike.isEmpty())
            throw new TodoAppException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());

        //이미 좋아요를 누른 상태 시 좋아요 취소
        postLikeRepository.delete(existingPostLike.get());
        post.removeLikeCount();
    }

    public Page<PostLikeListResponseDto> readAllPostLikes(final Long userId, final Long teamId, final Long postId, final Integer offset) {
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.validateTeam(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        memberRepository.existsByTeamAndUserOrThrow(team, user);

        Pageable pageable = PageRequest.of(offset - 1, 20, Sort.by("id").descending());
        return postLikeRepository.findAllByPost(post, pageable).map(PostLikeListResponseDto::new);
    }

}
