import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class CrawlerTest
{

    @Test
    public void testHttpGet() throws Exception
    {
        DataReader dr = new DataReader();
        Jobs job = new Jobs();
        File inData = new File("data.csv");
        List<Company> list = new ArrayList<>(dr.readFile(inData));
        List<URLObject> urlObjectList = job.convertURL(list);

        job.fireHttpGET(urlObjectList, "result.txt", "https://www.ovh.com/fr/");
    }
}
