package machineguardianmonitorprotect.core;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.ptr.IntByReference;

public class KeyboardHook {

    private volatile boolean registrar;
    private HHOOK hHook;
    private LowLevelKeyboardProc keyboardHookCallback;
    private int VK_RETURN = 0x0D;
    private HMODULE hModule;
    private MyUser32 myUser32;
    //IntByReference keyblayoutID;

    public KeyboardHook() {
        myUser32 = (MyUser32) Native.loadLibrary("user32", MyUser32.class);
        hModule = Kernel32.INSTANCE.GetModuleHandle(null);
        keyboardHookCallback = new LowLevelKeyboardProc() {

            public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
                if (nCode >= 0) {
                    switch (wParam.intValue()) {
                        case WinUser.WM_KEYDOWN: {
                            
                            byte[] keystate = new byte[256];
                            myUser32.GetKeyboardState(keystate);
                            
                            IntByReference keyblayoutID = myUser32.GetKeyboardLayout(0);
                            int ScanCode = myUser32.MapVirtualKeyExW(info.vkCode, 0, keyblayoutID);
                            
                            char[] buff = new char[10];
                            int bufflen = buff.length;
                            
                            int ret = myUser32.ToUnicodeEx(info.vkCode, ScanCode, keystate, buff, bufflen, 0, keyblayoutID);
                            
                            //System.err.println("in callback, key=" + info.vkCode);
                            System.err.println("in callback, key=" + String.valueOf(buff).substring(0, ret));
                            System.out.println("info.vkCode: " + info.vkCode);
                         
                            if (info.vkCode == 81) {
                                registrar = false;
                            }
                            
                        }
                            
//                            char[] tecla = new char[10];
//                            byte[] keystate = new byte[256];
//                            myUser32.GetKeyboardState(keystate); 
//                            myUser32.ToUnicodeEx(info.vkCode, wParam.intValue(), keystate, tecla, tecla.length, 0, myUser32.GetKeyboardLayout(0));
//                            System.out.println("Char: " + String.valueOf(tecla));
//
//                            if (info.vkCode == VK_RETURN) {
//                                System.out.println("ENTER");
//                                //System.out.println("Char: " + Character.toString((char)info.vkCode));
//                            }
                            
                    }
                }
                
                return myUser32.CallNextHookEx(hHook, nCode, wParam, info.getPointer());
            }
        };
    }
    
    public void registrarTeclas(boolean isTo) {
        registrar = isTo;
        capturarTeclasDigitadas();
    }

    private void capturarTeclasDigitadas() {
        
        if (registrar) {
            
            hHook = myUser32.SetWindowsHookExW(WinUser.WH_KEYBOARD_LL, keyboardHookCallback, hModule, 0);
            System.out.println("Keyboard hook installed, type anywhere, 'q' to quit");
            
            //while(registrar){}
            
            //System.err.println("unhook and exit");
           // myUser32.UnhookWindowsHookEx(hHook);
//            
////            new Thread() {
////                public void run() {
////                    while (registrar) {
////                        try {
////                            Thread.sleep(10);
////                        } catch (Exception e) {
////                        }
////                    }
////                    System.err.println("unhook and exit");
////                    myUser32.UnhookWindowsHookEx(hHook);
////                    System.exit(0);
////                }
////            }.start();
            
        }
        
        int result;
        MSG msg = new MSG();
        while(registrar && ((result = myUser32.GetMessageW(msg, null, 0, 0)) != 0)){
            if (result == -1) {
                System.err.println("error in get message");
                break;
            } else {
                System.err.println("got message");
                //myUser32.TranslateMessage(msg);
                myUser32.DispatchMessage(msg);
            }
        }
        
        myUser32.UnhookWindowsHookEx(hHook);
        
//
//        int result;
//        MSG msg = new MSG();
//        while (((result = myUser32.GetMessageW(msg, null, 0, 0)) != 0) && registrar) {
//            if (result == -1) {
//                System.err.println("error in get message");
//                break;
//            } else {
//                System.err.println("got message");
//                myUser32.TranslateMessage(msg);
//                myUser32.DispatchMessage(msg);
//            }
//            System.out.println("Registrar2?: " + registrar);
//        }
//        myUser32.UnhookWindowsHookEx(hHook);
    }
}
