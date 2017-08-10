package nerubian.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Init strings used to store results & params
        int i = 0;
        File input = new File("data.csv");
        String filePath;
        String fileName;
        String link;

        // Read CSV input
        DataReader dr = new DataReader();


        // Init a Jobs class
        Jobs job = new Jobs();

        // Loop through the list and launch methods
        for (Company company : dr.readFile(input))
        {
            // Give a new name, each company should have its own report
            filePath = "HTTP_GET_RESULT_" + company.getCompanyName() + ".txt";
            fileName = "METADATA_" + company.getCompanyName() + ".txt";
            link = company.getURL();

            // Fire HTTP GET request
            String result = job.fireHttpGET(company.getURL());
            job.writetHttpGetResult(result, filePath);

            // Write HTTP GET result in a file
            job.writetHttpGetResult(result, filePath);

            // Clean the GET result of HTML tags
            String cleanedHtml = job.removeHtmlTags(filePath);

            // Write clean text in a .txt file
            job.writeCleanTextResult(cleanedHtml);

            // Get metadata
            String resultat = job.getMetaData(link);
            String[] metadata = job.formatMetaData(resultat);
            job.writeMetaData(metadata, fileName);
        }

        /*
        // Fire the HTTP GET request
        String result = job.fireHttpGET(link);

        // Write HTTP GET result into a file
        job.writetHttpGetResult(result, filePath);

        // Clean the GET result of HTML tags
        String cleanedHtml = job.removeHtmlTags(filePath);

        // Write clean text in a .txt file
        job.writeCleanTextResult(cleanedHtml);

        // Get metadata
        String resultat = job.getMetaData(link);
        String[] metadata = job.formatMetaData(resultat);
        job.writeMetaData(metadata);

        // Open the file to check results
        job.openFile();
        */
    }
}
