package nerubian.core;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Init strings used to store results & params
        String filePath = "HTTP_GET_RESULT.txt";
        String link = "https://www.ovh.com/fr/";

        // Init a Jobs class
        Jobs job = new Jobs();

        // Fire the HTTP GET request
        String resulat1 = job.fireHttpGET(link);

        // Write HTTP GET result into a file
        job.writetHttpGetResult(resulat1, filePath);

        // Clean the GET result of HTML tags
        String cleanedHtml = job.removeHtmlTags(filePath);

        // Write clean text in a .txt file
        job.writeCleanTextResult(cleanedHtml);

        // Open the file to check results
        job.openFile();
    }
}
