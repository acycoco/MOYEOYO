package com.example.todo.service.post;

import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.image.Image;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.image.ImageRepository;
import com.example.todo.domain.repository.post.PostRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.post.request.PostCreateRequestDto;
import com.example.todo.dto.post.response.PostCreateResponseDto;
import com.example.todo.exception.TodoAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static com.example.todo.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TeamReposiotry teamReposiotry;
    private final ImageRepository imageRepository;

    @Transactional
    public PostCreateResponseDto createPost(final PostCreateRequestDto createDto, Long userId, Long teamId) {
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.getById(teamId);

        Post post = postRepository.save(createDto.toEntity(user, team));

        // 이미지 첨부했을 경우
        List<MultipartFile> images = createDto.getImages();
        String teamDir = createTeamDir(teamId);

        for (MultipartFile image : images) {
            String postFilename = createPostFilename(image);
            String postPath = teamDir + postFilename;
            Path path = Path.of(postPath);

            try {
                image.transferTo(path);
            } catch (IOException e) {
                log.error("createPost error = {}", e);
                throw new TodoAppException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());
            }

            String imageUrl = String.format("/static/%d/%s", teamId, postFilename);
            Image savedImage = Image.builder()
                    .image(imageUrl)
                    .post(post)
                    .build();
            imageRepository.save(savedImage);
        }

        return new PostCreateResponseDto(post);
    }

    private String createTeamDir(final Long teamId) {
        String teamDir = String.format("team/%d/", teamId);

        try {
            Files.createDirectories(Path.of(teamDir));
        } catch (IOException e) {
            log.error("createPost error = {}", e);
            throw new TodoAppException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());
        }
        return teamDir;
    }

    private String createPostFilename(final MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        String[] filenameSplit = originalFilename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];
        String uuid = UUID.randomUUID() + ".";
        return uuid + extension;
    }
}
