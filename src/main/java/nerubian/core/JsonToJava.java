package nerubian.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonToJava
{

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonToJava.class);

    /**
     * Convert Json to POJO
     *
     * @throws IOException throws IOE
     */
    private void run() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            // Convert JSON to String from file to Object
            Company c = mapper.readValue(new File("company.json"), Company.class);
            LOGGER.debug(c.toString());

            // Convert JSON String to Object
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
        }
    }
}
