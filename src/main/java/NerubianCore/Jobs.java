package NerubianCore;

import ch.qos.logback.classic.Level;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Jobs contains all the core processing methods for the web crawler
 */
public class Jobs
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Jobs.class);
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String CLEAN_TEXT_FILE = "cleanText.txt";

    /**
     * This method will convert a NerubianCore.Company object into a useable URL
     */
    public List<URLObject> convertURL(List<Company> list)
    {
        List<URLObject> resultList = new ArrayList<>();

        // Extract URL out of each NerubianCore.Company
        for (Company company : list)
        {
            URLObject url = new URLObject(company.getURL());
            resultList.add(url);
        }
        if (resultList.isEmpty())
        {
            LOGGER.warn("WARNING : The list is empty.");
        }
        else
        {
            LOGGER.debug(resultList.toString());
        }
        return resultList;
    }

    /**
     * This method will fire HTTP GET method
     */
    public String fireHttpGET(String link) throws IOException
    {

        // Init
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Request method
        conn.setRequestMethod("GET");

        // Request header
        conn.setRequestProperty("User-Agent", USER_AGENT);

        // Response code
        final int responseCode = conn.getResponseCode();
        LOGGER.info("Sending 'GET' request to URL : {}", url);
        LOGGER.info("Response code : {}", responseCode);

        // Check if response code is valid
        if (responseCode != 200)
        {
            LOGGER.error("Bad response code : {}", responseCode);
        }
        else
        {
            LOGGER.info("HTTP GET request successful.");
        }

        // Get the response via InputStream
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inLine;

        while ((inLine = br.readLine()) != null)
        {
            response.append(inLine);
        }
        br.close();

        return response.toString();
    }

    /**
     * This method will remove HTML tags from a text file
     */
    public String removeHtmlTags(String doc) throws IOException
    {
        try (Scanner in = new Scanner(new File("HTTP_GET_RESULT.txt")))
        {
            // Clean html tags with Jsoup
            LOGGER.info("Now cleaning HTML tags...");

            // Create a new file via the scanner
            String html = in.useDelimiter("\\A").next();
            doc = Jsoup.clean(html, "", Whitelist.none().addTags("br", "p"),
                new OutputSettings().prettyPrint(true));

            LOGGER.debug(String.valueOf(doc));
            LOGGER.info("Tags cleaned successfully.");
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
        }
        return Jsoup.clean(doc, "", Whitelist.none(), new OutputSettings().prettyPrint(false));
    }


    /**
     * This method writes the result of the HTTP GET method on a file
     * @param filePath takes a String in param
     * @param conn takes an HttpURLConnection in param
     * @throws IOException throws an IOEx
     */
    public void getHttpGetResult(String filePath, HttpURLConnection conn) throws IOException
    {
        try (FileWriter fw = new FileWriter(filePath))
        {
            try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream())))
            {
                LOGGER.info("Saving result...");
                ;
                BufferedWriter bw = new BufferedWriter(fw);
                StringBuilder response = new StringBuilder();
                String inLine;

                while ((inLine = br.readLine()) != null)
                {
                    response.append(inLine);
                    bw.write(inLine);
                }

                br.close();
                bw.close();

                // Log result
                LOGGER.info("GET Request successful. Results saved in a file.");
                LOGGER.debug(response.toString());
            }
            catch (IOException e)
            {
                LOGGER.error(e.getMessage());
            }
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * This method writes the cleaned up text into a file
     *
     * @param doc Takes a String as param
     * @throws IOException throws IOE
     */
    public void getCleanTextResult(String doc) throws IOException
    {
        try (FileWriter fw = new FileWriter(CLEAN_TEXT_FILE))
        {
            try (BufferedWriter bw = new BufferedWriter(fw))
            {
                // Write to file
                bw.write(String.valueOf(doc));
            }
            LOGGER.info("Clean text written with success and available in project folder.");
        }
        catch (IOException e)
        {
            LOGGER.error("Something went wrong while writing the result on a file : {}",
                e.getMessage());
        }
    }

    /**
     * This method will open up the result file
     */
    public void openFile()
    {
        try
        {
            LOGGER.info("Opening up file...");
            if ((new File(CLEAN_TEXT_FILE)).exists())
            {
                Desktop.getDesktop().open(new File(CLEAN_TEXT_FILE));
            }
            LOGGER.info("File opened.");
        }
        catch (IOException e)
        {
            LOGGER.error("Error while trying to open the result file. Exception : %s", e);
        }
    }
}
