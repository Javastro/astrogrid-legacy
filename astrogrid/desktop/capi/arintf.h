#ifndef INTF_H_
#define INTF_H_
/*
C interface to the AR
Paul Harrison paul.harrison@manchester.ac.uk
produced on 2007-12-09Z

DO NOT EDIT - this file is produced automatically by the AR build process

 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
*/
   
   
      
#include "core/acrtypes.h"
#ifdef __cplusplus
 extern "C" {
#endif


/* enumeration of the types available */
 enum AcrType {
UserInformation,TableBeanComparator,TableBean,ParameterReferenceBean,ParameterBean,NodeInformation,InterfaceBean,ExecutionMessage,ExecutionInformation,DatabaseBean,ColumnBean,CeaService,CeaServerCapability,CeaApplication,AbstractInformation,SesamePositionBean,SkyNodeTableBean,SkyNodeColumnBean,FunctionBean,AvailabilityBean,Validation,TabularDB,Source,SiapService,SiapCapability,SiapCapability_SkySize,SiapCapability_ImageSize,SiapCapability_SkyPos,SiapCapability_Query,Service,SecurityMethod,SearchCapability,ResourceName,Resource,Relationship,RegistryService,Organisation,Interface,HasCoverage,HarvestCapability,Handler,Format,CurationDate,DataService,DataCollection,Curation,Creator,Coverage,Content,Contact,ConeService,ConeCapability,ConeCapability_Query,CatalogService,Catalog,Capability,Authority,AccessURL,VoMonBean };

/* type tree */

/* list types */
   
typedef struct {
    int n;
    struct AccessURL *list; /* note that this is an array */        
   } ListOfAccessURL;

typedef struct {
    int n;
    struct Capability_Base *list; /* note that this is an array */        
   } ListOfCapability_Base;

typedef struct {
    int n;
    struct Catalog_Base *list; /* note that this is an array */        
   } ListOfCatalog_Base;

typedef struct {
    int n;
    struct ColumnBean_Base *list; /* note that this is an array */        
   } ListOfColumnBean_Base;

typedef struct {
    int n;
    struct Contact *list; /* note that this is an array */        
   } ListOfContact;

typedef struct {
    int n;
    struct Creator *list; /* note that this is an array */        
   } ListOfCreator;

typedef struct {
    int n;
    struct CurationDate *list; /* note that this is an array */        
   } ListOfCurationDate;

typedef struct {
    int n;
    struct ExecutionInformation *list; /* note that this is an array */        
   } ListOfExecutionInformation;

typedef struct {
    int n;
    struct ExecutionMessage *list; /* note that this is an array */        
   } ListOfExecutionMessage;

typedef struct {
    int n;
    struct Format *list; /* note that this is an array */        
   } ListOfFormat;

typedef struct {
    int n;
    struct FunctionBean *list; /* note that this is an array */        
   } ListOfFunctionBean;

typedef struct {
    int n;
    struct Interface *list; /* note that this is an array */        
   } ListOfInterface;

typedef struct {
    int n;
    struct InterfaceBean *list; /* note that this is an array */        
   } ListOfInterfaceBean;

typedef struct {
    int n;
    struct NodeInformation *list; /* note that this is an array */        
   } ListOfNodeInformation;

typedef struct {
    int n;
    struct ParameterBean *list; /* note that this is an array */        
   } ListOfParameterBean;

typedef struct {
    int n;
    struct ParameterReferenceBean *list; /* note that this is an array */        
   } ListOfParameterReferenceBean;

typedef struct {
    int n;
    struct Relationship *list; /* note that this is an array */        
   } ListOfRelationship;

typedef struct {
    int n;
    struct Resource_Base *list; /* note that this is an array */        
   } ListOfResource_Base;

typedef struct {
    int n;
    struct ResourceName *list; /* note that this is an array */        
   } ListOfResourceName;

typedef struct {
    int n;
    struct SecurityMethod *list; /* note that this is an array */        
   } ListOfSecurityMethod;

typedef struct {
    int n;
    struct Service_Base *list; /* note that this is an array */        
   } ListOfService_Base;

typedef struct {
    int n;
    struct SkyNodeColumnBean *list; /* note that this is an array */        
   } ListOfSkyNodeColumnBean;

typedef struct {
    int n;
    struct SkyNodeTableBean *list; /* note that this is an array */        
   } ListOfSkyNodeTableBean;

typedef struct {
    int n;
    struct TableBean_Base *list; /* note that this is an array */        
   } ListOfTableBean_Base;

typedef struct {
    int n;
    struct Validation *list; /* note that this is an array */        
   } ListOfValidation;

typedef struct {
    int n;
    struct VoMonBean *list; /* note that this is an array */        
   } ListOfVoMonBean;

/*
structures - map onto the java beans
*/ 

struct  AbstractInformation {
       /*
        from 
org.astrogrid.acr.astrogrid.AbstractInformation
Base class - all 'information' structures returned by ACR extend this class.
   */
    enum AcrType _type;

/* data members from AbstractInformation */
IvornOrURI id;
JString name;

};

struct  AccessURL {
       /*
        from 
org.astrogrid.acr.ivoa.resource.AccessURL
Access URL for a service.
   */
    enum AcrType _type;

/* data members from AccessURL */
URLString value;
IvornOrURI valueURI;
JString use;

};

struct  Validation {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Validation
a validation stamp combining a validation level and the ID of 
         the validator.
   */
    enum AcrType _type;

/* data members from Validation */
IvornOrURI validatedBy;
int validationLevel;

};

struct  ResourceName {
       /*
        from 
org.astrogrid.acr.ivoa.resource.ResourceName
the name of a potentially registered resource.  That is, the entity
         referred to may have an associated identifier.
   */
    enum AcrType _type;

/* data members from ResourceName */
IvornOrURI id;
JString value;

};

struct  Contact {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Contact
Information that can be used for contacting someone
   */
    enum AcrType _type;

/* data members from Contact */
JString address;
JString email;
struct ResourceName name;
JString telephone;

};

struct  Creator {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Creator
The entity (e.g. person or organisation) primarily responsible 
            for creating something
   */
    enum AcrType _type;

/* data members from Creator */
URLString logo;
IvornOrURI logoURI;
struct ResourceName name;

};

struct  CurationDate {
       /*
        from 
org.astrogrid.acr.ivoa.resource.CurationDate
Date associated with an event in the life cycle of the
               resource.
   */
    enum AcrType _type;

/* data members from CurationDate */
JString role;
JString value;

};

struct  Curation {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Curation
Information regarding the general curation of a resource
   */
    enum AcrType _type;

/* data members from Curation */
ListOfContact contacts;
ListOfResourceName contributors;
ListOfCreator creators;
struct ResourceName publisher;
JString version;
ListOfCurationDate dates;

};

struct  Relationship {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Relationship
A description of the relationship between one resource and one or
           more other resources.
   */
    enum AcrType _type;

/* data members from Relationship */
ListOfResourceName relatedResources;
JString relationshipType;

};

struct  Source {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Source
a bibliographic reference from which the present resource is 
                derived or extracted.
   */
    enum AcrType _type;

/* data members from Source */
JString format;
JString value;

};

struct  Content {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Content
Information regarding the general content of a resource
   */
    enum AcrType _type;

/* data members from Content */
ListOfJString contentLevel;
JString description;
URLString referenceURL;
IvornOrURI referenceURI;
ListOfRelationship relationships;
ListOfJString subject;
ListOfJString type;
struct Source source;

};

struct  Authority {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Authority
a naming authority; an assertion of control over a
           namespace represented by an authority identifier.
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from Authority */
struct ResourceName managingOrg;

};

struct  AvailabilityBean {
       /*
        from 
org.astrogrid.acr.ivoa.AvailabilityBean
Description of the availability of a server
   */
    enum AcrType _type;

/* data members from AvailabilityBean */
JString location;
JString message;
JString serverName;
JString timeOnServer;
JString upTime;
JString validTo;

};

struct  SecurityMethod {
       /*
        from 
org.astrogrid.acr.ivoa.resource.SecurityMethod
a description of a security mechanism.
   */
    enum AcrType _type;

/* data members from SecurityMethod */
IvornOrURI standardID;

};

struct  Interface {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Interface
A description of a service interface.
   */
    enum AcrType _type;

/* data members from Interface */
ListOfAccessURL accessUrls;
JString role;
ListOfSecurityMethod securityMethods;
JString version;
JString type;

};

struct  Capability {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Capability
a description of what the service does (in terms of 
            context-specific behavior), and how to use it (in terms of
            an interface)
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

};

struct  ColumnBean {
       /*
        from 
org.astrogrid.acr.astrogrid.ColumnBean
describes a single column in a tabular database.
   */
    enum AcrType _type;

/* data members from ColumnBean */
JString datatype;
JString description;
JString name;
JString uCD;
JString unit;

};

struct  TableBean {
       /*
        from 
org.astrogrid.acr.astrogrid.TableBean
descripition of a  single table in a  registry entry
   */
    enum AcrType _type;

/* data members from TableBean */
ListOfColumnBean_Base columns;
JString description;
JString name;

};

struct  Catalog {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Catalog
Represents a catalog in a data collection.
   */
    enum AcrType _type;

/* data members from Catalog */
JString description;
JString name;
ListOfTableBean_Base tables;

};

struct  Coverage {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Coverage
A description of how a resource's contents or behavior maps
           to the sky, to time, and to frequency space, including
           coverage and resolution.
   */
    enum AcrType _type;

/* data members from Coverage */
struct ResourceName footprint;
XMLString stcResourceProfile;
ListOfJString wavebands;

};

struct  CatalogService {
       /*
        from 
org.astrogrid.acr.ivoa.resource.CatalogService
A service that interacts with one or more specified tables
            having some coverage of the sky, time, and/or frequency.

           Previously Named TabularSkyService
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from Service */
ListOfJString rights;
ListOfCapability_Base capabilities;

/* data members from HasCoverage */
struct Coverage coverage;

/* data members from DataService */
ListOfResourceName facilities;
ListOfResourceName instruments;

/* data members from CatalogService */
ListOfTableBean_Base tables;

};

struct  ParameterReferenceBean {
       /*
        from 
org.astrogrid.acr.astrogrid.ParameterReferenceBean
Description of a parameter occuring in an interface to a remote application
   */
    enum AcrType _type;

/* data members from ParameterReferenceBean */
int max;
int min;
JString ref;

};

struct  InterfaceBean {
       /*
        from 
org.astrogrid.acr.astrogrid.InterfaceBean
Description of  an interface to a remote application.
   */
    enum AcrType _type;

/* data members from InterfaceBean */
ListOfParameterReferenceBean inputs;
JString name;
ListOfParameterReferenceBean outputs;

};

struct  ParameterBean {
       /*
        from 
org.astrogrid.acr.astrogrid.ParameterBean
description of a single parameter for a remote application.
 
 NB - all fields, apart from <tt>name</tt> and <tt>type</tt> may be null
   */
    enum AcrType _type;

/* data members from ParameterBean */
JString defaultValue;
JString description;
JString name;
ListOfJString options;
JString subType;
JString type;
JString ucd;
JString uiName;
JString units;

};

struct  CeaApplication {
       /*
        from 
org.astrogrid.acr.astrogrid.CeaApplication
registry entry for a cea application
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from CeaApplication */
ListOfInterfaceBean interfaces;
ListOfParameterBean parameters;

};

struct  CeaServerCapability {
       /*
        from 
org.astrogrid.acr.astrogrid.CeaServerCapability
Registry description of a CEA server.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

/* data members from CeaServerCapability */
ListOfIvornOrURI managedApplications;

};

struct  CeaService {
       /*
        from 
org.astrogrid.acr.astrogrid.CeaService
Registry record for a CEA server.
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from Service */
ListOfJString rights;
ListOfCapability_Base capabilities;

/* data members from CeaService */

};

struct  ConeCapability_Query {
       /*
        from 
org.astrogrid.acr.ivoa.resource.ConeCapability.Query
description of a test query for a cone service
   */
    enum AcrType _type;

/* data members from ConeCapability.Query */
JString catalog;
double dec;
JString extras;
double ra;
double sr;
int verb;

};

struct  ConeCapability {
       /*
        from 
org.astrogrid.acr.ivoa.resource.ConeCapability
Describes the capabilities of a cone search service.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

/* data members from ConeCapability */
int maxRecords;
double maxSR;
struct ConeCapability_Query testQuery;

};

struct  ConeService {
       /*
        from 
org.astrogrid.acr.ivoa.resource.ConeService
Registry description of a cone-search service
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from Service */
ListOfJString rights;
ListOfCapability_Base capabilities;

/* data members from ConeService */

};

struct  Format {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Format
Describes a data format that a service can return
   */
    enum AcrType _type;

/* data members from Format */
JString value;

};

struct  DataCollection {
       /*
        from 
org.astrogrid.acr.ivoa.resource.DataCollection
A logical grouping of data which, in general, is composed of one 
           or more accessible datasets.  A collection can contain any
           combination of images, spectra, catalogs, or other data.
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from HasCoverage */
struct Coverage coverage;

/* data members from DataCollection */
ListOfResourceName facilities;
ListOfResourceName instruments;
ListOfJString rights;
ListOfFormat formats;
ListOfCatalog_Base catalogues;
struct AccessURL accessURL;

};

struct  DataService {
       /*
        from 
org.astrogrid.acr.ivoa.resource.DataService
A service for accessing astronomical data.
  Previously was called SkyService.
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from Service */
ListOfJString rights;
ListOfCapability_Base capabilities;

/* data members from HasCoverage */
struct Coverage coverage;

/* data members from DataService */
ListOfResourceName facilities;
ListOfResourceName instruments;

};

struct  DatabaseBean {
       /*
        from 
org.astrogrid.acr.astrogrid.DatabaseBean
!!Deprecated: 
				prefer the 'Catalog' type instead.
			describes a  single database in a TablularDB registry entry
   */
    enum AcrType _type;

/* data members from Catalog */
JString description;
JString name;
ListOfTableBean_Base tables;

/* data members from DatabaseBean */

};

struct  ExecutionInformation {
       /*
        from 
org.astrogrid.acr.astrogrid.ExecutionInformation
description of  the progress of a remote process - e.g. a workflow job, or CEA application.
  
 <tt>getId()</tt> will return the execution identifier - either a job urn (for workfows) or an execution ivorn (for cea and other remote appilications).
   */
    enum AcrType _type;

/* data members from AbstractInformation */
IvornOrURI id;
JString name;

/* data members from ExecutionInformation */
JString description;
ACRDate startTime;
ACRDate finishTime;
JString status;

};

struct  ExecutionMessage {
       /*
        from 
org.astrogrid.acr.astrogrid.ExecutionMessage
A single message returned by a remote process - e.g.
 a workflow job or remote application
   */
    enum AcrType _type;

/* data members from ExecutionMessage */
JString content;
JString level;
JString source;
JString status;
ACRDate timestamp;

};

struct  FunctionBean {
       /*
        from 
org.astrogrid.acr.ivoa.FunctionBean
description of one ADQL function
   */
    enum AcrType _type;

/* data members from FunctionBean */
JString description;
JString name;
ListOfParameterBean parameters;

};

struct  Handler {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Handler
Implementation object that backs Resource instances. Only used internally.
   */
    enum AcrType _type;

/* data members from Handler */

};

struct  HarvestCapability {
       /*
        from 
org.astrogrid.acr.ivoa.resource.HarvestCapability
Capability of registries that can be harvested.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

/* data members from HarvestCapability */
int maxRecords;

};

struct  HasCoverage {
       /*
        from 
org.astrogrid.acr.ivoa.resource.HasCoverage
indicates that a registry resource contains coverage information.
   */
    enum AcrType _type;

/* data members from HasCoverage */
struct Coverage coverage;

};

struct  NodeInformation {
       /*
        from 
org.astrogrid.acr.astrogrid.NodeInformation
descripiton of the properties of a myspace resource (a file or folder).
 
 <tt>getId()</tt> will return a myspace resouce locator - an ivorn of form 
 <tt>ivo://<i>Community-Id</i>/<i>User-Id</i>#<i>File-Path</i></tt>
   */
    enum AcrType _type;

/* data members from AbstractInformation */
IvornOrURI id;
JString name;

/* data members from NodeInformation */
ACRKeyValueMap attributes;
ACRDate createDate;
ACRDate modifyDate;
long size;
IvornOrURI contentLocation;

};

struct  Organisation {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Organisation
A resource that brinds people togehter to persue participatio in VO applications.
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from Organisation */
ListOfResourceName facilities;
ListOfResourceName instruments;

};

struct  RegistryService {
       /*
        from 
org.astrogrid.acr.ivoa.resource.RegistryService
Resource describing a Registry Service
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from Service */
ListOfJString rights;
ListOfCapability_Base capabilities;

/* data members from RegistryService */
ListOfJString managedAuthorities;

};

struct  Resource {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Resource
Any entity or component of a VO application that is
           describable and identifiable by a IVOA Identifier.
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

};

struct  SearchCapability {
       /*
        from 
org.astrogrid.acr.ivoa.resource.SearchCapability
registry search capability..
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

/* data members from SearchCapability */
JString extensionSearchSupport;
int maxRecords;
ListOfJString optionalProtocol;

};

struct  Service {
       /*
        from 
org.astrogrid.acr.ivoa.resource.Service
a resource that can be invoked by a user to perform some action
           on its behalf.
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from Service */
ListOfJString rights;
ListOfCapability_Base capabilities;

};

struct  SesamePositionBean {
       /*
        from 
org.astrogrid.acr.cds.SesamePositionBean
Datastructure representing the position of an object known to Sesame.
   */
    enum AcrType _type;

/* data members from SesamePositionBean */
double dec;
double decErr;
JString oName;
JString oType;
JString posStr;
double ra;
double raErr;
JString target;
JString service;
ListOfJString aliases;

};

struct  SiapCapability_SkySize {
       /*
        from 
org.astrogrid.acr.ivoa.resource.SiapCapability.SkySize

   */
    enum AcrType _type;

/* data members from SiapCapability.SkySize */
double lat;
double lon;

};

struct  SiapCapability_ImageSize {
       /*
        from 
org.astrogrid.acr.ivoa.resource.SiapCapability.ImageSize

   */
    enum AcrType _type;

/* data members from SiapCapability.ImageSize */
int lat;
int lon;

};

struct  SiapCapability_SkyPos {
       /*
        from 
org.astrogrid.acr.ivoa.resource.SiapCapability.SkyPos

   */
    enum AcrType _type;

/* data members from SiapCapability.SkyPos */
double lat;
double lon;

};

struct  SiapCapability_Query {
       /*
        from 
org.astrogrid.acr.ivoa.resource.SiapCapability.Query

   */
    enum AcrType _type;

/* data members from SiapCapability.Query */
JString extras;
struct SiapCapability_SkyPos pos;
struct SiapCapability_SkySize size;
int verb;

};

struct  SiapCapability {
       /*
        from 
org.astrogrid.acr.ivoa.resource.SiapCapability
Descripiton of the Capabilities of a Simple Image Access Service.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

/* data members from SiapCapability */
JString imageServiceType;
int maxFileSize;
struct SiapCapability_SkySize maxImageExtent;
struct SiapCapability_ImageSize maxImageSize;
struct SiapCapability_SkySize maxQueryRegionSize;
struct SiapCapability_Query testQuery;
int maxRecords;

};

struct  SiapService {
       /*
        from 
org.astrogrid.acr.ivoa.resource.SiapService
Registrion description for a Simple Image Access Service.
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from Service */
ListOfJString rights;
ListOfCapability_Base capabilities;

/* data members from SiapService */

};

struct  SkyNodeColumnBean {
       /*
        from 
org.astrogrid.acr.ivoa.SkyNodeColumnBean
extension of column bean for sky node services, which provide further metadata
   */
    enum AcrType _type;

/* data members from ColumnBean */
JString datatype;
JString description;
JString name;
JString uCD;
JString unit;

/* data members from SkyNodeColumnBean */
int byteSize;
int precision;
int rank;

};

struct  SkyNodeTableBean {
       /*
        from 
org.astrogrid.acr.ivoa.SkyNodeTableBean
extension of table bean for Sky Node services, which return further metadata
   */
    enum AcrType _type;

/* data members from TableBean */
ListOfColumnBean_Base columns;
JString description;
JString name;

/* data members from SkyNodeTableBean */
JString primaryKey;
int rank;
ACRKeyValueMap relations;
int rows;

};

struct  TableBeanComparator {
       /*
        from 
org.astrogrid.acr.astrogrid.TableBeanComparator
comparator for table beans.
 
 tests for equality on name of the table only
   */
    enum AcrType _type;

/* data members from TableBeanComparator */

};

struct  TabularDB {
       /*
        from 
org.astrogrid.acr.ivoa.resource.TabularDB
!!Deprecated: 
				I suspect this type is going to be removed. Prefer DataCollection instead.
			A type of IVOA resource describing (exactly) one database independently of
        any service giving access to the database. The mandatory core of the element 
        details the database itself. There may, optionally, be additional elements 
        describing the provenance and astronomical usage of the resource.
   */
    enum AcrType _type;

/* data members from Resource */
ListOfValidation validationLevel;
JString title;
IvornOrURI id;
JString shortName;
struct Curation curation;
struct Content content;
JString status;
JString created;
JString updated;
JString type;

/* data members from HasCoverage */
struct Coverage coverage;

/* data members from DataCollection */
ListOfResourceName facilities;
ListOfResourceName instruments;
ListOfJString rights;
ListOfFormat formats;
ListOfCatalog_Base catalogues;
struct AccessURL accessURL;

/* data members from TabularDB */
struct DatabaseBean database;

};

struct  UserInformation {
       /*
        from 
org.astrogrid.acr.astrogrid.UserInformation
Information about the currently logged in user.
 
 <ul>
 <li><tt>id</tt></li> - will be the user ivorn
 <li><tt>name</tt></li> - will be the user's name.
 </ul>
   */
    enum AcrType _type;

/* data members from AbstractInformation */
IvornOrURI id;
JString name;

/* data members from UserInformation */
JString community;
JString password;

};

struct  VoMonBean {
       /*
        from 
org.votech.VoMonBean
Description of a service's availabiltiy.
   */
    enum AcrType _type;

/* data members from VoMonBean */
int code;
IvornOrURI id;
long millis;
JString status;
JString timestamp;

};

/*
Unions for derived types - functions can return derived types

*/

   struct AbstractInformation_Base {
   enum AcrType _type;
   union {
       struct AbstractInformation abstractinformation;

   struct UserInformation userinformation;
   struct NodeInformation nodeinformation;
   struct ExecutionInformation executioninformation;
   } d;
};

   struct TableBean_Base {
   enum AcrType _type;
   union {
       struct TableBean tablebean;

   struct SkyNodeTableBean skynodetablebean;
   } d;
};

   struct Catalog_Base {
   enum AcrType _type;
   union {
       struct Catalog catalog;

   struct DatabaseBean databasebean;
   } d;
};

   struct ColumnBean_Base {
   enum AcrType _type;
   union {
       struct ColumnBean columnbean;

   struct SkyNodeColumnBean skynodecolumnbean;
   } d;
};

   struct Resource_Base {
   enum AcrType _type;
   union {
       struct Resource resource;

   struct Service service;
   struct CeaService ceaservice;
   struct SiapService siapservice;
   struct RegistryService registryservice;
   struct DataService dataservice;
   struct CatalogService catalogservice;
   struct ConeService coneservice;
   struct CeaApplication ceaapplication;
   struct DataCollection datacollection;
   struct TabularDB tabulardb;
   struct Organisation organisation;
   struct Authority authority;
   } d;
};

   struct Service_Base {
   enum AcrType _type;
   union {
       struct Service service;

   struct CeaService ceaservice;
   struct SiapService siapservice;
   struct RegistryService registryservice;
   struct DataService dataservice;
   struct CatalogService catalogservice;
   struct ConeService coneservice;
   } d;
};

   struct DataService_Base {
   enum AcrType _type;
   union {
       struct DataService dataservice;

   struct CatalogService catalogservice;
   } d;
};

   struct DataCollection_Base {
   enum AcrType _type;
   union {
       struct DataCollection datacollection;

   struct TabularDB tabulardb;
   } d;
};

   struct Capability_Base {
   enum AcrType _type;
   union {
       struct Capability capability;

   struct CeaServerCapability ceaservercapability;
   struct SiapCapability siapcapability;
   struct SearchCapability searchcapability;
   struct HarvestCapability harvestcapability;
   struct ConeCapability conecapability;
   } d;
};

   struct HasCoverage_Base {
   enum AcrType _type;
   union {
       struct HasCoverage hascoverage;

   } d;
};


    
/*

functions

*/
/* begin package astrogrid */
           
/* begin class astrogrid.applications
    Work with remote applications -  Compute and Data Query services.
 
 
 The Common Execution Architecture (CEA) provides a uniform way to describe and execute astronomical applications and data services on the VO.
 This interface provides methods to
 <ul>
 <li>Discover available applications</li>
 <li>Build invocation documents containing the correct parameters</li>
 <li>Submit invocation documents for execution on remote servers</li>
 <li>Monitor progress and retreive results of execution</li>
 </ul>
 
 Each new application invocation is assigned a new globally unique id.
  These id's  should be treated as opaque objects - the internal structure is still liable to change.See: 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/applications/design/CEADesignIVOANote.html">Common Execution Architecture - IVOA Proposal</a>
 <br/>
			 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/Workflow.html#element_tool">Tool Document Schema-Documentation</a>
			 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/AGParameterDefinition.html#type_parameter">Value Parameter Element Schema-Documentation</a>
			 
				<a href="http://www.astrogrid.org/viewcvs/astrogrid/workflow-objects/schema/">XSD Schemas</a>
 <br/>
			 
				<a href="doc-files/run-app-demo.py">Calling CEA services - example python script</a>
			 
				<a href="doc-files/runAppDemo.groovy">Calling CEA services - example groovy script</a>
			 
				<a href="../dialogs/doc-files/example-tool.xml"> Example Tool Document</a>
 <br/>
			 
				org.astrogrid.acr.ui.ApplicationLauncher
			 
				org.astrogrid.acr.astrogrid.ApplicationInformation
			 
				org.astrogrid.acr.dialogs.ToolEditor
			 
				org.astrogrid.acr.astrogrid.ExecutionInformation
			  */
	 
	
			
			
/* function astrogrid_applications_list()list remote applications available in the registry.
		
		
	Returns ListOfIvornOrURI - a list of the registry identifiers of available applications
       */
ListOfIvornOrURI astrogrid_applications_list ( );
			
			
/* function astrogrid_applications_getRegistryAdqlQuery()helper method - returns the ADQL/s query that should be passed to the registry to
 list all available applications.
 
 can be used as a starting point to build up filters, etc.
		
		
	Returns JString - an adql query string.
       */
JString astrogrid_applications_getRegistryAdqlQuery ( );
			
			
/* function astrogrid_applications_getRegistryXQuery()helper method - returns the Xquery that should be passed to the registry to
 list all available applications.
 
 can be used as a starting point to build up filters, etc.
		
		
	Returns JString - an xquery string.
       */
JString astrogrid_applications_getRegistryXQuery ( );
			
			
/* function astrogrid_applications_getCeaApplication(applicationName)get information for a specific application from the registry.
		
		applicationName - name of the application to hunt for(IvornOrURI)
		
	Returns struct CeaApplication - details of this application
       */
struct CeaApplication astrogrid_applications_getCeaApplication ( IvornOrURI);
			
			
/* function astrogrid_applications_getDocumentation(applicationName)get formatted information about an application
		
		applicationName - (IvornOrURI)
		
	Returns JString - formatted, human-readable information about the application
       */
JString astrogrid_applications_getDocumentation ( IvornOrURI);
			
			
/* function astrogrid_applications_createTemplateDocument(applicationName, interfaceName)create a template invocation document for a particular application.
 
 Examines the registry entry for this application, and constructs a template document containing fields for the required input and output parameters.
		
		applicationName - the application to create the template for(IvornOrURI)
		interfaceName - interface of this application to create a template from.(JString)
		
	Returns XMLString - a tool document. (xml form)
       */
XMLString astrogrid_applications_createTemplateDocument ( IvornOrURI, JString);
			
			
/* function astrogrid_applications_createTemplateStruct(applicationName, interfaceName)create a template invocation datastucture for a particular application.
 
 Examines the registry entry for this application, and constructs a template document containing fields for the required input and output parameters. 
 <br />
 The datastructure returned is equivalent to the document returned by {@link #createTemplateDocument(URI, String)} - 
 this is a convenience method for scripting languages with minimal
 xml abilities.
		
		applicationName - the application to create the template for(IvornOrURI)
		interfaceName - interface of this application to create a template from.(JString)
		
	Returns ACRKeyValueMap - a tool object (structure)
       */
ACRKeyValueMap astrogrid_applications_createTemplateStruct ( IvornOrURI, JString);
			
			
/* function astrogrid_applications_convertDocumentToStruct(document)convert a invocation document to a invocation structure. 
 <br />
 Translates an invocation document between two equvalent forms - a datastructure and a document
		
		document - a tool document(XMLString)
		
	Returns ACRKeyValueMap - the equvalent tool structure
       */
ACRKeyValueMap astrogrid_applications_convertDocumentToStruct ( XMLString);
			
			
/* function astrogrid_applications_convertStructToDocument(structure)convert a invocation structure to the equivalent document.
		
		structure - a tool structure(ACRKeyValueMap)
		
	Returns XMLString - the equivalent tool document
       */
XMLString astrogrid_applications_convertStructToDocument ( ACRKeyValueMap);
			
			
/* function astrogrid_applications_validate(document)Validate an invocation document against the  application's description
 <br />
 Verifies that all required parameters are present.
		
		document - tool document to validate(XMLString)
		
	Returns void - 
       */
void astrogrid_applications_validate ( XMLString);
			
			
/* function astrogrid_applications_validateStored(documentLocation)Validate an invocation document (referenced by url) against 
                an application description
		
		documentLocation - location of a resource containing the tool document to validate(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_applications_validateStored ( IvornOrURI);
			
			
/* function astrogrid_applications_listServersProviding(applicationId)list the remote servers that provides a particular application.
 
  (It's possible, for CEA especially, that an application may be provided by multiple servers)
		
		applicationId - registry identifier of the application to search servers for.(IvornOrURI)
		
	Returns ListOfService_Base - list of registry summaries of cea servers that support this application
       */
ListOfService_Base astrogrid_applications_listServersProviding ( IvornOrURI);
			
			
/* function astrogrid_applications_submit(document)submit an invocation document for execution..
 
 No particular remote server is specified - the system will select a suitable one.
		
		document - tool document to execute(XMLString)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_applications_submit ( XMLString);
			
			
/* function astrogrid_applications_submitTo(document, server)submit an invocation document for execution  on a named remote server.
		
		document - tool document to execute(XMLString)
		server - remote server to execute on(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_applications_submitTo ( XMLString, IvornOrURI);
			
			
/* function astrogrid_applications_submitStored(documentLocation)a variant of {@link #submit} where invocation document is stored somewhere and referenced by URI.
		
		documentLocation - pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_applications_submitStored ( IvornOrURI);
			
			
/* function astrogrid_applications_submitStoredTo(documentLocation, server)variant of {@link #submitTo} where tool document is referenced by URI. 
      * @param documentLocation pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols
		
		documentLocation - (IvornOrURI)
		server - remote server to execute on(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_applications_submitStoredTo ( IvornOrURI, IvornOrURI);
			
			
/* function astrogrid_applications_cancel(executionId)cancel execution of an application.
		
		executionId - id of execution to cancel(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_applications_cancel ( IvornOrURI);
			
			
/* function astrogrid_applications_getExecutionInformation(executionId)retrive  information about an application execution.
		
		executionId - id of application to query(IvornOrURI)
		
	Returns struct ExecutionInformation - summary of this execution
       */
struct ExecutionInformation astrogrid_applications_getExecutionInformation ( IvornOrURI);
			
			
/* function astrogrid_applications_getResults(executionid)retreive results of the application execution .
		
		executionid - id of application to query(IvornOrURI)
		
	Returns ACRKeyValueMap - results of this execution (name - value pairs). Note that this will only be the actual results for <b>direct</b> output parameters. For output parameters specified as <b>indirect</b>, the value returned  will be the URI pointing to the location where the results are stored.
       */
ACRKeyValueMap astrogrid_applications_getResults ( IvornOrURI);
      /* end class
      astrogrid.applications
      */
   /* begin class astrogrid.community
    astogrid identity and authentication.
 
 At the moment provides login ability. Later will provide access to permissioning and quota information for the current user.
 <img src="doc-files/login.png"/> */
	 
	
			
			
/* function astrogrid_community_login(username, password, community)login to astrogrid - identify yourself
		
		username - user name (e.g. <tt>fredbloggs</tt>)(JString)
		password - password for this user(JString)
		community - community the user is registered with (e.g. <tt>uk.ac.astogrid</tt> )(JString)
		
	Returns void - 
       */
void astrogrid_community_login ( JString, JString, JString);
			
			
/* function astrogrid_community_getUserInformation()access information about the currently logged in user.
 
 <b>This method forces login if not already logged in.</b>
		
		
	Returns struct UserInformation - information about the current user.
       */
struct UserInformation astrogrid_community_getUserInformation ( );
			
			
/* function astrogrid_community_logout()log current user out of astrogrid
		
		
	Returns void - 
       */
void astrogrid_community_logout ( );
			
			
/* function astrogrid_community_isLoggedIn()verify user is currently logged in.
		
		
	Returns BOOL - true if the user is logged in
       */
BOOL astrogrid_community_isLoggedIn ( );
			
			
/* function astrogrid_community_guiLogin()display the login dialogue to prompt the user for input, and then log in
		
		
	Returns void - 
       */
void astrogrid_community_guiLogin ( );
      /* end class
      astrogrid.community
      */
   /* begin class astrogrid.jobs
    Deprecated: 
				JES is no longer supported.
			Execute and control workflows on remote job servers.


 For now, an interface to a single JES server  - which is configured in the system properties for the ACR.
 In future, JES servers should be registered, and a default server associated with a user in a community .
 It may also be necessary to be able to browse a selection of job servers, and maybe aggregate a user's jobs from a set of servers.
 <br />
 Each workflow submitted is assigned a globally-unique identifier. This takes the form of a URI, but should be treated as opaque - the structure is
 liable to change  once JES servers are registered.See: 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/jes/userguide-architecture.html">Workflow Userguide</a>
			 
				<a href="http://wiki.astrogrid.org/bin/view/Astrogrid/JesScriptingFAQ">Workflow Scripting FAQ</a> 
 <br/>
			 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/Workflow.html">Workflow Schema-Documentation</a>
			 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/ExecutionRecord.html">Execution Record Schema-Document</a>
			 
				<a href="http://www.astrogrid.org/viewcvs/astrogrid/workflow-objects/schema/">XSD Schemas</a>
 <br/>
			 
				<a href="doc-files/example-workflow.xml">Example workflow</a>
			 
				<a href="doc-files/example-workflow-transcript.xml">Example execution transcript</a>
			 
				<a href="doc-files/example-workflow-transcript.html">Html-formatted execution transcript</a>
 <b/>
			 
				org.astrogrid.acr.astrogrid.Applications Information about executing single applications
			 
				org.astrogrid.acr.astrogrid.Myspace Information about distributed file storage
			 
				org.astrogrid.acr.astrogrid.RemoteProcessManager for more general process management - methods
 in this class are convenicne wrapper around operations in <tt>RemoteProcessManager</tt>
 <br/>
			 
				org.astrogrid.acr.ui.JobMonitor
			 
				org.astrogrid.acr.ui.WorkflowBuilder
			 
				org.astrogrid.acr.astrogrid.ExecutionInformation
			  */
	 
	
			
			
/* function astrogrid_jobs_list()list the jobs for the current user.
		
		
	Returns ListOfIvornOrURI - list of identifiers for the user's jobs (both current and completed jobs )
       */
ListOfIvornOrURI astrogrid_jobs_list ( );
			
			
/* function astrogrid_jobs_listFully()list summaries of the jobs for the current user.
		
		
	Returns ListOfExecutionInformation - a beanful of information on each job
       */
ListOfExecutionInformation astrogrid_jobs_listFully ( );
			
			
/* function astrogrid_jobs_createJob()create a new initialized workflow document, suitable as a starting point for building workflows.
		
		
	Returns XMLString - a workflow document - a <tt>workflow</tt> document in the the <tt>http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace
       */
XMLString astrogrid_jobs_createJob ( );
			
			
/* function astrogrid_jobs_wrapTask(taskDocument)wrap a remote application invocation document, to create a single-step workflow.
		
		taskDocument - - a task document in the <tt> http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace(XMLString)
		
	Returns XMLString - a workflow document with a single step that executes the parameter task - a <tt>workflow</tt> document in the the <tt>http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace
       */
XMLString astrogrid_jobs_wrapTask ( XMLString);
			
			
/* function astrogrid_jobs_getJobTranscript(jobURN)retrieve the execution transcript for a job.
		
		jobURN - the identifier of the job to retrieve(IvornOrURI)
		
	Returns XMLString - a workflow transcript  document - A <tt>workflow</tt> document in   the <tt>http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace, annotated with execution information from the   <tt>http://www.astrogrid.org/schema/ExecutionRecord/v1</tt> namespace.
       */
XMLString astrogrid_jobs_getJobTranscript ( IvornOrURI);
			
			
/* function astrogrid_jobs_getJobInformation(jobURN)retrive summary for a job.
		
		jobURN - the identifier of the job to summarize(IvornOrURI)
		
	Returns struct ExecutionInformation - information about this job.
       */
struct ExecutionInformation astrogrid_jobs_getJobInformation ( IvornOrURI);
			
			
/* function astrogrid_jobs_cancelJob(jobURN)cancel the exeuciton of a running job.
		
		jobURN - identifier of the job to cancel.(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_jobs_cancelJob ( IvornOrURI);
			
			
/* function astrogrid_jobs_deleteJob(jobURN)delete all record of a job from the job server.
		
		jobURN - identifier of the job to delete(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_jobs_deleteJob ( IvornOrURI);
			
			
/* function astrogrid_jobs_submitJob(workflow)submit a workflow for execution.
		
		workflow - workflow document to submit(XMLString)
		
	Returns IvornOrURI - a unique identifier for this new job
       */
IvornOrURI astrogrid_jobs_submitJob ( XMLString);
			
			
/* function astrogrid_jobs_submitStoredJob(workflowReference)submit a workflow (stored in a file) for execution.
		
		workflowReference - url  refernce to the workflow document to submit (may be file://, http://, ftp:// or ivo:// - a myspace reference.)(IvornOrURI)
		
	Returns IvornOrURI - a unique identifier for this new job.
       */
IvornOrURI astrogrid_jobs_submitStoredJob ( IvornOrURI);
      /* end class
      astrogrid.jobs
      */
   /* begin class astrogrid.myspace
    Work with  Myspace - a distributed storage system, AstroGrid's implementation of VOSpace.
 
 All resources in myspace are uniquely identified by a myspace resource identifier - which is an URI of form
  * <tt>ivo://<i>Community-Id</i>/<i>User-Id</i>#<i>File-Path</i></tt>. However, for convenience methods in this interface also accept an
 abridged form of reference - <tt>#<i>File-Path</i></tt> - this is resolved relative to the currently logged-in user. The abridged
 form is more concise, and means hard-coded file references can be avoided if needed.   
 <br/>
 <b>NB</b>: At present this interface doesn't contain suficient functionality to work with myspace in a truly efficient manner. Expect a cleaner, more efficient interface
 to myspace to be added later. However this interface and it's current methods will remain available, and won't be deprecated.See: 
				<a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaGridAndWebServices#VO_Store_Proposal">IVOA VOStore</a>
			 
				org.astrogrid.acr.ui.MyspaceBrowser
			 
				org.astrogrid.acr.dialogs.ResourceChooser
			 
				org.astrogrid.acr.astrogrid.NodeInformation
			  */
	 
	
			
			
/* function astrogrid_myspace_getHome()retreive the identifier of the current user's home folder in myspace. 
 
 Each user has a single root folder. this method returns the name of it.
		
		
	Returns IvornOrURI - uri of the home folder - typically has form  <tt>ivo://<i>Community-Id</i>/<i>User-Id</i>#</tt>
       */
IvornOrURI astrogrid_myspace_getHome ( );
			
			
/* function astrogrid_myspace_exists(ivorn)test whether a myspace resource exists.
		
		ivorn - uri to check (full or abridged form)(IvornOrURI)
		
	Returns BOOL - true if the resource exists
       */
BOOL astrogrid_myspace_exists ( IvornOrURI);
			
			
/* function astrogrid_myspace_getNodeInformation(ivorn)access metadata about a myspace resource.
 <b>NB: </b> At the moment, this is a costly operation.
		
		ivorn - resource to investigate(IvornOrURI)
		
	Returns struct NodeInformation - a beanful of information
       */
struct NodeInformation astrogrid_myspace_getNodeInformation ( IvornOrURI);
			
			
/* function astrogrid_myspace_createFile(ivorn)create a new myspace file. 
 
 Any parent folders that are missing will be created too.
		
		ivorn - the resource to create.(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_createFile ( IvornOrURI);
			
			
/* function astrogrid_myspace_createFolder(ivorn)create a new myspace folder.
 
 Any parent folders that are missing will be created too.
		
		ivorn - the resource to create.(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_createFolder ( IvornOrURI);
			
			
/* function astrogrid_myspace_createChildFolder(parentIvorn, name)create a child folder of  the specified resource.
		
		parentIvorn - parent of the new resource (must be a folder)(IvornOrURI)
		name - name of the new folder(JString)
		
	Returns IvornOrURI - the ivorn of the new folder
       */
IvornOrURI astrogrid_myspace_createChildFolder ( IvornOrURI, JString);
			
			
/* function astrogrid_myspace_createChildFile(parentIvorn, name)create a child file of the specified resource.
		
		parentIvorn - parent of the new resource (must be a folder)(IvornOrURI)
		name - name of the new file(JString)
		
	Returns IvornOrURI - the ivorn of the new file
       */
IvornOrURI astrogrid_myspace_createChildFile ( IvornOrURI, JString);
			
			
/* function astrogrid_myspace_getParent(ivorn)retrieve the ID  of the parent of a myspace resource
		
		ivorn - uri of the resource to find parent for(IvornOrURI)
		
	Returns IvornOrURI - uri of the parent
       */
IvornOrURI astrogrid_myspace_getParent ( IvornOrURI);
			
			
/* function astrogrid_myspace_list(ivorn)list the names of the children (files and folders) of a myspace folder
		
		ivorn - uri of the folder to inspect(IvornOrURI)
		
	Returns ListOfJString - an array of the names of the contents.
       */
ListOfJString astrogrid_myspace_list ( IvornOrURI);
			
			
/* function astrogrid_myspace_listIvorns(ivorn)list the identifiers of the children ( files and folders)  of a myspace folder
		
		ivorn - uri of the folder to inspect(IvornOrURI)
		
	Returns ListOfIvornOrURI - an array of the ivorns of the contents.
       */
ListOfIvornOrURI astrogrid_myspace_listIvorns ( IvornOrURI);
			
			
/* function astrogrid_myspace_listNodeInformation(ivorn)list the node information objects for the children ( files and folders)  of a myspace folder   
 
 <b>NB: </b> Expensive operation at present.
		
		ivorn - uri of the folder to inspect(IvornOrURI)
		
	Returns ListOfNodeInformation - an array of the node information objects.
       */
ListOfNodeInformation astrogrid_myspace_listNodeInformation ( IvornOrURI);
			
			
/* function astrogrid_myspace_refresh(ivorn)refresh the metadata held about  a myspace resource with the server.
 <br/>
 For performance, metadata about myspace resources is used in a LRU cache. This method forces the ACR to re-query the myspace server
 about this resource.
		
		ivorn - resource to refresh(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_refresh ( IvornOrURI);
			
			
/* function astrogrid_myspace_delete(ivorn)delete a myspace resource
		
		ivorn - uri of the resource to delete(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_delete ( IvornOrURI);
			
			
/* function astrogrid_myspace_rename(srcIvorn, newName)rename a myspace resource
		
		srcIvorn - uri of the resource to renam(IvornOrURI)
		newName - new name for this resource(JString)
		
	Returns IvornOrURI - uri pointing to the renamed resource (original uri may now be invalid)
       */
IvornOrURI astrogrid_myspace_rename ( IvornOrURI, JString);
			
			
/* function astrogrid_myspace_move(srcIvorn, newParentIvorn, newName)move a myspace resource
		
		srcIvorn - ivorn of the resource to move(IvornOrURI)
		newParentIvorn - ivorn of the new parent(IvornOrURI)
		newName - new name for this resource.(JString)
		
	Returns IvornOrURI - uri pointing to the moved resouce (original uri will now be invalid)
       */
IvornOrURI astrogrid_myspace_move ( IvornOrURI, IvornOrURI, JString);
			
			
/* function astrogrid_myspace_changeStore(srcIvorn, storeIvorn)relocate a myspace resource to a different store.
 
 The relocated file remains in the same position in the user's myspace filetree. However, this method moves the data associated with the 
 file from one filestore to another.
		
		srcIvorn - uri of the resource to relocate(IvornOrURI)
		storeIvorn - uri of the store server to relocat to.(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_changeStore ( IvornOrURI, IvornOrURI);
			
			
/* function astrogrid_myspace_copy(srcIvorn, newParentIvorn, newName)make a copy of a resource
		
		srcIvorn - uri of the resource to copy(IvornOrURI)
		newParentIvorn - uri of the folder to copy to(IvornOrURI)
		newName - name to copy to(JString)
		
	Returns IvornOrURI - uri pointing to the resource copy
       */
IvornOrURI astrogrid_myspace_copy ( IvornOrURI, IvornOrURI, JString);
			
			
/* function astrogrid_myspace_read(ivorn)read the content of a myspace resource directly.
 
 <b>NB:</b> not a good idea for large files. in this case use {@link #copyContentToURL(URI, URL) } or {@link #getReadContentURL(URI) }
		
		ivorn - resource to read(IvornOrURI)
		
	Returns JString - content of this resource (as a string)
       */
JString astrogrid_myspace_read ( IvornOrURI);
			
			
/* function astrogrid_myspace_write(ivorn, content)Write data to a myspace resource.
 
 <b>NB: </b> not a good idea for large files. In this case use {@link #copyURLToContent(URL, URI) }
		
		ivorn - resource to write to(IvornOrURI)
		content - the data to write(JString)
		
	Returns void - 
       */
void astrogrid_myspace_write ( IvornOrURI, JString);
			
			
/* function astrogrid_myspace_readBinary(ivorn)read the binary content of a myspace resource directly.
 
 <b>NB: </b> not a good idea for large files. in this case use {@link #copyContentToURL(URI, URL) } or {@link #getReadContentURL(URI) }
		
		ivorn - resource to read(IvornOrURI)
		
	Returns ListOfchar - byte array of the contents of this resource
       */
ListOfchar astrogrid_myspace_readBinary ( IvornOrURI);
			
			
/* function astrogrid_myspace_writeBinary(ivorn, content)Write binary data to a myspace resource.

<b>NB: </b> not a good idea for large files. In this case use {@link #copyURLToContent(URL, URI) }
		
		ivorn - resource to write to(IvornOrURI)
		content - the data to write(ListOfchar)
		
	Returns void - 
       */
void astrogrid_myspace_writeBinary ( IvornOrURI, ListOfchar);
			
			
/* function astrogrid_myspace_getReadContentURL(ivorn)compute a URL which can then be read to access the contents (data) of a myspace resource.
		
		ivorn - resource to read(IvornOrURI)
		
	Returns URLString - a url from which the contents of the resource can be read
       */
URLString astrogrid_myspace_getReadContentURL ( IvornOrURI);
			
			
/* function astrogrid_myspace_getWriteContentURL(ivorn)compute  a URL which can then be written to set the contents (i.e. data) of a myspace resource.
		
		ivorn - resource to write to(IvornOrURI)
		
	Returns URLString - a url to  which the contents of the resource can be written
       */
URLString astrogrid_myspace_getWriteContentURL ( IvornOrURI);
			
			
/* function astrogrid_myspace_transferCompleted(ivorn)Notify the filemanager  server that the data for a filestore node has been changed. 
 
 This method must be called after storing data in a myspace file via the URL returned by {@link #getWriteContentURL}.
 There's no need to call this method when storing data using any other method
		
		ivorn - the myspace resource just written to(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_transferCompleted ( IvornOrURI);
			
			
/* function astrogrid_myspace_copyContentToURL(ivorn, destination)Copy the contents (data) of a resource out of myspace into a URL location.
		
		ivorn - the myspace resource to write out(IvornOrURI)
		destination - a writable URL - file:/, http:/ or ftp:/ protocol(URLString)
		
	Returns void - 
       */
void astrogrid_myspace_copyContentToURL ( IvornOrURI, URLString);
			
			
/* function astrogrid_myspace_copyURLToContent(src, ivorn)Copy the contents (data) of a URL location into a myspace resource
		
		src - url to read data from - file:/, http:/ or ftp:/ protocol.(URLString)
		ivorn - the myspace resource to store the data in.(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_copyURLToContent ( URLString, IvornOrURI);
			
			
/* function astrogrid_myspace_listStores()List the available filestores
		
		
	Returns ListOfService_Base - an array of service descriptions.
       */
ListOfService_Base astrogrid_myspace_listStores ( );
      /* end class
      astrogrid.myspace
      */
   /* begin class astrogrid.publish
    Publish to a registry. */
	 
	
			
			
/* function astrogrid_publish_register(registry, entry)Publish a resource in a registry.
		
		registry - the IVOA identifier of the registry.(IvornOrURI)
		entry - the resource to be published.(XMLString)
		
	Returns void - 
       */
void astrogrid_publish_register ( IvornOrURI, XMLString);
      /* end class
      astrogrid.publish
      */
   /* begin class astrogrid.processManager
    a general manager for the execution , monitoring, and control of all remote processes.

 RemoteProcessManager unifies the  functionality in {@link org.astrogrid.acr.astrogrid.Jobs} and {@link org.astrogrid.acr.astrogrid.Applications}
 and provides additional features - notably ability to register callbacks for progress notifications. It is still valid to use the <tt>Jobs</tt> or <tt>Applications</tt>, 
 however, this interface also knows how to invoke other kinds of service, and provides a uniform interface to cea, cone, siap, ssap services. */
	 
	
			
			
/* function astrogrid_processManager_list()list current remote processes  belonging to the current user
		
		
	Returns ListOfIvornOrURI - list of identifiers for the user's remote processes that are currently being managed by AR
       */
ListOfIvornOrURI astrogrid_processManager_list ( );
			
			
/* function astrogrid_processManager_submit(document)Submit a document for execution.
 
 No particular  server is specified - the system will choose a suitable server.
		
		document - the document to execute - a program / script / workflow / tool document(XMLString)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_processManager_submit ( XMLString);
			
			
/* function astrogrid_processManager_submitTo(document, server)Submit a document for execution  on a named server
		
		document - the document to execute - a workflow, cea task, etc(XMLString)
		server - server to execute on(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_processManager_submitTo ( XMLString, IvornOrURI);
			
			
/* function astrogrid_processManager_submitStored(documentLocation)variant of {@link #submit} where document is stored somewhere and referenced by URI
		
		documentLocation - pointer to document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_processManager_submitStored ( IvornOrURI);
			
			
/* function astrogrid_processManager_submitStoredTo(documentLocation, server)variant of {@link #submitTo} where document is referenced by URL 
      * @param documentLocation pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols
		
		documentLocation - (IvornOrURI)
		server - to execute on(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_processManager_submitStoredTo ( IvornOrURI, IvornOrURI);
			
			
/* function astrogrid_processManager_halt(executionId)halt execution of a process
		
		executionId - id of execution to cancel(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_processManager_halt ( IvornOrURI);
			
			
/* function astrogrid_processManager_delete(executionId)delete all record of a process
		
		executionId - (IvornOrURI)
		
	Returns void - 
       */
void astrogrid_processManager_delete ( IvornOrURI);
			
			
/* function astrogrid_processManager_getExecutionInformation(executionId)get  information about an process execution
		
		executionId - id of application to query(IvornOrURI)
		
	Returns struct ExecutionInformation - summary of this execution
       */
struct ExecutionInformation astrogrid_processManager_getExecutionInformation ( IvornOrURI);
			
			
/* function astrogrid_processManager_getMessages(executionId)return the messages received from a remote process
		
		executionId - id of process to query/(IvornOrURI)
		
	Returns ListOfExecutionMessage - an array of all messages received from process
       */
ListOfExecutionMessage astrogrid_processManager_getMessages ( IvornOrURI);
			
			
/* function astrogrid_processManager_getResults(executionid)Retreive results of the application execution
		
		executionid - id of application to query(IvornOrURI)
		
	Returns ACRKeyValueMap - results of this execution (name - value pairs).   Note that this will only be the actual results for <b>direct</b> output parameters. For output parameters specified as <b>indirect</b>, the value returned  will be the URI pointing to the location where the results are stored.
       */
ACRKeyValueMap astrogrid_processManager_getResults ( IvornOrURI);
			
			
/* function astrogrid_processManager_getSingleResult(executionId, resultName)convenience method to retreive a single result from an application executioin.
 
 equaivalent to <tt>getResults(execId).get(resultName)</tt>, but may be more convenient from some scripting languages, or cases
 where there's only a single result returned.
		
		executionId - id of the application to query(IvornOrURI)
		resultName - name of the result to return. If the name is null or unrecognized AND this application only returns one result, that is returned.(JString)
		
	Returns JString - a result. Never null.
       */
JString astrogrid_processManager_getSingleResult ( IvornOrURI, JString);
      /* end class
      astrogrid.processManager
      */
   /* begin class astrogrid.stap
    Query for Images from Simple Time Access Protocol (STAP) servicesSee: 
				http://software.astrogrid.org/schema/vo-resource-types/Stap/v0.1/Stap.xsd
			  */
	 
	
			
			
/* function astrogrid_stap_constructQuery(service, start, end)construct query on time -  START, END
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQuery ( IvornOrURI, ACRDate, ACRDate);
			
			
/* function astrogrid_stap_constructQueryF(service, start, end, format)construct query on time and format -  START, DATE, FORMAT
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		format - format of images or time series data (as described in stap spec)     *(JString)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQueryF ( IvornOrURI, ACRDate, ACRDate, JString);
			
			
/* function astrogrid_stap_constructQueryP(service, start, end, ra, dec, size)construct query on time and position - START, END RA, DEC, SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		size - radius of cone ( as described in siap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQueryP ( IvornOrURI, ACRDate, ACRDate, double, double, double);
			
			
/* function astrogrid_stap_constructQueryPF(service, start, end, ra, dec, size, format)construct query on time, position and format - START, END, RA, DEC, SIZE, FORMAT
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		size - radius of cone ( as described in siap spec)(double)
		format - format of images or time series data (as described in stap spec)     *(JString)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQueryPF ( IvornOrURI, ACRDate, ACRDate, double, double, double, JString);
			
			
/* function astrogrid_stap_constructQueryS(service, start, end, ra, dec, ra_size, dec_size)construct query on time and full position START, END, RA, DEC, RA_SIZE, DEC_SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		ra_size - size of ra ( as described in siap spec)(double)
		dec_size - size of dec (as described in siap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQueryS ( IvornOrURI, ACRDate, ACRDate, double, double, double, double);
			
			
/* function astrogrid_stap_constructQuerySF(service, start, end, ra, dec, ra_size, dec_size, format)construct query on time, full position and format START, END, RA, DEC, RA_SIZE, DEC_SIZE, FORMAT
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		ra_size - size of ra ( as described in siap spec)(double)
		dec_size - size of dec (as described in siap spec)(double)
		format - format of images or time series data (as described in stap spec)     *(JString)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQuerySF ( IvornOrURI, ACRDate, ACRDate, double, double, double, double, JString);
			
			
/* function astrogrid_stap_addOption(query, optionName, optionValue)add an option to a previously constructed query
		
		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)
		
	Returns URLString - <tt>query</tt> with the option appended.
       */
URLString astrogrid_stap_addOption ( URLString, JString, JString);
			
			
/* function astrogrid_stap_execute(query)execute a DAL query, returning a datastructure
		
		query - query url to execute(URLString)
		
	Returns ListOfACRKeyValueMap - A model the DAL query response as a list of  of rows. Each row is represented is a map between UCD keys or datamodel names   and values from the response
       */
ListOfACRKeyValueMap astrogrid_stap_execute ( URLString);
			
			
/* function astrogrid_stap_executeVotable(query)execute a DAL query, returning a votable document.
 
 This is a convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
		
		query - query url to execute(URLString)
		
	Returns XMLString - a votable document of results
       */
XMLString astrogrid_stap_executeVotable ( URLString);
			
			
/* function astrogrid_stap_executeAndSave(query, saveLocation)execute a DAL query and save the resulting document.
		
		query - query url to execute(URLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_stap_executeAndSave ( URLString, IvornOrURI);
			
			
/* function astrogrid_stap_saveDatasets(query, saveLocation)save the datasets pointed to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		
	Returns int - number of datasets saved.
       */
int astrogrid_stap_saveDatasets ( URLString, IvornOrURI);
			
			
/* function astrogrid_stap_saveDatasetsSubset(query, saveLocation, rows)save a subset of the datasets point to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset.(ACRList)
		
	Returns int - number of datasets saved.
       */
int astrogrid_stap_saveDatasetsSubset ( URLString, IvornOrURI, ACRList);
			
			
/* function astrogrid_stap_getRegistryAdqlQuery()helper method - returns an ADQL/s query that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an adql query string
       */
JString astrogrid_stap_getRegistryAdqlQuery ( );
			
			
/* function astrogrid_stap_getRegistryXQuery()helper method - returns an Xquery that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an xquery string
       */
JString astrogrid_stap_getRegistryXQuery ( );
      /* end class
      astrogrid.stap
      */
   
/* end package astrogrid */

/* begin package cds */
           
/* begin class cds.coordinate
    Astronomical Coordinate Web Service, from CDSSee: 
				http://cdsweb.u-strasbg.fr/cdsws/astroCoo.gml
			  */
	 
	
			
			
/* function cds_coordinate_convert(x, y, z, precision)convert a coordinate
		
		x - (10.0)(double)
		y - (15.0)(double)
		z - (20.0)(double)
		precision - (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)(int)
		
	Returns JString - a String like 03 45 14.3838 +47 58 07.990 (J2000.0)
       */
JString cds_coordinate_convert ( double, double, double, int);
			
			
/* function cds_coordinate_convertL(lon, lat, precision)convert a longitude-lattitude coordinate
		
		lon - (12.0)(double)
		lat - (45.0)(double)
		precision - (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)(int)
		
	Returns JString - a String like 04 21 34. +53 32.5 (J2000.0)
       */
JString cds_coordinate_convertL ( double, double, int);
			
			
/* function cds_coordinate_convertE(frame1, frame2, x, y, z, precision, equinox1, equinox2)convert a coordinate, considering equinox
		
		frame1 - (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)(int)
		frame2 - (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)(int)
		x - (10.0)(double)
		y - (15.0)(double)
		z - (20.0)(double)
		precision - (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)(int)
		equinox1 - (Julian Years or Besselian, unused for GAL, SGAL, ICRS)(double)
		equinox2 - (Julian Years or Besselian, unused for GAL, SGAL, ICRS)(double)
		
	Returns JString - a String like 150.4806267 -05.3873952 (Gal)
       */
JString cds_coordinate_convertE ( int, int, double, double, double, int, double, double);
			
			
/* function cds_coordinate_convertLE(frame1, frame2, lon, lat, precision, equinox1, equinox2)convert a longitude-latitude coordinate,  considering equinox
		
		frame1 - (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)(int)
		frame2 - (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)(int)
		lon - (12.0)(double)
		lat - (45.0)(double)
		precision - (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)(int)
		equinox1 - (Julian Years or Besselian, unused for GAL, SGAL, ICRS)(double)
		equinox2 - (Julian Years or Besselian, unused for GAL, SGAL, ICRS)(double)
		
	Returns JString - 
       */
JString cds_coordinate_convertLE ( int, int, double, double, int, double, double);
      /* end class
      cds.coordinate
      */
   /* begin class cds.glu
    Deprecated: 
				the webservice interface at CDS this client calls doesn't seem to be maintained
			Webservice to resolve  GLU  (Generateur de Liens Uniformes).tags.See: 
				http://cdsweb.u-strasbg.fr/cdsws/glu_resolver.gml
			  */
	 
	
			
			
/* function cds_glu_getURLfromTag(id)Resolve a tag.
		
		id - a tag to resolve (example : VizieR.MetaCat)(JString)
		
	Returns JString - Result :  URL corresponding to the tag (example : http://vizier.u-strasbg.fr/cgi-bin/votable?-meta)
       */
JString cds_glu_getURLfromTag ( JString);
      /* end class
      cds.glu
      */
   /* begin class cds.sesame
    Resolve object  names to position by querying  Simbad and/or NED and/or VizieR.
 
 {@link #resolve} resolves an object name to a datastructure containing position, error, aliases, etc.
 <br />
 {@link #sesame} and {@link #sesameChooseService(String, String, boolean, String)}
 provide low-level access to the raw webservice.See: 
				http://cdsweb.u-strasbg.fr/cdsws/name_resolver.gml
			 
				http://vizier.u-strasbg.fr/xml/sesame_1.dtd
			 
				http://vizier.u-strasbg.fr/xml/sesame_1.xsd
			  */
	 
	
			
			
/* function cds_sesame_resolve(name)Resolve an object name to a position using Sesame
		
		name - the object name to resolve(JString)
		
	Returns struct SesamePositionBean - a datastructure of positional information about the object.
       */
struct SesamePositionBean cds_sesame_resolve ( JString);
			
			
/* function cds_sesame_sesame(name, resultType)resolve a name to position
		
		name - the name to resolve(JString)
		resultType - <pre>            u = usual (corresponding to the deprecated Sesame(String name) output)                 H = html                  x = XML (XSD at http://vizier.u-strasbg.fr/xml/sesame_1.xsd, DTD at http://vizier.u-strasbg.fr/xml/sesame_1.dtd)                  p (for plain (text/plain)) and i (for all identifiers) options can be added to H or x                 </pre>(JString)
		
	Returns JString - format depending on the resultTtype parameter
       */
JString cds_sesame_sesame ( JString, JString);
			
			
/* function cds_sesame_sesameChooseService(name, resultType, all, service)resolve a name, selecing which services to use.
		
		name - the name to resolve(JString)
		resultType - * <pre>            u = usual (corresponding to the deprecated Sesame(String name) output)                 H = html                x = XML (XSD at http://vizier.u-strasbg.fr/xml/sesame_1.xsd, DTD at http://vizier.u-strasbg.fr/xml/sesame_1.dtd)                 p (for plain (text/plain)) and i (for all identifiers) options can be added to H or x                 </pre>(JString)
		all - true if all identifiers wanted(BOOL)
		service - <pre>    S=Simbad         N=NED          V=VizieR         A=all         </pre>         (examples : S to query in Simbad, NS to query in Ned if not found in Simbad,         NS to query in Ned and Simbad, A to query in Ned, Simbad and VizieR, ...)(JString)
		
	Returns JString - format depending on the resultTtype parameter
       */
JString cds_sesame_sesameChooseService ( JString, JString, BOOL, JString);
      /* end class
      cds.sesame
      */
   /* begin class cds.ucd
    Web Service for manipulating 
Unified Content Descriptors (UCD).See: 
				http://cdsweb.u-strasbg.fr/cdsws/ucdClient.gml
			  */
	 
	
			
			
/* function cds_ucd_UCDList()list of UCD1
		
		
	Returns JString - html document containing all ucd1
       */
JString cds_ucd_UCDList ( );
			
			
/* function cds_ucd_resolveUCD(ucd)resolve a UCD1 (wont work with UCD1+)
		
		ucd - ucd  the UCD1 to resolve (example : PHOT_JHN_V)(JString)
		
	Returns JString - sentence corresponding to the UCD1 (example : Johnson magnitude V (JHN))
       */
JString cds_ucd_resolveUCD ( JString);
			
			
/* function cds_ucd_UCDofCatalog(catalog_designation)
		
		catalog_designation - designes the catalog (example : I/239)(JString)
		
	Returns JString - list of UCD1 (in raw text) contained in a given catalog
       */
JString cds_ucd_UCDofCatalog ( JString);
			
			
/* function cds_ucd_translate(ucd)makes the translation of old-style UCD1 into the newer UCD1+ easier:
		
		ucd - The argument is a UCD1 (not UCD1+ !).(JString)
		
	Returns JString - String ucd. This function returns the default UCD1+ corresponding to an old-style UCD1.
       */
JString cds_ucd_translate ( JString);
			
			
/* function cds_ucd_upgrade(ucd)upgrade a ucd
		
		ucd - a deprecated UCD1+ (word or combination).                     Useful when the 'validate' method returns with code 2.(JString)
		
	Returns JString - String ucd. This function returns a valid UCD1+ corresponding to a deprecated word.                       It is useful when some reference words of the UCD1+ vocabulary are changed,                       and ensures backward compatibility.
       */
JString cds_ucd_upgrade ( JString);
			
			
/* function cds_ucd_validate(ucd)validate a ucd
		
		ucd - (e.g. ivoa:phot.mag;em.opt.B)(JString)
		
	Returns JString - String, this function checks that a UCD is well-formed <pre> The first word of the string is an error code, possibly followed by an explanation of the error.  A return value of 0 indicates no error (valid UCD).  The error-code results from the combination (logical OR) of the following values:   1: warning indicating use of non-standard namespace (not ivoa:)  2: use of deprecated word  4: use of non-existing word  8: syntax error (extra space or unallowed character)  </pre>
       */
JString cds_ucd_validate ( JString);
			
			
/* function cds_ucd_explain(ucd)returns description of a ucd
		
		ucd - (e.g. ivoa:phot.mag;em.opt.B)(JString)
		
	Returns JString - String, this function gives a human-readable explanation for a UCD composed of one or several words
       */
JString cds_ucd_explain ( JString);
			
			
/* function cds_ucd_assign(descr)Find the UCD associated with a description
		
		descr - Plain text description of a parameter to be described(JString)
		
	Returns JString - String ucd. This function returns the UCD1+ corresponding to the description
       */
JString cds_ucd_assign ( JString);
      /* end class
      cds.ucd
      */
   /* begin class cds.vizier
    Access VizieR catalogues from CDSSee: 
				http://cdsweb.u-strasbg.fr/cdsws/vizierAccess.gml
			  */
	 
	
			
			
/* function cds_vizier_cataloguesMetaData(target, radius, unit, text)get metadata about catalogues.
		
		target - (example : M31)(JString)
		radius - (example : 1.0)(double)
		unit - (example : arcmin)(JString)
		text - (author, ..., example : Ochsenbein)(JString)
		
	Returns XMLString - metadata about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesMetaData ( JString, double, JString, JString);
			
			
/* function cds_vizier_cataloguesMetaDataWavelength(target, radius, unit, text, wavelength)get metadata about catalogues
		
		target - (example : M31)(JString)
		radius - (example : 1.0)(double)
		unit - (example : arcmin)(JString)
		text - (author, ..., example : Ochsenbein)(JString)
		wavelength - (example : optical, Radio, like in the VizieR Web interface)(JString)
		
	Returns XMLString - metadata about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesMetaDataWavelength ( JString, double, JString, JString, JString);
			
			
/* function cds_vizier_cataloguesData(target, radius, unit, text)get catalogue data
		
		target - (example : M31)(JString)
		radius - (example : 1.0)(double)
		unit - (example : arcmin)(JString)
		text - (author, ..., example : Ochsenbein)(JString)
		
	Returns XMLString - data about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesData ( JString, double, JString, JString);
			
			
/* function cds_vizier_cataloguesDataWavelength(target, radius, unit, text, wavelength)get catalogue data for a wavelength
		
		target - (example : M31)(JString)
		radius - (example : 1.0)(double)
		unit - (example : arcmin)(JString)
		text - (author, ..., example : Ochsenbein)(JString)
		wavelength - (example : optical, Radio, like in the VizieR Web interface)(JString)
		
	Returns XMLString - data about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesDataWavelength ( JString, double, JString, JString, JString);
			
			
/* function cds_vizier_metaAll()get metadata for all catalogues
		
		
	Returns XMLString - all metadata about catalogues in VizieR (VOTable format)
       */
XMLString cds_vizier_metaAll ( );
      /* end class
      cds.vizier
      */
   
/* end package cds */

/* begin package dialogs */
           
/* begin class dialogs.registryGoogle
    prompt the user to select a registry resource by displaying  a more advanced registry chooser dialogue. */
	 
	
			
			
/* function dialogs_registryGoogle_selectResources(prompt, multiple)display the resource chooser dialogue.
		
		prompt - message to prompt user for input.(JString)
		multiple - if true, allow multiple selections.(BOOL)
		
	Returns ListOfResource_Base - 0 or more selected resources. never null.
       */
ListOfResource_Base dialogs_registryGoogle_selectResources ( JString, BOOL);
			
			
/* function dialogs_registryGoogle_selectResourcesAdqlFilter(prompt, multiple, adqlFilter)display the resource chooser dialogue, enabling only resources which match a filter.
		
		prompt - message to prompt user for input.(JString)
		multiple - if true, allow multiple selections.(BOOL)
		adqlFilter - adql-like 'where' clause.(JString)
		
	Returns ListOfResource_Base - 0 or more selected resources. never null.
       */
ListOfResource_Base dialogs_registryGoogle_selectResourcesAdqlFilter ( JString, BOOL, JString);
			
			
/* function dialogs_registryGoogle_selectResourcesXQueryFilter(prompt, multiple, xqueryFilter)display the resource chooser dialogue, enabling only resources which match a filter
		
		prompt - message to prompt user for input.(JString)
		multiple - if true, allow multiple selections.(BOOL)
		xqueryFilter - xpath-like condition(JString)
		
	Returns ListOfResource_Base - 0 or more selected resources. never null.
       */
ListOfResource_Base dialogs_registryGoogle_selectResourcesXQueryFilter ( JString, BOOL, JString);
      /* end class
      dialogs.registryGoogle
      */
   /* begin class dialogs.resourceChooser
    Prompt the user to select a local file / myspace resource / url by displaying a resource chooser dialogue.
  
 This is a  generalisation of the 'open/save file' browser that also allows local and  remote ( myspace / vospace / URL) resources to be selected.See: 
				org.astrogrid.acr.astrogrid.Myspace
			  */
	 
	
			
			
/* function dialogs_resourceChooser_chooseResource(title, enableRemote)show the resource chooser, and block until user selects a file
		
		title - title for the dialogue - e.g.'choose file to open'(JString)
		enableRemote - - if true,allow selection of a remote resource (myspace / vospace / URL). Selection of local resources is enabled always.(BOOL)
		
	Returns IvornOrURI - URI of the selected resource, or null if the user cancelled.
       */
IvornOrURI dialogs_resourceChooser_chooseResource ( JString, BOOL);
			
			
/* function dialogs_resourceChooser_chooseFolder(title, enableRemote)show the resource chooser, and block untiil user selects a folder.
		
		title - title for the dialogue - e.g.'choose file to open'(JString)
		enableRemote - - if true,allow selection of a remote resource (myspace / vospace / URL). Selection of local resources is enabled always.(BOOL)
		
	Returns IvornOrURI - URI of the selected folder, or null if the user cancelled.
       */
IvornOrURI dialogs_resourceChooser_chooseFolder ( JString, BOOL);
			
			
/* function dialogs_resourceChooser_fullChooseResource(title, enableVospace, enableLocal, enableURL)fully-configurable resource chooser - a generalization of {@link #chooseResource}
		
		title - title for the dialogue(JString)
		enableVospace - if true,allow selection of a remote myspace / vospace resource.(BOOL)
		enableLocal - if true, allow selection of local files(BOOL)
		enableURL - if true, enable the 'enter a URL' tab, so an arbitrary URL can be entered.(BOOL)
		
	Returns IvornOrURI - the URI of the selected resource, or null if the user cancelled
       */
IvornOrURI dialogs_resourceChooser_fullChooseResource ( JString, BOOL, BOOL, BOOL);
			
			
/* function dialogs_resourceChooser_fullChooseFolder(title, enableVospace, enableLocal, enableURL)fully-configurable resource chooser - a generalization of {@link #chooseFolder}
		
		title - title for the dialogue(JString)
		enableVospace - if true,allow selection of a remote myspace / vospace folder(BOOL)
		enableLocal - if true, allow selection of local folders(BOOL)
		enableURL - if true, enable the 'enter a URL' tab, so an arbitrary URL can be entered. No verification that this _is_ a folder in some sense is performed.(BOOL)
		
	Returns IvornOrURI - the URI of the selected folder, or null if the user cancelled
       */
IvornOrURI dialogs_resourceChooser_fullChooseFolder ( JString, BOOL, BOOL, BOOL);
      /* end class
      dialogs.resourceChooser
      */
   /* begin class dialogs.toolEditor
    Display the remote invocation document editor as a dialogue.
 
 Can be used to construct calls to Remote applications, such as data processor and catalogues.

 <br />
 Displays the content of a invocation document, augmented with data about this application loaded from
 the registry. Enables user to edit input ad output parameters (including using the {@link org.astrogrid.acr.dialogs.ResourceChooser}
 dialogue to select indirect parameters.
 <br />
 <img src="doc-files/pw-params.png"/>See: 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/Workflow.html#element_tool">Tool Document Schema-Documentation</a>
			 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/AGParameterDefinition.html#type_parameter">Value Parameter Element Schema-Documentation</a>
			 
				<a href="http://www.astrogrid.org/viewcvs/astrogrid/workflow-objects/schema/">XSD Schemas</a>
			 
				<a href="doc-files/example-tool.xml"> Example Tool Document</a>
			 
				org.astrogrid.acr.astrogrid.Applications
			  */
	 
	
			
			
/* function dialogs_toolEditor_edit(t)Prompt the user to edit a tool document
		
		t - document conforming to Tool schema(XMLString)
		
	Returns XMLString - an edited copy of this document
       */
XMLString dialogs_toolEditor_edit ( XMLString);
			
			
/* function dialogs_toolEditor_editStored(documentLocation)prompt the user to edit a tool document stored elsewhere
		
		documentLocation - location the tool document is stored at (http://, ftp://, ivo://)(IvornOrURI)
		
	Returns XMLString - edited copy of this document
       */
XMLString dialogs_toolEditor_editStored ( IvornOrURI);
			
			
/* function dialogs_toolEditor_selectAndBuild()prompt the user to select a VO service (application, datacenter, or something else) and construct a query against it.
		
		
	Returns XMLString - a new tool document
       */
XMLString dialogs_toolEditor_selectAndBuild ( );
      /* end class
      dialogs.toolEditor
      */
   
/* end package dialogs */

/* begin package ivoa */
           
/* begin class ivoa.adql
    Support for working with ADQL queries. */
	 
	
			
			
/* function ivoa_adql_s2x(s)convert an adq/s string to an adql/x document
		
		s - (JString)
		
	Returns XMLString - 
       */
XMLString ivoa_adql_s2x ( JString);
      /* end class
      ivoa.adql
      */
   /* begin class ivoa.cache
    data cache. */
	 
	
			
			
/* function ivoa_cache_flush()flush all cached data - for example registry entries
		
		
	Returns void - 
       */
void ivoa_cache_flush ( );
      /* end class
      ivoa.cache
      */
   /* begin class ivoa.cone
    Query catalogs using Cone-search services. */
	 
	
			
			
/* function ivoa_cone_addOption(query, optionName, optionValue)add an option to a previously constructed query
		
		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)
		
	Returns URLString - <tt>query</tt> with the option appended.
       */
URLString ivoa_cone_addOption ( URLString, JString, JString);
			
			
/* function ivoa_cone_execute(query)execute a DAL query, returning a datastructure
		
		query - query url to execute(URLString)
		
	Returns ListOfACRKeyValueMap - A model the DAL query response as a list of  of rows. Each row is represented is a map between UCD keys or datamodel names   and values from the response
       */
ListOfACRKeyValueMap ivoa_cone_execute ( URLString);
			
			
/* function ivoa_cone_executeVotable(query)execute a DAL query, returning a votable document.
 
 This is a convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
		
		query - query url to execute(URLString)
		
	Returns XMLString - a votable document of results
       */
XMLString ivoa_cone_executeVotable ( URLString);
			
			
/* function ivoa_cone_executeAndSave(query, saveLocation)execute a DAL query and save the resulting document.
		
		query - query url to execute(URLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void ivoa_cone_executeAndSave ( URLString, IvornOrURI);
			
			
/* function ivoa_cone_saveDatasets(query, saveLocation)save the datasets pointed to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		
	Returns int - number of datasets saved.
       */
int ivoa_cone_saveDatasets ( URLString, IvornOrURI);
			
			
/* function ivoa_cone_saveDatasetsSubset(query, saveLocation, rows)save a subset of the datasets point to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset.(ACRList)
		
	Returns int - number of datasets saved.
       */
int ivoa_cone_saveDatasetsSubset ( URLString, IvornOrURI, ACRList);
			
			
/* function ivoa_cone_getRegistryAdqlQuery()helper method - returns an ADQL/s query that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an adql query string
       */
JString ivoa_cone_getRegistryAdqlQuery ( );
			
			
/* function ivoa_cone_getRegistryXQuery()helper method - returns an Xquery that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an xquery string
       */
JString ivoa_cone_getRegistryXQuery ( );
			
			
/* function ivoa_cone_constructQuery(service, ra, dec, sr)construct a query on RA, DEC, SR
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension(double)
		dec - declination(double)
		sr - search radius(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute the query
       */
URLString ivoa_cone_constructQuery ( IvornOrURI, double, double, double);
      /* end class
      ivoa.cone
      */
   /* begin class ivoa.externalRegistry
    Query an arbitrary registry service.
 
 This interface gives access to a range of querying functions - for querying using xQuery, 
 keyword, adlq/s and adql/x. The functions either return a raw XML document, or a series of
 datastructures that contain the parsed information of the registry entries.s
 
 The first parameter to each query method is the endpoint URL of the registry service to connect to.
 Such endpoints either need to be already known, or can be located using the RegistryOfRegistries.
 
 In future, these functions will also accept the IVORN name of a registry - which 
 will then be resolved using the RegistryOfRegistries before processing the query.
 However, the RegistryOfRegistries isn't available yet.

These functions are useful when you want to access records in a registry
 other than the 'system configured' registry,
 or if you wish to access the raw xml of the records.
 For other cases, we recommend using the simple 'ivoa.Registry' service.See: 
				<a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>
			 
				<a href="http://www.ivoa.net/twiki/bin/view/IVOA/ResourceMetadata">Resource Metadata</a>
			 
				<a href="http://www.ivoa.net/Documents/latest/RM.html">IVOA Resource Metadata for the VO</a>
			 
				<a href="http://www.ivoa.net/Documents/latest/ADQL.html">ADQL Query Language Specification</a>
			 
				<a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaResReg">IVOA Registry Working Group</a>
			 
				<a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
			 
				org.astrogrid.acr.ui.RegistryBrowser
			 
				org.astrogrid.acr.ivoa.Registry - queries the system-configured registry - suitable for most cases.
			  */
	 
	
			
			
/* function ivoa_externalRegistry_adqlxSearchXML(registry, adqlx, identifiersOnly)Perform an ADQL/x query
 
 Equivalent to  {@link #adqlsSearchXML} but expects the full xml form of ADQL - this is less
 error prone than the adql/s variant until someone defines adql/s properly and implements parsers for it.
		
		registry - (IvornOrURI)
		adqlx - (XMLString)
		identifiersOnly - (BOOL)
		
	Returns XMLString - 
       */
XMLString ivoa_externalRegistry_adqlxSearchXML ( IvornOrURI, XMLString, BOOL);
			
			
/* function ivoa_externalRegistry_adqlxSearch(registry, adqlx)Perform an ADQL/x query, returning an array of datastructures.
 
 Equivalent to  {@link #adqlsSearch} but expects the full xml form of ADQL - which is less
 error prone than the adql/s variant until someone defines adql/s properly and implements parsers for it.
		
		registry - (IvornOrURI)
		adqlx - (XMLString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_externalRegistry_adqlxSearch ( IvornOrURI, XMLString);
			
			
/* function ivoa_externalRegistry_adqlsSearchXML(registry, adqls, identifiersOnly)Perform a ADQL/s query.
 Although convenient, prefer xquerySearch instead - as ADQL is less expressive and more poorly (especially adql/s) defined than xquery
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		adqls - (JString)
		identifiersOnly - (BOOL)
		
	Returns XMLString - xml document of search results -  a series of matching registry records contained within an element  called <tt>VOResources</tt> in the namespace <tt>http://www.ivoa.net/wsdl/RegistrySearch/v1.0</tt>
       */
XMLString ivoa_externalRegistry_adqlsSearchXML ( IvornOrURI, JString, BOOL);
			
			
/* function ivoa_externalRegistry_adqlsSearch(registry, adqls)Perform an ADQL/s query, returning an array of datastructures.
 
 Equivalent to {@link #adqlsSearchXML} but returning results in form that can be more easily used.
		
		registry - (IvornOrURI)
		adqls - (JString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_externalRegistry_adqlsSearch ( IvornOrURI, JString);
			
			
/* function ivoa_externalRegistry_keywordSearchXML(registry, keywords, orValues)perform a keyword search
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		keywords - space separated list of keywords to search for(JString)
		orValues - - true to 'OR' together matches. false to 'AND' together matches(BOOL)
		
	Returns XMLString - xml document of search results, same format as result of {@link #adqlSearchXML}
       */
XMLString ivoa_externalRegistry_keywordSearchXML ( IvornOrURI, JString, BOOL);
			
			
/* function ivoa_externalRegistry_keywordSearch(registry, keywords, orValues)Perform a keyword search and return a list of datastructures.
        A more convenient variant of {@link #keywordSearchXML}
		
		registry - (IvornOrURI)
		keywords - (JString)
		orValues - (BOOL)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_externalRegistry_keywordSearch ( IvornOrURI, JString, BOOL);
			
			
/* function ivoa_externalRegistry_getResourceXML(registry, id)Retreive a record document from the registry
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		id - identifier of the registry entry to retrrieve(IvornOrURI)
		
	Returns XMLString - xml document of the registry entry - a <tt>Resource</tt> document   probably in the <tt>http://www.ivoa.net/xml/VOResource/v1.0</tt> namespace
       */
XMLString ivoa_externalRegistry_getResourceXML ( IvornOrURI, IvornOrURI);
			
			
/* function ivoa_externalRegistry_getResource(registry, id)Retrieve a record from the registry, returning it as a datastructure
 
 For most uses, it's better to use this method instead of {@link #getResourceXML} as the result is easier to work with.
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		id - identifier of the registry entry to retrieve(IvornOrURI)
		
	Returns struct Resource_Base - a  datastructue representing the registry entry - will be a {@link Resource} or one of it's   subclasses depending on the registry entry type.
       */
struct Resource_Base ivoa_externalRegistry_getResource ( IvornOrURI, IvornOrURI);
			
			
/* function ivoa_externalRegistry_xquerySearchXML(registry, xquery)perform an XQuery
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		xquery - the query to perform. Must return a well-formed xml document - i.e. starting with a single root element.(JString)
		
	Returns XMLString - the result of executing this xquery over the specified registry - a document of arbitrary structure.
       */
XMLString ivoa_externalRegistry_xquerySearchXML ( IvornOrURI, JString);
			
			
/* function ivoa_externalRegistry_xquerySearch(registry, xquery)Variant of xquerySearchXML that returns registry records as data structures
		
		registry - endpoint of registry service to connect to.(IvornOrURI)
		xquery - should return a document, or nodeset, containing &lt;vor:Resource&gt; elements.   Results are not required to be single-rooted, and resource elements may be embedded within other elements.(JString)
		
	Returns ListOfResource_Base - an array containing any registry records present in the query result.
       */
ListOfResource_Base ivoa_externalRegistry_xquerySearch ( IvornOrURI, JString);
			
			
/* function ivoa_externalRegistry_getIdentityXML(registry)Retreive a a description of this registry, returning it asan xml document
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		
	Returns XMLString - that registries own service description - a single Resource documnt
       */
XMLString ivoa_externalRegistry_getIdentityXML ( IvornOrURI);
			
			
/* function ivoa_externalRegistry_getIdentity(registry)Retreive a a description of this registry, returning it as a datastructure
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		
	Returns struct RegistryService - that registries own service description - a single ResourceDocument
       */
struct RegistryService ivoa_externalRegistry_getIdentity ( IvornOrURI);
			
			
/* function ivoa_externalRegistry_buildResources(doc)convenience function - build an array of resouce objects from an xml document
		
		doc - (XMLString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_externalRegistry_buildResources ( XMLString);
			
			
/* function ivoa_externalRegistry_getRegistryOfRegistriesEndpoint()returns the service endpoint of the standard IVOA registry of registries
 this registry can be used to query for other registry services
		
		
	Returns IvornOrURI - 
       */
IvornOrURI ivoa_externalRegistry_getRegistryOfRegistriesEndpoint ( );
      /* end class
      ivoa.externalRegistry
      */
   /* begin class ivoa.registry
    Access  the system-configured  registry service.
 
 ACR uses an IVOA-compliant registry to retreive details of available resources
  - servers, applications, catalogues, etc.
  
  The endpoint of this registry service can be inspected by calling {@link #getSystemRegistryEndpoint()}.
  In cases where this service is unavailable, registry queries will automatically fall-back to the
  backup registry service, whose endpoint is defined by {@link #getFallbackSystemRegistryEndpoint()}
 
 The query functions in this interface are the equivalent to their counterparts in the 
 {@link ExternalRegistry} interface, but against the System and Fallback registries.See: 
				<a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>
			 
				<a href="http://www.ivoa.net/twiki/bin/view/IVOA/ResourceMetadata">Resource Metadata</a>
			 
				<a href="http://www.ivoa.net/Documents/latest/RM.html">IVOA Resource Metadata for the VO</a>
			 
				<a href="http://www.ivoa.net/Documents/latest/ADQL.html">ADQL Query Language Specification</a>
			 
				<a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaResReg">IVOA Registry Working Group</a>
			 
				<a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
			 
				org.astrogrid.acr.ui.RegistryBrowser
			 
				org.astrogrid.acr.ivoa.ExternalRegistry - to query other IVOA registries, and the Registry of Registries.
			  */
	 
	
			
			
/* function ivoa_registry_adqlxSearch(adqlx)Perform an ADQL/x registry search, return a list of matching resources
		
		adqlx - (XMLString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_registry_adqlxSearch ( XMLString);
			
			
/* function ivoa_registry_adqlsSearch(adqls)Perform an ADQL/s registry search, return a list of matching resources
		
		adqls - (JString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_registry_adqlsSearch ( JString);
			
			
/* function ivoa_registry_keywordSearch(keywords, orValues)Perform a keyword registry search, return a list of matching resources
		
		keywords - (JString)
		orValues - (BOOL)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_registry_keywordSearch ( JString, BOOL);
			
			
/* function ivoa_registry_getResource(id)Retrieve a resource by identifier
		
		id - (IvornOrURI)
		
	Returns struct Resource_Base - 
       */
struct Resource_Base ivoa_registry_getResource ( IvornOrURI);
			
			
/* function ivoa_registry_xquerySearch(xquery)Perform an xquery registry search, return a list of matching resources
		
		xquery - (JString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_registry_xquerySearch ( JString);
			
			
/* function ivoa_registry_xquerySearchXML(xquery)Perform an xquery registry search, return a document
		
		xquery - (JString)
		
	Returns XMLString - 
       */
XMLString ivoa_registry_xquerySearchXML ( JString);
			
			
/* function ivoa_registry_getIdentity()Access the registry entry describing the system registry itself
		
		
	Returns struct RegistryService - 
       */
struct RegistryService ivoa_registry_getIdentity ( );
			
			
/* function ivoa_registry_getSystemRegistryEndpoint()gives the endpoint of the system registry
		
		
	Returns IvornOrURI - 
       */
IvornOrURI ivoa_registry_getSystemRegistryEndpoint ( );
			
			
/* function ivoa_registry_getFallbackSystemRegistryEndpoint()gives the endpoint of the fallback system registry
		
		
	Returns IvornOrURI - 
       */
IvornOrURI ivoa_registry_getFallbackSystemRegistryEndpoint ( );
      /* end class
      ivoa.registry
      */
   /* begin class ivoa.registryAdqlBuilder
    Builds ADQL queries for the registry */
	 
	
			
			
/* function ivoa_registryAdqlBuilder_allRecords()query that returns all active records in the registry
		
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_allRecords ( );
			
			
/* function ivoa_registryAdqlBuilder_fullTextSearch(recordSet, searchTerm)build a full text search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_fullTextSearch ( JString, JString);
			
			
/* function ivoa_registryAdqlBuilder_summaryTextSearch(recordSet, searchTerm)build a summary text search 
 
 searches on identifier, shortName, title and content/description
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_summaryTextSearch ( JString, JString);
			
			
/* function ivoa_registryAdqlBuilder_identifierSearch(recordSet, searchTerm)build an identifier search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_identifierSearch ( JString, JString);
			
			
/* function ivoa_registryAdqlBuilder_shortNameSearch(recordSet, searchTerm)build a short-name search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_shortNameSearch ( JString, JString);
			
			
/* function ivoa_registryAdqlBuilder_titleSearch(recordSet, searchTerm)build a search on title
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_titleSearch ( JString, JString);
			
			
/* function ivoa_registryAdqlBuilder_descriptionSearch(recordSet, searchTerm)build a search on description
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_descriptionSearch ( JString, JString);
      /* end class
      ivoa.registryAdqlBuilder
      */
   /* begin class ivoa.registryXQueryBuilder
    Builds XQueries for the registry */
	 
	
			
			
/* function ivoa_registryXQueryBuilder_allRecords()query that returns all active records in the registry
		
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_allRecords ( );
			
			
/* function ivoa_registryXQueryBuilder_fullTextSearch(recordSet, searchTerm)build a full text search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_fullTextSearch ( JString, JString);
			
			
/* function ivoa_registryXQueryBuilder_summaryTextSearch(recordSet, searchTerm)build a summary text search 
 
 searches on identifier, shortName, title and content/description
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_summaryTextSearch ( JString, JString);
			
			
/* function ivoa_registryXQueryBuilder_identifierSearch(recordSet, searchTerm)build an identifier search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_identifierSearch ( JString, JString);
			
			
/* function ivoa_registryXQueryBuilder_shortNameSearch(recordSet, searchTerm)build a short-name search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_shortNameSearch ( JString, JString);
			
			
/* function ivoa_registryXQueryBuilder_titleSearch(recordSet, searchTerm)build a search on title
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_titleSearch ( JString, JString);
			
			
/* function ivoa_registryXQueryBuilder_descriptionSearch(recordSet, searchTerm)build a search on description
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_descriptionSearch ( JString, JString);
      /* end class
      ivoa.registryXQueryBuilder
      */
   /* begin class ivoa.siap
    Query for Images from Simple Image Access Protocol (SIAP) servicesSee: 
				http://www.ivoa.net/Documents/latest/SIA.html
			  */
	 
	
			
			
/* function ivoa_siap_constructQuery(service, ra, dec, size)construct query on RA, DEC, SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		size - radius of cone ( as described in siap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_siap_constructQuery ( IvornOrURI, double, double, double);
			
			
/* function ivoa_siap_constructQueryF(service, ra, dec, size, format)construct query on RA, DEC, SIZE, FORMAT
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		size - radius of cone ( as described in siap spec)(double)
		format - format of images (as described in siap spec)(JString)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_siap_constructQueryF ( IvornOrURI, double, double, double, JString);
			
			
/* function ivoa_siap_constructQueryS(service, ra, dec, ra_size, dec_size)construct query on RA, DEC, RA_SIZE, DEC_SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		ra_size - size of ra ( as described in siap spec)(double)
		dec_size - size of dec (as described in siap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_siap_constructQueryS ( IvornOrURI, double, double, double, double);
			
			
/* function ivoa_siap_constructQuerySF(service, ra, dec, ra_size, dec_size, format)construct query on RA, DEC, RA_SIZE, DEC_SIZE, FORMAT
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		ra_size - size of ra ( as described in siap spec)(double)
		dec_size - size of dec (as described in siap spec)(double)
		format - format of images (as described in siap spec)(JString)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_siap_constructQuerySF ( IvornOrURI, double, double, double, double, JString);
			
			
/* function ivoa_siap_addOption(query, optionName, optionValue)add an option to a previously constructed query
		
		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)
		
	Returns URLString - <tt>query</tt> with the option appended.
       */
URLString ivoa_siap_addOption ( URLString, JString, JString);
			
			
/* function ivoa_siap_execute(query)execute a DAL query, returning a datastructure
		
		query - query url to execute(URLString)
		
	Returns ListOfACRKeyValueMap - A model the DAL query response as a list of  of rows. Each row is represented is a map between UCD keys or datamodel names   and values from the response
       */
ListOfACRKeyValueMap ivoa_siap_execute ( URLString);
			
			
/* function ivoa_siap_executeVotable(query)execute a DAL query, returning a votable document.
 
 This is a convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
		
		query - query url to execute(URLString)
		
	Returns XMLString - a votable document of results
       */
XMLString ivoa_siap_executeVotable ( URLString);
			
			
/* function ivoa_siap_executeAndSave(query, saveLocation)execute a DAL query and save the resulting document.
		
		query - query url to execute(URLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void ivoa_siap_executeAndSave ( URLString, IvornOrURI);
			
			
/* function ivoa_siap_saveDatasets(query, saveLocation)save the datasets pointed to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		
	Returns int - number of datasets saved.
       */
int ivoa_siap_saveDatasets ( URLString, IvornOrURI);
			
			
/* function ivoa_siap_saveDatasetsSubset(query, saveLocation, rows)save a subset of the datasets point to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset.(ACRList)
		
	Returns int - number of datasets saved.
       */
int ivoa_siap_saveDatasetsSubset ( URLString, IvornOrURI, ACRList);
			
			
/* function ivoa_siap_getRegistryAdqlQuery()helper method - returns an ADQL/s query that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an adql query string
       */
JString ivoa_siap_getRegistryAdqlQuery ( );
			
			
/* function ivoa_siap_getRegistryXQuery()helper method - returns an Xquery that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an xquery string
       */
JString ivoa_siap_getRegistryXQuery ( );
      /* end class
      ivoa.siap
      */
   /* begin class ivoa.skyNode
    Query for data from SkyNode servicesSee: 
				http://www.ivoa.net/Documents/latest/SkyNodeInterface.html
			  */
	 
	
			
			
/* function ivoa_skyNode_getRegistryAdqlQuery()helper method - returns an adql query that should be passed to a registry to list all known skynode services
		
		
	Returns JString - an adql query string
       */
JString ivoa_skyNode_getRegistryAdqlQuery ( );
			
			
/* function ivoa_skyNode_getRegistryXQuery()returns an xquery that should be passed to a registry to list all known skynode services
		
		
	Returns JString - an xquery string
       */
JString ivoa_skyNode_getRegistryXQuery ( );
			
			
/* function ivoa_skyNode_getMetadata(service)interrogate skynode for  complete metadata about it's database
		
		service - identifier of the service to retrieve metadata for(IvornOrURI)
		
	Returns ListOfSkyNodeTableBean - a list of will use the tablebeans - actually, {@link SkyNodeTableBean} and   {@link SkyNodeColumnBean}  subclasses that present additional metadata
       */
ListOfSkyNodeTableBean ivoa_skyNode_getMetadata ( IvornOrURI);
			
			
/* function ivoa_skyNode_getFormats(service)interrogate skynode for supported output formats
		
		service - identifier of the service to retrieve metadata for(IvornOrURI)
		
	Returns ListOfJString - 
       */
ListOfJString ivoa_skyNode_getFormats ( IvornOrURI);
			
			
/* function ivoa_skyNode_getFunctions(service)interrogate skynode for the functions it supports
		
		service - identifier of the service to retrieve metadata for(IvornOrURI)
		
	Returns ListOfFunctionBean - 
       */
ListOfFunctionBean ivoa_skyNode_getFunctions ( IvornOrURI);
			
			
/* function ivoa_skyNode_getResults(service, adqlx)execute an adql query
		
		service - identifier of the service to execute query on(IvornOrURI)
		adqlx - the query to execute(XMLString)
		
	Returns XMLString - a document containing a votable
       */
XMLString ivoa_skyNode_getResults ( IvornOrURI, XMLString);
			
			
/* function ivoa_skyNode_saveResults(service, adqlx, saveLocation)execute an adql query, saving results to specified location
		
		service - identifier of the service to execute query on(IvornOrURI)
		adqlx - the query to execute(XMLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void ivoa_skyNode_saveResults ( IvornOrURI, XMLString, IvornOrURI);
			
			
/* function ivoa_skyNode_getResultsF(service, adqlx, format)execute an adql query, specifying required output format
		
		service - identifier of the service to execute query on(IvornOrURI)
		adqlx - the query to execute(XMLString)
		format - required format for results (one of the results returned from {@link #getFormats()}(JString)
		
	Returns JString - a string of results @todo consider whether byte[] is a safer bet here.
       */
JString ivoa_skyNode_getResultsF ( IvornOrURI, XMLString, JString);
			
			
/* function ivoa_skyNode_saveResultsF(service, adqlx, saveLocation, format)execute an adql query, saving results to specified location, specifying required output format.
		
		service - identifier of the service to execute query on(IvornOrURI)
		adqlx - the query to execute(XMLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		format - (JString)
		
	Returns void - 
       */
void ivoa_skyNode_saveResultsF ( IvornOrURI, XMLString, IvornOrURI, JString);
			
			
/* function ivoa_skyNode_getFootprint(service, region)query the server's footprint for this region (FULL Skynode only)
      * @param service identifier of the service to interrogate
      * @param region a STC document describing a region
		
		service - (IvornOrURI)
		region - (XMLString)
		
	Returns XMLString - another STC document describing the intersection between the parameter <tt>region</tt> and the holdings of this skynode
       */
XMLString ivoa_skyNode_getFootprint ( IvornOrURI, XMLString);
			
			
/* function ivoa_skyNode_estimateQueryCost(planId, adql)interrogate the service to estimate the cost of a query (FULL Skynode only)
		
		planId - not known @todo(long)
		adql - query to estimate cost for.(XMLString)
		
	Returns double - estimation of query cost
       */
double ivoa_skyNode_estimateQueryCost ( long, XMLString);
			
			
/* function ivoa_skyNode_getAvailability(service)interrogate server for system information
		
		service - identifier of the service to interrogate(IvornOrURI)
		
	Returns struct AvailabilityBean - availability information for this server
       */
struct AvailabilityBean ivoa_skyNode_getAvailability ( IvornOrURI);
      /* end class
      ivoa.skyNode
      */
   /* begin class ivoa.ssap
    Querying for Spectra from Simple Spectral Access Protool (SSAP) Services.
 <b>NB:</b> working, but based on unfinished IVOA specification - interface may need to change to follow specificaiton. */
	 
	
			
			
/* function ivoa_ssap_constructQuery(service, ra, dec, size)construct query on RA, DEC, SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in ssap spec)(double)
		dec - declination (as described in ssap spec)(double)
		size - radius of cone ( as described in ssap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_ssap_constructQuery ( IvornOrURI, double, double, double);
			
			
/* function ivoa_ssap_constructQueryS(service, ra, dec, ra_size, dec_size)construct query on RA, DEC, RA_SIZE, DEC_SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in ssap spec)(double)
		dec - declination (as described in ssap spec)(double)
		ra_size - size of ra ( as described in ssap spec)(double)
		dec_size - size of dec (as described in ssap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_ssap_constructQueryS ( IvornOrURI, double, double, double, double);
			
			
/* function ivoa_ssap_addOption(query, optionName, optionValue)add an option to a previously constructed query
		
		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)
		
	Returns URLString - <tt>query</tt> with the option appended.
       */
URLString ivoa_ssap_addOption ( URLString, JString, JString);
			
			
/* function ivoa_ssap_execute(query)execute a DAL query, returning a datastructure
		
		query - query url to execute(URLString)
		
	Returns ListOfACRKeyValueMap - A model the DAL query response as a list of  of rows. Each row is represented is a map between UCD keys or datamodel names   and values from the response
       */
ListOfACRKeyValueMap ivoa_ssap_execute ( URLString);
			
			
/* function ivoa_ssap_executeVotable(query)execute a DAL query, returning a votable document.
 
 This is a convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
		
		query - query url to execute(URLString)
		
	Returns XMLString - a votable document of results
       */
XMLString ivoa_ssap_executeVotable ( URLString);
			
			
/* function ivoa_ssap_executeAndSave(query, saveLocation)execute a DAL query and save the resulting document.
		
		query - query url to execute(URLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void ivoa_ssap_executeAndSave ( URLString, IvornOrURI);
			
			
/* function ivoa_ssap_saveDatasets(query, saveLocation)save the datasets pointed to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		
	Returns int - number of datasets saved.
       */
int ivoa_ssap_saveDatasets ( URLString, IvornOrURI);
			
			
/* function ivoa_ssap_saveDatasetsSubset(query, saveLocation, rows)save a subset of the datasets point to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset.(ACRList)
		
	Returns int - number of datasets saved.
       */
int ivoa_ssap_saveDatasetsSubset ( URLString, IvornOrURI, ACRList);
			
			
/* function ivoa_ssap_getRegistryAdqlQuery()helper method - returns an ADQL/s query that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an adql query string
       */
JString ivoa_ssap_getRegistryAdqlQuery ( );
			
			
/* function ivoa_ssap_getRegistryXQuery()helper method - returns an Xquery that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an xquery string
       */
JString ivoa_ssap_getRegistryXQuery ( );
      /* end class
      ivoa.ssap
      */
   
/* end package ivoa */

/* begin package resource */
           

/* end package resource */

/* begin package nvo */
           
/* begin class nvo.cone
    Deprecated: 
				use the ivoa.cone interface instead.
			Query  catalogs using Cone-search services */
	 
	
			
			
/* function nvo_cone_constructQuery(service, ra, dec, sr)construct a query on RA, DEC, SR
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension(double)
		dec - declination(double)
		sr - search radius(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute the query
       */
URLString nvo_cone_constructQuery ( IvornOrURI, double, double, double);
			
			
/* function nvo_cone_addOption(coneQuery, optionName, optionValue)Add an option to a previously constructed query
		
		coneQuery - the query url(URLString)
		optionName - the name of the option to add(JString)
		optionValue - value for the new option(JString)
		
	Returns URLString - <tt>query</tt> with the option appended.
       */
URLString nvo_cone_addOption ( URLString, JString, JString);
			
			
/* function nvo_cone_getResults(coneQuery)execute a cone query.
 
 Convenience method - just performs a 'GET' on the query URL - many programming languages
 support this themselves
		
		coneQuery - query url to execute(URLString)
		
	Returns XMLString - a votable of results
       */
XMLString nvo_cone_getResults ( URLString);
			
			
/* function nvo_cone_saveResults(coneQuery, saveLocation)execute a cone query and save the results
		
		coneQuery - the query url to execute(URLString)
		saveLocation - location to save result document - may be file://, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void nvo_cone_saveResults ( URLString, IvornOrURI);
			
			
/* function nvo_cone_getRegistryAdqlQuery()returns an ADQL/s query that should be passed to a registry to list all available cone services
		
		
	Returns JString - 
       */
JString nvo_cone_getRegistryAdqlQuery ( );
			
			
/* function nvo_cone_getRegistryXQuery()returns an xquery that should be passed to a registry to list all available cone services
		
		
	Returns JString - 
       */
JString nvo_cone_getRegistryXQuery ( );
      /* end class
      nvo.cone
      */
   
/* end package nvo */

/* begin package ui */
           
/* begin class userInterface.applicationLauncher
    Control the  Application Launcher GUI.
 
 <img src="doc-files/applauncher.png"/>See: 
				org.astrogrid.acr.astrogrid.Applications
			  */
	 
	
			
			
/* function userInterface_applicationLauncher_show()display a  application launcher UI
		
		
	Returns void - 
       */
void userInterface_applicationLauncher_show ( );
      /* end class
      userInterface.applicationLauncher
      */
   /* begin class userInterface.astroscope
    Control  AstroScope.
 
 <img src="doc-files/astroscope.png"/> */
	 
	
			
			
/* function userInterface_astroscope_show()display a new instance of astroscope
		
		
	Returns void - 
       */
void userInterface_astroscope_show ( );
      /* end class
      userInterface.astroscope
      */
   /* begin class userInterface.fileManager
     */
	 
	
			
			
/* function userInterface_fileManager_show()
		
		
	Returns void - 
       */
void userInterface_fileManager_show ( );
      /* end class
      userInterface.fileManager
      */
   /* begin class userInterface.jobMonitor
    Deprecated: 
				replaced by {@link Lookout}
			Control the Job Monitor GUI.See: 
				org.astrogrid.acr.astrogrid.Jobs
			  */
	 
	
			
			
/* function userInterface_jobMonitor_show()show the job monitor window
		
		
	Returns void - 
       */
void userInterface_jobMonitor_show ( );
			
			
/* function userInterface_jobMonitor_hide()hide the job monitor  window
		
		
	Returns void - 
       */
void userInterface_jobMonitor_hide ( );
			
			
/* function userInterface_jobMonitor_refresh()manually refresh the job list
		
		
	Returns void - 
       */
void userInterface_jobMonitor_refresh ( );
			
			
/* function userInterface_jobMonitor_addApplication(name, executionId)Add a new application to the monitor
		
		name - user-friendly name of the app(JString)
		executionId - identifier of the application(IvornOrURI)
		
	Returns void - 
       */
void userInterface_jobMonitor_addApplication ( JString, IvornOrURI);
			
			
/* function userInterface_jobMonitor_displayApplicationTab()bring the application tab of the monitor uppermost
		
		
	Returns void - 
       */
void userInterface_jobMonitor_displayApplicationTab ( );
			
			
/* function userInterface_jobMonitor_displayJobTab()bring the jes tab of the monitor uppermost
		
		
	Returns void - 
       */
void userInterface_jobMonitor_displayJobTab ( );
      /* end class
      userInterface.jobMonitor
      */
   /* begin class userInterface.lookout
    Deprecated: Control the Lookout UI.
 
 <img src="doc-files/lookout.png"/> */
	 
	
			
			
/* function userInterface_lookout_show()
		
		
	Returns void - 
       */
void userInterface_lookout_show ( );
			
			
/* function userInterface_lookout_hide()
		
		
	Returns void - 
       */
void userInterface_lookout_hide ( );
			
			
/* function userInterface_lookout_refresh()
		
		
	Returns void - 
       */
void userInterface_lookout_refresh ( );
      /* end class
      userInterface.lookout
      */
   /* begin class userInterface.myspaceBrowser
    Deprecated: 
				prefer filemanager
			Control the  Myspace Browser UI.
 
 <img src="doc-files/filemanager.png"/>See: 
				org.astrogrid.acr.astrogrid.Myspace
			  */
	 
	
			
			
/* function userInterface_myspaceBrowser_show()show the explorer gui
		
		
	Returns void - 
       */
void userInterface_myspaceBrowser_show ( );
			
			
/* function userInterface_myspaceBrowser_hide()hide the explorer gui
		
		
	Returns void - 
       */
void userInterface_myspaceBrowser_hide ( );
      /* end class
      userInterface.myspaceBrowser
      */
   /* begin class userInterface.queryBuilder
     */
	 
	
			
			
/* function userInterface_queryBuilder_show()
		
		
	Returns void - 
       */
void userInterface_queryBuilder_show ( );
      /* end class
      userInterface.queryBuilder
      */
   /* begin class userInterface.registryBrowser
    Control the registry browser UI.
 
 <img src="doc-files/registry.png"/>See: 
				org.astrogrid.acr.astrogrid.Registry
			  */
	 
	
			
			
/* function userInterface_registryBrowser_show()show a new instance of registry browser ui
		
		
	Returns void - 
       */
void userInterface_registryBrowser_show ( );
			
			
/* function userInterface_registryBrowser_hide()hide the registry browser ui
		
		
	Returns void - 
       */
void userInterface_registryBrowser_hide ( );
			
			
/* function userInterface_registryBrowser_search(s)show an new instance of the registry browser, and perform the requiested search (keywords)
		
		s - (JString)
		
	Returns void - 
       */
void userInterface_registryBrowser_search ( JString);
			
			
/* function userInterface_registryBrowser_open(uri)display a particular record in a new instacne of the browser
		
		uri - (IvornOrURI)
		
	Returns void - 
       */
void userInterface_registryBrowser_open ( IvornOrURI);
      /* end class
      userInterface.registryBrowser
      */
   
/* end package ui */

/* begin package util */
           
/* begin class util.tables
    Utility functions for working with tables.
 Exposes some of the functionality of STILSee: 
				http://www.star.bris.ac.uk/~mbt/stil/
			  */
	 
	
			
			
/* function util_tables_convertFiles(inLocation, inFormat, outLocation, outFormat)Converts a table in a file between supported formats.
		
		inLocation - input location: may be a http://, file://, ivo:// , ftp://                      compressed using unix compress, gzip or bzip2(IvornOrURI)
		inFormat - input handler name: generally one of                        fits, votable, ascii, csv, ipac, wdc or null(JString)
		outLocation - output location: file://, ivo://, ftp://(IvornOrURI)
		outFormat - output format: generally one of                       fits, fits-plus,                       votable, votable-tabledata, votable-binary-inline,                       votable-binary-href, votable-fits-inline,                        votable-fits-href,                       text, ascii, csv, html, html-element, latex,                       latex-document or null(JString)
		
	Returns void - 
       */
void util_tables_convertFiles ( IvornOrURI, JString, IvornOrURI, JString);
			
			
/* function util_tables_convertToFile(input, inFormat, outLocation, outFormat)Writes an in-memory table to a table in a file, converting between supported formats.
		
		input - the input table(JString)
		inFormat - input handler name: generally one of                        fits, votable, ascii, csv, ipac, wdc or null(JString)
		outLocation - output location: file://, ivo://, ftp://(IvornOrURI)
		outFormat - output format: generally one of                       fits, fits-plus,                       votable, votable-tabledata, votable-binary-inline,                       votable-binary-href, votable-fits-inline,                        votable-fits-href,                       text, ascii, csv, html, html-element, latex,                       latex-document or null(JString)
		
	Returns void - 
       */
void util_tables_convertToFile ( JString, JString, IvornOrURI, JString);
			
			
/* function util_tables_convertFromFile(inLocation, inFormat, outFormat)Reads a table in a file into an in-memory table, converting between supported formats
 Will only give good results for text-based table formats.
		
		inLocation - input location: may be a http://, file://, ivo:// , ftp://                      compressed using unix compress, gzip or bzip2(IvornOrURI)
		inFormat - input handler name: generally one of                        fits, votable, ascii, csv, ipac, wdc or null(JString)
		outFormat - output format: generally one of                       fits, fits-plus,                       votable, votable-tabledata, votable-binary-inline,                       votable-binary-href, votable-fits-inline,                        votable-fits-href,                       text, ascii, csv, html, html-element, latex,                       latex-document or null(JString)
		
	Returns JString - the converted representation of the table.
       */
JString util_tables_convertFromFile ( IvornOrURI, JString, JString);
			
			
/* function util_tables_convert(input, inFormat, outFormat)Converts an in-memory table between supported formats. 
 Will only give good results for text-based table formats.
		
		input - the input table.(JString)
		inFormat - input handler name: generally one of                        fits, votable, ascii, csv, ipac, wdc or null(JString)
		outFormat - output format: generally one of                       fits, fits-plus,                       votable, votable-tabledata, votable-binary-inline,                       votable-binary-href, votable-fits-inline,                        votable-fits-href,                       text, ascii, csv, html, html-element, latex,                       latex-document or null(JString)
		
	Returns JString - a table in the requested format.
       */
JString util_tables_convert ( JString, JString, JString);
			
			
/* function util_tables_listOutputFormats()list the names of the table formats this module can write out as
		
		
	Returns ListOfJString - 
       */
ListOfJString util_tables_listOutputFormats ( );
			
			
/* function util_tables_listInputFormats()list the names of the table formats this module can read in from
		
		
	Returns ListOfJString - 
       */
ListOfJString util_tables_listInputFormats ( );
      /* end class
      util.tables
      */
   
/* end package util */

/* begin package votech */
           
/* begin class votech.vomon
    Monitor service availability using the VoMon serviceSee: 
				vomon.sourceforge.net
			  */
	 
	
			
			
/* function votech_vomon_reload()forces the status information to be reloaded from the vomon server 
 - potentially expensive operation
		
		
	Returns void - 
       */
void votech_vomon_reload ( );
			
			
/* function votech_vomon_checkAvailability(id)check the availability of a service
		
		id - registry id of the service(IvornOrURI)
		
	Returns struct VoMonBean - a monitor bean describing this service's availability, or null if this  service is not known
       */
struct VoMonBean votech_vomon_checkAvailability ( IvornOrURI);
			
			
/* function votech_vomon_checkCeaAvailability(id)check the availability of a cea application
		
		id - registry id of the application(IvornOrURI)
		
	Returns ListOfVoMonBean - an array of monitoring beans, one for each server that  states it provides this application. May be null if the application is unknown,   i.e if no servers provide this application.
       */
ListOfVoMonBean votech_vomon_checkCeaAvailability ( IvornOrURI);
      /* end class
      votech.vomon
      */
   
/* end package votech */

/* begin package voevent */
           

/* end package voevent */


#ifdef __cplusplus
 }
#endif
#endif
