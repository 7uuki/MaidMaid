package de.fhandshit.maidmaid;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonToStringConverter {

    public String convertJsonToString(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String extractProductNameFromJson(String jsonString) {
        Gson gson = new Gson();
        ProductResponse response = gson.fromJson(jsonString, ProductResponse.class);
        return response != null && response.getProduct() != null ? response.getProduct().getProductNameEn() : null;
    }

    // Define POJO classes to map JSON response
    class ProductResponse {
        private ProductInfo product;

        public ProductInfo getProduct() {
            return product;
        }

        public void setProduct(ProductInfo product) {
            this.product = product;
        }
    }

    class ProductInfo {
        private String product_name_en;

        public String getProductNameEn() {
            return product_name_en;
        }

        public void setProductNameEn(String product_name_en) {
            this.product_name_en = product_name_en;
        }
    }
}

