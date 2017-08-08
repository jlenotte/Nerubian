import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Init variables and objects
        File file = new File("result.txt");
        String html = String.valueOf(file);
        File inData = new File("data.csv");
        DataReader dr = new DataReader();
        Jobs job = new Jobs();
        List<Company> list = new ArrayList<>(dr.readFile(inData));


        // Method calls
        List<URLObject> urlObjectList = job.convertURL(list);
        job.fireHttpGET(urlObjectList, "result.txt", "https://www.ovh.com/fr/");
        job.removeHtmlTags();
        //job.crawl(urlObjectList);

        // Not my code
        // new Links().getPageLinks("https://github.com/");

    }
}
