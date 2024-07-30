package org.example.functions;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;

public class DataManager {
    Retrofit retrofit;
    private String nextLink = null;
    private MeasurementsApi measurementsApi;
    private String filter = "TIMESTAMP gt 2024-07-20T16:15:25.103Z";
    private String orderby = "TIMESTAMP";
    public String jsonData;
    private String urlString = "https://e742-77-255-163-211.ngrok-free.app";

    DataManager(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://purple-moss-0945ff003.5.azurestaticapps.net/data-api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        measurementsApi = retrofit.create(MeasurementsApi.class);
    }




    public void getDataJson() {
        Call<ResponseBody> call = measurementsApi.getMeasurementsJson(filter,orderby);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                try {
                    jsonData = response.body().string();
                    System.out.println("Measurements fetched!");

                    sendData();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendData(){
        try {
            // Trust all certificates
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            // Set up SSL context
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Bypass hostname verification
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            // Send POST request
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            // Write JSON data to output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Check response
            int responseCode = connection.getResponseCode();
            System.out.println("POST Response Code: " + responseCode);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



