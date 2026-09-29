package com.vro.rfc.constants;

public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    public static final String CONFIG_FILE = "config.properties";

    public static final String DEFAULT_BASE_URL = "https://qa.valueresearch.in/login";
    public static final int DEFAULT_EXPLICIT_WAIT_SECONDS = 15;
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS = 30;

    public static final String SCREENSHOT_DIR = "screenshots";
    public static final String REPORT_DIR = "reports";

    /** Classpath-relative prefix (src/test/resources/testdata/...). */
    public static final String TESTDATA_CLASSPATH_PREFIX = "testdata/";
}
