## What is JSONProxy?

[JSONProxy](https://github.com/slopjong/JSONProxy) is a Java-based HTTP proxy to load a JSON file from another server.

## When do I need such a thing?

You need it if you have to deal with the [same-origin policy](http://en.wikipedia.org/wiki/Same_origin_policy) (SOP). Or if a shared host is blocking all traffic on non-standard ports then this proxy is handy too.

## Do I have to install it?

No but you can. JSONProxy is hosted on a [Heroku](http://heroku.com) server accessible on [http://jsonproxy.herokuapp.com](http://jsonproxy.herokuapp.com).
    
## How do I install it?

TODO: explain how to download and compile the source. Also how to deploy and run it.

## How do I use it?

Now it's time for some code.

First both examples make sense to bypass the SOP whereas the latter makes sense to bypass a firewall because cURL doesn't have to deal with SOP.

### jQuery

	// JSON available on port 80 on the remote host
	var json_url = "http://openspace.slopjong.de/directory.json";
	var api_key = "52fffef6c49490f912e8ce3b99c4679bc85af8d0278abb5ca4a7b055cf54c2d2";
	
	jQuery.getJSON("http://jsonproxy.herokuapp.com?api="+ api_key +"&url="+ json_url,
		function(data){
			// something with the loaded JSON file
		}
	);

### PHP

	// JSON available on port 80 on the remote host
	
	$json_url = "http://openspace.slopjong.de/directory.json";
	$api_key = "52fffef6c49490f912e8ce3b99c4679bc85af8d0278abb5ca4a7b055cf54c2d2";
	
	$proxy_call_url = "http://jsonproxy.herokuapp.com?api=" . $api_key . "&url=". $json_url;
	
	$ch = curl_init($proxy_call_url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	$data = curl_exec($ch);
	
	
### Bash

	// JSON available on port 8000 on the remote host,
	// in this case the proxy works as a port mapper
	
	json_url=http://openspace.slopjong.de:8000/directory.json
	api_key=52fffef6c49490f912e8ce3b99c4679bc85af8d0278abb5ca4a7b055cf54c2d2
	
	proxy_call_url="http://jsonproxy.herokuapp.com?api=${api_key}&url=${json_url}"
	curl --silent $proxy_call_url
