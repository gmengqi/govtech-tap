package com.example.football_championship.DTO;

import jakarta.persistence.Column;

import java.time.LocalDate;

public class UpdateTeamDTO {
    private String teamName;
    private String newName=null;
    private LocalDate newRegistrationDate=null;
    private Integer groupNumber=null;
    public Integer totalGoals=null;
    public Integer matchPoints=null;
    public Integer alternatePoints=null;
    public Integer matchesPlayed=null;

    // Getters and setters
    public String getTeamName() {
        return teamName;
    }
    public String getNewName() {
        return newName;
    }

    public void setTeamName(String teamName) { this.teamName = teamName; }

    public LocalDate getNewRegistrationDate() {
        return newRegistrationDate;
    }

    public Integer getMatchesPlayed() {
        return this.matchesPlayed;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public Integer getTotalGoals() {
        return this.totalGoals;
    }

    public void setTotalGoals(Integer totalGoals) { this.totalGoals = totalGoals; }

    public Integer getMatchPoints() {
        return matchPoints;
    }
    public void setMatchPoints(Integer matchPoints) { this.matchPoints = matchPoints; }

    public Integer getAlternatePoints() {
        return alternatePoints;
    }

    public void setNewName(String name) {
        this.newName = name;
    }

    public void setGroupNumber(Integer number) {
        this.groupNumber = number;
    }

    public void setMatchesPlayed(Integer matches) {
        this.matchesPlayed = matches;
    }

    public void setAlternatePoints(Integer points) {
        this.alternatePoints = points;
    }
}
