package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 01837484333
 */
public class NetworkUtil {

    public static Proxy getLocalProxy() {
        
        try {

            System.setProperty("java.net.useSystemProxies", "true");
            Proxy proxy = (Proxy) ProxySelector.getDefault().select(new URI("http://127.0.0.1")).iterator().next();

            if (((InetSocketAddress) proxy.address()) == null) {
                return Proxy.NO_PROXY;
            }
            
            return proxy;

        } catch (URISyntaxException ex) {
            Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
            return Proxy.NO_PROXY;
        }
    }
    
    
}
