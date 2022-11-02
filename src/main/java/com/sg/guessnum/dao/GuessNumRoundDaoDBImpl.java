/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.dao;

import com.sg.guessnum.dto.Round;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Daryl del Rosario
 */
@Repository
public class GuessNumRoundDaoDBImpl implements GuessNumRoundDao {
    
    @Autowired
    JdbcTemplate jdbct;

    @Override
    @Transactional
    public Round addRound(Round round) {
        final String INSERT_ROUND = "INSERT INTO Round(guess, timeOfGuess, result, gameId) VALUES(?,?,?,?)";
        jdbct.update(INSERT_ROUND,
                round.getGuess(),
                round.getTimeOfGuess(),
                round.getResult(),
                round.getGameId());
        
        int newRoundId = jdbct.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        round.setRoundId(newRoundId);
        return round;
    }

    @Override
    public List<Round> getAllRoundsByGameId(int gameId) {
        try {
            final String SELECT_ROUND_BY_GAMEID = "SELECT * FROM Round r " +
                    "INNER JOIN Game g ON r.gameId = g.id " +
                    "WHERE r.gameId = ?";
            List<Round> roundList = jdbct.query(SELECT_ROUND_BY_GAMEID, new RoundMapper(), gameId);
            return roundList;
        } catch(DataAccessException ex) {
            return null;
        }
    }
    
    @Override
    public Round getRoundById(int roundId) {
        try {
            final String SELECT_ROUND_BY_ID = "SELECT * FROM Round WHERE id = ?";
            return jdbct.queryForObject(SELECT_ROUND_BY_ID, new RoundMapper(), roundId);
        } catch(DataAccessException ex) {
            return null;
        }
    }
    
    @Override
    public List<Round> getAllRounds() {
        final String SELECT_ALL_ROUNDS = "SELECT * FROM Round";
        return jdbct.query(SELECT_ALL_ROUNDS, new RoundMapper());
    }
    
    @Override
    public void updateRound(Round round) {
        final String UPDATE_ROUND = "UPDATE Round SET id = ?, guess = ?, timeOfGuess = ?, result = ?, gameId = ?";
        jdbct.update(UPDATE_ROUND,
                round.getRoundId(),
                round.getGuess(),
                round.getTimeOfGuess(),
                round.getResult(),
                round.getGameId());
    }
    
    @Override
    @Transactional
    public void deleteAllRoundsByGameId(int gameId) {
        final String DELETE_ROUND_BY_GAMEID = "DELETE FROM Round WHERE gameId = ?";
        jdbct.update(DELETE_ROUND_BY_GAMEID, gameId);
    }
    
    public static final class RoundMapper implements RowMapper<Round> {
        
        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setRoundId(rs.getInt("id"));
            round.setGuess(rs.getString("guess"));
            round.setTimeOfGuess(rs.getTimestamp("timeOfGuess").toLocalDateTime());
            round.setResult(rs.getString("result"));
            round.setGameId(rs.getInt("gameId"));
            return round;
        }
    }

}
