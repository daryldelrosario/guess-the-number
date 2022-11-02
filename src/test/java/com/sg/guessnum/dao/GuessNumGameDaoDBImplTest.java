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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
public class GuessNumGameDaoDBImplTest {
    
    @Autowired
    GuessNumGameDao gameDaoTest;
    
    @Autowired
    GuessNumRoundDao roundDaoTest;
    
    @Autowired
    JdbcTemplate jdbct;
    
    public GuessNumGameDaoDBImplTest() {
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
     * Test of addGame and getGameById method, of class GuessNumGameDaoDBImpl.
     */
    @Test
    public void testAddGetGameById() {
        System.out.println("Test: addGame and getGameById Dao");
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);
        game = gameDaoTest.addGame(game);
        
        Game fromDao = gameDaoTest.getGameById(game.getGameId());
        
        assertEquals(game, fromDao);
    }

    /**
     * Test of getAllGames method, of class GuessNumGameDaoDBImpl.
     */
    @Test
    public void testGetAllGames() {
        System.out.println("Test: getAllGames Dao");
        Game gameOne = new Game();
        gameOne.setAnswer("1234");
        gameOne.setIsFinished(false);
        gameDaoTest.addGame(gameOne);
        
        Game gameTwo = new Game();
        gameTwo.setAnswer("5678");
        gameTwo.setIsFinished(false);
        gameDaoTest.addGame(gameTwo);
        
        List<Game> gameList = gameDaoTest.getAllGames();
        
        assertEquals(2, gameList.size());
        assertTrue(gameList.contains(gameOne));
        assertTrue(gameList.contains(gameTwo));
    }

    /**
     * Test of updateGame method, of class GuessNumGameDaoDBImpl.
     */
    @Test
    public void testUpdateGame() {
        System.out.println("Test: updateGame Dao");
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);
        gameDaoTest.addGame(game);
        
        Game fromDao = gameDaoTest.getGameById(game.getGameId());
        assertEquals(game, fromDao);
        
        game.setIsFinished(true);
        gameDaoTest.updateGame(game);
        assertNotEquals(game, fromDao);
        
        fromDao = gameDaoTest.getGameById(game.getGameId());
        assertEquals(game, fromDao);
    }
    
    @Test
    public void testDeleteGameById() {
        System.out.println("Test: deleteGameById Dao");
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
        
        Game fromDao = gameDaoTest.getGameById(game.getGameId());
        assertEquals(game, fromDao);
        
        gameDaoTest.deleteGameById(game.getGameId());
        fromDao = gameDaoTest.getGameById(game.getGameId());
        assertNotEquals(game, fromDao);
        assertNull(fromDao);
    }
    
}
