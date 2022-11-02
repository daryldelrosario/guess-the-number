/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.dao;

import com.sg.guessnum.dto.Round;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public interface GuessNumRoundDao {
    
    // Required CRUD Operations for Round Dao
    Round addRound(Round round);
    List<Round> getAllRoundsByGameId(int gameId);
    
    // CRUD Operations to Complete Round Dao
    Round getRoundById(int roundId);
    List<Round> getAllRounds();
    void updateRound(Round round);
    void deleteAllRoundsByGameId(int gameId);
    
}
