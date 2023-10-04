package com.example.todo.service.post;

import com.example.todo.domain.entity.Post;
import com.example.todo.domain.entity.Team;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.PostRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.post.PostCreateRequestDto;
import com.example.todo.dto.post.PostCreateResponseDto;
import com.example.todo.dto.post.PostReadAllResponseDto;
import com.example.todo.dto.post.PostResponseDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import com.example.todo.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;



import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TeamReposiotry teamReposiotry;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Transactional
    public PostCreateResponseDto createPost(final PostCreateRequestDto createRequestDto,
                                      final Long teamId, final Long userId
    ) {
        Team team = teamReposiotry.getById(teamId);
        User user = userRepository.getById(userId);

        if (!team.containsMember(user))
            throw new TodoAppException(ErrorCode.NOT_MATCH_MEMBERID);


        Post post = postRepository.save(createRequestDto.toEntity(user, team));

        if (createRequestDto.getImages() == null){
            return new PostCreateResponseDto(post);
        }

        imageService.createImage(createRequestDto.getImages(), team, post);
//        post = postRepository.save(post);
        return new PostCreateResponseDto(post);
    }

    @Transactional
    public PostResponseDto readPost(final Long teamId, final Long postId) {
        Team team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_POST));

        if (!post.isTeam(team))
            throw new TodoAppException(ErrorCode.NOT_MATCH_TEAM_AND_POST);

        post.increaseViewCount();

        return PostResponseDto.fromEntity(post);
    }


    @Transactional
    public Page<PostReadAllResponseDto> readPostAll(final Long teamId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(
                page - 1, limit, Sort.by("id").descending());

        Team team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));
        return postRepository.findAllByTeam(team, pageable).map(PostReadAllResponseDto::fromEntity);
    }

    @Transactional
    public PostResponseDto updatePost(final String title, final String content, final List<MultipartFile> images,
                                      final Long teamId, final Long userId, final Long postId
    ) {
        Team team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));

        if (!team.containsMember(user))
            throw new TodoAppException(ErrorCode.NOT_MATCH_MEMBERID);

        Post post = postRepository.getById(postId);

        if (!post.isWriter(user))
            throw new TodoAppException(ErrorCode.NOT_MATCH_USER_AND_POST);

        if (!post.isTeam(team))
            throw new TodoAppException(ErrorCode.NOT_MATCH_TEAM_AND_POST);

        post.changeTitle(title);
        post.changeContent(content);

        post = postRepository.save(post);
        imageService.updateImage(images, teamId, postId);

        return PostResponseDto.fromEntity(post);
    }

    @Transactional
    public void deletePost(final Long teamId, final Long userId, final Long postId) {
        Team team = teamReposiotry.getById(teamId);

        User user = userRepository.getById(userId);

        if (!team.containsMember(user))
            throw new TodoAppException(ErrorCode.NOT_MATCH_MEMBERID);

        Post post = postRepository.getById(postId);

        if (!post.isWriter(user))
            throw new TodoAppException(ErrorCode.NOT_MATCH_USER_AND_POST);

        if (!post.isTeam(team))
            throw new TodoAppException(ErrorCode.NOT_MATCH_TEAM_AND_POST);

        postRepository.delete(post);
        imageService.deleteImage(postId);
    }

}
