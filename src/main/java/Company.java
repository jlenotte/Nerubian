public class Company
{
    // Attributes
    private String companyName;
    private String URL;

    // Empty constructor
    Company()
    {
        this.companyName = null;
        this.URL = null;
    }

    // Constructor
    Company(String companyName, String URL)
    {
        this.companyName = companyName;
        this.URL = URL;
    }

    // Getters & Setters
    String getCompanyName()
    {
        return companyName;
    }

    void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    String getURL()
    {
        return URL;
    }

    void setURL(String URL)
    {
        this.URL = URL;
    }

    @Override
    public String toString()
    {
        return "Name : " + companyName +
            ", URL : '" + URL + "\n";
    }
}
