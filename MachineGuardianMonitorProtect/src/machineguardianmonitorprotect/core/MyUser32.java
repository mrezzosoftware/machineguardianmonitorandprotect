package machineguardianmonitorprotect.core;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HINSTANCE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;


public interface MyUser32 extends Library {

    HWND GetForegroundWindow();
    int GetWindowTextA(HWND hwnd, byte[] chars, int i);
    int GetWindowTextLengthA(HWND hwnd);
    int GetWindowThreadProcessId(HWND hWnd, PointerByReference pref);
    
    
    
    int ToUnicodeEx(int wVirtKey, int wScanCode, byte[] lpKeyState, char[] pwszBuff, int cchBuff, int wFlags, IntByReference dwhkl);
    int MapVirtualKeyExW(int uCode, int nMapType, IntByReference dwhkl);
    IntByReference GetKeyboardLayout(int idThread);
    short GetAsyncKeyState(int key);
    short GetKeyState(int key);
    boolean GetKeyboardState(byte[] lpKeyState);
    HHOOK SetWindowsHookExW(int i, HOOKPROC hkprc, HINSTANCE hnstnc, int i1);
    LRESULT CallNextHookEx(HHOOK hhook, int i, WinDef.WPARAM wparam, Pointer pntr);
    boolean UnhookWindowsHookEx(HHOOK hhook);
    public int GetMessageW(WinUser.MSG msg, HWND hwnd, int i, int i1);
    public boolean TranslateMessage(WinUser.MSG msg);
    public LRESULT DispatchMessage(WinUser.MSG msg);
    
}
