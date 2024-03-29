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
import br.com.mrezzosoftware.machineguardianmonitorprotect.windows.monitor.Geolocation;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sf.feeling.swt.win32.extension.shell.Windows;
import org.sf.feeling.swt.win32.extension.system.WindowsSession;
import org.sf.feeling.swt.win32.extension.widgets.Window;

/**
 *
 * @author 01837484333
 */
public class MGMPAgent implements Runnable {

    private Operacoes operacoesAtuais;

    public MGMPAgent() {
        //this.operacoesAtuais = new Operacoes();
    }

    public void run() {

        Operacoes operacoesNovas;
        MGMPWindows windows = new MGMPWindows();
        Geolocation geolocalizacao = new Geolocation();

        while (true) {

            try {
                Thread.sleep(operacoesAtuais == null ? 10000 : operacoesAtuais.getTempoAtualizacao());
            } catch (InterruptedException ex) {
                Logger.getLogger(MGMPAgent.class.getName() + " - Erro Thread").log(Level.SEVERE, null, ex);
            }

            operacoesNovas = ServidorWeb.obterOperacoes();


            if (operacoesNovas != null) {

                if (operacoesAtuais == null) {
                    operacoesAtuais = operacoesNovas;
                } else if (operacoesNovas.getUltimaAtualizacaoMobile().getTime() > operacoesAtuais.getUltimaAtualizacaoMobile().getTime()) {
                    operacoesAtuais = operacoesNovas;
                } else {
                    System.out.println("RETORNANDO AO INÍCIO DO WHILE");
                    continue;
                }

                System.out.println("PASSEI O WHILE");

                if (!operacoesAtuais.irParaModoEspera()) {

                    System.out.println("EXECUTANDO");

                    if (!(operacoesAtuais.getTempoAtualizacao() == Integer.valueOf(PreferencesUtil.getInstance().obterValor(Constantes.PREF_TEMPO_ATUALIZACAO)))) {
                        PreferencesUtil.getInstance().registrarValor(Constantes.PREF_TEMPO_ATUALIZACAO, String.valueOf(operacoesAtuais.getTempoAtualizacao()));
                    }

                    if (operacoesAtuais.isToCapturarTeclas() && !windows.Teclado.capturandoTeclas) {
                        windows.Teclado.iniciarCapturaTeclasDigitadas();
                    } else if (!operacoesAtuais.isToCapturarTeclas()) {
                        windows.Teclado.pararCapturaTeclasDigitadas();
                    }

                    if (operacoesAtuais.isToGeolocalizacao()) {
                        Geolocation.Coordenadas coords = geolocalizacao.localizarUsuario();
                        if (coords != null) {

                            String retornoServidor = ServidorWeb.registrarLocalizacaoMaquina(coords);

                            if (retornoServidor.equalsIgnoreCase("true")) {
                                System.out.println("LOCALIZAÇÃO INSERIDA");
                            } else {
                                System.out.println("ERRO AO INSERIR LOCALIZAÇÃO");
                            }

                        }
                    }

                    switch (operacoesAtuais.getIdAcao()) {
                        case Constantes.AC_BLOQUEAR: {
                            windows.SO.bloquearEstacaoTrabalho();
                            break;
                        }
                        case Constantes.AC_LOGOFF: {
                            windows.SO.fazerLogoff(true);
                            break;
                        }
                        case Constantes.AC_HIBERNAR: {
                            windows.SO.hibernarComputador(true);
                            break;
                        }
                        case Constantes.AC_REINICIAR: {
                            windows.SO.reiniciarComputador(true);
                            break;
                        }
                        case Constantes.AC_DESLIGAR: {
                            WindowsSession.Shutdown(true);
                            break;
                        }
                    }

                } else {
                    System.out.println("EM ESPERA");
                }

            } else {
                System.out.println("OPERAÇÕES NULAS");
            }

        }

    }
}
