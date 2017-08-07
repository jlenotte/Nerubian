import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
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
    List<URLObject> convertURL(List<Company> list)
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
    void fireHttpGET() throws Exception
    {
        try(FileWriter fw = new FileWriter("result.txt"))
        {
            String link = "https://www.ovh.com/fr/";

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
            LOGGER.debug("Saving result...");
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
            LOGGER.debug("GET Request successful. Results saved in a file.");
        }
        catch (IOException e)
        {
            LOGGER.error("Error while performing the GET request.");
            LOGGER.error("Exception message : " + e.getMessage());
        }
    }

    /**
     *
     * This method will take the filtered
     * data and write it into a text file
     *
     */
/*    void removeHtmlTags()
    {
        try
        {
            // Init
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader("result.txt"));
            FileWriter fw = new FileWriter("cleanHtml.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
                bw.write(line);
            }
            String noHtml = sb.toString().replaceAll("\\<.*?>","");
            LOGGER.debug(noHtml);
            LOGGER.debug("HTML tags removed successfully.");
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
    }*/

    void removeHtmlTags()
    {
        File file = new File("result.txt");
        String html = String.valueOf(file);
        String cleanText = Jsoup.parse(html).text();
        LOGGER.debug(cleanText);
    }
}
