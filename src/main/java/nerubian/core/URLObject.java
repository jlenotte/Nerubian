package nerubian.core;

public class URLObject
{

    // Attributes
    private String url;

    // Construct
    public URLObject()
    {
        this.url = null;
    }

    public URLObject(String url)
    {
        this.url = url;
    }

    // Getters & Setters

    public String getURL()
    {
        return url;
    }

    public void setURL(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "url : " + url + "\n";
    }
}
