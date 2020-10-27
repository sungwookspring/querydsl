# 1. QueryDsl 구현 과정
1. queryFactory 초기화
2. Q클래스 초기화
3. 쿼리문 초기화
```java
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
```
* 결과: 실행된 쿼리문 결과
```java
select
    member0_.member_id as member_i1_0_,
    member0_.age as age2_0_,
    member0_.team_id as team_id4_0_,
    member0_.username as username3_0_ 
from
    member member0_ 
where
    member0_.username=?
```