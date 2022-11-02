/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.controller;

import com.sg.guessnum.dto.Game;
import com.sg.guessnum.dto.Round;
import com.sg.guessnum.service.GuessNumService;
import com.sg.guessnum.service.InvalidGameIdException;
import com.sg.guessnum.service.InvalidGuessException;
import com.sg.guessnum.service.NoGameInDatabaseException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Daryl del Rosario
 */

@RestController
@RequestMapping("/api/mseven")
public class GuessNumController {
    
    @Autowired
    GuessNumService service;
    
    @PostMapping("/begin")
    public ResponseEntity beginNewGame() {
        Game newGame = service.createNewGame();
        String message  = "New game SUCCESSFULLY created: Check Status.\nGame Id #: " + newGame.getGameId();
        System.out.println("Number: " + newGame.getAnswer());
        return new ResponseEntity(message, HttpStatus.CREATED);
    }

    @PostMapping("/guess")
    public ResponseEntity displayGuessResult(@RequestBody Round round) throws NoGameInDatabaseException, InvalidGameIdException, InvalidGuessException {
        Round newRound = service.createNewRound(round);
        return ResponseEntity.ok(newRound);
    }
    
    @GetMapping("/game")
    public ResponseEntity displayAllGames() throws NoGameInDatabaseException {
        List<Game> gamesList = service.getAllGames();
        return ResponseEntity.ok(gamesList);
    }
    
    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> displayGameById(@PathVariable int gameId) throws InvalidGameIdException, NoGameInDatabaseException {
        Game chosenGame = service.getGameById(gameId);
        return ResponseEntity.ok(chosenGame);
    }
    
    @GetMapping("/rounds/{gameId}") 
    public ResponseEntity displayRoundsByGameId(@PathVariable int gameId) throws NoGameInDatabaseException, InvalidGameIdException {
        List<Round> roundsByGameIdList = service.getAllRoundsByGameId(gameId);
        return ResponseEntity.ok(roundsByGameIdList);
    }
    
    @PostMapping("/reset")
    public ResponseEntity resetGame() throws Exception {
        System.out.println("RESETING DATABASE");
        String mysqlFile = "C:\\Users\\DNAProductions\\Desktop\\Amazon TSG 2022 Part II\\Assesment\\02 Guess The Number REST Service\\zGuess The Number Resources\\v3\\GuessNumDB.sql";
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        String mysqlUrl = "jdbc:mysql://localhost:3306/GuessNumDB";
        Connection con = DriverManager.getConnection(mysqlUrl, "root", "2022Rootpassword+");
        ScriptRunner sr = new ScriptRunner(con);
        Reader reader = new BufferedReader(new FileReader(mysqlFile));
        sr.runScript(reader);
        
        String msg = "The Game Has Been Succcessfuly RESET";
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
