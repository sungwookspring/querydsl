package practice.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.querydsl.domain.Team;

public interface TemRepository extends JpaRepository<Team, Long> {
}
