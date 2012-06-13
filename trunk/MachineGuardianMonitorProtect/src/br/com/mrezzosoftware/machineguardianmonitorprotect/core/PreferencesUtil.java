/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import java.util.prefs.Preferences;

public class PreferencesUtil {
    
    private final Preferences prefs;
    private static final PreferencesUtil pUtil = new PreferencesUtil();
    
    private PreferencesUtil(){
        prefs = Preferences.userRoot().node(Constantes.SIGLA_APP);
    }
    
    public static PreferencesUtil getInstance() {
        return pUtil;
    }
    
    public void registrarValor(String chave, String valor) {
        prefs.put(chave, valor);
    }
    
    public String obterValor(String chave) {
        String v = prefs.get(chave, "NREG");
        return v;
    }
}
