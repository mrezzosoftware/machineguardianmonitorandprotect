package br.com.mrezzosoftware.machineguardianmonitorprotect;

import br.com.mrezzosoftware.machineguardianmonitorprotect.core.Constantes;
import br.com.mrezzosoftware.machineguardianmonitorprotect.core.PreferencesUtil;
import br.com.mrezzosoftware.machineguardianmonitorprotect.core.ServidorWeb;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MachineGuardianMonitorProtect {

    public static boolean executar = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //registrarInicioWindows();


        System.out.println("PREF_MAIL: " + PreferencesUtil.getInstance().obterValor(Constantes.PREF_EMAIL));
        System.out.println("PREF_ID_MAQUINA: " + PreferencesUtil.getInstance().obterValor(Constantes.PREF_ID_MAQUINA));
        
        if (PreferencesUtil.getInstance().obterValor(Constantes.PREF_EMAIL).equalsIgnoreCase("NREG")
                || PreferencesUtil.getInstance().obterValor(Constantes.PREF_ID_MAQUINA).equalsIgnoreCase("NREG")) {
            
            new MGMPRegistrarMaquina();

            while (MachineGuardianMonitorProtect.executar == false) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MachineGuardianMonitorProtect.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (!PreferencesUtil.getInstance().obterValor(Constantes.PREF_EMAIL).equalsIgnoreCase("NREG")
                    && !PreferencesUtil.getInstance().obterValor(Constantes.PREF_ID_MAQUINA).equalsIgnoreCase("NREG")) {
                iniciarExecucao();
            }

        } else if (ServidorWeb.maquinaCadastrada(PreferencesUtil.getInstance().obterValor(Constantes.PREF_EMAIL),
                PreferencesUtil.getInstance().obterValor(Constantes.PREF_ID_MAQUINA)) == false) {

            new MGMPRegistrarMaquina();
        } else {
            iniciarExecucao();
        }



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

    private static void iniciarExecucao() {
        new Thread(new MGMPAgent()).start();
    }
}
