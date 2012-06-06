package br.com.mrezzosoftware.machineguardianmonitorprotect.windows;

import br.com.mrezzosoftware.machineguardianmonitorprotect.core.*;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sf.feeling.swt.win32.extension.hook.Hook;
import org.sf.feeling.swt.win32.extension.hook.data.HookData;
import org.sf.feeling.swt.win32.extension.hook.data.KeyboardHookData;
import org.sf.feeling.swt.win32.extension.hook.listener.HookEventListener;

/**
 *
 * @author MRezzo Software
 */
public class Windows {

    private MyUser32 user32 = (MyUser32) Native.loadLibrary("user32", MyUser32.class);
    private MyKernel32 kernel32 = (MyKernel32) Native.loadLibrary("kernel32", MyKernel32.class);
    private MyPsapi psapi = (MyPsapi) Native.loadLibrary("psapi", MyPsapi.class);
    private MyPowrProf powrProf = (MyPowrProf) Native.loadLibrary("powrprof", MyPowrProf.class);
    public final Processos Processos;
    public final Teclado Teclado;
    public final SO SO;

    public Windows() {
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
            char[] nomeExecutavel = new char[1024 * 2];
            PointerByReference ponteiroIdProcesso = new PointerByReference();

            user32.GetWindowThreadProcessId(user32.GetForegroundWindow(), ponteiroIdProcesso);
            WinNT.HANDLE processo = kernel32.OpenProcess(MyKernel32.PROCESS_QUERY_INFORMATION | MyKernel32.PROCESS_VM_READ,
                    false,
                    ponteiroIdProcesso.getValue());

            psapi.GetModuleBaseNameW(processo, null, nomeExecutavel, 1024);
            kernel32.CloseHandle(processo);

            return Native.toString(nomeExecutavel);
        }

        /**
         * Retorna o título da janela que está ativa (em primeiro plano).
         *
         * @return String título da janela ativa
         */
        public String getTituloJanelaAtiva() {
            byte[] windowText = new byte[512];

            user32.GetWindowTextA(user32.GetForegroundWindow(), windowText, user32.GetWindowTextLengthA(user32.GetForegroundWindow()) + 1);

            return Native.toString(windowText);
        }
    }

    public class SO {

        private final int LOGOFF = 0;
        private final int POWEROFF = 0x00000008;
        private final int REBOOT = 0x00000002;
        private final int RESTARTAPPS = 0x00000040;
        private final int SHUTDOWN = 0x00000001;
        private final int FORCE = 0x00000004;
        private final int FORCEIFHUNG = 0x00000010;

        private SO() {
        }

        public void bloquearEstacaoTrabalho() {
            user32.LockWorkStation();
        }

        public void fazerLogoff(boolean forcarLogoff) {
            user32.ExitWindowsEx(LOGOFF, ((forcarLogoff) ? FORCE : 0));
        }
        
        public void hibernarComputador() {
            powrProf.SetSuspendState(true, false, true);
        }

        public void reiniciarComputador(boolean forcarReinicializacao) {

            user32.ExitWindowsEx(REBOOT, ((forcarReinicializacao) ? FORCE : 0));

        }

        public void desligarComputador(boolean forcarReinicializacao) {
            user32.ExitWindowsEx(SHUTDOWN, ((forcarReinicializacao) ? FORCE : 0));
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
                                
                                System.out.println("posicaoCarroEscritaPalavra: " + posicaoCarroEscritaPalavra);
                                System.out.println("palavra.length(): " + palavra.length());

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
                                    palavra.deleteCharAt(posicaoCarroEscritaPalavra-1);
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
            Hook.KEYBOARD.addListener(Windows.this, hookEventListener);
            Hook.KEYBOARD.install(Windows.this);
            rodando = true;

            new Thread(new Runnable() {

                @Override
                public void run() {

                    while (rodando) {
                        if (reinstalarHook && Hook.KEYBOARD.isInstalled(Windows.this)) {
                            Hook.KEYBOARD.uninstall(Windows.this);
                            Hook.KEYBOARD.install(Windows.this);
                            reinstalarHook = false;
                        }
                        try {

                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("Desinstalando Hook");
                    Hook.KEYBOARD.removeListener(Windows.this, hookEventListener);
                    Hook.KEYBOARD.uninstall(Windows.this);
                }
            }).start();
        }

        public void pararCapturaTeclasDigitadas() {
            rodando = false;
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
