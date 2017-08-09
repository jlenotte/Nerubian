package NerubianCore;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataReader
{

    private static final Logger LOGGER = LoggerFactory.getLogger(DataReader.class);

    public List<Company> readFile(File inFile) throws IOException
    {
        ArrayList<Company> list = new ArrayList<>();

        // Try with res (CSV file reader that opens the file, separates values with commas)
        try(CSVReader reader = new CSVReader(new FileReader(String.valueOf(inFile)), ','))
        {
            LOGGER.debug("Reading CSV file...");
            String[] nextLine;
            URLObject urlObject = new URLObject();
            String link = urlObject.getURL();

            /*Read each line while next line isnt null,
            and place each value in an index*/
            while ((nextLine = reader.readNext()) != null)
            {
                String companyName = nextLine[0];
                link = nextLine[1];
                Company c = new Company(companyName, link);
                list.add(c);
            }
            LOGGER.debug("File was read successfully.");
        }
        catch (IOException e)
        {
            LOGGER.debug(e.getMessage());
        }
        return list;
    }
}
