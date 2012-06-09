/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import enumerations.Dot11BSSType;
import functions.JWifiAPI;
import structures.AvailableNetwork;
import structures.Bss;
import structures.InterfaceInfo;

/**
 *
 * @author MRezzoSoftware
 */
public class JWifiApiTest {

    public static void main(String[] args) {
        //Get 'guid' and 'negotiatedVersion' (needed to init JWifiAPI) 
        InterfaceInfo[] interfaces = JWifiAPI.enumInterfaces();
        String guid = interfaces[0].interfaceGuid;
        int negotiatedVersion = JWifiAPI.getNegotiatedVersion();
        System.out.println("Guid: " + guid);
        System.out.println("Negotiated Version: " + negotiatedVersion);
        //Init JWifiAPI 
        JWifiAPI jWifiAPI = new JWifiAPI(negotiatedVersion, guid);
        //Get wifi network list 
        AvailableNetwork[] networks = jWifiAPI.getAvailableNetworkList(null);
        //Print network names and its signal level System.out.println("Total " + networks.length + " networks.");
        
//        System.out.println("AvailableNetwork Result:");
//        System.out.println("networks.dot11Ssid: " + networks[0].dot11Ssid);
//        System.out.println("networks.strProfileName: " + networks[0].strProfileName);
//        System.out.println("networks.dot11BssType: " + networks[0].dot11BssType);
//        System.out.println("networks.dot11DefaultAuthAlgorithm: " + networks[0].dot11DefaultAuthAlgorithm);
//        System.out.println("networks.dot11DefaultCipherAlgorithm: " + networks[0].dot11DefaultCipherAlgorithm);
//        System.out.println("networks.dot11PhyTypes: " + networks[0].dot11PhyTypes.toString());
//        System.out.println("networks.uNumberOfBssids: " + networks[0].uNumberOfBssids);
//        System.out.println("networks.uNumberOfPhyTypes: " + networks[0].uNumberOfPhyTypes);
//        System.out.println("networks.wlanNotConnectableReason.name: " + networks[0].wlanNotConnectableReason.name());
//        System.out.println("networks.wlanSignalQuality: " + networks[0].wlanSignalQuality);
        
        Bss bss[] = jWifiAPI.getNetworkBssList(null, Dot11BSSType.any, false);
        
        System.out.println("Total " + bss.length + " networks.");
        for (int i = 0; i < bss.length;  i++) {
            Bss network = bss[i];
            System.out.println ( 
            "PHY on which the AP is operating: " + network.uPhyId +
            "\nMAC address: " + network.dot11Bssid +
            "\nNetwork SSID: " + network.dot11Ssid + 
            "\nNetwork type: " + network.dot11BssType + 
            "\nPHY type: " + network.dot11BssPhyType + 
            "\nSignal strength (dBm): " + network.lRssi + 
            "\nLink quality (0-100): " + network.uLinkQuality + 
            "\nComplies with the conf. regulatory domain: " + network.bInRegDomain + 
            "\nBeacon interval: " + network.usBeaconPeriod + 
            "\nTimestamp from beacon packet or probe response: " + network.ullTimestamp +
            "\nHost timestamp value when beacon is received: " + network.ullHostTimestamp + 
            "\nCapability value from the beacon packet: " + network.usCapabilityInformation + 
            "\nFrequency of the center channel (kHz): " + network.ulChCenterFrequency + 
            "\nOffset of the data blob: " + network.ulIeOffset + 
            "\nSize of the data blob (bytes): " + network.ulIeSize + "\n--" );
        }
        
        
//        for (int i = 0; i < networks.length; i++) {
//            System.out.println("Network: " + networks[i].dot11Ssid);
//            System.out.print("Level: ");
//            System.out.println(networks[i].wlanSignalQuality + "%");
//            System.out.println("--");
//        }
    }
}
