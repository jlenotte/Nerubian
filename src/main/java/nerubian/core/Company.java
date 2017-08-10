package nerubian.core;

public class Company
{

    // Attributes
    private String companyName;
    private String url;

    // Empty constructor
    public Company()
    {
        this.companyName = null;
        this.url = null;
    }

    // Constructor
    public Company(String companyName, String url)
    {
        this.companyName = companyName;
        this.url = url;
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
        return url;
    }

    void setURL(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "Name : " + companyName +
            ", URL : '" + url + "\n";
    }
}
