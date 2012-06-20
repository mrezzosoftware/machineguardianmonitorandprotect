package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import br.com.mrezzosoftware.machineguardianmonitorprotect.windows.monitor.Geolocation;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    
    public static String cadastrarMaquina(String idMaquina) {

        URL url;
        HttpURLConnection connection = null;

        try {
            //Create connection
            String parametrosRequisicao = "email=" + URLEncoder.encode(PreferencesUtil.getInstance().obterValor(Constantes.PREF_EMAIL), "UTF-8");
            parametrosRequisicao += "&idMaquina=" + URLEncoder.encode(idMaquina, "UTF-8");
            parametrosRequisicao += "&cadastrarMaquina=" + URLEncoder.encode("true", "UTF-8");
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
                    
                } else if (resposta.contains("existente")) {
                    
                    return "existente";
                    
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
    
    public static Operacoes obterOperacoes() {

        URL url;
        HttpURLConnection connection = null;

        try {
            //Create connection
            String parametrosRequisicao = "email=" + URLEncoder.encode(PreferencesUtil.getInstance().obterValor(Constantes.PREF_EMAIL), "UTF-8");
            parametrosRequisicao += "&idMaquina=" + URLEncoder.encode(PreferencesUtil.getInstance().obterValor(Constantes.PREF_ID_MAQUINA), "UTF-8");
            parametrosRequisicao += "&listarOperacoes=" + URLEncoder.encode("true", "UTF-8");
            
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
                Operacoes operacoes;
                
                if (resposta.contains(":")) {
                    
                    System.out.println("OPERAÇÕES: " + resposta);
                    String[] arrayStringOper = resposta.trim().split(":");
                    
                    operacoes = new Operacoes();
                    operacoes.setModoEspera((arrayStringOper[0].equalsIgnoreCase("1")) ? true : false);
                    operacoes.setTempoAtualizacao(Byte.parseByte(arrayStringOper[1]));
                    operacoes.setIdOperacao(Byte.parseByte(arrayStringOper[2]));
                    operacoes.setCapturarTeclas((arrayStringOper[3].equalsIgnoreCase("1")) ? true : false);
                    operacoes.setGeolocalizacao((arrayStringOper[4].equalsIgnoreCase("1")) ? true : false);
                    
                    
                    
                    return operacoes;
                    
                } else if (resposta.contains("inexistente")) {
                    
                    return null;
                    
                } else if (resposta.contains("existente")) {
                    
                    return null;
                    
                } else {
                    return null;
                }
                
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    public static String registrarLocalizacaoMaquina(Geolocation.Coordenadas coords) {

        URL url;
        HttpURLConnection connection = null;

        try {
            //Create connection
            String parametrosRequisicao = "email=" + URLEncoder.encode(PreferencesUtil.getInstance().obterValor(Constantes.PREF_EMAIL), "UTF-8");
            parametrosRequisicao += "&idMaquina=" + URLEncoder.encode(PreferencesUtil.getInstance().obterValor(Constantes.PREF_ID_MAQUINA), "UTF-8");
            parametrosRequisicao += "&inserirLocalizacao=" + URLEncoder.encode("true", "UTF-8");
            parametrosRequisicao += "&latitude=" + URLEncoder.encode(coords.getLatitude(), "UTF-8");
            parametrosRequisicao += "&longitude=" + URLEncoder.encode(coords.getLongitude(), "UTF-8");
            parametrosRequisicao += "&dataHora=" + URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), "UTF-8");
            
            
            
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
                    return "false";
                }
                
            } else {
                return "false";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        } finally {
            
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
