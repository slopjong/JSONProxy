package de.slopjong.openspace.proxy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The request processor instantiated by each HTTP request.
 * 
 * @author slopjong
 *
 */
public class Worker
{		
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
		
	/**
	 * Constructor which makes the request and response objects global
	 * within this class. After checking if the request comes from an
	 * allowed server it reads the 'url' GET parameter and loads the
	 * JSON from that URL. Finally it prints it to the output.
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	Worker(HttpServletRequest req, HttpServletResponse resp) 
	throws IOException
	{
		
		request = req;
		response = resp;			
	}
	
	public void run() 
	throws IOException
	{				
		String url = request.getParameter("url");
		printOutput(getData(url));
	}
	
	/**
	 * Loads the space JSON by running a system command with cURL used.
	 * 
	 * @param url
	 * @return
	 */
	private String getData(String url)
	{        	
		String output = ""; 
		
		try 
		{ 
			/**
			 * --silent   => Don't show progress meter or error messages. Makes Curl mute.
			 * --location => Follow redirects (301, 302)
			 * --max-time => Don't execute the curl command(s) longer than x seconds
			 */
	    	Process p = Runtime.getRuntime().exec("curl --silent --location --max-time 10 "+ url); 
	    	p.waitFor(); 
	    	BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
	    	String line=reader.readLine(); 
	    	while(line!=null) 
	    	{ 
		    	output += (line); 
		    	line=reader.readLine(); 
	    	} 
		} 
		
		catch(IOException e) 
		{ 
			output = "{\"error\":\"Something very bad happend :-( " 
							+ e.getMessage() + "\"}"; 
		} 
		catch(InterruptedException e) 
		{ 
			output = "{\"error\":\"Something very bad happend :-( " 
					+ e.getMessage() + "\"}";
		} 
		
		return output;
	}
	
	/**
	 * Outputs the JSON or error message depending on the space's server.
	 * 
	 * @param output
	 * @throws IOException
	 */
	private void printOutput(String output) 
	throws IOException
	{    		
		/*
		 * TODO: maybe return an object as the first-stage (php) proxy does?
		 * 
		 *    $response = new StdClass();
		 *    $response->status = $status;
		 *    $response->length = $contentLength;
		 *    $response->url = $info['url'];
		 *    $response->content = $data;
		 */
		
		response.setHeader("Content-type", "application/json");
		response.getWriter().print(output + "\n");
	}
	
	/**
	 * Little helper to see the headers. For debugging purpose only.
	 * 
	 * @param req
	 * @return
	 */
	private String headerList(HttpServletRequest req)
	{
		String output = "";
	
		Enumeration headers = req.getHeaderNames();
		String header, value;
		while(headers.hasMoreElements()) {
			header = headers.nextElement().toString();
			value = req.getHeader(header);
			output += header +": "+ value +";\n";
		}
		
		return output;
	}
}	