package machineguardianmonitorprotect.core;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface MyUser32 extends User32 {
    
    @Override
    HWND GetForegroundWindow();
    int GetWindowTextA(HWND hwnd, byte[] chars, int i);
    int GetWindowTextLengthA(HWND hwnd);
    int GetWindowThreadProcessId(HWND hWnd, PointerByReference pref);

}
