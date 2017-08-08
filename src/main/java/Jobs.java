import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.text.Document;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;

public class Jobs
{

    private final static Logger LOGGER = LoggerFactory.getLogger(Jobs.class);
    private final String USER_AGENT = "Mozilla/5.0";

    /**
     *
     * This method will convert a Company
     * object into a useable URL
     *
     */
    public List<URLObject> convertURL(List<Company> list)
    {
        List<URLObject> resultList = new ArrayList<>();

        // Extract URL out of each Company
        for (Company company : list)
        {
            URLObject url = new URLObject(company.getURL());
            resultList.add(url);
        }
        LOGGER.debug(resultList.toString());
        return resultList;
    }

    /**
     *
     * This method will fire HTTP GET method
     *
     */
    public void fireHttpGET(List<URLObject> urls, String filePath, String link) throws Exception
    {

        try (FileWriter fw = new FileWriter(filePath))
        {
            // Init
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Request method
            conn.setRequestMethod("GET");

            // Request header
            conn.setRequestProperty("User-Agent", USER_AGENT);

            // Response code
            int responseCode = conn.getResponseCode();
            LOGGER.debug("\n Sending 'GET' request to URL : " + url);
            LOGGER.debug("Response code : " + responseCode);

            // Save result in a file
            LOGGER.info("Saving result...");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuffer response = new StringBuffer();
            String inLine;

            while ((inLine = br.readLine()) != null)
            {
                response.append(inLine);
                bw.write(inLine);
            }
            br.close();

            // Log result
            LOGGER.debug(response.toString());
            LOGGER.info("GET Request successful. Results saved in a file.");
        }
        catch (IOException e)
        {
            LOGGER.error("Error while performing the GET request.");
            LOGGER.error("Exception message : " + e.getMessage());
        }
    }

    /**
     *
     * This method will take the text file
     * data and filter it of any html tag
     * and write the result into a text file
     *
     */
    public void removeHtmlTags() throws FileNotFoundException
    {
        try
        {
            // Init
            LOGGER.info("Now cleaning HTML tags...");
            String html = new Scanner(new File("result.txt")).useDelimiter("\\A").next();
            String cleanText = Jsoup.parse(html).text();
            LOGGER.debug(cleanText);

            /*
             * To implement : if the text contains another link,
             * then crawl that page too, and if that one link also
             * contains links, crawl them too, with a maximum depth
             * of links to dig in.
             */

            // Write to file
            FileWriter fw = new FileWriter("cleanHtml.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cleanText);
            bw.close();

            // Log
            LOGGER.info("Tags cleaned successfully.");
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
        }
    }

    void crawl(List<URLObject> urls) throws Exception
    {
        for (URLObject url : urls)
        {
            //fireHttpGET(urls);
            removeHtmlTags();
        }
    }

    public void writeResult(String filePath, HttpURLConnection conn) throws IOException
    {
        try (FileWriter fw = new FileWriter(filePath))
        {
            LOGGER.info("Saving result...");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuffer response = new StringBuffer();
            String inLine;

            while ((inLine = br.readLine()) != null)
            {
                response.append(inLine);
                bw.write(inLine);
            }
            br.close();

            // Log result
            LOGGER.debug(response.toString());
            LOGGER.info("GET Request successful. Results saved in a file.");
        }
        catch (IOException e)
        {

        }
    }
}
