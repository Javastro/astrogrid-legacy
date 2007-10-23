#include "URL.h"

namespace java
{

URL::URL(string url1)
{
	url = url1;
	string::size_type colonslashslash = url.find("://");
	if ( colonslashslash == string::npos) 
	{
		cerr << "Can only deal with URLs that begin protocol://";
		throw "No known URL protocol";	
	}
	string::size_type searchfrom = (colonslashslash==string::npos) ? 0 : colonslashslash+3;
	string::size_type firstslash = url.find("/", searchfrom);
	string fullhost = url.substr(colonslashslash+3, firstslash-colonslashslash-3);
	string::size_type colon = fullhost.find(":",searchfrom);
	if (colon==string::npos) 
	{
		port = 80;
		host = fullhost;
	}
	else 
	{
		host = fullhost.substr(0, colon);
		string portNumber = fullhost.substr(colon+1,1000);
		port = atoi(portNumber.c_str());	
	}
	
	path = url.substr(firstslash);
}

URL::~URL()
{
}

string URL::getHost() 
{
	return host;
	
}

int URL::getPort() 
{
	return port;
}

string URL::getPath()
{
	return path;
}
}
