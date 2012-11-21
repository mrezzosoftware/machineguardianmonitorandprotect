package br.com.mrezzosoftware.machineguardianmonitorprotect.windows;

import br.com.mrezzosoftware.machineguardianmonitorprotect.core.teclado.Caracter;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sf.feeling.swt.win32.extension.hook.Hook;
import org.sf.feeling.swt.win32.extension.hook.data.HookData;
import org.sf.feeling.swt.win32.extension.hook.data.KeyboardHookData;
import org.sf.feeling.swt.win32.extension.hook.listener.HookEventListener;
import org.sf.feeling.swt.win32.extension.shell.Windows;
import org.sf.feeling.swt.win32.extension.system.Kernel;
import org.sf.feeling.swt.win32.extension.system.ProcessEntry;
import org.sf.feeling.swt.win32.extension.system.WindowsSession;

/**
 *
 * @author MRezzo Software
 */
public class MGMPWindows {
    
    public final Processos Processos;
    public final Teclado Teclado;
    public final SO SO;

    public MGMPWindows() {
        Processos = new Processos();
        Teclado = new Teclado();
        SO = new SO();
    }

    public class Processos {

        private Processos() {
        }

        /**
         * Retorna o nome do processo da janela que está ativa (em primeiro
         * plano).
         *
         * @return String nome do processo da janela ativa
         */
        public String getNomeProcessoJanelaAtiva() {
            
            String nomeProcesso = "";
            
            ProcessEntry process[] = Kernel.getSystemProcessesSnap();
            for (ProcessEntry p : process) {
                if (p.getProcessId() == Windows.getProcessId(Windows.getForegroundWindow())) {
                    nomeProcesso = p.getProcessName();
                    break;
                }
            }
            
            return nomeProcesso;
            
        }

        /**
         * Retorna o título da janela que está ativa (em primeiro plano).
         *
         * @return String título da janela ativa
         */
        public String getTituloJanelaAtiva() {
            String titulo = "";

            titulo = Windows.getWindowText(Windows.getForegroundWindow());
            
            return titulo;
        }
    }

    public class SO {        
        

        private SO() {}

        public void bloquearEstacaoTrabalho() {
            WindowsSession.LockWorkstation();
        }

        public void fazerLogoff(boolean notificar) {
            WindowsSession.Logoff(notificar);
        }
        
        public void hibernarComputador(boolean notificar) {
            WindowsSession.HibernateWorkstation(notificar);
        }

        public void reiniciarComputador(boolean notificar) {
            WindowsSession.Reboot(notificar);

        }

        public void desligarComputador(boolean notificar) {
            WindowsSession.Shutdown(notificar);
        }
        
    }

    public class Teclado {

        private boolean isTeclaPressionada = false;
        private boolean isTeclaMantidaPressionada = false;
        private boolean shiftPressionado = false;
        private boolean ctrlPressionado = false;
        private boolean altPressionado = false;
        private boolean isCapslockAtivado = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        private boolean rodando = false;
        private boolean reinstalarHook = false;
        private char acento;
        private char teclaDigitada;
        private int vkCode, scanCode;
        private int posicaoCarroEscritaPalavra = 0;
        private int totalRepeticoesTeclasNormaisPressionadas = 0;
        private int tamanhoLinhaAtual = 0;
        private KeyboardHookData keyboardHookData;
        private HookEventListener hookEventListener;
        private Caracter caracter = new Caracter();
        private String caracterEspecial = "";
        private StringBuilder palavra = new StringBuilder();
        
        public boolean capturandoTeclas;

        private Teclado() {
            criarObjetos();
        }

        private void criarObjetos() {
            hookEventListener = new HookEventListener() {

                @Override
                public void acceptHookData(HookData hookData) {

                    keyboardHookData = (KeyboardHookData) hookData;
                    // Indica se a tecla foi pressionada (keyPressed = true) ou solta (keyReleased = false).
                    isTeclaPressionada = keyboardHookData.getTransitionState();
                    // Indica se a tecla pressionada anteriormente continua mantida pressionada.
                    isTeclaMantidaPressionada = keyboardHookData.getPreviousState();
                    // Obtém o Virtual Key Code da tecla pressionada.
                    vkCode = keyboardHookData.getWParam();

                    if (isTeclaPressionada) {

                        if (!caracter.isAcento(vkCode)) {

                            teclaDigitada = caracter.converteASCIItoCHAR(vkCode, scanCode);

                            if (caracter.isVogal(teclaDigitada) && acento != '0') {
                                char s = caracter.vogalAcentuada(teclaDigitada, acento);
                                
                                if (palavra.length() > 0) {
                                    palavra.insert(posicaoCarroEscritaPalavra, s);
                                } else {
                                    palavra.append(s);
                                }
                                
                                posicaoCarroEscritaPalavra++;

                                acento = '0';

                            } else if (!Character.isISOControl(teclaDigitada) && caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("")) {

                                char s = caracter.shiftCharacter(teclaDigitada, shiftPressionado);
                                
                                if (palavra.length() > 0) {
                                    //palavra.append(s);
                                    palavra.insert(posicaoCarroEscritaPalavra, s);
                                } else {
                                    palavra.append(s);
                                }
                                
                                posicaoCarroEscritaPalavra++;

                            } else if (!caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("")) {

                                caracterEspecial = caracter.isCaracterEspecial(vkCode);

                                if (caracterEspecial.equalsIgnoreCase("[ENTER]")) {
                                    imprimeString(palavra.toString());
                                    palavra.delete(0, palavra.length());
                                    posicaoCarroEscritaPalavra = 0;
                                    tamanhoLinhaAtual = 0;
                                } else if (caracterEspecial.equalsIgnoreCase("[SHIFT]")) {
                                    shiftPressionado = true;
                                } else if (caracterEspecial.equalsIgnoreCase("[CTRL]")) {
                                    ctrlPressionado = true;
                                } else if (caracterEspecial.equalsIgnoreCase("[ALT]")) {
                                    altPressionado = true;
                                } else if (caracterEspecial.equalsIgnoreCase("[CAPSLOCK]")) {
                                    isCapslockAtivado = !isCapslockAtivado;
                                } else if (caracterEspecial.equalsIgnoreCase("[SETA_ESQUERDA]")) {
                                    
                                    if ((posicaoCarroEscritaPalavra-1) >= 0) {
                                        posicaoCarroEscritaPalavra--;
                                    }
                                    
                                } else if (caracterEspecial.equalsIgnoreCase("[SETA_DIREITA]")) {
                                    
                                    if ((posicaoCarroEscritaPalavra+1) <= palavra.length()) {
                                        posicaoCarroEscritaPalavra++;
                                    }
                                    
                                } else if (caracterEspecial.equalsIgnoreCase("[HOME]")) {
                                    
                                    posicaoCarroEscritaPalavra = 0;
                                    
                                } else if (caracterEspecial.equalsIgnoreCase("[END]")) {
                                    
                                    posicaoCarroEscritaPalavra = palavra.length();
                                    
                                } else if (caracterEspecial.equalsIgnoreCase("[BACKSPACE]")) {
                                    if (posicaoCarroEscritaPalavra > 0) {
                                        palavra.deleteCharAt(--posicaoCarroEscritaPalavra);
                                    }
                                }

                                caracterEspecial = "";
                            }

                        } else {

                            acento = caracter.getAcento(vkCode, shiftPressionado);
                        }

                        if (palavra.length() >= 80) {
                            imprimeString(palavra.toString());
                            palavra.delete(0, palavra.length());
                            posicaoCarroEscritaPalavra = 0;
                        }

                    } else if (!isTeclaPressionada) {

                        if (!caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("")) {

                            caracterEspecial = caracter.isCaracterEspecial(vkCode);

                            if (caracterEspecial.equalsIgnoreCase("[SHIFT]")) {
                                shiftPressionado = false;
                            } else if (caracterEspecial.equalsIgnoreCase("[CTRL]")) {
                                ctrlPressionado = false;
                            } else if (caracterEspecial.equalsIgnoreCase("[ALT]")) {
                                altPressionado = false;
                            }

                            if (ctrlPressionado) {

                                if (altPressionado && caracterEspecial.equalsIgnoreCase("[DELETE]")) {
                                    imprimeString(caracterEspecial);
                                    reinstalarHook = true;
                                } else if (shiftPressionado && caracterEspecial.equalsIgnoreCase("[ESC]")) {
                                    imprimeString(caracterEspecial);
                                    reinstalarHook = true;
                                }
                            }

                            if (altPressionado) {

                                if (caracterEspecial.equalsIgnoreCase("[TAB]")) {
                                    imprimeString(caracterEspecial);
                                } else if (caracterEspecial.equalsIgnoreCase("[F4]")) {
                                    imprimeString(caracterEspecial);
                                }

                            }

                            caracterEspecial = "";
                        }
                    }

                }
            };
        }

        public void iniciarCapturaTeclasDigitadas() {
            Hook.KEYBOARD.addListener(MGMPWindows.this, hookEventListener);
            Hook.KEYBOARD.install(MGMPWindows.this);
            capturandoTeclas = (rodando = true);

            new Thread(new Runnable() {

                @Override
                public void run() {

                    while (rodando) {
                        if (reinstalarHook && Hook.KEYBOARD.isInstalled(MGMPWindows.this)) {
                            Hook.KEYBOARD.uninstall(MGMPWindows.this);
                            Hook.KEYBOARD.install(MGMPWindows.this);
                            reinstalarHook = false;
                        }
                        try {

                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MGMPWindows.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("Desinstalando Hook");
                    Hook.KEYBOARD.removeListener(MGMPWindows.this, hookEventListener);
                    Hook.KEYBOARD.uninstall(MGMPWindows.this);
                }
            }).start();
        }

        public void pararCapturaTeclasDigitadas() {
            capturandoTeclas = (rodando = false);
        }

        private void imprimeChar(char s) {

            System.out.print(captulacaoCorreta(s));
            adicionarValorTamanhoLinhaAtual(1);
            totalRepeticoesTeclasNormaisPressionadas = 0;


            if (totalRepeticoesTeclasNormaisPressionadas > 1) {
                for (int i = 0; i < totalRepeticoesTeclasNormaisPressionadas; i++) {
                    System.out.print(captulacaoCorreta(s));

                }
            } else {
                if (isTeclaMantidaPressionada
                        && !caracterEspecial.equalsIgnoreCase("")
                        && !caracterEspecial.equalsIgnoreCase("NOT_PRINT")) {
                    imprimeString(caracterEspecial);
                    caracterEspecial = "NOT_PRINT";
                }

            }
        }

        private void imprimeString(String s) {
            System.out.print(s);
            adicionarValorTamanhoLinhaAtual(s.length());
        }
//
//        public void lePalavra(char key) {
//
//            if (!palavra.equals(null)) {
//
//                if (Character.isSpaceChar(key)) {
//                    int ini = 0;
//                    int fim = palavra.length();
//                    //System.out.println(palavra.toString());
//                    palavra.delete(ini, fim);
//
//                } else {
//                    palavra.append(key);
//                }
//
//
//            }
//
//
//        }

        private char captulacaoCorreta(char aCaracter) {
            return (shiftPressionado || isCapslockAtivado) ? Character.toUpperCase(aCaracter) : aCaracter;
        }

        private void adicionarValorTamanhoLinhaAtual(int tamanho) {
            tamanhoLinhaAtual += tamanho;

            if (tamanhoLinhaAtual >= 80) {
                System.out.println("");
                tamanhoLinhaAtual = 0;
            }
        }
    }
}
