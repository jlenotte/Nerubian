package nerubian.core;

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
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

        // The LOGGER.isDebugEnabled ensures that resultList
        // logging is not invoked only conditionally
        if (LOGGER.isDebugEnabled() && resultList.isEmpty())
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
    public String fireHttpGET(String link) throws IOException, BadResponseCodeException
    {

        // Init
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        StringBuilder response = new StringBuilder();

        // Request method
        conn.setRequestMethod("GET");

        // Request header
        conn.setRequestProperty("User-Agent", USER_AGENT);

        // Response code
        final int responseCode = conn.getResponseCode();
        LOGGER.info("Sending 'GET' request to URL : {}", url);
        LOGGER.info("Response code : {}", responseCode);

        // Check if response code is valid
        if (responseCode == 200)
        {
            LOGGER.info("HTTP GET request successful.");
            // Get the response via InputStream
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inLine;

            while ((inLine = br.readLine()) != null)
            {
                response.append(inLine);
            }
            br.close();
        }
        else
        {
            LOGGER.error("Fetching failed, bad response code : {}", responseCode);
            response.append("");
        }

        return response.toString();
    }


    /**
     * This method writes the result of the HTTP GET method on a file
     *
     * @param filePath takes a String in param
     * @param filePath takes an HttpURLConnection in param
     * @throws IOException throws an IOEx
     */
    public void writetHttpGetResult(String result, String filePath) throws IOException
    {
        try (FileWriter fw = new FileWriter(filePath))
        {
            try (BufferedWriter bw = new BufferedWriter(fw))
            {
                LOGGER.info("Saving result...");
                bw.write(result);
            }
        }
    }


    /**
     * Get metadata
     */
    public String getMetaData(String link) throws IOException
    {
        String description = null;
        String keywords = null;
        LOGGER.info("Fetching {} ...", link);
        try
        {
            // Get a document after parsing html from given url
            Document document = Jsoup.connect(link).timeout(600000).get();

            // Get description from document object
            description = document.select("meta[name=description]").get(0).attr("content");

            // Log it
            LOGGER.debug("Meta description : {}", description);

            // Get keywords from document objet
            keywords = document.select("meta[name=keywords]").first().attr("content");

            // Log it
            LOGGER.debug("Meta keywords : {}", keywords);
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
        }

        return description + keywords;
    }


    /**
     * Format Metadata for readability
     */
    public String[] formatMetaData(String result)
    {
        String[] list = result.split(",");

        // For each value of the list, trim it
        for (int i = 0; i < list.length; i++)
        {
            list[i] = list[i].trim();
            LOGGER.debug(list[i]);
        }

        return list;
    }

    /**
     * Write metadata
     */
    public void writeMetaData(String[] metadata, String fileName) throws IOException
    {
        try (FileWriter fw = new FileWriter(fileName))
        {
            try (BufferedWriter bw = new BufferedWriter(fw))
            {
                for (int i = 0; i < metadata.length; i++)
                {
                    bw.write(metadata[i] + "\n");
                }
            }
        }
    }

    /**
     * This method will remove HTML tags from a text file
     */
    public String removeHtmlTags(String filePath) throws IOException
    {
        String doc;
        try (Scanner in = new Scanner(new File(filePath)))
        {
            // Clean html tags with Jsoup
            LOGGER.info("Now cleaning HTML tags...");

            // Create a new file via the scanner
            String html = in.useDelimiter("\\A").next();
            doc = Jsoup.clean(html, "", Whitelist.none().addTags("br", "p"),
                new OutputSettings().prettyPrint(true));

            LOGGER.info("Tags cleaned successfully.");
        }
        return Jsoup.clean(doc, "", Whitelist.none(), new OutputSettings().prettyPrint(false));
    }


    /**
     * This method writes the cleaned up text into a file
     * @throws IOException throws IOE
     */
    public void writeCleanTextResult(String cleanHtml) throws IOException
    {
        try (FileWriter fw = new FileWriter(CLEAN_TEXT_FILE))
        {
            try (BufferedWriter bw = new BufferedWriter(fw))
            {
                // Write to file
                bw.write(cleanHtml);
            }
            LOGGER.info("Clean text written with success and available in project folder.");
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

    /**
     *
     */
}
