package com.example.football_championship.controller;

import com.example.football_championship.DTO.CreateTeamDTO;
import com.example.football_championship.DTO.ProcessingResult;
import com.example.football_championship.DTO.UpdateTeamDTO;
import com.example.football_championship.model.Team;
import com.example.football_championship.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/getTeam/{teamName}")
    public ResponseEntity<Team> getTeamByName(@PathVariable("teamName") String teamName) {
        Team teamRetrieved = teamService.getTeamDetails(teamName);
        return ResponseEntity.ok(teamRetrieved);
    }

    @PostMapping(value = "/addTeams", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProcessingResult> addTeam(@RequestBody List<CreateTeamDTO> teamDTOs) {
        ProcessingResult result = teamService.addTeams(teamDTOs);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @DeleteMapping("/deleteTeam/{teamName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeam(@PathVariable String teamName) {
        boolean isDeleted = teamService.deleteTeamByName(teamName);
    }

    @GetMapping("/rankings/{groupNumber}")
    public ResponseEntity<List<Team>> getRankings(@PathVariable int groupNumber) {
        return ResponseEntity.ok(teamService.getRankingsByGroup(groupNumber));
    }

    @PutMapping(value = "/updateTeam/{operation}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Team> updateTeamDetails(@PathVariable String operation, @RequestBody UpdateTeamDTO updateTeamDTO) {
        return ResponseEntity.ok(teamService.updateTeamDetails(operation, updateTeamDTO));
    }

    @GetMapping("/rankings/getOutcome/{teamName}/{groupNumber}")
    public ResponseEntity<Boolean> getOutcome(@PathVariable String teamName, @PathVariable int groupNumber) {
        return ResponseEntity.ok(teamService.getOutcomeForTeam(teamName, groupNumber));
    }
}
