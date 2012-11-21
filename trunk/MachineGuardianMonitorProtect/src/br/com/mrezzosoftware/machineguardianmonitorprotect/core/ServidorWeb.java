package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import br.com.mrezzosoftware.machineguardianmonitorprotect.windows.monitor.Geolocation;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServidorWeb {

    private static final String URL_SERVIDOR = "http://www.mrsoftware.qlix.com.br";

    public static String cadastrarMaquina(String email, String idMaquina) {

        URL url;
        HttpURLConnection connection = null;

        try {
            //Create connection
            //String parametrosRequisicao = "email=" + URLEncoder.encode(email, "UTF-8");
            String parametrosRequisicao = "email=" + email;
            parametrosRequisicao += "&dispositivo=pc";
            parametrosRequisicao += "&operacao=cadastrarMaquina";
            parametrosRequisicao += "&maquina=" + idMaquina;

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
            leitorResposta.readLine(); // Lê a primeira linha para descarte (não é informação útil).

            if (connection.getResponseCode() == 200 && (resposta = leitorResposta.readLine()) != null) {
                leitorResposta.close();

                return resposta;

            } else {
                return connection.getResponseCode() + " - " + connection.getResponseMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            parametrosRequisicao += "&dispositivo=pc";
            parametrosRequisicao += "&operacao=obterAcoesMaquina";
            parametrosRequisicao += "&maquina=" + URLEncoder.encode(PreferencesUtil.getInstance().obterValor(Constantes.PREF_ID_MAQUINA), "UTF-8");

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
            leitorResposta.readLine(); // Lê a primeira linha para descarte (não é informação útil).

            if (connection.getResponseCode() == 200 && (resposta = leitorResposta.readLine()) != null) {

                Operacoes operacoes;

                if (resposta.contains("AC-OBT-SUC")) {
                    resposta = leitorResposta.readLine();
                    String[] arrayStringAcaoes = resposta.trim().split("#");

                    operacoes = new Operacoes();
                    operacoes.setModoEspera((arrayStringAcaoes[0].equalsIgnoreCase("1")) ? true : false);
                    operacoes.setTempoAtualizacao(Integer.parseInt(arrayStringAcaoes[1]));
                    operacoes.setUltimaAtualizacaoMobile(new SimpleDateFormat("y-M-d H:m:s").parse(arrayStringAcaoes[2]));
                    operacoes.setIdAcao(Byte.parseByte(arrayStringAcaoes[3]));
                    operacoes.setCapturarTeclas((arrayStringAcaoes[4].equalsIgnoreCase("1")) ? true : false);
                    operacoes.setGeolocalizacao((arrayStringAcaoes[5].equalsIgnoreCase("1")) ? true : false);


                    leitorResposta.close();
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

    public static boolean maquinaCadastrada(String email, String senha) {

        URL url;
        HttpURLConnection connection = null;

        try {
            //Create connection
            String parametrosRequisicao = "email=" + URLEncoder.encode(PreferencesUtil.getInstance().obterValor(Constantes.PREF_EMAIL), "UTF-8");
            parametrosRequisicao += "&dispositivo=pc";
            parametrosRequisicao += "&operacao=maquinaCadastrada";
            parametrosRequisicao += "&maquina=" + URLEncoder.encode(PreferencesUtil.getInstance().obterValor(Constantes.PREF_ID_MAQUINA), "UTF-8");

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
            leitorResposta.readLine(); // Lê a primeira linha para descarte (não é informação útil).

            if (connection.getResponseCode() == 200 && (resposta = leitorResposta.readLine()) != null) {

                System.out.println("RESPOSTA: " + resposta);
                
                if (resposta.equalsIgnoreCase("S")) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
