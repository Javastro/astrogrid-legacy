#ifndef AR_H_
#define AR_H_

#include "XmlRpc.h"
#include "JavaProperties.h"
#include "URL.h"
#include <fstream>
#include <iostream>
#include <string>



using namespace std;
using namespace XmlRpc;
using namespace java;

namespace astrogrid
{
/**
 * Exposes the functionality of the Astro Runtime.
 * To use: instantiate an AR, and call the execute method, passing in the xml-rpc function you want to call.
 * Note that the AR daemon must be running first, and if it drops and restarts after this class has been instatiated,
 * you'll need to call refresh().
 * @author Paul Harrison, Jon Tayler
 */
class AR
{
public:
	AR();
	virtual ~AR();
	/**
	 * Refreshes connection to AR.  Possible you need to do this if the AR stops and restarts.
	 */
	void refresh();
	/**
	 * Executes a command
	 */

	bool execute(const string& command, XmlRpcValue& args, XmlRpcValue& result);
private:
	XmlRpcClient* server;
	bool isOK;
};

}
//execute even if the AR object has not yet been initialized.
bool ARexecute(const string& command, XmlRpcValue& args, XmlRpcValue& result);

// globally scoped variable - note dummy initializer in AR.cpp to allocate storage
extern astrogrid::AR* myAR;


#endif /*AR_H_*/
