#ifndef URL_H_
#define URL_H_
#include<string>
#include<iostream>
using namespace std;
namespace java
{
/**
 * A simple URL class that parses the full URL into host, port and path.
 * @author Jon Tayler
 */
class URL
{
public:
	URL(string url);
	virtual ~URL();
	string getHost();
	int getPort();
	string getPath();
private:	
	string url;
	int port;
	string path;
	string host;
};

}

#endif /*URL_H_*/
