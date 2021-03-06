import nerubian.core.Jobs;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Test;

public class CrawlerTest
{

    @Test
    public void testHttpGet() throws Exception
    {
        String link = "https://www.ovh.com/fr/";
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Jobs job = new Jobs();

        job.fireHttpGET("https://www.ovh.com/fr/");
//        job.writetHttpGetResult("HTTP_GET_RESULT.txt");
    }

    @Test
    public void removeHtmlTagsTest() throws IOException
    {
        String link = "https://www.ovh.com/fr/";
        String doc = "";
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Jobs job = new Jobs();

        String cleanedHtml = job.removeHtmlTags(doc);
        job.writeCleanTextResult(cleanedHtml, "cleanHtml");
    }

    @Test
    public void getMetaData() throws IOException
    {
        String link = "https://www.jetbrains.com/";
        Jobs job = new Jobs();

        String resultat = job.getMetaData(link);
        job.formatMetaData(resultat);
    }

    @Test
    public void scrapeHtml() throws IOException
    {
        Jobs job = new Jobs();
        String link = "http://www.boursier.com/indices/composition/cac-40-FR0003500008,FR.html?tri=dcapi";
        try
        {
            job.scrapeCac40HtmlCssSelector(link);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
