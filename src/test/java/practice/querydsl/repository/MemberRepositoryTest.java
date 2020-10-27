package practice.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import practice.querydsl.domain.Member;
import practice.querydsl.domain.QMember;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional(readOnly = true)
public class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    @Transactional
    public void 회원생성(){
        create_member("test1", 10);
    }

    private Member create_member(String username, int age){
        //given
        Member new_member = Member.builder()
                .age(age)
                .username(username)
                .build();

        //when
        Long saveId = memberRepository.save(new_member).getId();

        //then
        Member find_member = memberRepository.findById(saveId).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 계정")
        );

        assertThat(find_member.getUsername()).isEqualTo(new_member.getUsername());
        assertThat(find_member.getAge()).isEqualTo(new_member.getAge());

        return find_member;
    }

    @Test
    public void findAll(){
        List<Member> members = memberRepository.findAll();

        members.stream()
                .forEach(member -> System.out.println(member.getUsername()));

    }

    @Test
    public void startQuerydsl(){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        QMember m = new QMember("m");

        Member find_member = jpaQueryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("test23"))
                .fetchOne();

        assertThat(find_member.getUsername()).isEqualTo("test23");
    }

}