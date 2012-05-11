package machineguardianmonitorprotect.core;

import org.sf.feeling.swt.win32.extension.Win32;
import org.sf.feeling.swt.win32.extension.hook.Hook;
import org.sf.feeling.swt.win32.extension.hook.data.HookData;
import org.sf.feeling.swt.win32.extension.hook.data.KeyboardHookData;
import org.sf.feeling.swt.win32.extension.hook.listener.HookEventListener;
import org.sf.feeling.swt.win32.internal.extension.Extension;

public class Teclado {

    private boolean ts;
    private boolean shift = false;
    private boolean modoLetra = false;
    private boolean modoPalavra = true;
    private StringBuffer palavra = new StringBuffer();
    private int key, scanCode;
    private char acento;
    private char aux;
    private KeyboardHookData keyboardHookData;
    private Caracter c;

    public Teclado() {
        super();
        c = new Caracter();
        ts = false;
        shift = false;
        //init();

    }

    public void init() {

        Hook.KEYBOARD.addListener(this, new HookEventListener() {
            public void acceptHookData(HookData hookData) {

                keyboardHookData = (KeyboardHookData) hookData;
                ts = keyboardHookData.getTransitionState();
                key = keyboardHookData.getWParam();

                System.out.println("Tecla: " + key);
                
                if (ts) {

                    if (isPressedShift(key, ts)) {
                        shift = true;
                    }

                } else if (!ts) {
                    if (!c.isAcento(key)) {

                        aux = c.converteASCIItoCHAR(key, scanCode);

                        if (c.isVogal(aux) && acento != '0') {
                            char s = c.vogalAcentuada(aux, acento);
                            imprime(s);
                            lePalavra(s);

                            acento = '0';
                            shift = false;


                        } else if ((!Character.isISOControl(aux) || Character.isSpaceChar(aux) || Win32.VK_RETURN == aux) && !shift) {
                            lePalavra(aux);
                            imprime(aux);


                        } else if (!Character.isISOControl(aux) && shift) {

                            char s = c.shiftCharacter(aux, shift);
                            imprime(s);
                            lePalavra(s);
                        } else if (Win32.VK_SHIFT == key) {

                            shift = false;
                        }

                    } else {

                        acento = c.getAcento(key, shift);
                    }

                }
            }
        });
        Hook.KEYBOARD.install(this);

    }

    public boolean isPressedShift(int key, boolean ts) {

        return (key == Win32.VK_SHIFT && ts);

    }

    public void imprime(char s) {

        System.out.print(s);
    }

    public void lePalavra(char key) {

        if (!palavra.equals(null)) {

            if (Character.isSpaceChar(key)) {
                int ini = 0;
                int fim = palavra.length();
                System.out.println(palavra.toString());
                palavra.delete(ini, fim);

            } else {
                palavra.append(key);
            }


        }


    }

    public static void main(String[] args) {
        Teclado t = new Teclado();
        t.init();

    }
}
