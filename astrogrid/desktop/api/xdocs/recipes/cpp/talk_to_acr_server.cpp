/*
 * modification of xmlrpc_add_client to attempt to talk to AR
 * richard holbrey, university of leeds,UK
 */


#include <string>
#include <iostream>
#include <xmlrpc-c/girerr.hpp>
#include <xmlrpc-c/base.hpp>
#include <xmlrpc-c/client_simple.hpp>

#include <fstream>

/*
compile line for testing (FC3, gcc 3.4.4, xmlrpc-c from http://xmlrpc-c.sourceforge.net)

g++ -c -I/home/richardh/code/xml/xmlrpc-c-1.03.03/lib/abyss/src -I/home/richardh/code/xml/xmlrpc-c-1.03.03/include -Wall -Wundef -Wimplicit -W -Winline -Woverloaded-virtual -Wsynth   talk_to_acr_server.cpp

g++ -o talk_to_acr_server talk_to_acr_server.o  /home/richardh/code/xml/xmlrpc-c-1.03.03/src/cpp/libxmlrpc_client++.a -L/usr/lib -lwwwxml -lxmltok -lxmlparse -lwwwzip -lwwwinit -lwwwapp -lwwwhtml -lwwwtelnet -lwwwnews -lwwwhttp -lwwwmime -lwwwgopher -lwwwftp -lwwwfile -lwwwdir -lwwwcache -lwwwstream -lwwwmux -lwwwtrans -lwwwcore -lwwwutils -lmd5 -ldl -lz -L/usr/lib -lcurl -L/usr/kerberos/lib -lssl -lcrypto -lgssapi_krb5 -lkrb5 -lcom_err -lk5crypto -lresolv -ldl -lz -lgssapi_krb5 -lkrb5 -lk5crypto -lcom_err -lresolv -L/usr/lib -lidn -lssl -lcrypto -lz -lpthread /home/richardh/code/xml/xmlrpc-c-1.03.03/src/.libs/libxmlrpc_client.a /home/richardh/code/xml/xmlrpc-c-1.03.03/src/cpp/libxmlrpc++.a /home/richardh/code/xml/xmlrpc-c-1.03.03/src/.libs/libxmlrpc.a /home/richardh/code/xml/xmlrpc-c-1.03.03/lib/expat/xmlparse/.libs/libxmlrpc_xmlparse.a /home/richardh/code/xml/xmlrpc-c-1.03.03/lib/expat/xmltok/.libs/libxmlrpc_xmltok.a

*/
using namespace std;

#include <stdio.h>
#include <stdlib.h>


int
main(int argc, char **) {

    if (argc-1 > 0) {
        cerr << "This program has no arguments" << endl;
        exit(1);
    }

    fstream fin;
    const int buffer_size = 128;
    char* cbuffer = new char[buffer_size]; 
    char* ebuffer = getenv("HOME");
    strcpy(cbuffer,ebuffer);
    strcat(cbuffer,"/.astrogrid-desktop");
    cout << "cbuffer: " << cbuffer << ", size: " << strlen(cbuffer) << endl;
    
    fin.open(cbuffer, ios::in);
    if ( !fin ) {
      cerr << "Unable to open config file\n";
      exit(1);
    }

    fin.getline(cbuffer,buffer_size,'\n');
    cout << "cbuffer: " << cbuffer << ", size: " << strlen(cbuffer) << endl;
    fin.close();	

    strcat(cbuffer,"xmlrpc");
    cout << "cbuffer: " << cbuffer << ", size: " << strlen(cbuffer) << endl;


    

    try {
        const string serverUrl(cbuffer);

        const string methodName("system.apihelp.listMethods");

        xmlrpc_c::clientSimple myClient;
        xmlrpc_c::value result;
        
	//request a method of the server
        myClient.call(serverUrl, methodName, "", &result);

	xmlrpc_c::rpcOutcome rpc_result (result);
	if( rpc_result.succeeded() ) 
	  cout << "seemed ok" << endl;
	else 
	  cout << "result not good" << endl;

	cout << "resulting object of size: " << sizeof(result) 
	     << " of type: " << result.type()  //base.hpp defines these
	     << endl;
	
	
	xmlrpc_c::value_array rpc_array(result);
	cout << "array size: " << rpc_array.size() << endl;
	std::vector<xmlrpc_c::value> rpc_strings = rpc_array.vectorValueValue();
	
	for (unsigned int i = 0; i < rpc_strings.size(); ++i) {

	  const string string_val = xmlrpc_c::value_string( rpc_strings[i] );
	  cout << string_val << "\n";
	}

	//#########################################
	//try a dialog
	xmlrpc_c::value result2;  //need separate result space
	const string chooseFileMethod("dialogs.resourceChooser.chooseResource");

	//the "ii" indicates 2 extra parameters, by the length of the string
	myClient.call(serverUrl, chooseFileMethod, "ii", &result2, "Select a file", true);

	cout << "resulting object of of type: " << result2.type()  //base.hpp defines these
	     << endl;

	const string string_val = xmlrpc_c::value_string( result2 );
	cout << "with string value: " << string_val << endl;

	//	const string response = xmlrpc_c::value_string(result);
	//int const sum = xmlrpc_c::value_int(result);
	// Assume the method returned an integer; throws error if not


    } catch (girerr::error const error) {
        cerr << "Client threw error: " << error.what() << endl;
    } catch (...) {
        cerr << "Client threw unexpected error." << endl;
    }
    

    delete [] cbuffer;
    return 0;
}
