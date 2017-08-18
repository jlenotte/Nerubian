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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Jobs contains all the core processing
 * methods for the web crawler & result writing
 */
public class Jobs
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Jobs.class);
    private static final String USER_AGENT = "Mozilla/5.0";


    /**
     * This method will convert a NerubianCore.Company object into a useable URL
     * @deprecated unused method
     */
    @Deprecated
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
     * Scrape html table elements
     * @return returns a String of
     * @throws IOException throws IOE
     */
    public List<Company> scrapeCac40HtmlCssSelector(String link) throws IOException
    {
        // Mk document and connect to a link
        Document doc = Jsoup.connect(link).get();
        String companyName = "";
        String finalLink = "";
        List<Company> list = new ArrayList<>();

        // Loop through elements of the page to find the desired html tag's data
        for (Element table : doc.select("table > tbody > tr > td > a"))
        {
            Elements tds = table.select("a");
            String scrapedLink = "http://www.boursier.com" + tds.attr("href");

            // Repeat the operation with a new Document
            Document doc2 = Jsoup.connect(scrapedLink).get();

            for (Element nav : doc2.select("nav > ul > li > a"))
            {
                if ("Société".equals(nav.text()))
                {
                    Elements lis = nav.select("a");
                    String scrapedLink2 = "http://www.boursier.com"
                        + lis.attr("href");

                    // Repeat the operation again
                    Document doc3 = Jsoup.connect(scrapedLink2).get();

                    for (Element addr : doc3.select("address"))
                    {
                        for (Element strong : addr.select("strong"))
                        {
                            companyName = strong.text();
                        }

                        for (Element a3 : addr.select("a"))
                        {
                            Elements address = a3.select("a");
                            String scrapedLink3 = address.attr("href");

                            // If href equals to the tag's text, get the link in a new var
                            // Use .contains instead of .equals for more details
                            if (scrapedLink3.equals(a3.text()))
                            {
                                Company c = new Company(companyName, scrapedLink3);
                                list.add(c);
                                LOGGER.info("Company : {} {}", companyName, scrapedLink3);
                            }
                        }
                    }
                }
            }
        }
        LOGGER.debug("{} {}", companyName, finalLink);
        writeScrapedData(list);
        return list;
    }


    /**
     * write result into existing csv
     * @throws IOException throws IOE
     */
    public void writeScrapedData(List<Company> list) throws IOException
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data_new.csv", false)))
        {
            for (Company c : list)
            {
                bw.write(c.getCompanyName() + "," + c.getURL());
                bw.newLine();
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
    }


    /**
     * This method will fire HTTP GET method
     * @param link takes an url and gets the response code and connection status
     * @return returns a String
     * @throws IOException throws IOE
     */
    public String fireHttpGET(String link) throws IOException
    {
        LOGGER.info("Firing HTTP GET...");

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
        LOGGER.info("Sending 'GET' request to URL : {} ...", url);
        LOGGER.info("Response code : {}", responseCode);

        // Check if response code is valid
        if (responseCode == 200)
        {
            LOGGER.info("HTTP GET request successful.");
            // Get the response via InputStream
            BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
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
            throw new IOException();
        }
        return response.toString();
    }


    /**
     * This method writes the result of the HTTP GET method on a file
     * @param result takes the result of fireHttpGet as a String in param
     * @param filePath takes an HttpURLConnection in param
     * @throws IOException throws an IOEx
     */
    public void writetHttpGetResult(String result, String filePath) throws IOException
    {
        try (FileWriter fw = new FileWriter(filePath))
        {
            try (BufferedWriter bw = new BufferedWriter(fw))
            {
                LOGGER.info("Saving HTML...");
                bw.write(result);
            }
        }
    }


    /**
     * Get metadata
     * @param link takes an url and grabs the metadata
     * @return returns a String
     * @throws IOException throws IOE
     */
    public String getMetaData(String link) throws IOException
    {
        String description = null;
        String keywords = null;
        LOGGER.info("Fetching Metadata from {} ...", link);

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


            if (keywords == null)
            {
                LOGGER.warn("No Metadata found for {}", link);
            }
            else
            {
                LOGGER.debug("Meta keywords : {}", keywords);
            }
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
            LOGGER.error("Metadata may not be defined or available for {}", link);
        }

        return description + keywords;
    }


    /**
     * Format Metadata for readability
     * @param result uses the result from getMetaData to format it in a readable form
     * @return returns a String array
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
     * @param fileName takes the file's relative path
     * @param metadata takes the metadata to process it and write it in a file formatted
     * @throws IOException throws IOE
     */
    public void writeMetaData(String[] metadata, String fileName) throws IOException
    {
        LOGGER.info("Saving Metadata...");
        try (FileWriter fw = new FileWriter(fileName))
        {
            try (BufferedWriter bw = new BufferedWriter(fw))
            {
                for (String aMetadata : metadata)
                {
                    bw.write(aMetadata + "\n");
                }
                LOGGER.info("Metadata saved written with success.");
            }
        }
    }


    /**
     * This method will remove HTML tags from a text file
     * @param filePath takes the file's relative path
     * @return String returns a string
     * @throws IOException throws IOE
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
     * @param cleanHtml takes the clean HTML file to write it in a file
     * @param cleanTextFile takes the clean text file to use it as the filewriter's param
     * @throws IOException throws IOE
     */
    public void writeCleanTextResult(String cleanHtml, String cleanTextFile) throws IOException
    {
        LOGGER.info("Saving clean text...");
        try (FileWriter fw = new FileWriter(cleanTextFile))
        {
            try (BufferedWriter bw = new BufferedWriter(fw))
            {
                // Write to file
                bw.write(cleanHtml);
            }
            LOGGER.info("Clean text written with success.");
        }
    }


    /**
     * This method will open up the result file
     * @param filePath takes the file's relative path
     * @deprecated unused method
     * @throws IOException throws IOE
     */
    @Deprecated
    public void openFile(String filePath)
    {
        try
        {
            LOGGER.info("Opening up file...");
            if ((new File(filePath)).exists())
            {
                Desktop.getDesktop().open(new File(filePath));
            }
            LOGGER.info("File opened.");
        }
        catch (IOException e)
        {
            LOGGER.error("Error while trying to open the result file. Exception : %s", e);
        }
    }

}
