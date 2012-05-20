package machineguardianmonitorprotect.core;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.PointerByReference;

public class ActiveWindowInfo {

    private static MyUser32 user32 = (MyUser32) Native.loadLibrary("user32", MyUser32.class);
    private static MyKernel32 kernel32 = (MyKernel32) Native.loadLibrary("kernel32", MyKernel32.class);
    private static MyPsapi psapi = (MyPsapi) Native.loadLibrary("psapi", MyPsapi.class);
    
    public static String getTituloJanelaAtiva() {
        byte[] windowText = new byte[512];
        
        user32.GetWindowTextA(user32.GetForegroundWindow(), windowText, user32.GetWindowTextLengthA(user32.GetForegroundWindow()) + 1);
        
        return Native.toString(windowText);
    }
    
    public static String getNomeProcessoJanelaAtiva() {
        char[] nomeExecutavel = new char[1024 * 2];
        PointerByReference ponteiroIdProcesso = new PointerByReference();
        
        user32.GetWindowThreadProcessId(user32.GetForegroundWindow(), ponteiroIdProcesso);
        HANDLE processo = kernel32.OpenProcess(MyKernel32.PROCESS_QUERY_INFORMATION | MyKernel32.PROCESS_VM_READ,
                false,
                ponteiroIdProcesso.getValue());
        
        psapi.GetModuleBaseNameW(processo, null, nomeExecutavel, 1024);
        kernel32.CloseHandle(processo);
        
        return Native.toString(nomeExecutavel);
    }
}
