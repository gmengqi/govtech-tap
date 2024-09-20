package com.example.football_championship.utils;

import com.example.football_championship.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ValidationUtils {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private static TeamRepository teamRepository;
    public static LocalDate validateDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is empty or null");
        }
        try {
            // Parse the date string using the defined format
            int currYear = LocalDate.now().getYear();
            LocalDate updatedDate = LocalDate.parse(date + "/" + currYear, DATE_FORMAT);
            return updatedDate;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'dd/MM'. Provided: " + date);
        }
    }

    public static void validateInput(String teamName, int groupNumber) {
        if (teamRepository.findByName(teamName).isPresent()) {
            throw new IllegalArgumentException("Team name already exists");
        }
        if (groupNumber != 1 || groupNumber != 2) {
            throw new IllegalArgumentException("Group number should either be 1 or 2 ");
        }
    }

    public static List<Integer> calculatePoints(String pointSystem, int teamAGoals, int teamBGoals) {
        int teamAPoints = 0;
        int teamBPoints = 0;

        switch (pointSystem) {
            case "ALTERNATE":
                if (teamAGoals > teamBGoals) {
                    // Team A wins
                    teamAPoints = 5;
                    teamBPoints = 1;
                } else if (teamAGoals < teamBGoals) {
                    // Team B wins
                    teamBPoints = 5;
                    teamAPoints = 1;
                } else {
                    // Draw
                    teamBPoints = 3;
                    teamAPoints = 3;
                }
                return List.of(teamAPoints, teamBPoints);
            case "STANDARD":
                if (teamAGoals > teamBGoals) {
                    // Team A wins
                    teamAPoints = 3;
                } else if (teamAGoals < teamBGoals) {
                    // Team B wins
                    teamBPoints = 3;
                } else {
                    // Draw
                    teamAPoints = 1;
                    teamBPoints = 1;
                }
                return List.of(teamAPoints, teamBPoints);
            default:
                return null;
        }
    }

}
