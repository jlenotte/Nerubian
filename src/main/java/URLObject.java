public class URLObject
{
    // Attributes
    String URL;

    // Construct
    URLObject()
    {
        this.URL = null;
    }

    URLObject(String URL)
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
