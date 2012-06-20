/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrezzosoftware.machineguardianmonitorprotect.windows.monitor;

import br.com.mrezzosoftware.machineguardianmonitorprotect.core.NetworkUtil;
import enumerations.Dot11BSSType;
import functions.JWifiAPI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import structures.Bss;

/**
 *
 * @author MRezzoSoftware
 */
public class Geolocation {

    private static final String URL_BASE = "https://maps.googleapis.com/maps/api/browserlocation/json?browser=firefox&sensor=true";
    private static final String PARAMETROS_REQUISICAO = "&wifi=mac:%s|ssid:%s|ss:%d";

    public Coordenadas localizarUsuario() {
        Coordenadas localAtual = new Coordenadas();

        localAtual = getCoordenadas(localAtual);

        return localAtual;
    }

    private Coordenadas getCoordenadas(Coordenadas output) {
        Coordenadas coord = new Coordenadas();

        try {
            
            URL url = new URL(getUrlConsulta());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(NetworkUtil.getLocalProxy());
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String saida;
            while ((saida = br.readLine()) != null) {

                if (saida.contains("lat")) {
                    coord.setLatitude(removerCaracteres(saida, " ", "\"", ":", ",", "lat"));
                } else if (saida.contains("lng")) {
                    coord.setLongitude(removerCaracteres(saida, " ", "\"", ":", ",", "lng"));
                    break;
                }

            }

            br.close();
            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }


        return coord;
    }

    private String removerCaracteres(String alvo, String... caracteresParaRemover) {

        for (String valorRemover : caracteresParaRemover) {
            alvo = alvo.replaceAll(valorRemover, "");
        }

        return alvo;
    }

    private String getUrlConsulta() {
        String url = URL_BASE;
        ArrayList<WifiInfo> listaAccessPoints;

        try {

            JWifiAPI jWifiAPI = new JWifiAPI(JWifiAPI.getNegotiatedVersion(), JWifiAPI.enumInterfaces()[0].interfaceGuid);

            Bss accessPoints[] = jWifiAPI.getNetworkBssList(null, Dot11BSSType.any, false);

            if (accessPoints.length > 0) {
                listaAccessPoints = new ArrayList<WifiInfo>();

                for (int i = 0; i < accessPoints.length; i++) {
                    listaAccessPoints.add(new WifiInfo(accessPoints[i].dot11Bssid.replaceAll("-", ":"),
                            accessPoints[i].dot11Ssid,
                            accessPoints[i].lRssi,
                            accessPoints[i].uLinkQuality));
                }

                Collections.sort(listaAccessPoints);

                url = URL_BASE;
                Iterator<WifiInfo> itWifiInfo = listaAccessPoints.iterator();

                while (itWifiInfo.hasNext()) {

                    WifiInfo aWifi = itWifiInfo.next();
                    url += String.format(PARAMETROS_REQUISICAO, aWifi.mac, aWifi.ssid, aWifi.forcaSinal);
                }
            }

            url = codificarUrl(url);
        } catch (exceptions.InternalErrorException e) {
            System.out.println("SEM PLACA WIRELESS");
        }

        return url;
    }

    private String codificarUrl(String url) {
        return url.replaceAll(" ", "%20").replaceAll("\\|", "%7C");
    }

    public static void main(String args[]) {

//        String url = URL_BASE;
//        url += String.format(PARAMETROS_REQUISICAO, "00-1a-3f-4e-a8-60", "SIDNEI", -80);
//        url = url.replaceAll(" ", "%20");
//        url = url.replaceAll("\\|", "%7C");

        //System.out.println("Resultado: " + resultado);
        Geolocation geo = new Geolocation();
        //System.out.println("URL: " + geo.getUrlConsulta());
        Coordenadas coord = geo.localizarUsuario();
        System.out.println(coord.getLatitude() + " " + coord.getLongitude());


    }

    public class Coordenadas {

        private String latitude;
        private String longitude;

        public Coordenadas() {
        }

        public Coordenadas(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        /**
         * @return the latitude
         */
        public String getLatitude() {
            return latitude;
        }

        /**
         * @param latitude the latitude to set
         */
        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        /**
         * @return the longitude
         */
        public String getLongitude() {
            return longitude;
        }

        /**
         * @param longitude the longitude to set
         */
        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }

    private class WifiInfo implements Comparable<WifiInfo> {

        private String mac;
        private String ssid;
        private int forcaSinal;
        private int qualidadeLink;

        public WifiInfo() {
        }

        public WifiInfo(String mac, String ssid, int forcaSinal, int qualidadeLink) {
            this.mac = mac;
            this.ssid = ssid;
            this.forcaSinal = forcaSinal;
            this.qualidadeLink = qualidadeLink;
        }

        /**
         * @return the mac
         */
        public String getMac() {
            return mac;
        }

        /**
         * @param mac the mac to set
         */
        public void setMac(String mac) {
            this.mac = mac;
        }

        /**
         * @return the ssid
         */
        public String getSsid() {
            return ssid;
        }

        /**
         * @param ssid the ssid to set
         */
        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        /**
         * @return the forcaSinal
         */
        public int getForcaSinal() {
            return forcaSinal;
        }

        /**
         * @param forcaSinal the forcaSinal to set
         */
        public void setForcaSinal(int forcaSinal) {
            this.forcaSinal = forcaSinal;
        }

        /**
         * @return the qualidadeLink
         */
        public int getQualidadeLink() {
            return qualidadeLink;
        }

        /**
         * @param qualidadeLink the qualidadeLink to set
         */
        public void setQualidadeLink(int qualidadeLink) {
            this.qualidadeLink = qualidadeLink;
        }

        public int compareTo(WifiInfo o) {
            return this.qualidadeLink < o.qualidadeLink ? 1 : (this.qualidadeLink > o.qualidadeLink ? -1 : 0);
        }
    }
}
