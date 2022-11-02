/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.dao;

import com.sg.guessnum.dto.Game;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public interface GuessNumGameDao {
    
    // Required CRUD Operations for Game DAO
    Game addGame(Game game);
    List<Game> getAllGames();
    Game getGameById(int gameId);
    void updateGame(Game game);
    
    // CRUD Operations to complete Game DAO
    void deleteGameById(int gameId);
}
