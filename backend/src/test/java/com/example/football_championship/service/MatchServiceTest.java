package com.example.football_championship.service;

import com.example.football_championship.DTO.CreateMatchDTO;
import com.example.football_championship.DTO.ProcessingResult;
import com.example.football_championship.DTO.UpdateTeamDTO;
import com.example.football_championship.model.Match;
import com.example.football_championship.model.Team;
import com.example.football_championship.repository.AuditLogRepository;
import com.example.football_championship.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private MatchService matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    void testAddMatch() {
        // Prepare input data
        CreateMatchDTO matchDTO1 = new CreateMatchDTO();
        matchDTO1.setTeamA("TeamA");
        matchDTO1.setTeamB("TeamB");
        matchDTO1.setTeamAScore(2);
        matchDTO1.setTeamBScore(1);

        List<CreateMatchDTO> matchDTOList = Arrays.asList(matchDTO1);

        // Prepare mock Team objects
        Team teamA = new Team();
        teamA.setName("TeamA");
        Team teamB = new Team();
        teamB.setName("TeamB");

        // Mock behaviors of teamService and matchRepository
        when(teamService.getTeamDetails("TeamA")).thenReturn(teamA);
        when(teamService.getTeamDetails("TeamB")).thenReturn(teamB);
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method under test
        ProcessingResult<Match> result = matchService.addMatch(matchDTOList);

        // Verify that teamService.updateTeamDetails was called for both teams
        verify(teamService, times(2)).updateTeamDetails(any(), any(UpdateTeamDTO.class));

        // Verify that matchRepository.save was called
        verify(matchRepository, times(1)).saveAll(anyList());

        // Validate the result
        assertEquals(1, result.getValidData().size());
        assertEquals("TeamA", result.getValidData().get(0).getTeamA());
        assertEquals("TeamB", result.getValidData().get(0).getTeamB());
        assertEquals(2, result.getValidData().get(0).getTeamAScore());
        assertEquals(1, result.getValidData().get(0).getTeamBScore());
    }

    @Test
    void testAddMatch_InvalidScores() {
        // Prepare input data with invalid score
        CreateMatchDTO matchDTO1 = new CreateMatchDTO();
        matchDTO1.setTeamA("TeamA");
        matchDTO1.setTeamB("TeamB");
        matchDTO1.setTeamAScore(-1);  // Invalid score
        matchDTO1.setTeamBScore(1);

        List<CreateMatchDTO> matchDTOList = Arrays.asList(matchDTO1);

        ProcessingResult<Match> result = matchService.addMatch(matchDTOList);


        // Expect IllegalArgumentException when calling addMatch
        assertEquals(1, result.getErrors().size());
        assertEquals(0, result.getValidData().size());
    }
}
