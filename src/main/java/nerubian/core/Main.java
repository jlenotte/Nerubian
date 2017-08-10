package nerubian.core;

import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Init strings used to store results & params
        int i = 0;
        String filePath = "HTTP_GET_RESULT" + i++ + ".txt";
        String link = "https://www.ovh.com/fr/";

        // Init a Jobs class
        Jobs job = new Jobs();

        // Fire the HTTP GET request
        String result = job.fireHttpGET(link);

        // Write HTTP GET result into a file
        job.writetHttpGetResult(result, filePath);

        // Clean the GET result of HTML tags
        String cleanedHtml = job.removeHtmlTags(filePath);

        // Write clean text in a .txt file
        job.writeCleanTextResult(cleanedHtml);

        // Open the file to check results
        job.openFile();
    }
}
