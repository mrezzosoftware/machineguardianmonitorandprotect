package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public interface MyKernel32 extends Library {
    
    int PROCESS_QUERY_INFORMATION = 0x0400;
    int PROCESS_VM_READ = 0x0010;
    
    HANDLE OpenProcess(int dwDesiredAccess, boolean bInheritHandle, Pointer pointer);
    public boolean CloseHandle(HANDLE handle);
    
    int GetProcessId(HANDLE handle);
    int K32GetProcessImageFileNameA(HANDLE hProcess, char[] lpBaseName, int size);
    int QueryFullProcessImageNameA(HANDLE hProcess, char[] lpBaseName, int size);
    
    int K32GetModuleBaseNameW(HANDLE handler, HMODULE hmodule, char[] lpBaseName, int size);

    
}
