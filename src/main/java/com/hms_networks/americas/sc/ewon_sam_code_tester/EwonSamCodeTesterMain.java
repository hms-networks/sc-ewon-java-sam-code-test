package com.hms_networks.americas.sc.ewon_sam_code_tester;

import com.ewon.ewonitf.ScheduledActionManager;
import com.hms_networks.americas.sc.extensions.fileutils.FileAccessManager;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.system.http.SCHttpUtility;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUtils;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Main class for the Solution Center Ewon SAM Code Tester Project.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @version 1.0.0
 */
public class EwonSamCodeTesterMain {

  /** The file path to the resulting test report. */
  private static final String TEST_REPORT_FILE = "/usr/sc-ewon-java-sam-code-test/report.txt";

  /** The URL of the test server. */
  private static final String TEST_SERVER_URL = "httpstat.us";

  /** The body of requests sent to the test server. */
  private static final String TEST_SERVER_REQUEST_BODY = "";

  /** The header of requests sent to the test server. */
  private static final String TEST_SERVER_REQUEST_HEADER = "";

  /** The file path to the temporary response file from the test server. */
  private static final String TEST_SERVER_RESPONSE_FILE =
      "/usr/sc-ewon-java-sam-code-test/response.tmp";

  /** Boolean indicating if the HTTPS tests should be run. */
  private static final boolean TEST_SERVER_ENABLE_HTTPS = true;

  /**
   * Main method for the Solution Center Ewon SAM Code Tester Project.
   *
   * @param args project arguments
   */
  public static void main(String[] args) {
    // Configure logger to use highest level,
    Logger.SET_LOG_LEVEL(Logger.LOG_LEVEL_DEBUG);

    // Output application startup
    Logger.LOG_DEBUG(
        "Starting "
            + EwonSamCodeTesterMain.class.getPackage().getImplementationTitle()
            + " v"
            + EwonSamCodeTesterMain.class.getPackage().getImplementationVersion()
            + "...");

    try {
      // Inject local time in to the JVM
      try {
        SCTimeUtils.injectJvmLocalTime();

        final Date currentTime = new Date();
        final String currentLocalTime = SCTimeUtils.getIso8601LocalTimeFormat().format(currentTime);
        final String currentUtcTime = SCTimeUtils.getIso8601UtcTimeFormat().format(currentTime);
        Logger.LOG_DEBUG(
            "The local time zone is "
                + SCTimeUtils.getTimeZoneName()
                + " with an identifier of "
                + SCTimeUtils.getLocalTimeZoneDesignator()
                + ". The current local time is "
                + currentLocalTime
                + ", and the current UTC time is "
                + currentUtcTime
                + ".");
      } catch (Exception e) {
        Logger.LOG_CRITICAL("Unable to inject local time in to the JVM!");
        Logger.LOG_EXCEPTION(e);
      }

      // Create file for test report
      final String reportGenerationTime =
          SCTimeUtils.getIso8601LocalTimeFormat().format(new Date());
      Logger.LOG_DEBUG(
          "Creating test report file (existing will be overwritten): " + TEST_REPORT_FILE);
      File testReportFile = new File(TEST_REPORT_FILE);
      if (testReportFile.exists()) {
        testReportFile.delete();
      }
      FileAccessManager.writeStringToFile(
          TEST_REPORT_FILE, "Ewon SAM Code Tester Report (" + reportGenerationTime + ")\n");

      // Test RFC 9110 HTTP response codes
      int[] rfc9110HttpResponseCodes = {
        100, 101, 200, 201, 202, 203, 204, 205, 206, 300, 301, 302, 303, 304, 305, 306, 307, 308,
        400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417,
        418, 421, 422, 426, 500, 501, 502, 503, 504, 505
      };
      for (int i = 0; i < rfc9110HttpResponseCodes.length; i++) {
        int rfc9110HttpResponseCode = rfc9110HttpResponseCodes[i];
        // Build URL for test
        String httpTestUrl = "http://" + TEST_SERVER_URL + "/" + rfc9110HttpResponseCode;
        String httpsTestUrl = "https://" + TEST_SERVER_URL + "/" + rfc9110HttpResponseCode;

        // Test HTTP URL via GET
        int httpGetSamStatusCode = -1;
        try {
          httpGetSamStatusCode =
              ScheduledActionManager.RequestHttpX(
                  httpTestUrl,
                  SCHttpUtility.HTTPX_GET_STRING,
                  TEST_SERVER_REQUEST_HEADER,
                  TEST_SERVER_REQUEST_BODY,
                  "",
                  TEST_SERVER_RESPONSE_FILE);
        } catch (Exception e) {
          String logMessage =
              "An error occurred while testing HTTP GET request for RFC 9110 response code "
                  + rfc9110HttpResponseCode
                  + "!\n (URL: "
                  + httpTestUrl
                  + ")";
          outputTestError(logMessage, e);
        }

        // Test HTTP URL via POST
        int httpPostSamStatusCode = -1;
        try {
          httpPostSamStatusCode =
              ScheduledActionManager.RequestHttpX(
                  httpTestUrl,
                  SCHttpUtility.HTTPX_POST_STRING,
                  TEST_SERVER_REQUEST_HEADER,
                  TEST_SERVER_REQUEST_BODY,
                  "",
                  TEST_SERVER_RESPONSE_FILE);
        } catch (Exception e) {
          String logMessage =
              "An error occurred while testing HTTP POST request for RFC 9110 response code "
                  + rfc9110HttpResponseCode
                  + "!\n (URL: "
                  + httpTestUrl
                  + ")";
          outputTestError(logMessage, e);
        }

        // Test HTTPS URL via GET
        int httpsGetSamStatusCode = -1;
        if (TEST_SERVER_ENABLE_HTTPS) {
          try {
            httpsGetSamStatusCode =
                ScheduledActionManager.RequestHttpX(
                    httpsTestUrl,
                    SCHttpUtility.HTTPX_GET_STRING,
                    TEST_SERVER_REQUEST_HEADER,
                    TEST_SERVER_REQUEST_BODY,
                    "",
                    TEST_SERVER_RESPONSE_FILE);
          } catch (Exception e) {
            String logMessage =
                "An error occurred while testing HTTPS GET request for RFC 9110 response code "
                    + rfc9110HttpResponseCode
                    + "!\n (URL: "
                    + httpsTestUrl
                    + ")";
            outputTestError(logMessage, e);
          }
        }

        // Test HTTPS URL via POST
        int httpsPostSamStatusCode = -1;
        if (TEST_SERVER_ENABLE_HTTPS) {
          try {
            httpsPostSamStatusCode =
                ScheduledActionManager.RequestHttpX(
                    httpsTestUrl,
                    SCHttpUtility.HTTPX_POST_STRING,
                    TEST_SERVER_REQUEST_HEADER,
                    TEST_SERVER_REQUEST_BODY,
                    "",
                    TEST_SERVER_RESPONSE_FILE);
          } catch (Exception e) {
            String logMessage =
                "An error occurred while testing HTTPS POST request for RFC 9110 response code "
                    + rfc9110HttpResponseCode
                    + "!\n (URL: "
                    + httpsTestUrl
                    + ")";
            outputTestError(logMessage, e);
          }
        }

        // Output test results
        outputTestResult(
            httpTestUrl,
            httpsTestUrl,
            rfc9110HttpResponseCode,
            httpGetSamStatusCode,
            httpPostSamStatusCode,
            httpsGetSamStatusCode,
            httpsPostSamStatusCode);
      }
    } catch (Exception e) {
      Logger.LOG_CRITICAL("An error occurred while running the Ewon SAM Code Tester!");
      Logger.LOG_EXCEPTION(e);
    }

    // Output application finish
    Logger.LOG_DEBUG(
        "Finished running "
            + EwonSamCodeTesterMain.class.getPackage().getImplementationTitle()
            + ".");
  }

  /**
   * Outputs a test error to the log and test report file.
   *
   * @param message The error message to output.
   * @param exception The exception to output.
   * @throws IOException If an error occurs while writing to the test report file.
   */
  private static void outputTestError(String message, Exception exception) throws IOException {
    Logger.LOG_CRITICAL(message);
    Logger.LOG_EXCEPTION(exception);
    FileAccessManager.appendStringToFile(TEST_REPORT_FILE, "\n" + message);
  }

  /**
   * Outputs a test result to the log and test report file.
   *
   * @param httpUrl The HTTP URL used for the test.
   * @param httpsUrl The HTTPS URL used for the test.
   * @param rfc9110HttpResponseCode The tested RFC 9110 HTTP response code.
   * @param httpGetSamStatusCode The SAM code returned by the HTTP GET request.
   * @param httpPostSamStatusCode The SAM code returned by the HTTP POST request.
   * @param httpsGetSamStatusCode The SAM code returned by the HTTPS GET request.
   * @param httpsPostSamStatusCode The SAM code returned by the HTTPS POST request.
   * @throws IOException If an error occurs while writing to the test report file.
   */
  private static void outputTestResult(
      String httpUrl,
      String httpsUrl,
      int rfc9110HttpResponseCode,
      int httpGetSamStatusCode,
      int httpPostSamStatusCode,
      int httpsGetSamStatusCode,
      int httpsPostSamStatusCode)
      throws IOException {
    String output =
        "\nRFC 9110 HTTP Code "
            + rfc9110HttpResponseCode
            + ": \n"
            + "  HTTP GET SAM Code: "
            + httpGetSamStatusCode
            + "\n"
            + "  HTTP POST SAM Code: "
            + httpPostSamStatusCode
            + "\n"
            + "  HTTPS GET SAM Code: "
            + httpsGetSamStatusCode
            + "\n"
            + "  HTTPS POST SAM Code: "
            + httpsPostSamStatusCode
            + "\n"
            + "  HTTP URL: "
            + httpUrl
            + "\n"
            + "  HTTPS URL: "
            + httpsUrl
            + "\n";
    Logger.LOG_INFO(output);
    FileAccessManager.appendStringToFile(TEST_REPORT_FILE, output);
  }
}
