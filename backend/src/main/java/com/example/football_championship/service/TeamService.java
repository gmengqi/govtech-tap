package com.example.football_championship.service;

import com.example.football_championship.DTO.CreateTeamDTO;
import com.example.football_championship.DTO.ProcessingResult;
import com.example.football_championship.DTO.UpdateTeamDTO;
import com.example.football_championship.audit.AuditLog;
import com.example.football_championship.comparator.TeamRankingComparator;
import com.example.football_championship.model.Team;
import com.example.football_championship.repository.AuditLogRepository;
import com.example.football_championship.repository.TeamRepository;

import com.example.football_championship.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void createAuditLog(String action, String entityName, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityName(entityName);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    public ProcessingResult<Team> addTeams(List<CreateTeamDTO> teamDTOs) {
        List<Team> validTeams = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        teamDTOs.forEach(dto -> {
            try {
                // Validate date and create the team object
                LocalDate date = ValidationUtils.validateDate(dto.getRegistrationDate());

                String teamName = dto.getName();
                if (teamRepository.findByName(teamName).isPresent()) {
                    throw new IllegalArgumentException("Team name already exists");
                }

                int groupNum = dto.getGroupNumber();
                if (groupNum > 2 || groupNum < 1) {
                    throw new IllegalArgumentException("Group number should either be 1 or 2 ");
                }

                Team team = new Team();
                team.setName(teamName);
                team.setRegistrationDate(date);
                team.setGroupNumber(groupNum);

                // Add to the list of valid teams
                validTeams.add(team);
            } catch (IllegalArgumentException e) {
                String msg = String.format("Error processing team for %s: %s", dto.getName(), e.getMessage());
                errorMessages.add(msg);
            }
        });

        ProcessingResult<Team> result = new ProcessingResult<>();
        teamRepository.saveAll(validTeams);
        createAuditLog("INSERT", "Team", validTeams.toString());
        result.setValidData(validTeams);
        result.setErrors(errorMessages);
        return result;
    }

    public Team getTeamDetails(String name) {
        System.out.println("retrieving info for team: " + name);
        Optional<Team> team = teamRepository.findByName(name);

        if (team.isEmpty()) {
            throw new NoSuchElementException(name + " do not exists");
        }

        createAuditLog("GET", "Team", team.get().name);
        return team.get();
    }

    public boolean deleteTeamByName(String name) {
        Optional<Team> team = teamRepository.findByName(name);

        if (team.isPresent()) {
            teamRepository.deleteByName(name);
            createAuditLog("DELETE", "Team", team.get().name);
            return true;
        } else {
            throw new NoSuchElementException("Team do not exists");
        }
    }

    public Team updateTeamDetails(String operation, UpdateTeamDTO updateTeamDTO) {
        Optional<Team> team = teamRepository.findByName(updateTeamDTO.getTeamName());

        if (team.isPresent()) {
            Team existingTeam = team.get();
            // Update the team details with the new values
            String newName = updateTeamDTO.getNewName();
            if (teamRepository.findByName(newName).isPresent()) {
                throw new IllegalArgumentException("Team name already taken");
            }

            existingTeam.setName(updateTeamDTO.getNewName() != null ? updateTeamDTO.getNewName() : existingTeam.getName());
            existingTeam.setRegistrationDate(updateTeamDTO.getNewRegistrationDate() != null ? updateTeamDTO.getNewRegistrationDate() : existingTeam.getRegistrationDate());
            existingTeam.setGroupNumber(updateTeamDTO.getGroupNumber() != null ? updateTeamDTO.getGroupNumber() : existingTeam.getGroupNumber());
            switch (operation) {
                case "UPDATE":
                    try {
                        existingTeam.setTotalGoals(existingTeam != null ? updateTeamDTO.getTotalGoals() + existingTeam.totalGoals : updateTeamDTO.getTotalGoals());
                        existingTeam.setMatchPoints(existingTeam != null ? updateTeamDTO.getMatchPoints() + existingTeam.matchPoints: updateTeamDTO.getMatchPoints());
                        existingTeam.setAlternatePoints(existingTeam != null ? updateTeamDTO.getAlternatePoints() + existingTeam.alternatePoints: updateTeamDTO.getAlternatePoints());
                        existingTeam.setMatchesPlayed(existingTeam != null ? updateTeamDTO.getMatchesPlayed() + existingTeam.matchesPlayed: updateTeamDTO.getMatchesPlayed());
                        // Save the updated team back to the database
                        Team teamUpdated = teamRepository.save(existingTeam);
                        createAuditLog("UPDATE", "Team", existingTeam.name);
                        return teamUpdated;
                    } catch (Exception e) {
                        throw new RuntimeException("Team not found with name: " + existingTeam.name);
                    }
                case "EDIT":
                    try {
                        existingTeam.setTotalGoals(updateTeamDTO.getTotalGoals() != null ? updateTeamDTO.getTotalGoals(): existingTeam.totalGoals);
                        existingTeam.setMatchPoints(updateTeamDTO.getMatchPoints() != null ? updateTeamDTO.getMatchPoints(): existingTeam.matchPoints);
                        existingTeam.setAlternatePoints(updateTeamDTO.getAlternatePoints() != null ? updateTeamDTO.getAlternatePoints(): existingTeam.alternatePoints);
                        existingTeam.setMatchesPlayed(updateTeamDTO.getMatchesPlayed() != null ? updateTeamDTO.getMatchesPlayed() : existingTeam.matchesPlayed);
                        // Save the updated team back to the database
                        Team teamUpdated = teamRepository.save(existingTeam);
                        createAuditLog("EDIT", "Team", existingTeam.name);
                        return teamUpdated;
                    } catch (Exception e) {
                        throw new RuntimeException("Team not found with name: " + existingTeam.name);
                    }
                default:
                    return null;
            }
        } else {
            throw new NoSuchElementException("The team does not exists");
        }
    }

    public List<Team> getRankingsByGroup(int groupNumber) {
        List<Team> teams = teamRepository.findByGroupNumber(groupNumber).orElseThrow(
                () -> new NoSuchElementException("Such group number does not exist"));

        if (teams.isEmpty()) {
            throw new NoSuchElementException("Such group number does not exist");
        } else {
            Collections.sort(teams, new TeamRankingComparator());
            createAuditLog("GET", "Team", "Get ranking for teams");
            return teams;
        }
    }

    public boolean getOutcomeForTeam(String teamName, int groupNumber) {
        List<Team> teams = getRankingsByGroup(groupNumber);

        for (int i = 0; i < teams.size(); i++) {
            Team currTeam = teams.get(i);
            if (currTeam.name.equals(teamName) && i < 4) {
                return true;
            }
        }
        return false;
    }
}
