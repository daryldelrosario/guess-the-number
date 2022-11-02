/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.service;

import com.sg.guessnum.dto.Game;
import com.sg.guessnum.dto.Round;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public interface GuessNumService {
    
    
    // Game Rules and Methods
    Game createNewGame();
    Round createNewRound(Round round) throws NoGameInDatabaseException,InvalidGameIdException,InvalidGuessException;
    List<Game> getAllGames() throws NoGameInDatabaseException;
    Game getGameById(int gameId) throws NoGameInDatabaseException, InvalidGameIdException;
    List<Round> getAllRoundsByGameId(int gameId) throws NoGameInDatabaseException, InvalidGameIdException;
    
    // Game and Round DAO passthrough Service
    void updateGame(Game game);
    void deleteGameById(int gameId);
    List<Round> getAllRounds();
    void updateRound(Round round);
    void deleteAllRoundsByGameId(int gameId);
}
