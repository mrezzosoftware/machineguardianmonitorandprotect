package br.com.mrezzosoftware.machineguardianmonitorprotect;

import br.com.mrezzosoftware.machineguardianmonitorprotect.windows.MGMPWindows;

public class MachineGuardianMonitorProtect {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MGMPWindows w = new MGMPWindows();
        System.out.println("Título janela ativa: " + w.Processos.getTituloJanelaAtiva());
        System.out.println("Nome executável da janela: " + w.Processos.getNomeProcessoJanelaAtiva());
        //w.Teclado.iniciarCapturaTeclasDigitadas();
        //w.SO.fazerLogoff(true);
    }
}
