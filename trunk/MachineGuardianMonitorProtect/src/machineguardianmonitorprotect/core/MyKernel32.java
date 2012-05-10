package machineguardianmonitorprotect.core;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.ptr.IntByReference;

public interface MyKernel32 extends Kernel32 {
    
    int PROCESS_QUERY_INFORMATION = 0x0400;
    int PROCESS_VM_READ = 0x0010;
    
    @Override
    int GetProcessId(HANDLE handle);
    int K32GetProcessImageFileNameA(HANDLE hProcess, char[] lpBaseName, int size);
    int QueryFullProcessImageNameA(HANDLE hProcess, char[] lpBaseName, int size);
    HANDLE OpenProcess(int dwDesiredAccess, boolean bInheritHandle, Pointer pointer);
    int K32GetModuleBaseNameW(HANDLE handler, HMODULE hmodule, char[] lpBaseName, int size);

    
}
