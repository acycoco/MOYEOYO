package com.example.todo.service.comment;

import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.comment.Comment;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.comment.CommentRepository;
import com.example.todo.domain.repository.post.PostRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.comment.request.CommentCreateRequestDto;
import com.example.todo.dto.comment.response.*;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.todo.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TeamReposiotry teamReposiotry;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentCreateResponseDto createComment(final CommentCreateRequestDto createDto, final Long userId, final Long teamId, final Long postId) {
        log.info("userId {}", userId );
        User user = userRepository.getById(userId);
        log.info("teamId{} ", teamId );
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.isTeamMatch(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        memberRepository.existsByTeamAndUserOrThrow(team, user);

        Comment comment = commentRepository.save(createDto.toEntity(post, user));

        return new CommentCreateResponseDto(comment);
    }

    public Page<CommentListResponseDto> readAllComment(final Long userId, final Long teamId, final Long postId, final Integer offset) {
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.isTeamMatch(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        memberRepository.existsByTeamAndUserOrThrow(team, user);

        Pageable pageable = PageRequest.of(offset - 1, 20, Sort.by("id").descending());
        return commentRepository.findAllByPost(post, pageable).map(comment -> new CommentListResponseDto(comment, comment.getUser()));
    }

    public CommentOneResponseDto readOneComment(final Long userId, final Long teamId, final Long postId, final Long commentId){
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.isTeamMatch(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        Comment comment = commentRepository.getById(commentId);

        if (!comment.validatePost(post))
            throw new TodoAppException(NOT_MATCH_POST_AND_COMMENT, NOT_MATCH_POST_AND_COMMENT.getMessage());

        return new CommentOneResponseDto(comment, comment.getUser());
    }

    @Transactional
    public CommentUpdateResponseDto updateComment(final CommentCreateRequestDto updateDto, final Long userId, final Long teamId, final Long postId, final Long commentId){
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.isTeamMatch(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        Comment comment = commentRepository.getById(commentId);

        if (!comment.validatePost(post))
            throw new TodoAppException(NOT_MATCH_POST_AND_COMMENT, NOT_MATCH_POST_AND_COMMENT.getMessage());

        if (!comment.validateUser(user))
            throw new TodoAppException(NOT_MATCH_USERID, NOT_MATCH_USERID.getMessage());

        comment.update(updateDto);
        return new CommentUpdateResponseDto(comment);
    }

    @Transactional
    public CommentDeleteResponseDto deleteComment(final Long userId, final Long teamId, final Long postId, final Long commentId) {
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.isTeamMatch(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        Comment comment = commentRepository.getById(commentId);

        if (!comment.validatePost(post))
            throw new TodoAppException(NOT_MATCH_POST_AND_COMMENT, NOT_MATCH_POST_AND_COMMENT.getMessage());

        if (!comment.validateUser(user))
            throw new TodoAppException(NOT_MATCH_USERID, NOT_MATCH_USERID.getMessage());

        commentRepository.delete(comment);
        return new CommentDeleteResponseDto(comment, user);
    }
}
