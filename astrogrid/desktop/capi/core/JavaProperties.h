#ifndef JAVAPROPERTIES_H_
#define JAVAPROPERTIES_H_


#include<iostream>
#include<string>
#include<fstream>
#include <map>
using namespace std;
namespace java
{
/**
 * Emulates, to some extent, the Java java.util.Properties class.  In particular, it's capable
 * of reading Properties that have been serialised to a file.
 * @author Jon Tayler
 */
class JavaProperties
{
public:
	/**
	 * Create an empty Properties
	 */
	JavaProperties();
	/**
	 * dtor
	 */
	virtual ~JavaProperties();
	/**
	 * load properties from a file.  Returns true on success
	 */
	 bool load(ifstream& in);
	 /**
	  * get a property, supplying a default.
	  */
	 string get(const string& key);
	 
	 /**
	  * set a property
	  */
	  void put(const string& key, const string& value);
	 
private:
	map<string, string> data;
	 
};

}

#endif /*JAVAPROPERTIES_H_*/
