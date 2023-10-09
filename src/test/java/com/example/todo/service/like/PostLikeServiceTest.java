package com.example.todo.service.like;

import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.post.PostRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.post.request.PostCreateRequestDto;
import com.example.todo.dto.team.TeamCreateDto;
import com.example.todo.dto.team.TeamJoinDto;
import com.example.todo.facade.OptimisticLockPostLikeFacade;
import com.example.todo.facade.OptimisticLockTeamFacade;
import com.example.todo.service.post.PostService;
import com.example.todo.service.team.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
public class PostLikeServiceTest {

    @Autowired
    PostLikeService postLikeService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamService teamService;

    @Autowired
    TeamReposiotry teamReposiotry;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;


    @Autowired
    OptimisticLockPostLikeFacade optimisticLockPostLikeFacade;

    @Autowired
    PostRepository postRepository;


    @DisplayName("여러명이 게시글에 동시에 좋아요를 누르는 테스트")
    @Test
    void PostLikeWithRaceCondition() throws InterruptedException  {

        // given
        User user = createUser();
        createTeam(user.getId());
        TeamJoinDto joinDto = TeamJoinDto.builder()
                .joinCode("참여코드")
                .build();

        int threadCount = 100;
        //user 100명 생성 및 팀가입
        for (int i = 0; i < threadCount; i++) {
            User user1 = createUser();
            teamService.joinTeam(user1.getId(), joinDto, 1L);
        }


        //manager까지 101명 가입
        List<User> users = userRepository.findAll();
        List<TeamEntity> team = teamReposiotry.findAll();
        List<MemberEntity> members = memberRepository.findAll();

        int participantNumAfterTeamJoin = team.get(0).getParticipantNum();
        int memberAfterTeamJoin = members.size();

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Long> memberList = new ArrayList<>();
        for (MemberEntity member : members){
            memberList.add(member.getId());
        }

        //post 생성
        createPost(user.getId());

        for (Long userId : memberList){
            if (userId.equals(user.getId())){
                System.out.println("건너뜁니다");
                continue;
            }

            executorService.submit(() -> {
                try {
                    optimisticLockPostLikeFacade.createPostLike(userId, 1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }


        latch.await();

        team = teamReposiotry.findAll();
        members = memberRepository.findAll();

        Post post = postRepository.getById(1L);



        // then
        assertThat(users.size()).isEqualTo(101);
        assertThat(participantNumAfterTeamJoin).isEqualTo(101);
        assertThat(memberAfterTeamJoin).isEqualTo(101);

        //manager 제외하고 team leave
        assertThat(members.get(0).getId()).isEqualTo(1L);
        assertThat(post.getLikeCount()).isEqualTo(100);

    }

    private User createUser() {
        return userRepository.saveAndFlush(User.builder()
                .username("username")
                .password("password")
                .build());
    }

    private void createTeam(Long userId) {
        TeamCreateDto createDto = TeamCreateDto.builder()
                .name("구매팀")
                .joinCode("참여코드")
                .participantNumMax(101)
                .description("구매팀입니다.")
                .build();
        teamService.createTeam(userId, createDto);
    }

    private void createPost(Long userId) {
        PostCreateRequestDto postCreateRequestDto = PostCreateRequestDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postService.createPost(postCreateRequestDto, userId, 1L);
    }
}
