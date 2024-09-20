package com.example.football_championship.service;

import com.example.football_championship.DTO.CreateTeamDTO;
import com.example.football_championship.DTO.ProcessingResult;
import com.example.football_championship.DTO.UpdateTeamDTO;
import com.example.football_championship.model.Team;
import com.example.football_championship.repository.AuditLogRepository;
import com.example.football_championship.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTeams() {
        // Prepare input data
        CreateTeamDTO teamDTO1 = new CreateTeamDTO();
        teamDTO1.setName("TeamA");
        teamDTO1.setRegistrationDate("01/01");
        teamDTO1.setGroupNumber(1);

        CreateTeamDTO teamDTO2 = new CreateTeamDTO();
        teamDTO2.setName("TeamB");
        teamDTO2.setRegistrationDate("02/01");
        teamDTO2.setGroupNumber(1);

        List<CreateTeamDTO> teamDTOList = Arrays.asList(teamDTO1, teamDTO2);

        // Prepare mock team objects
        Team teamA = new Team();
        teamA.setName("TeamA");
        teamA.setRegistrationDate(LocalDate.of(2024, 1, 1));
        teamA.setGroupNumber(1);

        Team teamB = new Team();
        teamB.setName("TeamB");
        teamB.setRegistrationDate(LocalDate.of(2024, 1, 2));
        teamB.setGroupNumber(1);

        // Mock repository behavior
        when(teamRepository.saveAll(anyList())).thenReturn(Arrays.asList(teamA, teamB));

        // Call the method under test
        ProcessingResult result = teamService.addTeams(teamDTOList);

        // Verify that the teams were saved
        verify(teamRepository, times(1)).saveAll(anyList());

        // Validate result
        assertEquals(2, result.getValidData().size());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void testAddTeams_InvalidTeam() {
        // Prepare input data
        CreateTeamDTO teamDTO1 = new CreateTeamDTO();
        teamDTO1.setName("TeamA");
        teamDTO1.setRegistrationDate("01/01");
        teamDTO1.setGroupNumber(1);

        CreateTeamDTO teamDTO2 = new CreateTeamDTO();
        teamDTO2.setName("TeamB");
        teamDTO2.setRegistrationDate("0w231");
        teamDTO2.setGroupNumber(1);

        List<CreateTeamDTO> teamDTOList = Arrays.asList(teamDTO1, teamDTO2);

        // Prepare mock team objects
        Team teamA = new Team();
        teamA.setName("TeamA");
        teamA.setRegistrationDate(LocalDate.of(2024, 1, 1));
        teamA.setGroupNumber(1);

        Team teamB = new Team();
        teamB.setName("TeamB");
        teamB.setRegistrationDate(LocalDate.of(2024, 1, 2));
        teamB.setGroupNumber(1);

        // Mock repository behavior
        when(teamRepository.saveAll(anyList())).thenReturn(Arrays.asList(teamA));

        // Call the method under test
        ProcessingResult result = teamService.addTeams(teamDTOList);

        // Verify that the teams were saved
        verify(teamRepository, times(1)).saveAll(anyList());

        // Validate result
        assertEquals(1, result.getValidData().size());
        assertEquals(1, result.getErrors().size());
    }


    @Test
    void testGetTeamDetails_Success() {
        // Prepare mock team object
        Team team = new Team();
        team.setName("TeamA");

        // Mock repository behavior
        when(teamRepository.findByName("TeamA")).thenReturn(Optional.of(team));

        // Call the method under test
        Team result = teamService.getTeamDetails("TeamA");

        // Verify result
        assertEquals("TeamA", result.getName());
        verify(teamRepository, times(1)).findByName("TeamA");
    }

    @Test
    void testGetTeamDetails_TeamNotFound() {
        // Mock repository behavior for missing team
        when(teamRepository.findByName("TeamA")).thenReturn(Optional.empty());

        // Test exception
        assertThrows(NoSuchElementException.class, () -> teamService.getTeamDetails("TeamA"));
        verify(teamRepository, times(1)).findByName("TeamA");
    }

    @Test
    void testDeleteTeamByName_Success() {
        // Prepare mock team object
        Team team = new Team();
        team.setName("TeamA");

        // Mock repository behavior
        when(teamRepository.findByName("TeamA")).thenReturn(Optional.of(team));

        // Call the method under test
        boolean result = teamService.deleteTeamByName("TeamA");

        // Verify that the team was deleted
        assertTrue(result);
        verify(teamRepository, times(1)).deleteByName("TeamA");
    }

    @Test
    void testDeleteTeamByName_TeamNotFound() {
        // Mock repository behavior for missing team
        when(teamRepository.findByName("TeamA")).thenReturn(Optional.empty());

        // Test exception
        assertThrows(NoSuchElementException.class, () -> teamService.deleteTeamByName("TeamA"));
        verify(teamRepository, never()).deleteByName("TeamA");
    }

    @Test
    void testUpdateTeamDetails_Success() {
        // Prepare mock team object
        Team existingTeam = new Team();
        existingTeam.setName("TeamA");

        // Prepare update DTO
        UpdateTeamDTO updateDTO = new UpdateTeamDTO();
        updateDTO.setTeamName("TeamA");
        updateDTO.setNewName("TeamA New");
        updateDTO.setGroupNumber(2);
        updateDTO.setTotalGoals(3);
        updateDTO.setMatchesPlayed(1);
        updateDTO.setMatchPoints(3);
        updateDTO.setAlternatePoints(2);

        // Mock repository behavior
        when(teamRepository.findByName("TeamA")).thenReturn(Optional.of(existingTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(existingTeam);

        // Call the method under test
        Team result = teamService.updateTeamDetails("EDIT", updateDTO);

        // Verify the update was successful
        assertEquals("TeamA New", result.getName());
        assertEquals(3, result.getTotalGoals());
        verify(teamRepository, times(1)).save(existingTeam);
    }

    @Test
    void testGetRankingsByGroup_Success() {
        // Prepare mock teams list
        Team teamA = new Team();
        teamA.setName("TeamA");
        teamA.setMatchPoints(10);

        Team teamB = new Team();
        teamB.setName("TeamB");
        teamA.setMatchPoints(5);

        List<Team> teams = Arrays.asList(teamA, teamB);

        // Mock repository behavior
        when(teamRepository.findByGroupNumber(1)).thenReturn(Optional.of(teams));

        // Call the method under test
        List<Team> result = teamService.getRankingsByGroup(1);

        // Verify the rankings
        assertEquals(2, result.size());
        verify(teamRepository, times(1)).findByGroupNumber(1);
    }

    @Test
    void testGetOutcomeForTeam_Success() {
        // Prepare mock teams list
        Team teamA = new Team();
        teamA.setName("TeamA");
        teamA.setMatchPoints(10);
        Team teamB = new Team();
        teamB.setName("TeamB");
        teamB.setMatchPoints(5);

        List<Team> teams = Arrays.asList(teamA, teamB);

        // Mock repository behavior
        when(teamRepository.findByGroupNumber(1)).thenReturn(Optional.of(teams));

        // Call the method under test and verify outcome
        boolean outcome = teamService.getOutcomeForTeam("TeamA", 1);
        assertTrue(outcome);

        verify(teamRepository, times(1)).findByGroupNumber(1);
    }

    @Test
    void testGetOutcomeForTeam_Failure() {
        // Prepare mock teams list
        Team teamA = new Team();
        teamA.setName("TeamA");
        teamA.setMatchPoints(10);

        Team teamB = new Team();
        teamB.setName("TeamB");
        teamB.setMatchPoints(5);

        Team teamC = new Team();
        teamC.setName("teamC");
        teamC.setMatchPoints(5);

        Team teamD = new Team();
        teamD.setName("teamD");
        teamD.setMatchPoints(3);
        teamD.setTotalGoals(4);

        Team teamE = new Team();
        teamE.setName("teamE");
        teamE.setMatchPoints(3);
        teamE.setTotalGoals(10);


        List<Team> teams = Arrays.asList(teamA, teamB);

        // Mock repository behavior
        when(teamRepository.findByGroupNumber(1)).thenReturn(Optional.of(teams));

        // Call the method under test and verify outcome
        boolean outcome = teamService.getOutcomeForTeam("teamD", 1);
        assertFalse(outcome);

        verify(teamRepository, times(1)).findByGroupNumber(1);
    }
}
