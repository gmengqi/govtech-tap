package com.example.football_championship.repository;

import com.example.football_championship.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<List<Team>> findByGroupNumber(int groupNumber);
    Optional<Team> findByName(String name);
    void deleteByName(String name);

}
