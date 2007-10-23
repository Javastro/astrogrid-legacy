#include "JavaProperties.h"

namespace java
{

JavaProperties::JavaProperties()
{
}

JavaProperties::~JavaProperties()
{
}



string JavaProperties::get(const string& key)
{
	string val = data[key];
	return val;
}

void JavaProperties::put(const string& key, const string& value) 
{
	data[key] = value;
}

bool JavaProperties::load(ifstream& in) 
{
	string line;
	
	while (!in.eof()) {
		getline(in,line);
		
		if (line[0]=='#' || line[0]=='!') continue;  //ignore comments
		string::size_type equals=line.find('=');
		if (equals==string::npos) 
		{
			//cerr << "Badly formed properties line : " + line + " - missing equals";
			continue;	
		}
		string key = line.substr(0,equals);
		string value = line.substr(equals+1,1000); //hack!
		//erase any backslashes - hardly the right thing to do, but will do for now
		string::size_type pos;
		while ((pos=value.find('\\'))!=string::npos) {
			value.erase(pos,1); 
		}
		//cout << key << " = " << value << endl;
		data[key]=value;
	}	
	return true;
}
	

}
