/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machineguardianmonitorprotect.core.windows;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import machineguardianmonitorprotect.core.Caracter;
import machineguardianmonitorprotect.core.MyKernel32;
import machineguardianmonitorprotect.core.MyPsapi;
import machineguardianmonitorprotect.core.MyUser32;
import org.sf.feeling.swt.win32.extension.Win32;
import org.sf.feeling.swt.win32.extension.hook.Hook;
import org.sf.feeling.swt.win32.extension.hook.Keyboard_LLHook;
import org.sf.feeling.swt.win32.extension.hook.data.HookData;
import org.sf.feeling.swt.win32.extension.hook.data.KeyboardHookData;
import org.sf.feeling.swt.win32.extension.hook.data.Keyboard_LLHookData;
import org.sf.feeling.swt.win32.extension.hook.interceptor.InterceptorFlag;
import org.sf.feeling.swt.win32.extension.hook.interceptor.Keyboard_LLHookInterceptor;
import org.sf.feeling.swt.win32.extension.hook.listener.HookEventListener;
import org.sf.feeling.swt.win32.internal.extension.util.FlagSet;

/**
 *
 * @author Marina
 */
public class Windows {

    public static class Processos {

        private static MyUser32 user32 = (MyUser32) Native.loadLibrary("user32", MyUser32.class);
        private static MyKernel32 kernel32 = (MyKernel32) Native.loadLibrary("kernel32", MyKernel32.class);
        private static MyPsapi psapi = (MyPsapi) Native.loadLibrary("psapi", MyPsapi.class);

        /**
         * Retorna o nome do processo da janela que está ativa (em primeiro
         * plano).
         *
         * @return String nome do processo da janela ativa
         */
        public static String getNomeProcessoJanelaAtiva() {
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
        public static String getTituloJanelaAtiva() {
            byte[] windowText = new byte[512];

            user32.GetWindowTextA(user32.GetForegroundWindow(), windowText, user32.GetWindowTextLengthA(user32.GetForegroundWindow()) + 1);

            return Native.toString(windowText);
        }
    }





    public static class Teclado {

        private boolean isTeclaPressionada = false;
        private boolean isTeclaMantidaPressionada = false;
        private boolean shiftPressionado = false;
        private boolean isCapslockAtivado = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        private boolean hotKeyImpressa = false;
        private char acento;
        private char teclaDigitada;
        private int vkCode, scanCode;
        private int totalRepeticoesTeclasEspeciaisPressionadas = 1;
        private int totalRepeticoesTeclasNormaisPressionadas = 0;
        private int tamanhoLinhaAtual = 0;
        private String[] hotKeys = {"", "", ""};
        private KeyboardHookData keyboardHookData;
        //Keyboard_LLHookData keyboardLLHookData;
        private Caracter caracter = new Caracter();
        private FlagSet hotKeysCombinacaoAtual = new FlagSet();
        private FlagSet ctrlAltDel;
        private FlagSet altTab;
        private String caracterEspecial = "";
        private StringBuffer palavra = new StringBuffer();

        public Teclado() {
            super();
            //init();

        }

        public void init() {

            Keyboard_LLHookInterceptor kllhi = new Keyboard_LLHookInterceptor() {

                @Override
                public InterceptorFlag intercept(Keyboard_LLHookData kllhd) {
                    System.out.println(kllhd.getExtraInfo());
                    System.out.println(kllhd.getFlags());
                    System.out.println(kllhd.getScanCode());
                    System.out.println(kllhd.getTime());
                    System.out.println(kllhd.vkCode());
                    System.out.println(kllhd.getStruct().getDwExtraInfo());

                    if (!caracter.isCaracterEspecial(kllhd.vkCode()).equalsIgnoreCase("")) {
                        hotKeysCombinacaoAtual.and(kllhd.vkCode());

                        //if (ctrlAltDel.contains(vkCode))
                    }


                    return InterceptorFlag.TRUE;
                }
            };

            Keyboard_LLHook.addHookInterceptor(kllhi);
            if (Keyboard_LLHook.installHook()) {
                System.out.println("INSTALADO");
            }

            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (Keyboard_LLHook.isInstalled()) {
                        System.out.println("INSTALADO");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(machineguardianmonitorprotect.core.Teclado.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }).start();

            Hook.KEYBOARD.addListener(this, new HookEventListener() {

                public void acceptHookData(HookData hookData) {

                    keyboardHookData = (KeyboardHookData) hookData;
                    isTeclaPressionada = keyboardHookData.getTransitionState();
                    isTeclaMantidaPressionada = keyboardHookData.getPreviousState();
                    vkCode = keyboardHookData.getWParam();

                    //System.out.println("tecla: " + vkCode);
                    //System.out.println("keyboardHookData.getPreviousState(): " + keyboardHookData.getPreviousState());
                    //System.out.println("keyboardHookData.getRepeatCount(): " + keyboardHookData.getRepeatCount());

//                if (isTeclaMantidaPressionada) {
//                    if (vkCode != ultimoVKCode  && !isTeclaPressionada) {
//                        caracterEspecial = caracter.isCaracterEspecial(ultimoVKCode);
//                        if (!caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("")) {
//                            imprimeString(caracterEspecial);
//                            imprimeString(caracter.isCaracterEspecial(vkCode));
//                        }
//                    } else {
//                        vkCode = -1;
//                    }
//                }

                    if (isTeclaPressionada) {

                        if (isPressedShift(vkCode, isTeclaPressionada)) {
                            shiftPressionado = true;
                        }

                        if (!caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("")) {
                            caracterEspecial = caracter.isCaracterEspecial(vkCode);

                            if (caracterEspecial.equalsIgnoreCase("[CTRL]")) {
                                hotKeys[0] = caracterEspecial;
                                System.out.println("CTRL");
                            } else if (hotKeys[0].equalsIgnoreCase("[CTRL]")
                                    && caracterEspecial.equalsIgnoreCase("[ALT]")) {
                                hotKeys[1] = caracterEspecial;
                                System.out.println("ALT");
                            } else if (hotKeys[0].equalsIgnoreCase("[CTRL]")
                                    && hotKeys[1].equalsIgnoreCase("[ALT]")
                                    && caracterEspecial.equalsIgnoreCase("[DELETE]")) {
                                System.out.println("DELETE");
                                hotKeys[2] = caracterEspecial;
                                imprimirHotKeys(hotKeys);
                                hotKeyImpressa = true;
                            }

                            System.out.println(caracterEspecial);

                        }

                        if (isTeclaMantidaPressionada) {
                            if (caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("")) {
                                totalRepeticoesTeclasNormaisPressionadas++;
                            } else {
                                totalRepeticoesTeclasEspeciaisPressionadas++;
                            }
                        }

                    } else if (!isTeclaPressionada) {
                        if (!caracter.isAcento(vkCode)) {

                            teclaDigitada = caracter.converteASCIItoCHAR(vkCode, scanCode);

                            if (caracter.isVogal(teclaDigitada) && acento != '0') {
                                char s = caracter.vogalAcentuada(teclaDigitada, acento);
                                imprimeChar(s);

                                acento = '0';
                                shiftPressionado = false;


                            } else if (!Character.isISOControl(teclaDigitada) && caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("")) {

                                char s = caracter.shiftCharacter(teclaDigitada, shiftPressionado);
                                imprimeChar(s);

                            } else if (Win32.VK_SHIFT == vkCode) {

                                shiftPressionado = false;
                            } else if (!caracter.isCaracterEspecial(vkCode).equalsIgnoreCase("") && !caracterEspecial.equalsIgnoreCase("")) {

                                caracterEspecial = caracter.isCaracterEspecial(vkCode);

                                if (caracterEspecial.equalsIgnoreCase("[CAPSLOCK]")) {
                                    if ((isCapslockAtivado = !isCapslockAtivado) == true) {
                                        caracterEspecial += "[ON]";
                                    } else {
                                        caracterEspecial += "[OFF]";
                                    }
                                } else if (caracterEspecial.equalsIgnoreCase("[ENTER]\n")) {
                                    tamanhoLinhaAtual = 0;
                                }

                                if (caracterEspecial.equalsIgnoreCase("[CTRL]")) {
                                    hotKeys[0] = "";
                                } else if (caracterEspecial.equalsIgnoreCase("[ALT]")) {
                                    hotKeys[1] = "";
                                } else if (caracterEspecial.equalsIgnoreCase("[DELETE]") && hotKeyImpressa) {
                                    hotKeyImpressa = false;
                                    return;
                                }




                                imprimeString(caracterEspecial);
                            }

                        } else {

                            acento = caracter.getAcento(vkCode, shiftPressionado);
                        }

                    }
                }
            });
            //Hook.KEYBOARD.install(this);

        }

        public boolean isPressedShift(int key, boolean tcPressionada) {

            return (key == Win32.VK_SHIFT && tcPressionada);

        }

        private void imprimeChar(char s) {

            if (totalRepeticoesTeclasNormaisPressionadas > 1) {
                for (int i = 0; i < totalRepeticoesTeclasNormaisPressionadas; i++) {
                    System.out.print(captulacaoCorreta(s));
                    adicionarValorTamanhoLinhaAtual(1);
                }
            } else {
                if (!caracterEspecial.equalsIgnoreCase("")) {
                    imprimeString(caracterEspecial);
                    adicionarValorTamanhoLinhaAtual(caracterEspecial.length());
                    caracterEspecial = "";
                }
                System.out.print(captulacaoCorreta(s));
                adicionarValorTamanhoLinhaAtual(1);
            }

            totalRepeticoesTeclasNormaisPressionadas = 0;
            totalRepeticoesTeclasEspeciaisPressionadas = 1;
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

            caracterEspecial = "";
            totalRepeticoesTeclasEspeciaisPressionadas = 1;
        }

        private void imprimirHotKeys(String[] hk) {

            for (int i = 0; i < hk.length; i++) {
                System.out.println(hk[i]);
                hk[i] = "";
            }
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
    }
}
