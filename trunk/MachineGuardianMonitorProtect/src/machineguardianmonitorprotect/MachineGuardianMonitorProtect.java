package machineguardianmonitorprotect;

import machineguardianmonitorprotect.core.ActiveWindowInfo;
import machineguardianmonitorprotect.core.KeyboardHook;

public class MachineGuardianMonitorProtect {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ActiveWindowInfo janelaAtiva = new ActiveWindowInfo();
        KeyboardHook kbh = new KeyboardHook();
        kbh.capturarTeclasDigitadas();
        System.out.println("Título janela ativa: " + janelaAtiva.getTituloJanelaAtiva());
        System.out.println("Nome executável da janela: " + janelaAtiva.getNomeExecutavelJanelaAtiva());
    }
}
