/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.dto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Daryl del Rosario
 */
public class Round {
    
    private int roundId;
    private int gameId;
    private String guess;
    private String result;
    private String status;
    private LocalDateTime timeOfGuess;

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public LocalDateTime getTimeOfGuess() {
        return timeOfGuess;
    }

    public void setTimeOfGuess(LocalDateTime timeOfGuess) {
        this.timeOfGuess = timeOfGuess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus() {
        String[] resultToken = this.result.split(":");
        int exactMatches = Integer.parseInt(resultToken[1]);
        if(exactMatches == 4) {
            this.status = "COMPLETED";
        } else {
            this.status = "IN PROGRESS";
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.roundId;
        hash = 67 * hash + this.gameId;
        hash = 67 * hash + Objects.hashCode(this.guess);
        hash = 67 * hash + Objects.hashCode(this.result);
        hash = 67 * hash + Objects.hashCode(this.status);
        hash = 67 * hash + Objects.hashCode(this.timeOfGuess);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Round other = (Round) obj;
        if (this.roundId != other.roundId) {
            return false;
        }
        if (this.gameId != other.gameId) {
            return false;
        }
        if (!Objects.equals(this.guess, other.guess)) {
            return false;
        }
        if (!Objects.equals(this.result, other.result)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.timeOfGuess, other.timeOfGuess)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Round{" + "roundId=" + roundId + ", gameId=" + gameId + ", guess=" + guess + ", result=" + result + ", status=" + status + ", timeOfGuess=" + timeOfGuess + '}';
    }
}
