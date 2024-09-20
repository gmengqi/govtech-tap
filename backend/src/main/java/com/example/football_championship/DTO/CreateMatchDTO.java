package com.example.football_championship.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateMatchDTO {
    @NotNull(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @JsonProperty("teamAName")
    private String teamAName;

    @NotNull(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @JsonProperty("teamBName")
    private String teamBName;

    @JsonProperty("teamAGoals")
    private int teamAGoals;

    @JsonProperty("teamBGoals")
    private int teamBGoals;

    public String getTeamA() {
        return this.teamAName;
    }

    public int getTeamAScore() {
        return this.teamAGoals;
    }

    public String getTeamB() {
        return this.teamBName;
    }

    public int getTeamBScore() {
        return this.teamBGoals;
    }

    public String setTeamA(String teamAName) {
        return this.teamAName = teamAName;
    }

    public int setTeamAScore(int score) {
        return this.teamAGoals = score;
    }

    public String setTeamB(String teamBName) {
        return this.teamBName = teamBName;
    }

    public int setTeamBScore(int teamBGoals) {
        return this.teamBGoals = teamBGoals;
    }
}
