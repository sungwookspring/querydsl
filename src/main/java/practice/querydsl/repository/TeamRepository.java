package practice.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.querydsl.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
