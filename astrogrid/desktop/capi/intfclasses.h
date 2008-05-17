/*
C interface to the AR
Paul Harrison paul.harrison@manchester.ac.uk
produced on 2008-01-04Z

DO NOT EDIT - this file is produced automatically by the AR build process

 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
*/
   
   
#ifndef INTFCLASSES_H_
#define INTFCLASSES_H_
#include "arcontainers.h"
#include "arintf.h"
using namespace XmlRpc;

           
class AbstractInformation_{
public:
IvornOrURI id_;
JString name_;
AbstractInformation_( XmlRpcValue& v) : id_(v.mem("id")), name_(v.mem("name")) {
} 

virtual ~AbstractInformation_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct AbstractInformation* s ) const {
  s->id = id_;
  s->name = name_;
s->_type = AbstractInformation;
  
}

   virtual void asStruct (struct AbstractInformation_Base* s) const {
   asStruct(&s->d.abstractinformation);
   s->_type = AbstractInformation;
}

   static AbstractInformation_* create(XmlRpcValue & v) ;
      
};

class AccessURL_{
public:
URLString value_;
IvornOrURI valueURI_;
JString use_;
AccessURL_( XmlRpcValue& v) : value_(v.mem("value")), valueURI_(v.mem("valueURI")), use_(v.mem("use")) {
} 

virtual ~AccessURL_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct AccessURL* s ) const {
  s->value = value_;
  s->valueURI = valueURI_;
  s->use = use_;
s->_type = AccessURL;
  
}

};

class Validation_{
public:
IvornOrURI validatedBy_;
int validationLevel_;
Validation_( XmlRpcValue& v) : validatedBy_(v.mem("validatedBy")), validationLevel_(v.mem("validationLevel")) {
} 

virtual ~Validation_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Validation* s ) const {
  s->validatedBy = validatedBy_;
  s->validationLevel = validationLevel_;
s->_type = Validation;
  
}

};

class ResourceName_{
public:
IvornOrURI id_;
JString value_;
ResourceName_( XmlRpcValue& v) : id_(v.mem("id")), value_(v.mem("value")) {
} 

virtual ~ResourceName_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct ResourceName* s ) const {
  s->id = id_;
  s->value = value_;
s->_type = ResourceName;
  
}

};

class Contact_{
public:
JString address_;
JString email_;
ResourceName_ name_;
JString telephone_;
Contact_( XmlRpcValue& v) : address_(v.mem("address")), email_(v.mem("email")), name_(v.mem("name")), telephone_(v.mem("telephone")) {
} 

virtual ~Contact_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Contact* s ) const {
  s->address = address_;
  s->email = email_;
   name_.asStruct(&s->name);
  s->telephone = telephone_;
s->_type = Contact;
  
}

};

class Creator_{
public:
URLString logo_;
IvornOrURI logoURI_;
ResourceName_ name_;
Creator_( XmlRpcValue& v) : logo_(v.mem("logo")), logoURI_(v.mem("logoURI")), name_(v.mem("name")) {
} 

virtual ~Creator_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Creator* s ) const {
  s->logo = logo_;
  s->logoURI = logoURI_;
   name_.asStruct(&s->name);
s->_type = Creator;
  
}

};

class CurationDate_{
public:
JString role_;
JString value_;
CurationDate_( XmlRpcValue& v) : role_(v.mem("role")), value_(v.mem("value")) {
} 

virtual ~CurationDate_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct CurationDate* s ) const {
  s->role = role_;
  s->value = value_;
s->_type = CurationDate;
  
}

};

class Curation_{
public:
ListOf<Contact_> contacts_;
ListOf<ResourceName_> contributors_;
ListOf<Creator_> creators_;
ResourceName_ publisher_;
JString version_;
ListOf<CurationDate_> dates_;
Curation_( XmlRpcValue& v) : contacts_(v.mem("contacts")), contributors_(v.mem("contributors")), creators_(v.mem("creators")), publisher_(v.mem("publisher")), version_(v.mem("version")), dates_(v.mem("dates")) {
} 

virtual ~Curation_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Curation* s ) const {
   s->contacts.list = copyArrayAsStruct<Contact_, struct Contact>(contacts_);
   s->contacts.n = contacts_.size();
   s->contributors.list = copyArrayAsStruct<ResourceName_, struct ResourceName>(contributors_);
   s->contributors.n = contributors_.size();
   s->creators.list = copyArrayAsStruct<Creator_, struct Creator>(creators_);
   s->creators.n = creators_.size();
   publisher_.asStruct(&s->publisher);
  s->version = version_;
   s->dates.list = copyArrayAsStruct<CurationDate_, struct CurationDate>(dates_);
   s->dates.n = dates_.size();
s->_type = Curation;
  
}

};

class Relationship_{
public:
ListOf<ResourceName_> relatedResources_;
JString relationshipType_;
Relationship_( XmlRpcValue& v) : relatedResources_(v.mem("relatedResources")), relationshipType_(v.mem("relationshipType")) {
} 

virtual ~Relationship_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Relationship* s ) const {
   s->relatedResources.list = copyArrayAsStruct<ResourceName_, struct ResourceName>(relatedResources_);
   s->relatedResources.n = relatedResources_.size();
  s->relationshipType = relationshipType_;
s->_type = Relationship;
  
}

};

class Source_{
public:
JString format_;
JString value_;
Source_( XmlRpcValue& v) : format_(v.mem("format")), value_(v.mem("value")) {
} 

virtual ~Source_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Source* s ) const {
  s->format = format_;
  s->value = value_;
s->_type = Source;
  
}

};

class Content_{
public:
ListOf<JString> contentLevel_;
JString description_;
URLString referenceURL_;
IvornOrURI referenceURI_;
ListOf<Relationship_> relationships_;
ListOf<JString> subject_;
ListOf<JString> type_;
Source_ source_;
Content_( XmlRpcValue& v) : contentLevel_(v.mem("contentLevel")), description_(v.mem("description")), referenceURL_(v.mem("referenceURL")), referenceURI_(v.mem("referenceURI")), relationships_(v.mem("relationships")), subject_(v.mem("subject")), type_(v.mem("type")), source_(v.mem("source")) {
} 

virtual ~Content_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Content* s ) const {
   s->contentLevel.list = copyArray<JString, JString>(contentLevel_);
   s->contentLevel.n = contentLevel_.size();
  s->description = description_;
  s->referenceURL = referenceURL_;
  s->referenceURI = referenceURI_;
   s->relationships.list = copyArrayAsStruct<Relationship_, struct Relationship>(relationships_);
   s->relationships.n = relationships_.size();
   s->subject.list = copyArray<JString, JString>(subject_);
   s->subject.n = subject_.size();
   s->type.list = copyArray<JString, JString>(type_);
   s->type.n = type_.size();
   source_.asStruct(&s->source);
s->_type = Content;
  
}

};

class Resource_{
public:
ListOf<Validation_> validationLevel_;
JString title_;
IvornOrURI id_;
JString shortName_;
Curation_ curation_;
Content_ content_;
JString status_;
JString created_;
JString updated_;
JString type_;
Resource_( XmlRpcValue& v) : validationLevel_(v.mem("validationLevel")), title_(v.mem("title")), id_(v.mem("id")), shortName_(v.mem("shortName")), curation_(v.mem("curation")), content_(v.mem("content")), status_(v.mem("status")), created_(v.mem("created")), updated_(v.mem("updated")), type_(v.mem("type")) {
} 

virtual ~Resource_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Resource* s ) const {
   s->validationLevel.list = copyArrayAsStruct<Validation_, struct Validation>(validationLevel_);
   s->validationLevel.n = validationLevel_.size();
  s->title = title_;
  s->id = id_;
  s->shortName = shortName_;
   curation_.asStruct(&s->curation);
   content_.asStruct(&s->content);
  s->status = status_;
  s->created = created_;
  s->updated = updated_;
  s->type = type_;
s->_type = Resource;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.resource);
   s->_type = Resource;
}

   static Resource_* create(XmlRpcValue & v) ;
      
};

class Authority_ : public Resource_{
public:
ResourceName_ managingOrg_;
Authority_( XmlRpcValue& v) : Resource_(v), managingOrg_(v.mem("managingOrg")) {
} 

virtual ~Authority_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Authority* s ) const {
Resource_::asStruct((struct Resource *)s);
   managingOrg_.asStruct(&s->managingOrg);
s->_type = Authority;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.authority);
   s->_type = Authority;
}

};

class AvailabilityBean_{
public:
JString location_;
JString message_;
JString serverName_;
JString timeOnServer_;
JString upTime_;
JString validTo_;
AvailabilityBean_( XmlRpcValue& v) : location_(v.mem("location")), message_(v.mem("message")), serverName_(v.mem("serverName")), timeOnServer_(v.mem("timeOnServer")), upTime_(v.mem("upTime")), validTo_(v.mem("validTo")) {
} 

virtual ~AvailabilityBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct AvailabilityBean* s ) const {
  s->location = location_;
  s->message = message_;
  s->serverName = serverName_;
  s->timeOnServer = timeOnServer_;
  s->upTime = upTime_;
  s->validTo = validTo_;
s->_type = AvailabilityBean;
  
}

};

class SecurityMethod_{
public:
IvornOrURI standardID_;
SecurityMethod_( XmlRpcValue& v) : standardID_(v.mem("standardID")) {
} 

virtual ~SecurityMethod_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SecurityMethod* s ) const {
  s->standardID = standardID_;
s->_type = SecurityMethod;
  
}

};

class Interface_{
public:
ListOf<AccessURL_> accessUrls_;
JString role_;
ListOf<SecurityMethod_> securityMethods_;
JString version_;
JString type_;
Interface_( XmlRpcValue& v) : accessUrls_(v.mem("accessUrls")), role_(v.mem("role")), securityMethods_(v.mem("securityMethods")), version_(v.mem("version")), type_(v.mem("type")) {
} 

virtual ~Interface_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Interface* s ) const {
   s->accessUrls.list = copyArrayAsStruct<AccessURL_, struct AccessURL>(accessUrls_);
   s->accessUrls.n = accessUrls_.size();
  s->role = role_;
   s->securityMethods.list = copyArrayAsStruct<SecurityMethod_, struct SecurityMethod>(securityMethods_);
   s->securityMethods.n = securityMethods_.size();
  s->version = version_;
  s->type = type_;
s->_type = Interface;
  
}

};

class Capability_{
public:
JString description_;
ListOf<Interface_> interfaces_;
IvornOrURI standardID_;
ListOf<Validation_> validationLevel_;
JString type_;
Capability_( XmlRpcValue& v) : description_(v.mem("description")), interfaces_(v.mem("interfaces")), standardID_(v.mem("standardID")), validationLevel_(v.mem("validationLevel")), type_(v.mem("type")) {
} 

virtual ~Capability_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Capability* s ) const {
  s->description = description_;
   s->interfaces.list = copyArrayAsStruct<Interface_, struct Interface>(interfaces_);
   s->interfaces.n = interfaces_.size();
  s->standardID = standardID_;
   s->validationLevel.list = copyArrayAsStruct<Validation_, struct Validation>(validationLevel_);
   s->validationLevel.n = validationLevel_.size();
  s->type = type_;
s->_type = Capability;
  
}

   virtual void asStruct (struct Capability_Base* s) const {
   asStruct(&s->d.capability);
   s->_type = Capability;
}

   static Capability_* create(XmlRpcValue & v) ;
      
};

class ColumnBean_{
public:
JString datatype_;
JString description_;
JString name_;
JString uCD_;
JString unit_;
ColumnBean_( XmlRpcValue& v) : datatype_(v.mem("datatype")), description_(v.mem("description")), name_(v.mem("name")), uCD_(v.mem("uCD")), unit_(v.mem("unit")) {
} 

virtual ~ColumnBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct ColumnBean* s ) const {
  s->datatype = datatype_;
  s->description = description_;
  s->name = name_;
  s->uCD = uCD_;
  s->unit = unit_;
s->_type = ColumnBean;
  
}

   virtual void asStruct (struct ColumnBean_Base* s) const {
   asStruct(&s->d.columnbean);
   s->_type = ColumnBean;
}

   static ColumnBean_* create(XmlRpcValue & v) ;
      
};

class TableBean_{
public:
ListOfBase<ColumnBean_> columns_;
JString description_;
JString name_;
TableBean_( XmlRpcValue& v) : columns_(v.mem("columns")), description_(v.mem("description")), name_(v.mem("name")) {
} 

virtual ~TableBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct TableBean* s ) const {
   s->columns.list = copyArrayAsBaseStruct<ColumnBean_, struct ColumnBean_Base>(columns_);
   s->columns.n = columns_.size();
  s->description = description_;
  s->name = name_;
s->_type = TableBean;
  
}

   virtual void asStruct (struct TableBean_Base* s) const {
   asStruct(&s->d.tablebean);
   s->_type = TableBean;
}

   static TableBean_* create(XmlRpcValue & v) ;
      
};

class Catalog_{
public:
JString description_;
JString name_;
ListOfBase<TableBean_> tables_;
Catalog_( XmlRpcValue& v) : description_(v.mem("description")), name_(v.mem("name")), tables_(v.mem("tables")) {
} 

virtual ~Catalog_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Catalog* s ) const {
  s->description = description_;
  s->name = name_;
   s->tables.list = copyArrayAsBaseStruct<TableBean_, struct TableBean_Base>(tables_);
   s->tables.n = tables_.size();
s->_type = Catalog;
  
}

   virtual void asStruct (struct Catalog_Base* s) const {
   asStruct(&s->d.catalog);
   s->_type = Catalog;
}

   static Catalog_* create(XmlRpcValue & v) ;
      
};

class Service_ : public Resource_{
public:
ListOf<JString> rights_;
ListOfBase<Capability_> capabilities_;
Service_( XmlRpcValue& v) : Resource_(v), rights_(v.mem("rights")), capabilities_(v.mem("capabilities")) {
} 

virtual ~Service_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Service* s ) const {
Resource_::asStruct((struct Resource *)s);
   s->rights.list = copyArray<JString, JString>(rights_);
   s->rights.n = rights_.size();
   s->capabilities.list = copyArrayAsBaseStruct<Capability_, struct Capability_Base>(capabilities_);
   s->capabilities.n = capabilities_.size();
s->_type = Service;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.service);
   s->_type = Service;
}

   virtual void asStruct (struct Service_Base* s) const {
   asStruct(&s->d.service);
   
}

   static Service_* create(XmlRpcValue & v) ;
      
};

class Coverage_{
public:
ResourceName_ footprint_;
XMLString stcResourceProfile_;
ListOf<JString> wavebands_;
Coverage_( XmlRpcValue& v) : footprint_(v.mem("footprint")), stcResourceProfile_(v.mem("stcResourceProfile")), wavebands_(v.mem("wavebands")) {
} 

virtual ~Coverage_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Coverage* s ) const {
   footprint_.asStruct(&s->footprint);
  s->stcResourceProfile = stcResourceProfile_;
   s->wavebands.list = copyArray<JString, JString>(wavebands_);
   s->wavebands.n = wavebands_.size();
s->_type = Coverage;
  
}

};

class HasCoverage_{
public:
Coverage_ coverage_;
HasCoverage_( XmlRpcValue& v) : coverage_(v.mem("coverage")) {
} 

virtual ~HasCoverage_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct HasCoverage* s ) const {
   coverage_.asStruct(&s->coverage);
s->_type = HasCoverage;
  
}

   virtual void asStruct (struct HasCoverage_Base* s) const {
   asStruct(&s->d.hascoverage);
   s->_type = HasCoverage;
}

   static HasCoverage_* create(XmlRpcValue & v) ;
      
};

class DataService_ : public Service_,public HasCoverage_{
public:
ListOf<ResourceName_> facilities_;
ListOf<ResourceName_> instruments_;
DataService_( XmlRpcValue& v) : Service_(v), HasCoverage_(v), facilities_(v.mem("facilities")), instruments_(v.mem("instruments")) {
} 

virtual ~DataService_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct DataService* s ) const {
Service_::asStruct((struct Service *)s);
HasCoverage_::asStruct((struct HasCoverage *)s);
   s->facilities.list = copyArrayAsStruct<ResourceName_, struct ResourceName>(facilities_);
   s->facilities.n = facilities_.size();
   s->instruments.list = copyArrayAsStruct<ResourceName_, struct ResourceName>(instruments_);
   s->instruments.n = instruments_.size();
s->_type = DataService;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.dataservice);
   s->_type = DataService;
}

   virtual void asStruct (struct DataService_Base* s) const {
   asStruct(&s->d.dataservice);
   
}

   static DataService_* create(XmlRpcValue & v) ;
      
};

class CatalogService_ : public DataService_{
public:
ListOfBase<TableBean_> tables_;
CatalogService_( XmlRpcValue& v) : DataService_(v), tables_(v.mem("tables")) {
} 

virtual ~CatalogService_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct CatalogService* s ) const {
DataService_::asStruct((struct DataService *)s);
   s->tables.list = copyArrayAsBaseStruct<TableBean_, struct TableBean_Base>(tables_);
   s->tables.n = tables_.size();
s->_type = CatalogService;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.catalogservice);
   s->_type = CatalogService;
}

};

class ParameterReferenceBean_{
public:
int max_;
int min_;
JString ref_;
ParameterReferenceBean_( XmlRpcValue& v) : max_(v.mem("max")), min_(v.mem("min")), ref_(v.mem("ref")) {
} 

virtual ~ParameterReferenceBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct ParameterReferenceBean* s ) const {
  s->max = max_;
  s->min = min_;
  s->ref = ref_;
s->_type = ParameterReferenceBean;
  
}

};

class InterfaceBean_{
public:
ListOf<ParameterReferenceBean_> inputs_;
JString name_;
ListOf<ParameterReferenceBean_> outputs_;
InterfaceBean_( XmlRpcValue& v) : inputs_(v.mem("inputs")), name_(v.mem("name")), outputs_(v.mem("outputs")) {
} 

virtual ~InterfaceBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct InterfaceBean* s ) const {
   s->inputs.list = copyArrayAsStruct<ParameterReferenceBean_, struct ParameterReferenceBean>(inputs_);
   s->inputs.n = inputs_.size();
  s->name = name_;
   s->outputs.list = copyArrayAsStruct<ParameterReferenceBean_, struct ParameterReferenceBean>(outputs_);
   s->outputs.n = outputs_.size();
s->_type = InterfaceBean;
  
}

};

class ParameterBean_{
public:
JString defaultValue_;
JString description_;
JString name_;
ListOf<JString> options_;
JString subType_;
JString type_;
JString ucd_;
JString uiName_;
JString units_;
ParameterBean_( XmlRpcValue& v) : defaultValue_(v.mem("defaultValue")), description_(v.mem("description")), name_(v.mem("name")), options_(v.mem("options")), subType_(v.mem("subType")), type_(v.mem("type")), ucd_(v.mem("ucd")), uiName_(v.mem("uiName")), units_(v.mem("units")) {
} 

virtual ~ParameterBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct ParameterBean* s ) const {
  s->defaultValue = defaultValue_;
  s->description = description_;
  s->name = name_;
   s->options.list = copyArray<JString, JString>(options_);
   s->options.n = options_.size();
  s->subType = subType_;
  s->type = type_;
  s->ucd = ucd_;
  s->uiName = uiName_;
  s->units = units_;
s->_type = ParameterBean;
  
}

};

class CeaApplication_ : public Resource_{
public:
ListOf<InterfaceBean_> interfaces_;
ListOf<ParameterBean_> parameters_;
CeaApplication_( XmlRpcValue& v) : Resource_(v), interfaces_(v.mem("interfaces")), parameters_(v.mem("parameters")) {
} 

virtual ~CeaApplication_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct CeaApplication* s ) const {
Resource_::asStruct((struct Resource *)s);
   s->interfaces.list = copyArrayAsStruct<InterfaceBean_, struct InterfaceBean>(interfaces_);
   s->interfaces.n = interfaces_.size();
   s->parameters.list = copyArrayAsStruct<ParameterBean_, struct ParameterBean>(parameters_);
   s->parameters.n = parameters_.size();
s->_type = CeaApplication;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.ceaapplication);
   s->_type = CeaApplication;
}

};

class CeaServerCapability_ : public Capability_{
public:
ListOf<IvornOrURI> managedApplications_;
CeaServerCapability_( XmlRpcValue& v) : Capability_(v), managedApplications_(v.mem("managedApplications")) {
} 

virtual ~CeaServerCapability_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct CeaServerCapability* s ) const {
Capability_::asStruct((struct Capability *)s);
   s->managedApplications.list = copyArray<IvornOrURI, IvornOrURI>(managedApplications_);
   s->managedApplications.n = managedApplications_.size();
s->_type = CeaServerCapability;
  
}

   virtual void asStruct (struct Capability_Base* s) const {
   asStruct(&s->d.ceaservercapability);
   s->_type = CeaServerCapability;
}

};

class CeaService_ : public Service_{
public:
CeaService_( XmlRpcValue& v) : Service_(v) {
} 

virtual ~CeaService_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct CeaService* s ) const {
Service_::asStruct((struct Service *)s);
s->_type = CeaService;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.ceaservice);
   s->_type = CeaService;
}

};

class ConeCapability_Query_{
public:
JString catalog_;
double dec_;
JString extras_;
double ra_;
double sr_;
int verb_;
ConeCapability_Query_( XmlRpcValue& v) : catalog_(v.mem("catalog")), dec_(v.mem("dec")), extras_(v.mem("extras")), ra_(v.mem("ra")), sr_(v.mem("sr")), verb_(v.mem("verb")) {
} 

virtual ~ConeCapability_Query_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct ConeCapability_Query* s ) const {
  s->catalog = catalog_;
  s->dec = dec_;
  s->extras = extras_;
  s->ra = ra_;
  s->sr = sr_;
  s->verb = verb_;
s->_type = ConeCapability_Query;
  
}

};

class ConeCapability_ : public Capability_{
public:
int maxRecords_;
double maxSR_;
ConeCapability_Query_ testQuery_;
ConeCapability_( XmlRpcValue& v) : Capability_(v), maxRecords_(v.mem("maxRecords")), maxSR_(v.mem("maxSR")), testQuery_(v.mem("testQuery")) {
} 

virtual ~ConeCapability_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct ConeCapability* s ) const {
Capability_::asStruct((struct Capability *)s);
  s->maxRecords = maxRecords_;
  s->maxSR = maxSR_;
   testQuery_.asStruct(&s->testQuery);
s->_type = ConeCapability;
  
}

   virtual void asStruct (struct Capability_Base* s) const {
   asStruct(&s->d.conecapability);
   s->_type = ConeCapability;
}

};

class ConeService_ : public Service_{
public:
ConeService_( XmlRpcValue& v) : Service_(v) {
} 

virtual ~ConeService_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct ConeService* s ) const {
Service_::asStruct((struct Service *)s);
s->_type = ConeService;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.coneservice);
   s->_type = ConeService;
}

};

class Format_{
public:
JString value_;
Format_( XmlRpcValue& v) : value_(v.mem("value")) {
} 

virtual ~Format_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Format* s ) const {
  s->value = value_;
s->_type = Format;
  
}

};

class DataCollection_ : public Resource_,public HasCoverage_{
public:
ListOf<ResourceName_> facilities_;
ListOf<ResourceName_> instruments_;
ListOf<JString> rights_;
ListOf<Format_> formats_;
ListOfBase<Catalog_> catalogues_;
AccessURL_ accessURL_;
DataCollection_( XmlRpcValue& v) : Resource_(v), HasCoverage_(v), facilities_(v.mem("facilities")), instruments_(v.mem("instruments")), rights_(v.mem("rights")), formats_(v.mem("formats")), catalogues_(v.mem("catalogues")), accessURL_(v.mem("accessURL")) {
} 

virtual ~DataCollection_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct DataCollection* s ) const {
Resource_::asStruct((struct Resource *)s);
HasCoverage_::asStruct((struct HasCoverage *)s);
   s->facilities.list = copyArrayAsStruct<ResourceName_, struct ResourceName>(facilities_);
   s->facilities.n = facilities_.size();
   s->instruments.list = copyArrayAsStruct<ResourceName_, struct ResourceName>(instruments_);
   s->instruments.n = instruments_.size();
   s->rights.list = copyArray<JString, JString>(rights_);
   s->rights.n = rights_.size();
   s->formats.list = copyArrayAsStruct<Format_, struct Format>(formats_);
   s->formats.n = formats_.size();
   s->catalogues.list = copyArrayAsBaseStruct<Catalog_, struct Catalog_Base>(catalogues_);
   s->catalogues.n = catalogues_.size();
   accessURL_.asStruct(&s->accessURL);
s->_type = DataCollection;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.datacollection);
   s->_type = DataCollection;
}

   virtual void asStruct (struct DataCollection_Base* s) const {
   asStruct(&s->d.datacollection);
   
}

   static DataCollection_* create(XmlRpcValue & v) ;
      
};

class DatabaseBean_ : public Catalog_{
public:
DatabaseBean_( XmlRpcValue& v) : Catalog_(v) {
} 

virtual ~DatabaseBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct DatabaseBean* s ) const {
Catalog_::asStruct((struct Catalog *)s);
s->_type = DatabaseBean;
  
}

   virtual void asStruct (struct Catalog_Base* s) const {
   asStruct(&s->d.databasebean);
   s->_type = DatabaseBean;
}

};

class ExecutionInformation_ : public AbstractInformation_{
public:
JString description_;
ACRDate startTime_;
ACRDate finishTime_;
JString status_;
ExecutionInformation_( XmlRpcValue& v) : AbstractInformation_(v), description_(v.mem("description")), startTime_(v.mem("startTime")), finishTime_(v.mem("finishTime")), status_(v.mem("status")) {
} 

virtual ~ExecutionInformation_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct ExecutionInformation* s ) const {
AbstractInformation_::asStruct((struct AbstractInformation *)s);
  s->description = description_;
  s->startTime = startTime_;
  s->finishTime = finishTime_;
  s->status = status_;
s->_type = ExecutionInformation;
  
}

   virtual void asStruct (struct AbstractInformation_Base* s) const {
   asStruct(&s->d.executioninformation);
   s->_type = ExecutionInformation;
}

};

class ExecutionMessage_{
public:
JString content_;
JString level_;
JString source_;
JString status_;
ACRDate timestamp_;
ExecutionMessage_( XmlRpcValue& v) : content_(v.mem("content")), level_(v.mem("level")), source_(v.mem("source")), status_(v.mem("status")), timestamp_(v.mem("timestamp")) {
} 

virtual ~ExecutionMessage_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct ExecutionMessage* s ) const {
  s->content = content_;
  s->level = level_;
  s->source = source_;
  s->status = status_;
  s->timestamp = timestamp_;
s->_type = ExecutionMessage;
  
}

};

class FunctionBean_{
public:
JString description_;
JString name_;
ListOf<ParameterBean_> parameters_;
FunctionBean_( XmlRpcValue& v) : description_(v.mem("description")), name_(v.mem("name")), parameters_(v.mem("parameters")) {
} 

virtual ~FunctionBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct FunctionBean* s ) const {
  s->description = description_;
  s->name = name_;
   s->parameters.list = copyArrayAsStruct<ParameterBean_, struct ParameterBean>(parameters_);
   s->parameters.n = parameters_.size();
s->_type = FunctionBean;
  
}

};

class Handler_{
public:
Handler_( XmlRpcValue& v) {
} 

virtual ~Handler_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Handler* s ) const {
s->_type = Handler;
  
}

};

class HarvestCapability_ : public Capability_{
public:
int maxRecords_;
HarvestCapability_( XmlRpcValue& v) : Capability_(v), maxRecords_(v.mem("maxRecords")) {
} 

virtual ~HarvestCapability_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct HarvestCapability* s ) const {
Capability_::asStruct((struct Capability *)s);
  s->maxRecords = maxRecords_;
s->_type = HarvestCapability;
  
}

   virtual void asStruct (struct Capability_Base* s) const {
   asStruct(&s->d.harvestcapability);
   s->_type = HarvestCapability;
}

};

class NodeInformation_ : public AbstractInformation_{
public:
ACRKeyValueMap attributes_;
ACRDate createDate_;
ACRDate modifyDate_;
long size_;
IvornOrURI contentLocation_;
NodeInformation_( XmlRpcValue& v) : AbstractInformation_(v), attributes_(v.mem("attributes")), createDate_(v.mem("createDate")), modifyDate_(v.mem("modifyDate")), size_(v.mem("size")), contentLocation_(v.mem("contentLocation")) {
} 

virtual ~NodeInformation_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct NodeInformation* s ) const {
AbstractInformation_::asStruct((struct AbstractInformation *)s);
  s->attributes = attributes_;
  s->createDate = createDate_;
  s->modifyDate = modifyDate_;
  s->size = size_;
  s->contentLocation = contentLocation_;
s->_type = NodeInformation;
  
}

   virtual void asStruct (struct AbstractInformation_Base* s) const {
   asStruct(&s->d.nodeinformation);
   s->_type = NodeInformation;
}

};

class Organisation_ : public Resource_{
public:
ListOf<ResourceName_> facilities_;
ListOf<ResourceName_> instruments_;
Organisation_( XmlRpcValue& v) : Resource_(v), facilities_(v.mem("facilities")), instruments_(v.mem("instruments")) {
} 

virtual ~Organisation_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct Organisation* s ) const {
Resource_::asStruct((struct Resource *)s);
   s->facilities.list = copyArrayAsStruct<ResourceName_, struct ResourceName>(facilities_);
   s->facilities.n = facilities_.size();
   s->instruments.list = copyArrayAsStruct<ResourceName_, struct ResourceName>(instruments_);
   s->instruments.n = instruments_.size();
s->_type = Organisation;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.organisation);
   s->_type = Organisation;
}

};

class RegistryService_ : public Service_{
public:
ListOf<JString> managedAuthorities_;
RegistryService_( XmlRpcValue& v) : Service_(v), managedAuthorities_(v.mem("managedAuthorities")) {
} 

virtual ~RegistryService_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct RegistryService* s ) const {
Service_::asStruct((struct Service *)s);
   s->managedAuthorities.list = copyArray<JString, JString>(managedAuthorities_);
   s->managedAuthorities.n = managedAuthorities_.size();
s->_type = RegistryService;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.registryservice);
   s->_type = RegistryService;
}

};

class SearchCapability_ : public Capability_{
public:
JString extensionSearchSupport_;
int maxRecords_;
ListOf<JString> optionalProtocol_;
SearchCapability_( XmlRpcValue& v) : Capability_(v), extensionSearchSupport_(v.mem("extensionSearchSupport")), maxRecords_(v.mem("maxRecords")), optionalProtocol_(v.mem("optionalProtocol")) {
} 

virtual ~SearchCapability_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SearchCapability* s ) const {
Capability_::asStruct((struct Capability *)s);
  s->extensionSearchSupport = extensionSearchSupport_;
  s->maxRecords = maxRecords_;
   s->optionalProtocol.list = copyArray<JString, JString>(optionalProtocol_);
   s->optionalProtocol.n = optionalProtocol_.size();
s->_type = SearchCapability;
  
}

   virtual void asStruct (struct Capability_Base* s) const {
   asStruct(&s->d.searchcapability);
   s->_type = SearchCapability;
}

};

class SesamePositionBean_{
public:
double dec_;
double decErr_;
JString oName_;
JString oType_;
JString posStr_;
double ra_;
double raErr_;
JString target_;
JString service_;
ListOf<JString> aliases_;
SesamePositionBean_( XmlRpcValue& v) : dec_(v.mem("dec")), decErr_(v.mem("decErr")), oName_(v.mem("oName")), oType_(v.mem("oType")), posStr_(v.mem("posStr")), ra_(v.mem("ra")), raErr_(v.mem("raErr")), target_(v.mem("target")), service_(v.mem("service")), aliases_(v.mem("aliases")) {
} 

virtual ~SesamePositionBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SesamePositionBean* s ) const {
  s->dec = dec_;
  s->decErr = decErr_;
  s->oName = oName_;
  s->oType = oType_;
  s->posStr = posStr_;
  s->ra = ra_;
  s->raErr = raErr_;
  s->target = target_;
  s->service = service_;
   s->aliases.list = copyArray<JString, JString>(aliases_);
   s->aliases.n = aliases_.size();
s->_type = SesamePositionBean;
  
}

};

class SiapCapability_SkySize_{
public:
double lat_;
double lon_;
SiapCapability_SkySize_( XmlRpcValue& v) : lat_(v.mem("lat")), lon_(v.mem("lon")) {
} 

virtual ~SiapCapability_SkySize_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SiapCapability_SkySize* s ) const {
  s->lat = lat_;
  s->lon = lon_;
s->_type = SiapCapability_SkySize;
  
}

};

class SiapCapability_ImageSize_{
public:
int lat_;
int lon_;
SiapCapability_ImageSize_( XmlRpcValue& v) : lat_(v.mem("lat")), lon_(v.mem("lon")) {
} 

virtual ~SiapCapability_ImageSize_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SiapCapability_ImageSize* s ) const {
  s->lat = lat_;
  s->lon = lon_;
s->_type = SiapCapability_ImageSize;
  
}

};

class SiapCapability_SkyPos_{
public:
double lat_;
double lon_;
SiapCapability_SkyPos_( XmlRpcValue& v) : lat_(v.mem("lat")), lon_(v.mem("lon")) {
} 

virtual ~SiapCapability_SkyPos_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SiapCapability_SkyPos* s ) const {
  s->lat = lat_;
  s->lon = lon_;
s->_type = SiapCapability_SkyPos;
  
}

};

class SiapCapability_Query_{
public:
JString extras_;
SiapCapability_SkyPos_ pos_;
SiapCapability_SkySize_ size_;
int verb_;
SiapCapability_Query_( XmlRpcValue& v) : extras_(v.mem("extras")), pos_(v.mem("pos")), size_(v.mem("size")), verb_(v.mem("verb")) {
} 

virtual ~SiapCapability_Query_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SiapCapability_Query* s ) const {
  s->extras = extras_;
   pos_.asStruct(&s->pos);
   size_.asStruct(&s->size);
  s->verb = verb_;
s->_type = SiapCapability_Query;
  
}

};

class SiapCapability_ : public Capability_{
public:
JString imageServiceType_;
int maxFileSize_;
SiapCapability_SkySize_ maxImageExtent_;
SiapCapability_ImageSize_ maxImageSize_;
SiapCapability_SkySize_ maxQueryRegionSize_;
SiapCapability_Query_ testQuery_;
int maxRecords_;
SiapCapability_( XmlRpcValue& v) : Capability_(v), imageServiceType_(v.mem("imageServiceType")), maxFileSize_(v.mem("maxFileSize")), maxImageExtent_(v.mem("maxImageExtent")), maxImageSize_(v.mem("maxImageSize")), maxQueryRegionSize_(v.mem("maxQueryRegionSize")), testQuery_(v.mem("testQuery")), maxRecords_(v.mem("maxRecords")) {
} 

virtual ~SiapCapability_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SiapCapability* s ) const {
Capability_::asStruct((struct Capability *)s);
  s->imageServiceType = imageServiceType_;
  s->maxFileSize = maxFileSize_;
   maxImageExtent_.asStruct(&s->maxImageExtent);
   maxImageSize_.asStruct(&s->maxImageSize);
   maxQueryRegionSize_.asStruct(&s->maxQueryRegionSize);
   testQuery_.asStruct(&s->testQuery);
  s->maxRecords = maxRecords_;
s->_type = SiapCapability;
  
}

   virtual void asStruct (struct Capability_Base* s) const {
   asStruct(&s->d.siapcapability);
   s->_type = SiapCapability;
}

};

class SiapService_ : public Service_{
public:
SiapService_( XmlRpcValue& v) : Service_(v) {
} 

virtual ~SiapService_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SiapService* s ) const {
Service_::asStruct((struct Service *)s);
s->_type = SiapService;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.siapservice);
   s->_type = SiapService;
}

};

class SkyNodeColumnBean_ : public ColumnBean_{
public:
int byteSize_;
int precision_;
int rank_;
SkyNodeColumnBean_( XmlRpcValue& v) : ColumnBean_(v), byteSize_(v.mem("byteSize")), precision_(v.mem("precision")), rank_(v.mem("rank")) {
} 

virtual ~SkyNodeColumnBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SkyNodeColumnBean* s ) const {
ColumnBean_::asStruct((struct ColumnBean *)s);
  s->byteSize = byteSize_;
  s->precision = precision_;
  s->rank = rank_;
s->_type = SkyNodeColumnBean;
  
}

   virtual void asStruct (struct ColumnBean_Base* s) const {
   asStruct(&s->d.skynodecolumnbean);
   s->_type = SkyNodeColumnBean;
}

};

class SkyNodeTableBean_ : public TableBean_{
public:
JString primaryKey_;
int rank_;
ACRKeyValueMap relations_;
int rows_;
SkyNodeTableBean_( XmlRpcValue& v) : TableBean_(v), primaryKey_(v.mem("primaryKey")), rank_(v.mem("rank")), relations_(v.mem("relations")), rows_(v.mem("rows")) {
} 

virtual ~SkyNodeTableBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct SkyNodeTableBean* s ) const {
TableBean_::asStruct((struct TableBean *)s);
  s->primaryKey = primaryKey_;
  s->rank = rank_;
  s->relations = relations_;
  s->rows = rows_;
s->_type = SkyNodeTableBean;
  
}

   virtual void asStruct (struct TableBean_Base* s) const {
   asStruct(&s->d.skynodetablebean);
   s->_type = SkyNodeTableBean;
}

};

class TableBeanComparator_{
public:
TableBeanComparator_( XmlRpcValue& v) {
} 

virtual ~TableBeanComparator_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct TableBeanComparator* s ) const {
s->_type = TableBeanComparator;
  
}

};

class TabularDB_ : public DataCollection_{
public:
DatabaseBean_ database_;
TabularDB_( XmlRpcValue& v) : DataCollection_(v), database_(v.mem("database")) {
} 

virtual ~TabularDB_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct TabularDB* s ) const {
DataCollection_::asStruct((struct DataCollection *)s);
   database_.asStruct(&s->database);
s->_type = TabularDB;
  
}

   virtual void asStruct (struct Resource_Base* s) const {
   asStruct(&s->d.tabulardb);
   s->_type = TabularDB;
}

};

class UserInformation_ : public AbstractInformation_{
public:
JString community_;
JString password_;
UserInformation_( XmlRpcValue& v) : AbstractInformation_(v), community_(v.mem("community")), password_(v.mem("password")) {
} 

virtual ~UserInformation_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct UserInformation* s ) const {
AbstractInformation_::asStruct((struct AbstractInformation *)s);
  s->community = community_;
  s->password = password_;
s->_type = UserInformation;
  
}

   virtual void asStruct (struct AbstractInformation_Base* s) const {
   asStruct(&s->d.userinformation);
   s->_type = UserInformation;
}

};

class VoMonBean_{
public:
int code_;
IvornOrURI id_;
long millis_;
JString status_;
JString timestamp_;
VoMonBean_( XmlRpcValue& v) : code_(v.mem("code")), id_(v.mem("id")), millis_(v.mem("millis")), status_(v.mem("status")), timestamp_(v.mem("timestamp")) {
} 

virtual ~VoMonBean_() {
      /* TODO should only be virtual where there is a derived class  - also need to free resources...*/
}

virtual void asStruct( struct VoMonBean* s ) const {
  s->code = code_;
  s->id = id_;
  s->millis = millis_;
  s->status = status_;
  s->timestamp = timestamp_;
s->_type = VoMonBean;
  
}

};

#endif
        