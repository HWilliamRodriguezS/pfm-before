/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eui.miw.pfm.controllers.beans;

import java.util.Date;

/**
 *
 * @author William
 */
public class ConfProjectBean {
    
    
    public boolean validDates(Date start,Date end){
        
        boolean validDates = false;
        Date actual = new Date(System.currentTimeMillis() - (60 * 60 * 1000));
        if((start.after(actual) || start.equals(actual)) && (end.after(start))){
          validDates = true;
        }
        
        return validDates;
    }
    
}
