/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.service;

/**
 *
 * @author Daryl del Rosario
 */
public class NoGameInDatabaseException extends Exception {
    
    public NoGameInDatabaseException(String msg) {
        super(msg);
    }
    
    public NoGameInDatabaseException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
