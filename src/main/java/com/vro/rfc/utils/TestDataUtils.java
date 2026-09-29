package com.vro.rfc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vro.rfc.constants.FrameworkConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Loads test data files from src/test/resources/testdata/ (classpath). Formats are
 * kept intentionally minimal - add another loader here only when an RFC actually
 * needs it.
 */
public final class TestDataUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestDataUtils() {
    }

    public static Properties loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        try (InputStream in = openTestDataStream(fileName)) {
            properties.load(in);
        }
        return properties;
    }

    public static Map<String, Object> loadJson(String fileName) throws IOException {
        try (InputStream in = openTestDataStream(fileName)) {
            return MAPPER.readValue(in, Map.class);
        }
    }

    public static List<String[]> loadCsv(String fileName) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (InputStream in = openTestDataStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    rows.add(line.split(","));
                }
            }
        }
        return rows;
    }

    private static InputStream openTestDataStream(String fileName) {
        String path = FrameworkConstants.TESTDATA_CLASSPATH_PREFIX + fileName;
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (in == null) {
            throw new UncheckedIOException(new IOException("Test data file not found on classpath: " + path));
        }
        return in;
    }
}
