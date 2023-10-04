package com.example.todo.service.image;

import com.example.todo.domain.entity.Image;
import com.example.todo.domain.entity.Post;
import com.example.todo.domain.entity.Team;
import com.example.todo.domain.repository.ImageRepository;
import com.example.todo.domain.repository.PostRepository;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    @Transactional
    public void createImage(List<MultipartFile> images, Team team, Post post){

        for (MultipartFile image : images) {

            String imageUrl = uploadFile(image, teamId, postId);
            Image imageEntity = Image.builder()
                    .filename(image.getOriginalFilename())
                    .imageUrl(imageUrl)
                    .post(post)
                    .build();

            post.addImage(imageEntity);
            imageRepository.save(imageEntity);
        }

        postRepository.save(post);
    }

    @Transactional
    public void updateImage(List<MultipartFile> images, Long teamId, Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_POST));


        post.getImages().clear();
        imageRepository.deleteAllByPost_IdAndDeletedAtNull(postId);

        for (MultipartFile image : images) {

            String imageUrl = uploadFile(image, teamId, postId);
            Image imageEntity = Image.builder()
                    .filename(image.getOriginalFilename())
                    .imageUrl(imageUrl)
                    .post(post)
                    .build();

            post.addImage(imageEntity);
            imageRepository.save(imageEntity);
        }

        postRepository.saveAndFlush(post);
    }

    @Transactional
    public void deleteImage(Long postId){
        imageRepository.deleteAllByPost_Id(postId);
    }



    private String uploadFile(final MultipartFile image, final Long teamId, final Long postId) {

        String imageDir = String.format("media/%d/%d/", teamId, postId);

        try {
            Files.createDirectories(Path.of(imageDir));
        } catch (IOException e) {
            e.printStackTrace();
            throw new TodoAppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        String originalFilename = image.getOriginalFilename();
        String[] filenameSplit = originalFilename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];
        String imageFilename = getFileUrl() + "." + extension;
        String imagePath = imageDir + imageFilename;

        try {
            image.transferTo(Path.of(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new TodoAppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return String.format("/static/%d/%d/%s", teamId, postId, imageFilename);
    }

    private String getFileUrl(){
        return UUID.randomUUID().toString();
    }

    private String createTeamDir()
}
