import java.io.IOException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import nerubian.core.Company;
import nerubian.core.Jobs;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APITest
{

    private static final Logger LOGGER = LoggerFactory.getLogger(APITest.class);

    /**
     * format the company names to Strings with no special characters and place it in the API link
     * will take a String and a list param and return a string.
     */
    @Test
    public void openCorpApiTest()
    {
        Jobs job = new Jobs();
        try
        {
            // Declare liste/vars
            List<Company> list = new ArrayList<>(job.scrapeCac40HtmlCssSelector("http://www.boursier.com/indices/composition/cac-40-FR0003500008,FR.html?tri=dcapi"));
            String queryVariable;

            // Foreach company, place the name in a string and place that string into the api link
            for (Company company : list)
            {
                queryVariable = company.getCompanyName();
                queryVariable = Normalizer.normalize(queryVariable, Normalizer.Form.NFD);
                String s = queryVariable.replaceAll("[^a-zA-Z0-9]", "");

                String openCorp = "https://api.opencorporates.com/v0.4/companies/search?q="
                    + s
                    + "&format=json";
                LOGGER.debug(openCorp);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * same method as above with params and return
     *
     * @param list list
     * @return string
     */
    public List<String> openCorpApi(List<Company> list) throws IOException
    {
        List<String> openCorpList = new ArrayList<>();
        String openCorpLink;
        try
        {
            String rawName;
            String query;

            // Foreach company, place the name in a string and place that string into the api link
            for (Company company : list)
            {
                rawName = company.getCompanyName();
                rawName = Normalizer.normalize(rawName, Form.NFD);
                query = rawName.replaceAll("[^a-zA-Z0-9]", "");
                openCorpLink = "https://api.opencorporates.com/v0.4/companies/search?q="
                    + query
                    + "&format=json";
                LOGGER.debug(openCorpLink);
                openCorpList.add(openCorpLink);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return openCorpList;
    }

    @Test
    public void methodTest() throws IOException
    {
        Jobs job = new Jobs();
        List<Company> list = new ArrayList<>(job.scrapeCac40HtmlCssSelector("http://www.boursier.com/indices/composition/cac-40-FR0003500008,FR.html?tri=dcapi"));
        try
        {
            String result = String.valueOf(openCorpApi(list));
            LOGGER.debug(result);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void compareCompanies() throws IOException
    {
        Jobs job = new Jobs();
        List<Company> list = new ArrayList<>(job.scrapeCac40HtmlCssSelector("http://www.boursier.com/indices/composition/cac-40-FR0003500008,FR.html?tri=dcapi"));
        List<String> result = openCorpApi(list);
        String fileName;
        int i = 0;

        for (String s : result)
        {
            try
            {
                fileName = "JSON/OpenCorp_" + i + ".json";
                String httpGet = job.fireHttpGET(s);
                job.writetHttpGetResult(httpGet, fileName);
            }
            catch (IOException e)
            {
                LOGGER.error(e.getMessage());
            }
            i++;
        }
    }
}
