/*
 * IDLStruct.cpp
 *
 * Classes to dynamically create IDL structures directly from the XMLRPC.
 * This is made tricky to do because of two features of the IDL language
 * 1. arrays cannot have 0 length
 * 2. arrays cannot be of mixed type - in particular not mixed types of structs.
 *
 * The solution to these problems involves using IDL PTR types,
 * 1. a zero length array definition is changed to a PTR to NULL which stops IDL crashing - it behaves with sensible error messages when this is used...
 * 2. for an array of structs the whole array is searched to get all of the possible member tags and that single struct is used as the definition for the whole array
 *
 *  Created on: 09-Jul-2008
 *      Author: pharriso
 */

#include "IDLStruct.h"
IDLBase::IDLBase(){
}
IDLBase::~IDLBase(){
//	std::cerr << "deleting base";
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
    idlType = IDL_TYP_STRUCT;
    std::vector<std::string> membernames = v.listMemberNames();
    mmap = new std::map<std::string, IDLBase*>();

    for (unsigned int i = 0; i < membernames.size(); i++)
    {
//    	std::cerr << "member="<<membernames.at(i)<<"\n";
    	XmlRpcValue const vv = const_cast<XmlRpcValue&>(v)[membernames.at(i)];
    	std::string s = membernames.at(i);
    	if(s != "__interfaces"){ // this is a "control" member that is used in the c api - should not be reflected in the IDL struct
 	    std::transform(s.begin(),
 	                  s.end(),
 	                  s.begin(),
 	                  (int(*)(int))std::toupper);


    	mmap->insert(std::pair<std::string, IDLBase *>(s, IDLBase::factory(vv) ));
    	}
    }
//    std::cerr << "finished constructing struct\n" ;

}

IDL_VPTR IDLStruct::makeIDLVar(const std::string & name)
{
//	std::cerr << "making struc var\n";
	makeStag(name);
	//now fill data...

    IDL_MakeTempStructVector(sdef, (IDL_MEMINT)1, &var, IDL_TRUE);

    fillData(var->value.s.arr->data);

   return var;
}

IDLStruct::~IDLStruct() {
	mmap->clear();
}

IDL_STRUCT_TAG_DEF IDLStruct::makeStag(const std::string & name) {
	const std::map<std::string, IDLBase *>  allTags = getTags();
	return makeStags(name, allTags);
}

IDL_STRUCT_TAG_DEF IDLStruct::makeStags(const std::string & name, const std::map<std::string, IDLBase *> & allTags) {
//	std::cerr << "making struct stag name=" << name <<"\n";
	unionTags = allTags;
	IDL_STRUCT_TAG_DEF stag = IDLBase::makeStag(name);
	stag.dims = 0;
	std::vector<IDL_STRUCT_TAG_DEF> s_tags = std::vector<IDL_STRUCT_TAG_DEF>(allTags.size() +1);
	std::map<std::string, IDLBase *>::iterator i;
	int j = 0;
	for (i = unionTags.begin(); i != unionTags.end(); i++, j++)// just iterate over all the tags - cannot have different struct definitions for each member of an array... - would have been nice to have poointers to null
	{
//	   std::cerr << i->first << " :";

	      IDLBase* ib = i->second;
          s_tags[j] = i->second->makeStag(i->first);

//          std::cerr << " FOUNDTAG="<< i->first <<"\n";
//       std::cerr <<"IN STRUCT name="<<name<<"  tag="<< s_tags[j].name << " " << (s_tags[j].dims == 0 ? 0 : s_tags[j].dims[1]) << " " <<s_tags[j].type <<"\n";
	}
	//hacky way to ensure that there is a zero at the end of the array....needed by the IDL stuff
	int * endofarray = (int *)&s_tags[allTags.size()];
	*endofarray = 0;

	sdef = IDL_MakeStruct(0, &s_tags[0]);
	stag.type = sdef;
	stag.flags = 0;
//	std::cerr << "finished struct stag name ="<<name<<"\n";
	return stag;
}


std::map<std::string, IDLBase *> IDLStruct::getTags(void){
   std::map<std::string, IDLBase *> retval = std::map<std::string, IDLBase *>(*mmap);
   return retval;
}

void IDLStruct::fillData(void * data){
//  	std::cerr << "filling data for a struct\n";
	std::map<std::string, IDLBase*>::iterator i;

	//iterate over the tags
	for (i = unionTags.begin(); i != unionTags.end(); i++)
	{
	   IDL_MEMINT offset;
	   IDL_VPTR member_var;
	   std::string s = i->first;
	   offset = IDL_StructTagInfoByName(sdef, const_cast<char *>(s.c_str()), IDL_MSG_LONGJMP, &member_var);

 	   std::cerr << "filling data for " << s << " offset="<<offset <<"\n";
    	   std::map<std::string, IDLBase *>::iterator im;
  	   if((im = mmap->find(i->first)) != mmap->end()){
//  		   std::cerr << " DATA\n";
  		   im->second->fillData((char *)data + offset);

  	   } else {
//  		   std::cerr << " NULL\n";
  		   *((char **) data + offset) = NULL;
  	   }
	}
}
IDLArray::IDLArray(const XmlRpcValue & v){
	mvec = new std::vector<IDLBase*>(v.size());
//	std::cout << "making array size=" << v.size() <<"\n";
	for (int i = 0; i < v.size(); ++i) {
//		std::cerr << "index =" << i <<"\n";
		(*mvec)[i] = IDLBase::factory(v[i]);
	}

}
IDLArray::~IDLArray(){
	mvec->clear();
}

IDL_VPTR IDLArray::makeIDLVar(const std::string & name){
	//FIXME this is not working....
	int n = mvec->size();
	IDL_MEMINT dim[1];
	dim[0] = n;
    IDLBase* base = mvec->at(0);
    bool isStruct = false;
    char * arrdata;
    if(base->getType() == IDL_TYP_STRUCT){ // yuk - IDL treats arrays of structs differently....
//            std::cerr << "making array data - structure \n";
    	isStruct = true;
        IDLStruct * str = (IDLStruct *)base;
        arrdata =IDL_MakeTempStructVector(str->sdef, (IDL_MEMINT)n, &var, IDL_TRUE);

    } else {
	    arrdata = IDL_MakeTempArray( base->getType(), 1, dim, IDL_ARR_INI_ZERO, &var);
//  	           std::cerr << "making array data - type=" << base->getType() <<"\n";
    }

	fillData(arrdata); // actually creates the array variable as well as filling it...
    return var;
}


void IDLArray::fillData(void * data)
{
	int n = mvec->size();
	if(n == 0)
	{
		*((char **)data) = NULL;// this is a IDL_PTR to NULL - IDL at the command level does reasonable things...
	}
	else{
		IDL_MEMINT dim[1];
		dim[0] = n;
	    IDLBase* base = mvec->at(0);
	    bool isStruct = false;
	    IDL_StructDefPtr savedSdef = NULL;
	    char * arrdata;
	    IDL_VPTR arrvar; // need to create this temp var as otherwise cannot see a way of getting the correct array element size to use below
	    if(base->getType() == IDL_TYP_STRUCT){ // yuk - IDL treats arrays of structs differently....
//            std::cerr << "making array data - structure \n";
	    	isStruct = true;
            IDLStruct * str = (IDLStruct *)base;
            arrdata =IDL_MakeTempStructVector(str->sdef, (IDL_MEMINT)n, &arrvar, IDL_TRUE);
            savedSdef = str->sdef;

	    } else {
		    arrdata = IDL_MakeTempArray( base->getType(), 1, dim, IDL_ARR_INI_ZERO, &arrvar);
//  	           std::cerr << "making array data - type=" << base->getType() <<"\n";
	    }
        for (int i = 0; i < n; i++){
        	IDLBase* elem = mvec->at(i);
        	if(savedSdef != NULL){
        		((IDLStruct *)elem)->sdef = savedSdef;// set the structure definition of each of the structures in the array to be equal to the first on - this does not get initialized in array otherwise
        	}
          	std::cerr << "fill i=" << i << " at " << arrvar->value.arr->elt_len*i << " address="<< (void *)(arrdata+ arrvar->value.arr->elt_len*i) <<"\n";
        	elem->fillData((void *)((char *)data + arrvar->value.arr->elt_len*i));
        }
        IDL_Deltmp(arrvar);
	}
}

IDL_STRUCT_TAG_DEF IDLArray::makeStag(const std::string & name) {
	IDL_STRUCT_TAG_DEF stag;
//	std::cerr << "makeStag array ";
	if(mvec->size() >0){
		if((*mvec)[0]->getType() == IDL_TYP_STRUCT)
		{
			std::cerr << " (struct) name="<< name << "\n";
			std::map<std::string, IDLBase *> allTags = std::map<std::string, IDLBase *>();
			//for arrays of structs need to create union tags list
             for (int i = 0; i < mvec->size(); ++i) {
				IDLStruct * s = (IDLStruct*)(*mvec)[i];
				std::map<std::string, IDLBase *> theseTags = s->getTags();
				allTags.insert(theseTags.begin(), theseTags.end());
			}
             // now iterate through to create the individual tag lists
             for (int i = 0; i < mvec->size() -1; ++i) {
				IDLStruct * s = (IDLStruct*)(*mvec)[i];
				s->makeStags(name, allTags);
             }
             stag = ((IDLStruct*)(*mvec)[mvec->size() - 1])->makeStags(name,allTags);


		}
		else {
//           std::cerr << " not struct\n";
	       stag = (*mvec)[0]->makeStag(name); // assume that the vector is of one type - I think that it has to be for IDL anyway...

		}
		IDL_MEMINT * dims = new IDL_MEMINT[2];
		dims[0] = 1;
		dims[1] = mvec->size();
		stag.dims = dims;
	}
	else {
//		  std::cerr << " NULL array\n";
		 stag = IDLBase::makeStag(name);
		 stag.type=(void *)IDL_TYP_PTR; //USE a pointer - seems that pointer to NULL is just about the only way of specifying NULL
		 stag.dims=0;
	}

	// add the dimensionality
	return stag;
}


void IDLBase::fillData(void * data){
}

IDL_STRUCT_TAG_DEF IDLBase::makeStag(const std::string & name)
{
    IDL_STRUCT_TAG_DEF stag;
    std::string s = name;
	stag.name = const_cast<char *>(s.c_str());
	stag.flags=0;
    return stag;

}

IDL_VPTR IDLBase::makeIDLVar(const std::string & name){
   return IDL_Gettmp();
}

IDL_STRUCT_TAG_DEF IDLAtomic::makeStag(const std::string & name)
{
//	std::cerr<< "make Stag atomic name="<<name<<"\n";
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
		idlType = IDL_TYP_INT; // IDL header file is broken for osx as it thinks that INT is only short...
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
	std::cerr << "filling double="<< (double)xmlrv << "\n";
	*((double*)data) = (double)xmlrv;
}
void IDLInt::fillData(void * data){
	int tmp = (int)xmlrv;
	*((IDL_INT*)data) = tmp;
}
void IDLString::fillData(void * data){
	char * s = xmlrv;
	IDL_STRING* sptr = (IDL_STRING*)data;
	IDL_StrStore(sptr, s);
}

void IDLAtomic::fillData(void * data){
	std::cerr << "do not know how to convert atomic type" << xmlrv.getType() << "\n";
	}

IDL_VPTR IDLAtomic::makeIDLVar(const std::string & name){

   var = IDL_Gettmp();
   var->type = idlType;
// not sure about   var.flags;
   fillData((void *)&var->value);
   return var;
}
