/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.service;

import com.sg.guessnum.dao.GuessNumGameDao;
import com.sg.guessnum.dao.GuessNumRoundDao;
import com.sg.guessnum.dto.Game;
import com.sg.guessnum.dto.Round;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Daryl del Rosario
 */

@Service
public class GuessNumServiceImpl implements GuessNumService {
    
    @Autowired
    GuessNumGameDao gameDao;
    
    @Autowired
    GuessNumRoundDao roundDao;

    // Game Rules and Methods
    @Override
    public Game createNewGame() {
        String initialAnswerGenerated = this.generateInitialAnswer();
        
        Game newGame = new Game();
        newGame.setAnswer(initialAnswerGenerated);
        newGame.setIsFinished(false);
        gameDao.addGame(newGame);
        
        return newGame;
    }
    
    @Override
    public Round createNewRound(Round round) throws NoGameInDatabaseException, InvalidGameIdException, InvalidGuessException {
        Game chosenGame = gameDao.getGameById(round.getGameId());
        String userGuess = round.getGuess();
        
        this.validateGameGuess(chosenGame, userGuess);
        
        String gameAnswer = chosenGame.getAnswer();
        
        LocalDateTime rightNow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        round.setTimeOfGuess(rightNow);
        
        String roundResult = this.calculateGuessResult(gameAnswer, userGuess);
        round.setResult(roundResult);
        
        String [] resultToken = roundResult.split(":");
        int exactMatches = Integer.parseInt(resultToken[1]);
        
        if(exactMatches == 4) {
            chosenGame.setIsFinished(true);
            gameDao.updateGame(chosenGame);
        }
        
        round.setStatus();
        roundDao.addRound(round);
        return round;
    }
    
    @Override
    public List<Game> getAllGames() throws NoGameInDatabaseException {
        List<Game> gamesList = gameDao.getAllGames();
        int numberOfGames = gamesList.size();
        
        this.validateGameInDatabase(numberOfGames);
        
        for (Game g : gamesList) {
            if(!g.getIsFinished()) {
                g.setAnswer("Game is IN PROGRESS. Answer is HIDDEN until complete.");
            }
        }
        return gamesList;
    }
    
    @Override
    public Game getGameById(int gameId) throws NoGameInDatabaseException, InvalidGameIdException {
        List<Game> gamesList = gameDao.getAllGames();
        int numberOfGames = gamesList.size();
        
        this.validateGameInDatabase(numberOfGames);
        this.validateGameId(gameId);
        
        Game chosenGame = gameDao.getGameById(gameId);
        if(!chosenGame.getIsFinished()) {
            chosenGame.setAnswer("Game is IN PROGRESS. Answer is HIDDEN until complete.");
        }
        return chosenGame;
    }
    
    @Override
    public List<Round> getAllRoundsByGameId(int gameId) throws NoGameInDatabaseException, InvalidGameIdException{
        this.validateRoundsForGameId(gameId);
        
        List<Round> roundsByIdList = roundDao.getAllRoundsByGameId(gameId);
        for(Round r : roundsByIdList) {
            r.setStatus();
        }
        return roundsByIdList;
    }
    
    // Game and Round Dao passthrough Service  
    @Override
    public void updateGame(Game game) {
        gameDao.updateGame(game);
    }
    
    @Override
    public void deleteGameById(int gameId) {
        gameDao.deleteGameById(gameId);
    }
    
    @Override
    public List<Round> getAllRounds() {
        return roundDao.getAllRounds();
    }
    
    @Override
    public void updateRound(Round round) {
        roundDao.updateRound(round);
    }
    
    @Override
    public void deleteAllRoundsByGameId(int gameId) {
        roundDao.deleteAllRoundsByGameId(gameId);
    }
    
    // Helper Methods
    private String generateInitialAnswer() {
        List<Integer> zeroToNine = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            zeroToNine.add(i);
        }
        
        Collections.shuffle(zeroToNine);
        
        String fourDigit = "";
        for(int i = 0; i < 4; i++) {
            fourDigit += zeroToNine.get(i).toString();
        }
        
        return fourDigit;
    }
    
    private String calculateGuessResult(String gameAnswer, String userGuess) {
        int exactMatch = 0;
        int partialMatch = 0;
        String[] answerToken = gameAnswer.split("");
        String[] guessToken = userGuess.split("");
        
        for(int i = 0; i < gameAnswer.length(); i++) {
            if(answerToken[i].equals(guessToken[i])) {
                exactMatch += 1;
            } else if(gameAnswer.contains(guessToken[i])) {
                partialMatch += 1;
            }
        }
        
        String result = "e:" + exactMatch + ":" + "p:" + partialMatch;
        return result;
    }
    
    private void validateGameGuess(Game game, String userGuess) throws NoGameInDatabaseException, InvalidGameIdException, InvalidGuessException {
        String[] numberOfChar = userGuess.split("");
        List<Game> gamesList = gameDao.getAllGames();
        int numberOfGames = gamesList.size();
        
        if(numberOfGames == 0) {
            String msg = "No Games Have Begun! Try to POST http://localhost:8080/api/mseven/begin";
            throw new NoGameInDatabaseException(msg);
        }
        if(game == null) {
            String msg = "Game not found. There are only " + numberOfGames + " games available. Try another 'gameId:'";
            throw new InvalidGameIdException(msg);
        }
        
        if(game.getIsFinished()) {
            String msg = "Cannot guess for a completed game. Try another 'gameId:'";
            throw new InvalidGameIdException(msg);
        }
        
        if(numberOfChar.length != 4) {
            String msg = "There should be 4 unique digits. Try another 'guess:'";
            throw new InvalidGuessException(msg);
        }
        
        if(!isUnique(userGuess)) {
            String msg = "There should be 4 unique digits. Try another 'guess:'";
            throw new InvalidGuessException(msg);
        }    
    }
    
    private void validateGameInDatabase(int numberOfGames) throws NoGameInDatabaseException {
        if(numberOfGames == 0) {
            String msg = "No Games have begun! Try to POST http://localhost:8080/api/mseven/begin";
            throw new NoGameInDatabaseException(msg);
        }
    }
    
    private void validateGameId(int gameId) throws InvalidGameIdException {
        Game chosenGame = gameDao.getGameById(gameId);
        List<Game> gamesList = gameDao.getAllGames();
        
        if(chosenGame == null) {
            int numberOfGames = gamesList.size();
            String msg = "Game #" + gameId + " Not Found! There are only " + numberOfGames +
                    " game(s) available. Please try another 'gameId':";
            throw new InvalidGameIdException(msg);
        }
    }
    
    private void validateRoundsForGameId(int gameId) throws NoGameInDatabaseException, InvalidGameIdException {
        List<Game> gamesList = gameDao.getAllGames();
        List<Round> roundsListByGameId = roundDao.getAllRoundsByGameId(gameId);
        int numberOfGames = gamesList.size();
        
        this.validateGameInDatabase(numberOfGames);
        
        if(numberOfGames < gameId) {
            String msg = "There are only " + numberOfGames + " games available! Please try another 'gameId':";
            throw new InvalidGameIdException(msg);
        }
        
        if(roundsListByGameId.isEmpty()) {
            String msg = "No guesses were made for Game #" + gameId + ". Please try another 'gameId':";
            throw new InvalidGameIdException(msg);
        }
    }
    
    private boolean isUnique(String guess) {
        return guess.chars().distinct().count() == 4;
    }
}
