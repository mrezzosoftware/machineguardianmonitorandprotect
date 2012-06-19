/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

/**
 *
 * @author 01837484333
 */
public class Operacoes {

    private boolean modoEspera = false;
    private byte tempoAtualizacao = 1;
    private byte idOperacao = 0;
    private boolean capturarTeclas = false;
    private boolean geolocalizacao = false;

    /**
     * @return the capturarTeclas
     */
    public boolean irParaModoEspera() {
        return modoEspera;
    }

    /**
     * @param capturarTeclas the capturarTeclas to set
     */
    public void setModoEspera(boolean modoEspera) {
        this.modoEspera = modoEspera;
    }

    /**
     * @return the tempoAtualizacao
     */
    public byte getTempoAtualizacao() {
        return tempoAtualizacao;
    }

    /**
     * @param tempoAtualizacao the tempoAtualizacao to set
     */
    public void setTempoAtualizacao(byte tempoAtualizacao) {
        this.tempoAtualizacao = tempoAtualizacao;
    }

    /**
     * @return the idOperacao
     */
    public byte getIdOperacao() {
        return idOperacao;
    }

    /**
     * @param idOperacao the idOperacao to set
     */
    public void setIdOperacao(byte idOperacao) {
        this.idOperacao = idOperacao;
    }

    /**
     * @return the capturarTeclas
     */
    public boolean isToCapturarTeclas() {
        return capturarTeclas;
    }

    /**
     * @param capturarTeclas the capturarTeclas to set
     */
    public void setCapturarTeclas(boolean capturarTeclas) {
        this.capturarTeclas = capturarTeclas;
    }

    /**
     * @return the geolocalizacao
     */
    public boolean isToGeolocalizacao() {
        return geolocalizacao;
    }

    /**
     * @param geolocalizacao the geolocalizacao to set
     */
    public void setGeolocalizacao(boolean geolocalizacao) {
        this.geolocalizacao = geolocalizacao;
    }
}
