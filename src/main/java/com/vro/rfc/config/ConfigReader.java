package com.vro.rfc.config;

import com.vro.rfc.constants.FrameworkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads src/test/resources/config.properties (test classpath). Resolution order:
 * -D system property (command-line override) &gt; properties file &gt; hardcoded default.
 * Values shaped like ${ENV_VAR} are resolved from the environment - this is how
 * credentials stay out of the properties file and out of git.
 */
public final class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader()
                .getResourceAsStream(FrameworkConstants.CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException(
                        FrameworkConstants.CONFIG_FILE + " not found on classpath (expected under src/test/resources)");
            }
            PROPERTIES.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + FrameworkConstants.CONFIG_FILE, e);
        }
    }

    private ConfigReader() {
    }

    public static String getBaseUrl() {
        return getProperty("base.url", FrameworkConstants.DEFAULT_BASE_URL);
    }

    public static String getBrowser() {
        return System.getProperty("browser", getProperty("browser", "chrome"));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", getProperty("headless", "false")));
    }

    public static String getUsername() {
        return resolveEnv(getProperty("username", ""));
    }

    public static String getPassword() {
        return resolveEnv(getProperty("password", ""));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait",
                String.valueOf(FrameworkConstants.DEFAULT_EXPLICIT_WAIT_SECONDS)));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout",
                String.valueOf(FrameworkConstants.DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS)));
    }

    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    private static String resolveEnv(String rawValue) {
        if (rawValue != null && rawValue.startsWith("${") && rawValue.endsWith("}")) {
            String envVar = rawValue.substring(2, rawValue.length() - 1);
            String envValue = System.getenv(envVar);
            if (envValue == null || envValue.isEmpty()) {
                logger.warn("Environment variable {} is not set - credential will be blank", envVar);
                return "";
            }
            return envValue;
        }
        return rawValue;
    }
}
