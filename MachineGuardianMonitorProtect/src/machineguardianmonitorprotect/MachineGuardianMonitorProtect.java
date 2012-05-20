package machineguardianmonitorprotect;

import machineguardianmonitorprotect.core.windows.Windows;

public class MachineGuardianMonitorProtect {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Título janela ativa: " + Windows.Processos.getTituloJanelaAtiva());
        System.out.println("Nome executável da janela: " + Windows.Processos.getNomeProcessoJanelaAtiva());
    }
}
