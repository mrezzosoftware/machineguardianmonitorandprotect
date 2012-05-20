/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machineguardianmonitorprotect.core;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.win32.StdCallLibrary;

public interface MyPsapi extends StdCallLibrary {

    int GetModuleBaseNameW(HANDLE handler, HMODULE hmodule, char[] lpBaseName, int size);
    
    int GetProcessImageFileNameA(HANDLE hProcess, char[] lpBaseName, int size);
}
