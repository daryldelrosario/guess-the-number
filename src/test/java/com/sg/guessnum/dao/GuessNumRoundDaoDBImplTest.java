/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnum.dao;

import com.sg.guessnum.dto.Game;
import com.sg.guessnum.dto.Round;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.apache.ibatis.jdbc.ScriptRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author Daryl del Rosario
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
public class GuessNumRoundDaoDBImplTest {
    
    @Autowired
    GuessNumGameDao gameDaoTest;
    
    @Autowired
    GuessNumRoundDao roundDaoTest;
    
    @Autowired
    JdbcTemplate jdbct;
    
    public GuessNumRoundDaoDBImplTest() {
    }

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("RESETING TEST DATABASE");
        String mysqlFile = "C:\\Users\\DNAProductions\\Desktop\\Amazon TSG 2022 Part II\\Assesment\\02 Guess The Number REST Service\\zGuess The Number Resources\\v3\\GuessNumDbTest.sql";
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        String mysqlUrl = "jdbc:mysql://localhost:3306/GuessNumDBTest";
        Connection con = DriverManager.getConnection(mysqlUrl, "root", "2022Rootpassword+");
        ScriptRunner sr = new ScriptRunner(con);
        Reader reader = new BufferedReader(new FileReader(mysqlFile));
        sr.runScript(reader);
    }

    /**
     * Test of addRound and getAllRoundByGameId method, of class GuessNumRoundDaoDBImpl.
     */
    @Test
    public void testAddGetAllRoundsByGameId() {
        System.out.println("Test: addRound and getAllRoundsByGameId Dao");
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);
        gameDaoTest.addGame(game);
        
        Game gameFromDao = gameDaoTest.getGameById(game.getGameId());
        assertEquals(game, gameFromDao);
        
        Round roundOne = new Round();
        roundOne.setGameId(gameFromDao.getGameId());
        roundOne.setGuess("4321");
        roundOne.setResult("e:0:p:4");
        roundOne.setTimeOfGuess(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        roundDaoTest.addRound(roundOne);
        
        List<Round> roundsFromDao = roundDaoTest.getAllRoundsByGameId(gameFromDao.getGameId());
        assertEquals(1, roundsFromDao.size());
        assertTrue(roundsFromDao.contains(roundOne));
    }
    
    @Test
    public void testAddGetRoundById() {
        System.out.println("Test: getRoundById Dao");
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);
        gameDaoTest.addGame(game);
        
        Round round = new Round();
        round.setGameId(game.getGameId());
        round.setGuess("5678");
        round.setResult("e:0:p:0");
        round.setTimeOfGuess(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        roundDaoTest.addRound(round);
        
        Round fromDao = roundDaoTest.getRoundById(round.getRoundId());
        assertEquals(round, fromDao);
        
    }
    
    @Test
    public void testGetAllRounds() {
        System.out.println("Test: getAllRounds Dao");
        Game gameOne = new Game();
        gameOne.setAnswer("1234");
        gameOne.setIsFinished(false);
        gameDaoTest.addGame(gameOne);
        
        Game gameTwo = new Game();
        gameTwo.setAnswer("5678");
        gameTwo.setIsFinished(false);
        gameDaoTest.addGame(gameTwo);
        
        Round roundOne = new Round();
        roundOne.setGameId(gameOne.getGameId());
        roundOne.setGuess("5678");
        roundOne.setResult("e:0:p:0");
        roundOne.setTimeOfGuess(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        roundDaoTest.addRound(roundOne);
        
        Round roundTwo = new Round();
        roundTwo.setGameId(gameOne.getGameId());
        roundTwo.setGuess("8765");
        roundTwo.setResult("e:0:p:0");
        roundTwo.setTimeOfGuess(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        roundDaoTest.addRound(roundTwo);
        
        Round roundThree = new Round();
        roundThree.setGameId(gameTwo.getGameId());
        roundThree.setGuess("1234");
        roundThree.setResult("e:0:p:0");
        roundThree.setTimeOfGuess(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        roundDaoTest.addRound(roundThree);
        
        List<Round> fullRoundsList = roundDaoTest.getAllRounds();
        assertEquals(3, fullRoundsList.size());
        assertTrue(fullRoundsList.contains(roundOne));
        assertTrue(fullRoundsList.contains(roundTwo));
        assertTrue(fullRoundsList.contains(roundThree));
    }
    
    @Test
    public void testUpdateRound() {
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);
        gameDaoTest.addGame(game);
        
        Round round = new Round();
        round.setGameId(game.getGameId());
        round.setGuess("5678");
        round.setResult("e:0:p:0");
        round.setTimeOfGuess(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        roundDaoTest.addRound(round);
        
        Round roundFromDao = roundDaoTest.getRoundById(round.getRoundId());
        assertEquals(round, roundFromDao);
        
        round.setGuess("8765");
        roundDaoTest.updateRound(round);
        assertNotEquals(round, roundFromDao);
        
        roundFromDao = roundDaoTest.getRoundById(round.getRoundId());
        assertEquals(round, roundFromDao);
    }

    @Test
    public void testDeleteAllRoundsByGameId() {
        System.out.println("Test: deleteAllRoundsByGameId Dao");
        Game gameOne = new Game();
        gameOne.setAnswer("1234");
        gameOne.setIsFinished(false);
        gameDaoTest.addGame(gameOne);
        
        Game gameTwo = new Game();
        gameTwo.setAnswer("5678");
        gameTwo.setIsFinished(false);
        gameDaoTest.addGame(gameTwo);
        
        Round roundOne = new Round();
        roundOne.setGameId(gameOne.getGameId());
        roundOne.setGuess("5678");
        roundOne.setResult("e:0:p:0");
        roundOne.setTimeOfGuess(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        roundDaoTest.addRound(roundOne);
        
        Round roundTwo = new Round();
        roundTwo.setGameId(gameTwo.getGameId());
        roundTwo.setGuess("1234");
        roundTwo.setResult("e:0:p:0");
        roundTwo.setTimeOfGuess(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        roundDaoTest.addRound(roundTwo);
        
        Round roundThree = new Round();
        roundThree.setGameId(gameTwo.getGameId());
        roundThree.setGuess("4321");
        roundThree.setResult("e:0:p:0");
        roundThree.setTimeOfGuess(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        roundDaoTest.addRound(roundThree);
        
        List<Round> fullRoundsList = roundDaoTest.getAllRounds();
        assertEquals(3, fullRoundsList.size());
        
        roundDaoTest.deleteAllRoundsByGameId(gameTwo.getGameId());
        fullRoundsList = roundDaoTest.getAllRounds();
        assertEquals(1, fullRoundsList.size());
        assertTrue(fullRoundsList.contains(roundOne));
        assertFalse(fullRoundsList.contains(roundTwo));
        assertFalse(fullRoundsList.contains(roundThree));
        
        roundDaoTest.deleteAllRoundsByGameId(gameOne.getGameId());
        fullRoundsList = roundDaoTest.getAllRounds();
        assertTrue(fullRoundsList.isEmpty());
    }
}
