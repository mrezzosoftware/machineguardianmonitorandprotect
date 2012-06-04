/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import com.sun.jna.Library;

/**
 *
 * @author Marina
 */
public interface MyPowrProf extends Library {
    
    boolean SetSuspendState(boolean hibernate, boolean forceCritical, boolean disableWakeupEvent);
    
}
