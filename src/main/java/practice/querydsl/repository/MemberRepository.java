package practice.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.querydsl.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
