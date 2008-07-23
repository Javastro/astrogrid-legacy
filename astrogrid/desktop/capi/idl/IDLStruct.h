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

#include "idl_export.h"
#include "XmlRpc.h"

using namespace XmlRpc;

class IDLBase {
public:
	IDLBase();
	virtual ~IDLBase();
	static IDLBase * factory(const XmlRpcValue &v);
	virtual IDL_VPTR makeIDLVar(void);
	virtual IDL_STRUCT_TAG_DEF makeStag(const std::string & name);
	virtual void fillData(void *);
	int getType(void) {return idlType;}

protected:
    IDL_VPTR var; // the IDL variable definition
	int idlType;

};

class IDLStruct : public IDLBase {
public:
	IDLStruct(const XmlRpcValue & v);
	virtual ~IDLStruct();
	virtual IDL_VPTR makeIDLVar(void);
	virtual IDL_STRUCT_TAG_DEF makeStag(const std::string & name);

protected:

std::map<std::string, IDLBase *> * mmap;

private:
	void * sdef; // the structure definition

};

class IDLArray : public IDLBase {
public:
	IDLArray(const XmlRpcValue & v);
	virtual ~IDLArray();
	virtual IDL_STRUCT_TAG_DEF makeStag(const std::string & name);
	virtual void fillData(void *);
protected:
std::vector<IDLBase *> *mvec;

};


class IDLAtomic : public IDLBase {
	//note that this class is only meant to represent an atomic value within a struct or array...
public:
	IDLAtomic(const XmlRpcValue & v);
	virtual ~IDLAtomic();
	virtual void fillData(void *);
	virtual IDL_STRUCT_TAG_DEF makeStag(const std::string & name);

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
