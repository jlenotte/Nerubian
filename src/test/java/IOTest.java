import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOTest
{

    private final static Logger LOGGER = LoggerFactory.getLogger(IOTest.class);

    @Test
    public void readFile() throws IOException
    {
        ArrayList<Company> list = new ArrayList<>();
        File inputFile = new File("data.csv");
        try(CSVReader reader = new CSVReader(new FileReader(String.valueOf(inputFile)), ','))
        {
            LOGGER.debug("Reading CSV file...");
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null)
            {
                String companyName = nextLine[0];
                String companyURL = nextLine[1];

                Company c = new Company(companyName, companyURL);
                list.add(c);
            }
            LOGGER.debug("File was read successfully.");
            LOGGER.debug("Liste : " + list);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while trying to read the CSV file.");
            LOGGER.error("Exception message : " + e.getMessage());
        }

    }
}