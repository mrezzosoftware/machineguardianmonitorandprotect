/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import java.util.Date;

/**
 *
 * @author 01837484333
 */
public class Operacoes {

    private boolean modoEspera = false;
    private int tempoAtualizacao = 5000;
    private Date ultimaAtualizacaoMobile;
    private byte idAcao = 0;
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
    public int getTempoAtualizacao() {
        return tempoAtualizacao;
    }

    /**
     * @param tempoAtualizacao the tempoAtualizacao to set
     */
    public void setTempoAtualizacao(int tempoAtualizacao) {
        this.tempoAtualizacao = tempoAtualizacao;
    }

    /**
     * @return the ultimaAtualizacao
     */
    public Date getUltimaAtualizacaoMobile() {
        return ultimaAtualizacaoMobile;
    }

    /**
     * @param ultimaAtualizacao the ultimaAtualizacao to set
     */
    public void setUltimaAtualizacaoMobile(Date ultimaAtualizacaoMobile) {
        this.ultimaAtualizacaoMobile = ultimaAtualizacaoMobile;
    }

    /**
     * @return the idOperacao
     */
    public byte getIdAcao() {
        return idAcao;
    }

    /**
     * @param idOperacao the idOperacao to set
     */
    public void setIdAcao(byte idAcao) {
        this.idAcao = idAcao;
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
