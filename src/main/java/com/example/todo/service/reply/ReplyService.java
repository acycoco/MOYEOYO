package com.example.todo.service.reply;

import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.comment.Comment;
import com.example.todo.domain.entity.comment.Reply;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.comment.CommentRepository;
import com.example.todo.domain.repository.comment.ReplyRepository;
import com.example.todo.domain.repository.post.PostRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.comment.request.CommentRequestDto;
import com.example.todo.dto.comment.response.CommentCreateResponseDto;
import com.example.todo.dto.comment.response.CommentDeleteResponseDto;
import com.example.todo.dto.comment.response.CommentListResponseDto;
import com.example.todo.dto.comment.response.CommentUpdateResponseDto;
import com.example.todo.dto.reply.request.ReplyRequestDto;
import com.example.todo.dto.reply.response.*;
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
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TeamReposiotry teamReposiotry;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public ReplyCreateResponseDto createReply(final ReplyRequestDto createDto, final Long userId, final Long teamId, final Long postId, final Long commentId) {
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.validateTeam(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        memberRepository.existsByTeamAndUserOrThrow(team, user);

        Comment comment = commentRepository.getById(commentId);

        if (!comment.validatePost(post))
            throw new TodoAppException(NOT_MATCH_POST_AND_COMMENT, NOT_MATCH_POST_AND_COMMENT.getMessage());

        Reply reply = replyRepository.save(createDto.toEntity(comment, user));

        return new ReplyCreateResponseDto(reply);
    }

    public Page<ReplyListResponseDto> readAllReply(final Long userId, final Long teamId, final Long postId, final Long commentId, final Integer offset) {
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.validateTeam(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        memberRepository.existsByTeamAndUserOrThrow(team, user);

        Comment comment = commentRepository.getById(commentId);

        if (!comment.validatePost(post))
            throw new TodoAppException(NOT_MATCH_POST_AND_COMMENT, NOT_MATCH_POST_AND_COMMENT.getMessage());

        Pageable pageable = PageRequest.of(offset - 1, 20, Sort.by("id").descending());
        return replyRepository.findAllByComment(comment, pageable).map(reply -> new ReplyListResponseDto(reply, reply.getUser()));
    }

    public ReplyOneResponseDto readOneReply(final Long userId, final Long teamId, final Long postId, final Long commentId, final Long replyId){
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.validateTeam(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        memberRepository.existsByTeamAndUserOrThrow(team, user);

        Comment comment = commentRepository.getById(commentId);

        if (!comment.validatePost(post))
            throw new TodoAppException(NOT_MATCH_POST_AND_COMMENT, NOT_MATCH_POST_AND_COMMENT.getMessage());

        Reply reply = replyRepository.getById(replyId);

        if (!reply.validateComment(comment))
            throw new TodoAppException(NOT_MATCH_COMMENT_AND_REPLY, NOT_MATCH_COMMENT_AND_REPLY.getMessage());

        return new ReplyOneResponseDto(reply, reply.getUser());
    }

    @Transactional
    public ReplyUpdateResponseDto updateReply(final ReplyRequestDto updateDto, final Long userId, final Long teamId, final Long postId, final Long commentId, final Long replyId){
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.validateTeam(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        memberRepository.existsByTeamAndUserOrThrow(team, user);

        Comment comment = commentRepository.getById(commentId);

        if (!comment.validatePost(post))
            throw new TodoAppException(NOT_MATCH_POST_AND_COMMENT, NOT_MATCH_POST_AND_COMMENT.getMessage());

        Reply reply = replyRepository.getById(replyId);

        if (!reply.validateComment(comment))
            throw new TodoAppException(NOT_MATCH_COMMENT_AND_REPLY, NOT_MATCH_COMMENT_AND_REPLY.getMessage());

        if (!reply.validateUser(user))
            throw new TodoAppException(NOT_MATCH_USERID, NOT_MATCH_USERID.getMessage());

        reply.update(updateDto);
        return new ReplyUpdateResponseDto(reply);
    }

    @Transactional
    public ReplyDeleteResponseDto deleteReply(final Long userId, final Long teamId, final Long postId, final Long commentId, final Long replyId){
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);
        Post post = postRepository.getById(postId);

        if (!post.validateTeam(team))
            throw new TodoAppException(NOT_MATCH_TEAM_AND_POST, NOT_MATCH_TEAM_AND_POST.getMessage());

        memberRepository.existsByTeamAndUserOrThrow(team, user);

        Comment comment = commentRepository.getById(commentId);

        if (!comment.validatePost(post))
            throw new TodoAppException(NOT_MATCH_POST_AND_COMMENT, NOT_MATCH_POST_AND_COMMENT.getMessage());

        Reply reply = replyRepository.getById(replyId);

        if (!reply.validateComment(comment))
            throw new TodoAppException(NOT_MATCH_COMMENT_AND_REPLY, NOT_MATCH_COMMENT_AND_REPLY.getMessage());

        if (!reply.validateUser(user))
            throw new TodoAppException(NOT_MATCH_USERID, NOT_MATCH_USERID.getMessage());

        replyRepository.delete(reply);
        return new ReplyDeleteResponseDto(reply, user);
    }
}
