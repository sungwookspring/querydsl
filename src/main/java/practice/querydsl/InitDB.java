package practice.querydsl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import practice.querydsl.domain.Member;
import practice.querydsl.domain.Team;
import practice.querydsl.repository.MemberRepository;
import practice.querydsl.repository.TeamRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void run(){
        initService.run();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final MemberRepository memberRepository;
        private final TeamRepository teamRepository;

        public void run(){
            int teamA_member_length = 50;
            int teamB_member_length = 50;

            // == TeamA 멤버 초기화
            Team teamA = Team.builder()
                    .name("teamA")
                    .build();
            teamRepository.save(teamA);

            for (int i = 0; i < teamA_member_length; i++) {
                String username = "testA" + Integer.toString(i);
                int age = i;

                create_member(username, age, teamA);
            }

            // == TeamB 멤버 초기화
            Team teamB = Team.builder()
                    .name("teamB")
                    .build();
            teamRepository.save(teamB);

            for (int i = 0; i < teamB_member_length; i++) {
                String username = "testB" + Integer.toString(i);
                int age = i;

                create_member(username, age, teamB);
            }
        }

        private Member create_member(String username, int age, Team team){
            Member member = Member.builder()
                    .username(username)
                    .age(age)
                    .team(team)
                    .build();

            memberRepository.save(member);

            return member;
        }

    }
}
