package practice.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
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
import static practice.querydsl.domain.QMember.*;
import static practice.querydsl.domain.QMember.member;

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
                .where(m.username.eq("testA23"))
                .fetchOne();

        assertThat(find_member.getUsername()).isEqualTo("testA23");
    }

    @Test
    public void search(){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

        Member member = jpaQueryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.username.eq("testB24").and(QMember.member.age.eq(24)))
                .fetchOne();

        assertThat(member.getUsername()).isEqualTo("testB24");
    }

    @Test
    public void search2(){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

        Member member = jpaQueryFactory
                .selectFrom(QMember.member)
                .where(
                        QMember.member.username.eq("testA24"),
                        QMember.member.age.eq(24))
                .fetchOne();

        assertThat(member.getUsername()).isEqualTo("testA24");
    }

    @Test
    public void resultFetch(){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

        List<Member> members = jpaQueryFactory
                .selectFrom(member)
                .fetch();

//        Member find_member = jpaQueryFactory
//                .selectFrom(QMember.member)
//                .fetchOne();

        Member find_member1 = jpaQueryFactory
                .selectFrom(QMember.member)
                .fetchFirst();

        // getTotal함수 따로 실행 필요
        QueryResults<Member> results = jpaQueryFactory
                .selectFrom(member)
                .fetchResults();

        results.getTotal();
        List<Member> results1 = results.getResults();
    }


    @Test
    public void sort() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

        List<Member> members = jpaQueryFactory
                .selectFrom(member)
                .orderBy(member.age.desc(), member.username.desc().nullsLast())
                .fetch();

        members.stream()
                .forEach(member -> System.out.println(member.getUsername()));
    }

    /***
     * 1개를 건너뛰고 2개까지 조회
     */
    @Test
    public void paging(){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

        List<Member> members = jpaQueryFactory
                .selectFrom(member)
                .orderBy(member.username.asc())
                .offset(1) // skip -> start offset
                .limit(2)
                .fetch();

        assertThat(members.size()).isEqualTo(2);
    }

    /***
     * 전체를 조회하고 페이징?
     */
    @Test
    public void paging2(){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

        QueryResults<Member> results = jpaQueryFactory
                .selectFrom(member)
                .orderBy(member.username.asc())
                .offset(1) // skip -> start offset
                .limit(2)
                .fetchResults();

        System.out.println("멤버 수: " + results.getTotal());
        assertThat(results.getLimit()).isEqualTo(2);
        assertThat(results.getOffset()).isEqualTo(1);
        assertThat(results.getResults().size()).isEqualTo(2);
    }

    @Test
    public void aggregation(){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

        List<Tuple> results = jpaQueryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();
        
        // Tuple방식보다 Dto방식을 많이 선호
        Tuple tuple = results.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(100);
//        assertThat(tuple.get(member.sum())).isEqualTo(..?);
    }

    @Test
    public void group() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);


    }
}