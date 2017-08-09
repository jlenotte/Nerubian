package NerubianCore;

import java.net.HttpURLConnection;
import java.net.URL;

public class Main
{

    public static void main(String[] args) throws Exception
    {
        // Init strings used to store results & params
        String doc = "";
        String filePath = "HTTP_GET_RESULT.txt";

        // Init an URL & HTTP connection
        String link = "http://www.w3resource.com/";
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Init a Jobs class
        Jobs job = new Jobs();

        // HTTP GET
        job.fireHttpGET("http://www.w3resource.com/");

        // HTTP GET result into a file
        job.getHttpGetResult(filePath, conn);

        // Strip the GET result of HTML tags
        String cleanedHtml = job.removeHtmlTags(doc);

        // Clean text is written in a .txt file
        job.getCleanTextResult(cleanedHtml);
    }
}
