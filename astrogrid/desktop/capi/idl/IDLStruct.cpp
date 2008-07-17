/*
 * IDLStruct.cpp
 *
 *  Created on: 09-Jul-2008
 *      Author: pharriso
 */

#include "IDLStruct.h"
IDLBase::IDLBase(){
}
IDLBase::~IDLBase(){
	std::cerr << "deleting base";
}


IDLBase * IDLBase::factory(const XmlRpcValue & v) {

	 XmlRpcValue::Type type = v.getType();
	 switch (type) {
		case XmlRpcValue::TypeArray:
			   return new IDLArray(v);
			break;
		case XmlRpcValue::TypeStruct:
               return new IDLStruct(v);
			break;
		case XmlRpcValue::TypeInvalid:
		case XmlRpcValue::TypeBase64:
			std::cerr << "do not know how to deal with "<< v.toXml() << "\n";
			return NULL;
			break;
		case XmlRpcValue::TypeBoolean:
			return new IDLBoolean(v);
			break;
		case XmlRpcValue::TypeDouble:
			return new IDLDouble(v);
			break;
		case XmlRpcValue::TypeInt:
			return new IDLInt(v);
			break;
		case XmlRpcValue::TypeString:
			return new IDLString(v);
			break;

		default:
			return new IDLAtomic(v);
			break;
	}

};


IDLStruct::IDLStruct(const XmlRpcValue & v)
{
    std::vector<std::string> membernames = v.listMemberNames();
    mmap = new std::map<std::string, IDLBase*>();

    for (unsigned int i = 0; i < membernames.size(); i++)
    {
    	std::cerr << "member="<<membernames.at(i)<<"\n";
    	XmlRpcValue const vv = const_cast<XmlRpcValue&>(v)[membernames.at(i)];
    	mmap->insert(std::pair<std::string, IDLBase *>(membernames.at(i), IDLBase::factory(vv) ));
    }

}

IDL_VPTR IDLStruct::makeIDLVar(void)
{
	std::vector<IDL_STRUCT_TAG_DEF> s_tags = std::vector<IDL_STRUCT_TAG_DEF>(mmap->size() +1);
	std::map<std::string, IDLBase*>::iterator i;
	int j = 0;
	for (i = mmap->begin(); i != mmap->end(); i++, j++)
	{
		IDLBase* ib = i->second;
       s_tags[j] = i->second->makeStag(i->first);

       std::cerr << s_tags[j].name << " " << (s_tags[j].dims == 0 ? 0 : s_tags[j].dims[0]) << " " <<s_tags[j].type <<"\n";
	}
	//hacky way to ensure that there is a zero at the end of the array....needed by the IDL stuff
	int * endofarray = (int *)&s_tags[mmap->size()];
	*endofarray = 0;

	sdef = IDL_MakeStruct(0, &s_tags[0]);
	//now fill data...

    IDL_MakeTempStructVector(sdef, (IDL_MEMINT)1, &var, IDL_TRUE);


	for (i = mmap->begin(); i != mmap->end(); i++, j++)
	{
	   IDL_MEMINT offset;
	   offset = IDL_StructTagInfoByName(sdef, const_cast<char *>(i->first.c_str()), IDL_MSG_LONGJMP, NULL);
	   i->second->fillData(var->value.s.arr->data + offset);
	}
   return var;
}

IDLStruct::~IDLStruct() {
	mmap->clear();
}

IDL_STRUCT_TAG_DEF IDLStruct::makeStag(const std::string & name) {
	IDL_STRUCT_TAG_DEF stag;
	stag.dims = 0;
	stag.name = 0;
	stag.type = sdef;
	stag.flags = 0;
	return stag;
}

IDLArray::IDLArray(const XmlRpcValue & v){
	mvec = new std::vector<IDLBase*>(v.size());
	for (int i = 0; i < v.size(); ++i) {
		(*mvec)[i] = IDLBase::factory(v[i]);
	}

}
IDLArray::~IDLArray(){
	mvec->clear();
}

IDL_STRUCT_TAG_DEF IDLArray::makeStag(const std::string & name) {
	IDL_STRUCT_TAG_DEF stag = (*mvec)[0]->makeStag(name); // assume that the vector is of one type - I think that it has to be for IDL anyway...
	// add the dimensionality
	IDL_MEMINT * dims = new IDL_MEMINT[2];
	dims[0] = 1;
	dims[1] = mvec->size();
	stag.dims = dims;
	return stag;
}


void IDLBase::fillData(void * data){
}

IDL_STRUCT_TAG_DEF IDLBase::makeStag(const std::string & name)
{
    IDL_STRUCT_TAG_DEF stag;
	stag.name = const_cast<char *>(name.c_str());
	stag.flags=0;
    return stag;

}

IDL_VPTR IDLBase::makeIDLVar(void){

}

IDL_STRUCT_TAG_DEF IDLAtomic::makeStag(const std::string & name)
{
    IDL_STRUCT_TAG_DEF stag = IDLBase::makeStag(name);
	stag.dims = 0;
	stag.type = (void *)idlType;
	return stag;
}
IDLAtomic::IDLAtomic(const XmlRpcValue &v): xmlrv(v){
	switch (v.getType()) {
	case XmlRpcValue::TypeBoolean:
		idlType = IDL_TYP_INT;
		break;
	case XmlRpcValue::TypeInt:
		idlType = IDL_TYP_INT;
		break;
	case XmlRpcValue::TypeDouble:
		idlType = IDL_TYP_DOUBLE;
		break;
	case XmlRpcValue::TypeString:
		idlType = IDL_TYP_STRING;
		break;

		default:
			std::cerr << "unknown atomic type" << v.toXml();
			break;
	}
}

IDLAtomic::~IDLAtomic(){}

IDLBoolean::IDLBoolean(const XmlRpcValue &v): IDLAtomic(v){}
IDLBoolean::~IDLBoolean(){}
IDLInt::IDLInt(const XmlRpcValue &v): IDLAtomic(v){}
IDLInt::~IDLInt(){}
IDLDouble::IDLDouble(const XmlRpcValue &v): IDLAtomic(v){}
IDLDouble::~IDLDouble(){}
IDLString::IDLString(const XmlRpcValue &v): IDLAtomic(v){}
IDLString::~IDLString(){}

void IDLBoolean::fillData(void * data){
	   if((bool)xmlrv){
		  *((int *)data) = 1;
	   } else {
			  *((int *)data) = 1;
	   }

}
void IDLDouble::fillData(void * data){
	*((double*)data) = (double)xmlrv;
}
void IDLInt::fillData(void * data){
	*((IDL_INT*)data) = (int)xmlrv;
}
void IDLString::fillData(void * data){
	IDL_StrStore((IDL_STRING*)data, (char *)xmlrv);
}

void IDLAtomic::fillData(void * data){
	std::cerr << "do not know how to convert atomic type" << xmlrv.getType() << "\n";
	}
