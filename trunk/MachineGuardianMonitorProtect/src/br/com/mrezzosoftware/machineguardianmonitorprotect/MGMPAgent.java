/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrezzosoftware.machineguardianmonitorprotect;

import br.com.mrezzosoftware.machineguardianmonitorprotect.core.Constantes;
import br.com.mrezzosoftware.machineguardianmonitorprotect.core.Operacoes;
import br.com.mrezzosoftware.machineguardianmonitorprotect.core.PreferencesUtil;
import br.com.mrezzosoftware.machineguardianmonitorprotect.core.ServidorWeb;
import br.com.mrezzosoftware.machineguardianmonitorprotect.windows.MGMPWindows;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 01837484333
 */
public class MGMPAgent implements Runnable {

    private Operacoes operacoesAtuais;

    public MGMPAgent() {
        this.operacoesAtuais = new Operacoes();
    }

    public void run() {

        Operacoes operacoesNovas;
        MGMPWindows windows = new MGMPWindows();

        while (true) {

            try {
                Thread.sleep(operacoesAtuais.getTempoAtualizacao() * 10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MGMPAgent.class.getName() + " - Erro Thread").log(Level.SEVERE, null, ex);
            }

            operacoesNovas = ServidorWeb.obterOperacoes();

            if (operacoesNovas != null) {
                
                operacoesAtuais = operacoesNovas;

                if (!operacoesAtuais.irParaModoEspera()) {

                    System.out.println("EXECUTANDO");
                    
                    if (!(operacoesAtuais.getTempoAtualizacao() == Byte.valueOf(PreferencesUtil.getInstance().obterValor(Constantes.PREF_TEMPO_ATUALIZACAO)))) {
                        PreferencesUtil.getInstance().registrarValor(Constantes.PREF_TEMPO_ATUALIZACAO, String.valueOf(operacoesAtuais.getTempoAtualizacao()));
                    }

                    switch (operacoesAtuais.getIdOperacao()) {
                        case Constantes.OP_BLOQUEAR: {
                            windows.SO.bloquearEstacaoTrabalho();
                            break;
                        }
                        case Constantes.OP_LOGOFF: {
                            windows.SO.fazerLogoff(true);
                            break;
                        }
                        case Constantes.OP_HIBERNAR: {
                            windows.SO.hibernarComputador();
                            break;
                        }
                        case Constantes.OP_REINICIAR: {
                            windows.SO.reiniciarComputador(true);
                            break;
                        }
                        case Constantes.OP_DESLIGAR: {
                            windows.SO.desligarComputador(true);
                            break;

                        }
                    }

                } else {
                    System.out.println("EM ESPERA");
                }

            }

        }

    }
}
