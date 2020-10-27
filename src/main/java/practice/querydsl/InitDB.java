package practice.querydsl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import practice.querydsl.domain.Member;
import practice.querydsl.repository.MemberRepository;

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

        public void run(){
            int member_length = 100;

            for (int i = 0; i < member_length; i++) {
                String username = "test" + Integer.toString(i);
                int age = 100;

                create_member(username, age);
            }
        }

        private Member create_member(String username, int age){
            Member member = Member.builder()
                    .username(username)
                    .age(age)
                    .build();

            memberRepository.save(member);

            return member;
        }

    }
}
