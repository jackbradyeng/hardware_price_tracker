package com.priceTracker.testingData.documentCapture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class DocumentLoader {

    private static final String FIXTURE_ROOT = "src/test/resources/scraper_fixtures/";

    public static Document load(String relativePath) {
        try {
            return Jsoup.parse(new File(FIXTURE_ROOT + relativePath), "UTF-8");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load fixture: " + relativePath, e);
        }
    }
}