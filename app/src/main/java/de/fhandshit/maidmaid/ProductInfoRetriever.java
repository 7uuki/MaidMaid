package de.fhandshit.maidmaid;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductInfoRetriever {

    private final QrFragment qrManager;
    private final JsonToStringConverter jsonToStringConverter;

    public ProductInfoRetriever(QrFragment qrManager) {
        this.qrManager = qrManager;
        this.jsonToStringConverter = new JsonToStringConverter();
    }

    public void retrieveProductName(String barcode) {
        String apiUrl = "https://world.openfoodfacts.org/api/v0/product/" + barcode + "?field=product_name_en";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String jsonString = jsonToStringConverter.convertJsonToString(apiUrl);
            String productName = jsonToStringConverter.extractProductNameFromJson(jsonString);
            if (productName != null) {
                qrManager.setProductName(productName);
            } else {
                qrManager.setProductName("Product name not found");
            }
        });
    }
}



