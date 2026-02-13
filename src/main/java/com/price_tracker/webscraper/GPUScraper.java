package com.price_tracker.webscraper;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

@Data
@NoArgsConstructor
public class GPUScraper {

    public String scrapeGPUName(String url) {

        // css locations for Umart GPU parameters
        String umartCSSNameLocation = ".goods_title h1";
        String umartCSSPriceLocation = "span.goods-price";
        String umartCSSModelLocation = "li:contains(Model Number)";

        try {
            Document document = Jsoup.connect(url).get();
            String name = document.select(umartCSSNameLocation).text();
            String price = document.select(umartCSSPriceLocation).text();
            String rawModelNumber = document.select(umartCSSModelLocation).text();
            // if the return element has "Model Number: ", remove it.
            String modelNumber = rawModelNumber.contains(":") ? rawModelNumber.split(":")[1].trim() : rawModelNumber;
            System.out.println("====================");
            System.out.println("UMART - GPUs");
            System.out.println(name);
            System.out.println(price);
            System.out.println(modelNumber);
            return name;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
