import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Links
{

    private HashSet<String> links;
    private final static Logger LOGGER = LoggerFactory.getLogger(Links.class);


    Links()
    {
        links = new HashSet<String>();
    }

    void getPageLinks(String URL) throws IOException
    {
        DataReader dr = new DataReader();
        File file = new File("data.csv");
        List<URLObject> list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            URL = list.toString();
            // Check if the URLs have already been crawled
            if (links.contains(URL))
            {
                LOGGER.debug("Checking if URL's have been crawled...");
                try
                {
                    // if not, add the index
                    if (links.add(URL))
                    {
                        LOGGER.debug(URL);
                    }

                    // Fetch HTML code
                    Document doc = Jsoup.connect(URL).get();

                    // Parse HTML to extract links to other URLS
                    Elements linksOnPage = doc.select("a[href]");

                    // For each extracted URL, repeat the operation
                    for (Element page : linksOnPage)
                    {
                        LOGGER.debug("Repeating operation for the current page...");
                        getPageLinks(page.attr("abs:href"));
                    }
                }
                catch (IOException e)
                {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }
}
