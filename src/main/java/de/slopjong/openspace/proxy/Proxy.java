package de.slopjong.openspace.proxy;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Proxy web server for OpenSpaceLint.
 * 
 * @author slopjong
 *
 */
public class Proxy extends HttpServlet 
{
	private String _apikey = "";

	/**
	 * Initializes the web server
	 */
	public Proxy()
	{
		// On heroku ENV variables must be used!
		// Properties files as configuration files
		// cause trouble there.
		//
		// see https://devcenter.heroku.com/articles/config-vars
		_apikey = System.getenv("API_KEY");
		
		if(null == _apikey)
			_apikey = "";
		
		/*
		// This is commented out because of heroku which crashes.
		// Offline it worked (the path must be adapted yet).
		if( null==_apikey || _apikey.isEmpty())
		{
			// the proxy configurations
			Properties properties;
			
			try 
			{
				properties = ResourceUtils
						.readResouceProperties("config.properties");
				
				// the api key used to restrict the usage of this
				// second-stage proxy to people that shouldn't use it
				_apikey = properties.getProperty("api.key");
				
			} catch (IOException e) 
			{
				// TODO DO SOME LOGGING
			}
		}*/
	}
	
    /**
     * Processes the requests by delegating them to a new worker.
     * Too lazy this guy to do the job himself. Tzzz ...
     * 
     * ... at least he closes the door if somebody unknown is there  ...
     */
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException 
    {				
		// the api key sent by a privileged user
		String apikey = req.getParameter("api");
		
		// if both keys match then process the request
    	if( _apikey.equals(apikey))
    	{
    		(new Worker(req, resp)).run();
    	}
    }
}
