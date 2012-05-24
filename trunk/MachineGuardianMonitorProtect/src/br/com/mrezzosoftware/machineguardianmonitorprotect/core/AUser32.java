package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;

public class AUser32 {

    static interface User32 extends Library {

        public static User32 INSTANCE = (User32) Native.loadLibrary("User32", User32.class);

        short GetAsyncKeyState(int key);

        short GetKeyState(int key);
        
        IntByReference GetKeyboardLayout(int dwLayout);

        int MapVirtualKeyExW(int uCode, int nMapType, IntByReference dwhkl);

        boolean GetKeyboardState(byte[] lpKeyState);

        int ToUnicodeEx(int wVirtKey, int wScanCode, byte[] lpKeyState, char[] pwszBuff, int cchBuff, int wFlags, IntByReference dwhkl);
    }

    public static void main(String[] args) {
        long currTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < currTime + 20000) {
            for (int key = 1; key < 256; key++) {
                if (isKeyPressed(key)) {
                    getKeyType(key);
                }
            }
        }
    }

    private static boolean isKeyPressed(int key) {
        return User32.INSTANCE.GetAsyncKeyState(key) == -32767;
    }

    private static void getKeyType(int key) {

        boolean isDownShift = (User32.INSTANCE.GetKeyState(10) & 0x80) == 0x80;
        boolean isDownCapsLock = (User32.INSTANCE.GetKeyState(14)) != 0;


        byte[] keystate = new byte[256];
        User32.INSTANCE.GetKeyboardState(keystate);


        IntByReference keyblayoutID = User32.INSTANCE.GetKeyboardLayout(0);
        int ScanCode = User32.INSTANCE.MapVirtualKeyExW(key, 0, keyblayoutID);






        char[] buff = new char[10];

        int bufflen = buff.length;
        int ret = User32.INSTANCE.ToUnicodeEx(key, ScanCode, keystate, buff, bufflen, 0, keyblayoutID);


        switch (ret) {
            case -1:
                System.out.println("Error");
                break;

            case 0:  // no translation

                break;

            default:
                System.out.println("key: " + key);
                System.out.println("output=" + String.valueOf(buff).substring(0, ret));
        }



    }
}
