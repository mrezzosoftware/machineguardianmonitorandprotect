package br.com.mrezzosoftware.machineguardianmonitorprotect.core.teclado;

import org.sf.feeling.swt.win32.extension.Win32;
import org.sf.feeling.swt.win32.internal.extension.Extension;

public class Caracter {

    private final char[] vogal = {'a', 'e', 'i', 'o', 'u'};
    private static final int VK_CONTEXT_MENU = 93;
    
    public Caracter() {}

    public boolean isVogal(char caracter) {

        boolean res = false;

        for (int i = 0; i < vogal.length; i++) {
            if (vogal[i] == caracter) {

                res = true;
            }
        }

        return res;
    }
    
    public boolean isAlfabetico(int vkCode) {
        return (vkCode > 64 && vkCode < 91);
    }
    
    public String isCaracterEspecial(int vkCode) {
        
        String caracterEspecial = "";
        
        switch(vkCode) {
            case Win32.VK_ESCAPE:
                caracterEspecial = "[ESC]";
                break;
//            case Win32.VK_TAB:
//                caracterEspecial = "[TAB]";
//                break;
            case Win32.VK_CAPITAL:
                caracterEspecial = "[CAPSLOCK]";
                break;
            case Win32.VK_SHIFT:
                caracterEspecial = "[SHIFT]";
                break;
            case Win32.VK_CONTROL:
                caracterEspecial = "[CTRL]";
                break;
            case Win32.VK_LWIN:
                caracterEspecial = "[WINKEY]";
                break;
            case Win32.VK_MENU:
                caracterEspecial = "[ALT]";
                break;
//            case Win32.VK_SPACE:
//                caracterEspecial = "[ESPAÇO]";
//                break;
            case Caracter.VK_CONTEXT_MENU:
                caracterEspecial = "[CONTEXTMENU]";
                break;
            case Win32.VK_LEFT:
                caracterEspecial = "[SETA_ESQUERDA]";
                break;
            case Win32.VK_DOWN:
                caracterEspecial = "[SETA_BAIXO]";
                break;
            case Win32.VK_RIGHT:
                caracterEspecial = "[SETA_DIREITA]";
                break;
            case Win32.VK_UP:
                caracterEspecial = "[SETA_CIMA]";
                break;
            case Win32.VK_END:
                caracterEspecial = "[END]";
                break;
            case Win32.VK_NEXT:
                caracterEspecial = "[PAGE_DOWN]";
                break;
            case Win32.VK_PRIOR:
                caracterEspecial = "[PAGE_UP]";
                break;
            case Win32.VK_RETURN:
                caracterEspecial = "[ENTER]";
                break;
            case Win32.VK_HOME:
                caracterEspecial = "[HOME]";
                break;
            case Win32.VK_BACK:
                caracterEspecial = "[BACKSPACE]";
                break;
            case Win32.VK_DELETE:
                caracterEspecial = "[DELETE]";
                break;
            case Win32.VK_INSERT:
                caracterEspecial = "[INSERT]";
                break;
            case Win32.VK_SNAPSHOT:
                caracterEspecial = "[PRINTSCREEN]";
                break;
            case Win32.VK_NUMLOCK:
                caracterEspecial = "[NUMLOCK]";
                break;
            case Win32.VK_F1:
                caracterEspecial = "[F1]";
                break;
            case Win32.VK_F2:
                caracterEspecial = "[F2]";
                break;
            case Win32.VK_F3:
                caracterEspecial = "[F3]";
                break;
            case Win32.VK_F4:
                caracterEspecial = "[F4]";
                break;
            case Win32.VK_F5:
                caracterEspecial = "[F5]";
                break;
            case Win32.VK_F6:
                caracterEspecial = "[F6]";
                break;
            case Win32.VK_F7:
                caracterEspecial = "[F7]";
                break;
            case Win32.VK_F8:
                caracterEspecial = "[F8]";
                break;
            case Win32.VK_F9:
                caracterEspecial = "[F9]";
                break;
            case Win32.VK_F10:
                caracterEspecial = "[F10]";
                break;
            case Win32.VK_F11:
                caracterEspecial = "[F11]";
                break;
            case Win32.VK_F12:
                caracterEspecial = "[F12]";
                break;
        }
        
        return caracterEspecial;
    }

    public boolean isAcento(int keyCode) {

        boolean res = false;

        if (keyCode == 219 || keyCode == 222) {
            res = true;
        }

        return res;
    }

    public char getAcento(int vkCode, boolean shiftIsPressed) {

        char acento = '0';

        if (vkCode == 219 && shiftIsPressed) {
            acento = '`';
        } else if (vkCode == 219 && !shiftIsPressed) {
            acento = '´';
        } else if (vkCode == 222 && shiftIsPressed) {
            acento = '^';
        } else if (vkCode == 222 && !shiftIsPressed) {
            acento = '~';
        }

        return acento;

    }

    public char vogalAcentuada(char vogalLowCase, char acento) {

        char v = vogalLowCase;

        if (vogalLowCase == 'a' && acento == '`') {
            v = 'à';

        } else if (vogalLowCase == 'a' && acento == '´') {
            v = 'á';
        } else if (vogalLowCase == 'a' && acento == '~') {
            v = 'ã';
        } else if (vogalLowCase == 'e' && acento == '´') {
            v = 'é';
        } else if (vogalLowCase == 'e' && acento == '^') {
            v = 'ê';
        } else if (vogalLowCase == 'i' && acento == '´') {
            v = 'í';
        } else if (vogalLowCase == 'o' && acento == '´') {
            v = 'ó';
        } else if (vogalLowCase == 'o' && acento == '~') {
            v = 'õ';
        } else if (vogalLowCase == 'u' && acento == '´') {
            v = 'ú';
        }

        return v;
    }

    public char shiftCharacter(char teclaDigitada, boolean shiftPressionada) {

        char res = teclaDigitada;

        if (shiftPressionada) {
            switch (teclaDigitada) {
                
                case '1':
                    res = '!';
                    break;

                case '2':
                    res = '@';
                    break;

                case '3':
                    res = '#';
                    break;

                case '4':
                    res = '$';
                    break;

                case '5':
                    res = '%';
                    break;

                case '6':
                    res = '¨';
                    break;

                case '7':
                    res = '&';
                    break;

                case '8':
                    res = '*';
                    break;

                case '9':
                    res = '(';
                    break;

                case '0':
                    res = ')';
                    break;

                case '-':
                    res = '_';
                    break;

                case '=':

                    res = '+';
                    break;

                case '\\':
                    res = '|';
                    break;

                case '\'':
                    res = '"';
                    break;

                case '[':
                    res = '{';
                    break;

                case ']':
                    res = '}';
                    break;

                case ',':
                    res = '<';
                    break;

                case '.':
                    res = '>';
                    break;

                case ';':
                    res = ':';
                    break;

                case '/':
                    res = '?';
                    break;

                default:
                    break;
            }
        }

        return res;

    }

    public char converteASCIItoCHAR(int keyCode, int scanCode) {

        byte[] keyboard = new byte[256];
        short[] result = new short[1];
        Extension.ToAscii(keyCode, scanCode, keyboard, result, 0);

        return (char) result[0];
    }
}