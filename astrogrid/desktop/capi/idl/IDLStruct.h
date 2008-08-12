/*
 * IDLStruct.h
 *
 *  Created on: 09-Jul-2008
 *      Author: pharriso
 */

#ifndef IDLSTRUCT_H_
#define IDLSTRUCT_H_

#include <fstream>
#include <vector>
#include <string>
#include <algorithm>
#include <map>
#include <set>

#include "idl_export.h"
#include "XmlRpc.h"

using namespace XmlRpc;

class IDLBase {
public:
	IDLBase(); // constructor is to take xmlrpc value and work out if atomic or is array/struct and store constructed children
	virtual ~IDLBase();
	static IDLBase * factory(const XmlRpcValue &v); // works out what type of object to emit
	virtual IDL_VPTR makeIDLVar(const std::string & name); // actually create a variable definition - only called for the outermost xmlrpc value
	virtual IDL_STRUCT_TAG_DEF makeStag(const std::string & name); // create a structure tag
	virtual void fillData(void *); //fill the data - called after the structure tag has been created
	int getType(void) {return idlType;}

protected:
    IDL_VPTR var; // the IDL variable definition
	int idlType;

};

class IDLArray : public IDLBase {
public:
	IDLArray(const XmlRpcValue & v);
	virtual ~IDLArray();
	virtual IDL_STRUCT_TAG_DEF makeStag(const std::string & name);
	virtual void fillData(void *);
	virtual IDL_VPTR makeIDLVar(const std::string & name);

protected:
std::vector<IDLBase *> *mvec;

};



class IDLStruct : public IDLBase {
	friend void IDLArray::fillData(void * data);//needs to be a friend because of the way that IDL treats arrays of structs.
	friend IDL_VPTR IDLArray::makeIDLVar(const std::string & name);
public:
	IDLStruct(const XmlRpcValue & v);
	virtual ~IDLStruct();
	virtual IDL_VPTR makeIDLVar(const std::string & name);
	virtual IDL_STRUCT_TAG_DEF makeStag(const std::string & name);
	IDL_STRUCT_TAG_DEF makeStags(const std::string & name, const std::map<std::string, IDLBase *> & allTags); // create a structure tags including null ptr tags for those extra tags listed in allTags which are not part of the current struct, but members of other structs in the array
	virtual void fillData(void *);
	std::map<std::string, IDLBase *> getTags(void);

protected:

std::map<std::string, IDLBase *> * mmap; // map of the tags from the local xmlrpc
IDL_StructDefPtr sdef; // the structure definition
std::map<std::string, IDLBase *> unionTags; // all tags from possible array of structs at this level;

private:

};


class IDLAtomic : public IDLBase {
	//note that this class is only meant to represent an atomic value within a struct or array...
public:
	IDLAtomic(const XmlRpcValue & v);
	virtual ~IDLAtomic();
	virtual void fillData(void *);
	virtual IDL_STRUCT_TAG_DEF makeStag(const std::string & name);
	virtual IDL_VPTR makeIDLVar(const std::string & name);
protected:
	XmlRpcValue xmlrv;

};

class IDLBoolean : public IDLAtomic {
public:
	IDLBoolean(const XmlRpcValue & v);
	virtual ~IDLBoolean();
	virtual void fillData(void * data);
};

class IDLDouble : public IDLAtomic {
public:
	IDLDouble(const XmlRpcValue & v);
	virtual ~IDLDouble();
	virtual void fillData(void * data);
};

class IDLInt : public IDLAtomic {
public:
	IDLInt(const XmlRpcValue & v);
	virtual ~IDLInt();
	virtual void fillData(void * data);
};

class IDLString : public IDLAtomic {
public:
	IDLString(const XmlRpcValue & v);
	virtual ~IDLString();
	virtual void fillData(void * data);
};



#endif /* IDLSTRUCT_H_ */
