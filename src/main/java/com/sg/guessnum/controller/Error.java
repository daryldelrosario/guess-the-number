/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 *
 * @author Daryl del Rosario
 */
public class Error {
    
    private final LocalDateTime timestamp = LocalDateTime.now();
    private String timeOfError;
    private String msg;
    
    public String getTimeOfError() {
        return timeOfError;
    }
    
    public void setTimeOfError() {
        LocalDateTime rightNow = this.timestamp;
        String formatDate = rightNow.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        String formatTime = rightNow.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));
        
        String displayTime = formatDate + " @ " + formatTime;
        this.timeOfError = displayTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
