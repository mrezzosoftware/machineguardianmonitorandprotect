package machineguardianmonitorprotect.core;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

public class KeyboardHook {

    private volatile boolean quit;
    private HHOOK hHook;
    private LowLevelKeyboardProc keyboardHookCallback;
    private int VK_RETURN = 0x0D;
    private HMODULE hModule;
    private User32 user32;
    private MyUser32 myUser32;

    public KeyboardHook() {
        user32 = User32.INSTANCE;
        myUser32 = (MyUser32) Native.loadLibrary("user32", MyUser32.class);
        hModule = Kernel32.INSTANCE.GetModuleHandle(null);
    }

    public void capturarTeclasDigitadas() {
        keyboardHookCallback = new LowLevelKeyboardProc() {

            public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
                if (nCode >= 0) {
                    switch (wParam.intValue()) {
                        case WinUser.WM_KEYDOWN:
                            System.err.println("in callback, key=" + info.vkCode);
                            if (info.vkCode == 81) {
                                quit = true;
                            }
                            char[] tecla = new char[10];
                            byte[] keystate = new byte[256];
                            User32.INSTANCE.GetKeyboardState(keystate); 
                            myUser32.ToUnicodeEx(info.vkCode, wParam.intValue(), keystate, tecla, tecla.length, 0, myUser32.GetKeyboardLayout(0));
                            System.out.println("Char: " + String.valueOf(tecla));

                            if (info.vkCode == VK_RETURN) {
                                System.out.println("ENTER");
                                //System.out.println("Char: " + Character.toString((char)info.vkCode));
                            }
                            
                    }
                }
                
                return user32.CallNextHookEx(hHook, nCode, wParam, info.getPointer());
            }
        };

        hHook = user32.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHookCallback, hModule, 0);
        System.out.println("Keyboard hook installed, type anywhere, 'q' to quit");
        new Thread() {

            public void run() {
                while (!quit) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                    }
                }
                System.err.println("unhook and exit");
                user32.UnhookWindowsHookEx(hHook);
                System.exit(0);
            }
        }.start();

        int result;
        MSG msg = new MSG();
        while ((result = user32.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                System.err.println("error in get message");
                break;
            } else {
                System.err.println("got message");
                user32.TranslateMessage(msg);
                user32.DispatchMessage(msg);
            }
        }
        user32.UnhookWindowsHookEx(hHook);
    }
}
