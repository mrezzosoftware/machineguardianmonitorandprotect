package machineguardianmonitorprotect.core;

import org.sf.feeling.swt.win32.internal.extension.Extension;

public class Caracter {

    private final char[] vogal = {'a', 'e', 'i', 'o', 'u'};

    public Caracter() {
    }

    public boolean isVogal(char caracter) {

        boolean res = false;

        for (int i = 0; i < vogal.length; i++) {
            if (vogal[i] == caracter) {

                res = true;
            }
        }

        return res;
    }

    public boolean isAcento(int keyCode) {

        boolean res = false;

        if (keyCode == 219 || keyCode == 222) {
            res = true;
        }

        return res;
    }

    public char getAcento(int key, boolean shift) {

        char acento = '0';

        if (key == 219 && shift) {
            acento = '`';
        } else if (key == 219 && !shift) {
            acento = '´';
        } else if (key == 222 && shift) {
            acento = '^';
        } else if (key == 222 && !shift) {
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

    public char shiftCharacter(char key, boolean bool) {

        char res = key;

        if (bool) {


            switch (key) {
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