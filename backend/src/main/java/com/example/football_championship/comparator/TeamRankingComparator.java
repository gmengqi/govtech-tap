package com.example.football_championship.comparator;

import com.example.football_championship.model.Team;

import java.util.Comparator;

public class TeamRankingComparator implements Comparator<Team> {

    @Override
    public int compare(Team team1, Team team2) {
        // 1. Compare total match points (primary points)
        if (team1.getTotalMatchPoints() != team2.getTotalMatchPoints()) {
            return Integer.compare(team2.getTotalMatchPoints(), team1.getTotalMatchPoints());
        }

        // 2. Compare total goals scored
        if (team1.getTotalGoals() != team2.getTotalGoals()) {
            return Integer.compare(team2.getTotalGoals(), team1.getTotalGoals());
        }

        // 3. Compare alternate match points (secondary points)
        if (team1.getAlternatePoints() != team2.getAlternatePoints()) {
            return Integer.compare(team2.getAlternatePoints(), team1.getAlternatePoints());
        }

        // 4. Compare by registration date (earliest date wins)
        return team1.getRegistrationDate().compareTo(team2.getRegistrationDate());
    }
}
