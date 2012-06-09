/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author MRezzoSoftware
 */
public class NetClientGet {

    // http://localhost:8080/RESTfulExample/json/product/get
    public static void main(String[] args) {

        try {

            URL url = new URL("https://maps.googleapis.com/maps/api/browserlocation/json?browser=firefox&sensor=true&wifi=mac:f0:7d:68:da:11:2d%7Cssid:HANS%7Css:-89&wifi=mac:6c:2e:85:ef:12:2f%7Cssid:GVT-122B%7Css:-84&wifi=mac:00:1a:3f:4e:a8:60%7Cssid:SIDNEI%7Css:-82&wifi=mac:90:84:0d:e0:c6:ed%7Cssid:Apple%20Land%7Css:-77&wifi=mac:00:25:f1:eb:f8:95%7Cssid:GRAG%7Css:-76&wifi=mac:96:84:0d:e0:c6:ed%7Cssid:Wi%20Fi%7Css:-73&wifi=mac:00:1c:df:c4:5c:ed%7Cssid:Super%20Belkin%7Css:-70&wifi=mac:74:ea:3a:c3:0b:04%7Cssid:MJMC%7Css:-53");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
