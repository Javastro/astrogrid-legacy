/*
* A simple AR client based on the xml-rpc interface
* Uses modified version of http://xmlrpcpp.sourceforge.net/ which is nice and simple, and works
*/

#include "AR.h"


// TODO need to find the best cross platform solution for this...
#ifdef _WINDOWS
	const char * HOMEVAR = "USERPROFILE";
#else
	const char * HOMEVAR = "HOME";
#endif



namespace astrogrid
{

AR::AR()
{
	server=0;
	refresh();
}

AR::~AR()
{
	if (server) delete server;
}

bool AR::execute(const string& command, XmlRpcValue& args, XmlRpcValue& results)
{
//	cerr << command;
	bool retval = server->execute(command.c_str(), args, results);

//	cerr << results << endl;
	return retval;
}

void AR::refresh()
{
	string home = getenv(HOMEVAR);
    if (home.empty())
    {
    	cerr << "Must have defined an environment variable " << HOMEVAR << " pointing to your home directory" << endl;
    	throw "Must have defined an environment variable pointing to your home directory" ;
    }

	//
	// Note, we should really be loading .astrogrid-desktop here, not .plastic, and parsing that.
	// However, they both contain the xmlrpc server url, so out of sheer laziness I'm using .plastic.
	//
	string plasticLocation = home + "/.plastic";

	ifstream fin(plasticLocation.c_str());
    if ( !fin.is_open() )
    {
      cerr << "Unable to open .plastic file - please ensure the AstroRuntime is running."<< endl;
      exit(1);
    }

    JavaProperties props;
    props.load(fin);
    fin.close();
	URL hubUrl(props.get("plastic.xmlrpc.url"));
//	cout << "Hub is listening on "<<props.get("plastic.xmlrpc.url")<<endl;
//	cout << hubUrl.getHost() << " : " << hubUrl.getPort() << " " << hubUrl.getPath();


  // Create a client and connect to the server at hostname:port
    if (server) delete server;
  	server= new XmlRpcClient(hubUrl.getHost().c_str(), hubUrl.getPort(), hubUrl.getPath().c_str());
}



}
/*
 * The main C callable function for initializing the system.
 */
extern "C" {
int init_ar() {

	myAR = new astrogrid::AR();
  	XmlRpcValue noArgs, result;


}
}
extern astrogrid::AR* myAR = NULL; //declaration to actually cause storage to be allowcated
