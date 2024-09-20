package com.example.football_championship.controller;

import com.example.football_championship.DTO.CreateMatchDTO;
import com.example.football_championship.DTO.ProcessingResult;
import com.example.football_championship.model.Match;
import com.example.football_championship.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping(value = "/addMatches", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProcessingResult<Match>> addMatch(@RequestBody List<CreateMatchDTO> dtoList) {
        return ResponseEntity.ok(matchService.addMatch(dtoList));
    }
}
