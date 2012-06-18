package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ServidorWeb {

    //private static final String URL_SERVIDOR = "http://www.mrsoftware.qlix.com.br";
    private static final String URL_SERVIDOR = "http://mrs.ueuo.com/";

    public static String verificarEmail(String email) {

        URL url;
        HttpURLConnection connection = null;

        try {
            //Create connection
            String parametrosRequisicao = "email=" + URLEncoder.encode(email, "UTF-8");
            parametrosRequisicao += "&verificarEmail=" + URLEncoder.encode("true", "UTF-8");
            System.out.println("Parametros: " + parametrosRequisicao);
            url = new URL(URL_SERVIDOR);
            
            connection = (HttpURLConnection) url.openConnection(NetworkUtil.getLocalProxy());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametrosRequisicao.getBytes().length));


            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametrosRequisicao);
            wr.flush();
            wr.close();

            String resposta;
            InputStream is = connection.getInputStream();
            BufferedReader leitorResposta = new BufferedReader(new InputStreamReader(is));

            System.out.println("RESPOSTA: " + (resposta = leitorResposta.readLine()));
            
            if (connection.getResponseCode() == 200 && (resposta = leitorResposta.readLine()) != null) {
                leitorResposta.close();

                if (resposta.contains("true")) {
                    
                    return "true";
                    
                } else if (resposta.contains("false")) {
                    
                    return "false";
                    
                } else {
                    return connection.getResponseCode() + " - " + connection.getResponseMessage();
                }
                
            } else {
                return connection.getResponseCode() + " - " + connection.getResponseMessage();
            }

        } catch (Exception e) {
            return "ERRO";
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public static String cadastrarMaquina(String idMaquina) {

        URL url;
        HttpURLConnection connection = null;

        try {
            //Create connection
            String parametrosRequisicao = "email=" + URLEncoder.encode(PreferencesUtil.getInstance().obterValor(Constantes.PREF_EMAIL), "UTF-8");
            parametrosRequisicao += "idMaquina=" + URLEncoder.encode(idMaquina, "UTF-8");
            parametrosRequisicao += "cadastrarMaquina=" + URLEncoder.encode("true", "UTF-8");
            url = new URL(URL_SERVIDOR);
            
            connection = (HttpURLConnection) url.openConnection(NetworkUtil.getLocalProxy());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametrosRequisicao.getBytes().length));


            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametrosRequisicao);
            wr.flush();
            wr.close();

            String resposta;
            InputStream is = connection.getInputStream();
            BufferedReader leitorResposta = new BufferedReader(new InputStreamReader(is));

            if (connection.getResponseCode() == 200 && (resposta = leitorResposta.readLine()) != null) {
                leitorResposta.close();

                if (resposta.contains("true")) {
                    
                    return "true";
                    
                } else if (resposta.contains("false")) {
                    
                    return "false";
                    
                } else {
                    return connection.getResponseCode() + " - " + connection.getResponseMessage();
                }
                
            } else {
                return connection.getResponseCode() + " - " + connection.getResponseMessage();
            }

        } catch (Exception e) {
            return "ERRO";
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
