package com.example.todo.service.image;

import com.example.todo.domain.entity.image.Image;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.repository.image.ImageRepository;
import com.example.todo.dto.image.request.ImageCreateRequestDto;
import com.example.todo.dto.post.request.PostCreateRequestDto;
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
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void createImage(final PostCreateRequestDto createDto, final Post post, final Long teamId) {
        List<MultipartFile> images = createDto.getImages();
        String teamDir = createTeamDir(teamId, post);

        for (MultipartFile image : images) {
            String postFilename = createPostFilename(image);
            String postPath = teamDir + postFilename;
            saveImage(image, postPath);

            String imageUrl = String.format("/static/%d/%d/%s", teamId, post.getId(), postFilename);
            imageRepository.save(ImageCreateRequestDto.toEntity(post, imageUrl));
        }
    }

    private String createTeamDir(final Long teamId, final Post post) {
        String teamDir = String.format("team/%d/%d/", teamId, post.getId());

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

    private void saveImage(final MultipartFile image, final String postPath) {
        Path path = Path.of(postPath);

        try {
            image.transferTo(path);
        } catch (IOException e) {
            log.error("createPost error = {}", e);
            throw new TodoAppException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());
        }
    }

}
