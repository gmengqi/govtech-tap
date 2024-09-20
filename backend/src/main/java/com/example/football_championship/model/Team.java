package com.example.football_championship.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_team_entity")
//@EntityListeners(AuditListener.class)
public class Team extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public LocalDate registrationDate;

    @Column(nullable = false)
    public int groupNumber;

    public Integer totalGoals = 0;
    public Integer matchPoints = 0;
    public Integer alternatePoints = 0;
    public Integer matchesPlayed = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(Integer totalGoals) {
        this.totalGoals = totalGoals;
    }

    public int getMatchPoints() {
        return matchPoints;
    }

    public void setMatchPoints(Integer matchPoints) {
        this.matchPoints = matchPoints;
    }

    public int getAlternatePoints() {
        return alternatePoints;
    }

    public void setAlternatePoints(Integer alternatePoints) {
        this.alternatePoints = alternatePoints;
    }

    public Integer getTotalMatchPoints() {
        return this.matchPoints;
    }

    @Override
    public String toString() {
        return String.format("Adding team: %s", name);
    }
}
