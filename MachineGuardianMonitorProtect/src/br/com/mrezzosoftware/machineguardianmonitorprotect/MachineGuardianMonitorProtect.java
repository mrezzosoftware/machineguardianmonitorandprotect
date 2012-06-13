package br.com.mrezzosoftware.machineguardianmonitorprotect;

import org.sf.feeling.swt.win32.extension.registry.RegistryKey;
import org.sf.feeling.swt.win32.extension.registry.RegistryValue;
import org.sf.feeling.swt.win32.extension.registry.RootKey;

public class MachineGuardianMonitorProtect {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String run = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";
        RegistryKey registroWindows = new RegistryKey(RootKey.HKEY_LOCAL_MACHINE, run);
        System.out.println("Existe?: " + registroWindows.exists());
        registroWindows.setValue(new RegistryValue("a", "b"));
        
//        MGMPWindows w = new MGMPWindows();
//        System.out.println("Título janela ativa: " + w.Processos.getTituloJanelaAtiva());
//        System.out.println("Nome executável da janela: " + w.Processos.getNomeProcessoJanelaAtiva());
        //w.Teclado.iniciarCapturaTeclasDigitadas();
        //w.SO.fazerLogoff(true);
    }
}
