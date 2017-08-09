package NerubianCore;

public class URLObject
{

    // Attributes
    private String URL;

    // Construct
    public URLObject()
    {
        this.URL = null;
    }

    public URLObject(String URL)
    {
        this.URL = URL;
    }

    // Getters & Setters

    public String getURL()
    {
        return URL;
    }

    public void setURL(String URL)
    {
        this.URL = URL;
    }

    @Override
    public String toString()
    {
        return "URL : " + URL + "\n";
    }
}
