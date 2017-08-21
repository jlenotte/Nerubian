package nerubian.core;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Jobs.class);

    public static void main(String[] args) throws Exception
    {
        // Init strings used to store results & params
        File input = new File("data_new.csv");
        String rawHttpFile;
        String metaDataFile;
        String cleanTextFile;
        String link;

        // Read CSV input
        DataReader dr = new DataReader();


        // Init a Jobs class
        Jobs job = new Jobs();

        // Loop through the list and launch methods
        for (Company company : dr.readFile(input))
        {
            try
            {
                // Give a new name, each company should have its own report
                rawHttpFile = "results/HTTP_GET_RESULT_" + company.getCompanyName() + ".txt";
                metaDataFile = "results/METADATA_" + company.getCompanyName() + ".txt";
                cleanTextFile = "results/CLEAN_TEXT_" + company.getCompanyName() + ".txt";
                link = company.getURL();

                // Fire HTTP GET request
                String result = job.fireHttpGET(company.getURL());

                // Write HTTP GET result in a file
                job.writetHttpGetResult(result, rawHttpFile);

                // Clean the GET result of HTML tags
                String cleanedHtml = job.removeHtmlTags(rawHttpFile);

                // Write clean text in a .txt file
                job.writeCleanTextResult(cleanedHtml, cleanTextFile);

                // Get metadata
                String resultat = job.getMetaData(link);
                String[] metadata = job.formatMetaData(resultat);
                job.writeMetaData(metadata, metaDataFile);
                LOGGER.info("GIT TEST");
            }
            catch (Exception e)
            {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
