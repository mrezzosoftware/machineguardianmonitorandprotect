package br.com.mrezzosoftware.machineguardianmonitorprotect;

import br.com.mrezzosoftware.machineguardianmonitorprotect.core.Constantes;
import br.com.mrezzosoftware.machineguardianmonitorprotect.core.PreferencesUtil;

public class MachineGuardianMonitorProtect {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        registrarInicioWindows();
        
        if (PreferencesUtil.getInstance().obterValor(Constantes.PREF_EMAIL).equalsIgnoreCase("NREG")) {
            new MGMPMain();
        } else {
            MGMPMain mgmp = new MGMPMain();
            mgmp.setVisible(false);
        }
        
        
//        MGMPWindows w = new MGMPWindows();
//        System.out.println("Título janela ativa: " + w.Processos.getTituloJanelaAtiva());
//        System.out.println("Nome executável da janela: " + w.Processos.getNomeProcessoJanelaAtiva());
        //w.Teclado.iniciarCapturaTeclasDigitadas();
        //w.SO.fazerLogoff(true);
    }
    
    private static boolean registrarInicioWindows() {
        
//        final String runKey = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";
//        final String nome = "MGMP";
//        String dados = "javaw -jar ";
//        
//        RegistryKey registroWindows = new RegistryKey(RootKey.HKEY_LOCAL_MACHINE, runKey);
//        
//        if (registroWindows.getValue(nome) != null) {
//            System.out.println("Existe");
//        } else {
//            System.out.println("Não exis");
//        }
        
        
//        System.out.println("Existe?: " + registroWindows.exists());
//        registroWindows.setValue(new RegistryValue("a", "b"));
        
        return true;
    }
}
