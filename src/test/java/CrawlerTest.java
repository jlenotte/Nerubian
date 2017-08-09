import NerubianCore.Jobs;
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
        job.getHttpGetResult("HTTP_GET_RESULT.txt", conn);
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
        job.getCleanTextResult(cleanedHtml);
    }
}
