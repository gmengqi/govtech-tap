package com.example.football_championship.service;

import com.example.football_championship.DTO.CreateMatchDTO;
import com.example.football_championship.DTO.ProcessingResult;
import com.example.football_championship.DTO.UpdateTeamDTO;
import com.example.football_championship.audit.AuditLog;
import com.example.football_championship.model.Match;
import com.example.football_championship.model.Team;
import com.example.football_championship.repository.AuditLogRepository;
import com.example.football_championship.repository.MatchRepository;

import com.example.football_championship.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private TeamService teamService;

    public ProcessingResult<Match> addMatch(List<CreateMatchDTO> matchDTOList) {

        List<Match> validMatch = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        matchDTOList.forEach(dto -> {
            try {
                String teamAName = dto.getTeamA();
                String teamBName = dto.getTeamB();

                if (teamAName.equals(teamBName)) {
                    throw new IllegalArgumentException("Duplicate team name");
                }

                Team teamA = teamService.getTeamDetails(teamAName);
                Team teamB = teamService.getTeamDetails(teamBName);

                int teamAGoals = dto.getTeamAScore();
                int teamBGoals = dto.getTeamBScore();

                if (teamAGoals < 0 || teamBGoals < 0) {
                    throw new IllegalArgumentException("score must be more than 0");
                }

                List<Integer> standardMatchPoints = ValidationUtils.calculatePoints("STANDARD", teamAGoals, teamBGoals);
                int teamAPoints = standardMatchPoints.get(0);
                int teamBPoints = standardMatchPoints.get(1);

                List<Integer> altMatchPoints = ValidationUtils.calculatePoints("ALTERNATE", teamAGoals, teamBGoals);
                int teamAAltPoints = altMatchPoints.get(0);
                int teamBAltPoints = altMatchPoints.get(1);

                // Save updated teams to the database
                UpdateTeamDTO updateTeamDTOA = new UpdateTeamDTO();
                updateTeamDTOA.setTeamName(teamA.name);
                updateTeamDTOA.matchPoints = teamAPoints;
                updateTeamDTOA.totalGoals = teamAGoals;
                updateTeamDTOA.alternatePoints = teamAAltPoints;
                updateTeamDTOA.matchesPlayed = 1;

                UpdateTeamDTO updateTeamDTOB = new UpdateTeamDTO();
                updateTeamDTOB.setTeamName(teamB.name);
                updateTeamDTOB.matchPoints = teamBPoints;
                updateTeamDTOB.totalGoals = teamBGoals;
                updateTeamDTOB.alternatePoints = teamBAltPoints;
                updateTeamDTOB.matchesPlayed = 1;

                teamService.updateTeamDetails("UPDATE", updateTeamDTOA);
                teamService.updateTeamDetails("UPDATE", updateTeamDTOB);

                Match match = new Match();
                match.setTeamA(dto.getTeamA());
                match.setTeamB(dto.getTeamB());
                match.setTeamBScore(dto.getTeamBScore());
                match.setTeamAScore(dto.getTeamAScore());
                validMatch.add(match);

            } catch (IllegalArgumentException e) {
                String msg = String.format("Error processing match result for %s and %s: %s", dto.getTeamA(), dto.getTeamB(), e.getMessage());
                errorMessages.add(msg);
            }
        });

        ProcessingResult<Match> result = new ProcessingResult<>();
        matchRepository.saveAll(validMatch);
        createAuditLog("INSERT", "Match", validMatch.toString());
        result.setValidData(validMatch);
        result.setErrors(errorMessages);
        return result;
    }

    public void createAuditLog(String action, String entityName, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityName(entityName);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
