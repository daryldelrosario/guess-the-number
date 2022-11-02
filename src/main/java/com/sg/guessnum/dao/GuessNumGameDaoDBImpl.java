/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.dao;

import com.sg.guessnum.dto.Game;
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
public class GuessNumGameDaoDBImpl implements GuessNumGameDao {

    @Autowired
    JdbcTemplate jdbct;
    
    @Override
    @Transactional
    public Game addGame(Game game) {
        final String INSERT_GAME = "INSERT INTO Game(answer, isFinished) VALUES(?,?)";
        jdbct.update(INSERT_GAME,
                game.getAnswer(),
                game.getIsFinished());
        
        int newGameId = jdbct.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        game.setGameId(newGameId);
        return game;
    }

    @Override
    public List<Game> getAllGames() {
        final String SELECT_ALL_GAMES = "SELECT * FROM Game";
        return jdbct.query(SELECT_ALL_GAMES, new GameMapper());
    }

    @Override
    public Game getGameById(int gameId) {
        try {
            final String SELECT_GAME_BY_ID = "SELECT * FROM Game WHERE id = ?";
            return jdbct.queryForObject(SELECT_GAME_BY_ID, new GameMapper(), gameId);
        } catch(DataAccessException ex) {
            return null;
        }
    }
    
    @Override
    public void updateGame(Game game) {
        final String UPDATE_GAME = "UPDATE Game SET answer = ?, isFinished = ? WHERE id = ?";
        jdbct.update(UPDATE_GAME,
                game.getAnswer(),
                game.getIsFinished(),
                game.getGameId());
    }
    
    @Override
    @Transactional
    public void deleteGameById(int gameId) {
        final String DELETE_ROUND_BY_GAMEID = "DELETE FROM Round WHERE gameId =?";
        jdbct.update(DELETE_ROUND_BY_GAMEID, gameId);
        
        final String DELETE_GAME_BY_ID = "DELETE FROM Game WHERE id = ?";
        jdbct.update(DELETE_GAME_BY_ID, gameId);
    }
    
    public static final class GameMapper implements RowMapper<Game> {
        
        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameId(rs.getInt("id"));
            game.setAnswer(rs.getString("answer"));
            game.setIsFinished(rs.getBoolean("isFinished"));
            return game;
        }
    }

}
