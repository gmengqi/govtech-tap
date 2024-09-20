package com.example.football_championship.model;

import jakarta.persistence.*;

@Entity
@Table(name = "t_match_entity")
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String teamAName;

    @Column(nullable = false)
    private String teamBName;

    @Column(nullable = false)
    private int teamAGoals;

    @Column(nullable = false)
    private int teamBGoals;

    // Getters
    public Long getId() {
        return id;
    }

    public String getTeamA() {
        return teamAName;
    }

    public int getTeamAScore() {
        return teamAGoals;
    }

    public String getTeamB() {
        return teamBName;
    }

    public int getTeamBScore() {
        return teamBGoals;
    }

    // Setters
    public void setTeamA(String teamAName) {
        this.teamAName = teamAName;
    }

    public void setTeamAScore(int score) {
        this.teamAGoals = score;
    }

    public void setTeamB(String teamBName) {
        this.teamBName = teamBName;
    }

    public void setTeamBScore(int teamBGoals) {
        this.teamBGoals = teamBGoals;
    }
    @Override
    public String toString() {
        return String.format("Adding match result for %s and %s", teamAName, teamBName);
    }
}
