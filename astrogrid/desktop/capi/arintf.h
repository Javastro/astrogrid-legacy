
#ifndef INTF_H_
#define INTF_H_
/*
C interface to the AR
Paul Harrison paul.harrison@manchester.ac.uk
produced on 2008-10-06+01:00

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
UserInformation,TableBeanComparator,TableBean,ParameterReferenceBean,ParameterBean,NodeInformation,InterfaceBean,ExecutionMessage,ExecutionInformation,DatabaseBean,ColumnBean,CeaService,CeaServerCapability,CeaApplication,AbstractInformation,SesamePositionBean,FunctionBean,AvailabilityBean,WebServiceInterface,Validation,TabularDB,TableService,TableDataType,StcResourceProfile,StapService,StapCapability,StapCapability_Query,StandardSTC,SsapService,SsapCapability,SsapCapability_Query,SsapCapability_PosParam,Source,SimpleDataType,SiapService,SiapCapability,SiapCapability_SkySize,SiapCapability_ImageSize,SiapCapability_SkyPos,SiapCapability_Query,Service,SecurityMethod,SearchCapability,ResourceName,Resource,Relationship,RegistryService,RegistryCapability,ParamHttpInterface,Organisation,Interface,InputParam,HasCoverage,HarvestCapability,Handler,Format,CurationDate,DataService,DataCollection,Curation,Creator,Coverage,Content,Contact,ConeService,ConeCapability,ConeCapability_Query,CatalogService,Catalog,Capability,BaseParam,Authority,Application,AccessURL,VoMonBean };

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
    struct ColumnBean *list; /* note that this is an array */
   } ListOfColumnBean;

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
    struct InputParam *list; /* note that this is an array */
   } ListOfInputParam;

typedef struct {
    int n;
    struct Interface_Base *list; /* note that this is an array */
   } ListOfInterface_Base;

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
    struct StcResourceProfile *list; /* note that this is an array */
   } ListOfStcResourceProfile;

typedef struct {
    int n;
    struct TableBean *list; /* note that this is an array */
   } ListOfTableBean;

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
Abstract datastructure: Provides metadata for some sort of resource.
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
URL from which a capability of a service can be accessed.
   */
    enum AcrType _type;

/* data members from AccessURL */
IvornOrURI valueURI;
JString use;

};

struct  Validation {
       /*
        from
org.astrogrid.acr.ivoa.resource.Validation
A validation stamp for a resouce.
 It combinines a validation level and the ID of
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
The name of a potentially registered resource.
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
Personal contact information
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
The person or organisation responsible
            for creating a resource.
   */
    enum AcrType _type;

/* data members from Creator */
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
Information regarding the general curation of a resource.
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
A relationship between one resource and one or
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
A bibliographic reference.
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
Metadata about the general content of a resource.
   */
    enum AcrType _type;

/* data members from Content */
ListOfJString contentLevel;
JString description;
IvornOrURI referenceURI;
ListOfRelationship relationships;
ListOfJString subject;
ListOfJString type;
struct Source source;

};

struct  Application {
       /*
        from
org.astrogrid.acr.ivoa.resource.Application
An abstract notion of 'Application'.

 The IVOA schema is still in flux at the moment, so only partially
 implemented at present. Will wait for standard to settle and see what elements of it are used in practice
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

/* data members from Application */
ListOfIvornOrURI applicationCapabilities;

};

struct  Authority {
       /*
        from
org.astrogrid.acr.ivoa.resource.Authority
A naming authority.

  This kind of registry entry makes an assertion of control over a
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

struct  BaseParam {
       /*
        from
org.astrogrid.acr.ivoa.resource.BaseParam
Abstract Class: A description of some kind of data value.
 </p>
      This class places no restriction on
            the parameter's data type.
            As the parameter's data type is usually important, schemas
            normally employ a sub-class of this type
            rather than this type directly.
   */
    enum AcrType _type;

/* data members from BaseParam */
JString name;
JString description;
JString unit;
JString ucd;

};

struct  SecurityMethod {
       /*
        from
org.astrogrid.acr.ivoa.resource.SecurityMethod
An access-control mechanism
   */
    enum AcrType _type;

/* data members from SecurityMethod */
IvornOrURI standardID;

};

struct  Interface {
       /*
        from
org.astrogrid.acr.ivoa.resource.Interface
Gives access to a capability of a service.
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
Describes part of what a service does.

  A {@link Service} can contain one or more capabilities. Each capability
  describes something that the service does, and how to access that functionality.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface_Base interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

};

struct  TableDataType {
       /*
        from
org.astrogrid.acr.ivoa.resource.TableDataType
A datatype supported by a VOTable.
   */
    enum AcrType _type;

/* data members from TableDataType */
JString type;
JString arraysize;

};

struct  ColumnBean {
       /*
        from
org.astrogrid.acr.astrogrid.ColumnBean
Metadata for a column in a table.
   */
    enum AcrType _type;

/* data members from BaseParam */
JString name;
JString description;
JString unit;
JString ucd;

/* data members from ColumnBean */
struct TableDataType columnDataType;

};

struct  TableBean {
       /*
        from
org.astrogrid.acr.astrogrid.TableBean
Registry description of a table in a Catalog
   */
    enum AcrType _type;

/* data members from TableBean */
ListOfColumnBean columns;
JString description;
JString name;
JString role;

};

struct  Catalog {
       /*
        from
org.astrogrid.acr.ivoa.resource.Catalog
A catalog in a data collection.

 A catalog aggegates together a number of tables (modelled by {@link TableBean}). Each table in turn contains
 a number of columns (modelled by {@link ColumnBean}).
 <p />
 {@textdiagram database
   +-----------+
   |  Catalog  |
   +-----+-----+
         |
         | 1..*
         |
  +------+------+
  |  TableBean  | ...
  +------+------+
         |
         | 1..*
         |
  +------+-------+
  |  ColumnBean  | ...
  +--------------+
 }
   */
    enum AcrType _type;

/* data members from Catalog */
JString description;
JString name;
ListOfTableBean tables;

};

struct  StcResourceProfile {
       /*
        from
org.astrogrid.acr.ivoa.resource.StcResourceProfile
Spatial Coverage.

 Described using STC, which is too complex to usefully parse into arbitrary datastructures.
   */
    enum AcrType _type;

/* data members from StcResourceProfile */
XMLString stcDocument;

};

struct  Coverage {
       /*
        from
org.astrogrid.acr.ivoa.resource.Coverage
A description of how a resource's contents or behavior maps
           to the sky. Describes it's mapping to space, time and frequency.
   */
    enum AcrType _type;

/* data members from Coverage */
struct ResourceName footprint;
struct StcResourceProfile stcResourceProfile;
ListOfJString wavebands;

};

struct  CatalogService {
       /*
        from
org.astrogrid.acr.ivoa.resource.CatalogService
A service that interacts with tables, and posesses coverage information.
 <p/>
 The definition reads "A service that interacts one or more specified tables
            having some coverage of the sky, time, and/or frequency."
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


/* data members from DataService */
ListOfResourceName facilities;
ListOfResourceName instruments;
struct Coverage coverage;

/* data members from CatalogService */
ListOfTableBean tables;

};

struct  ParameterReferenceBean {
       /*
        from
org.astrogrid.acr.astrogrid.ParameterReferenceBean
A reference to parameter of a remote application (CEA).

 This class is used to to allow an {@link InterfaceBean interface} of a CEA application
 to refer to one of it's {@link ParameterBean parameters}.
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
Registry description of a parameter-set supported by a remote (CEA) application.
   */
    enum AcrType _type;

/* data members from InterfaceBean */
ListOfParameterReferenceBean inputs;
JString name;
ListOfParameterReferenceBean outputs;
JString description;

};

struct  ParameterBean {
       /*
        from
org.astrogrid.acr.astrogrid.ParameterBean
An input or output parameter of a remote application (CEA).
   */
    enum AcrType _type;

/* data members from BaseParam */
JString name;
JString description;
JString unit;
JString ucd;

/* data members from ParameterBean */
JString id;
ListOfJString options;
JString type;
JString uiName;
ListOfJString defaultValues;
JString uType;
JString mimeType;
JString arraysize;

};

struct  CeaApplication {
       /*
        from
org.astrogrid.acr.astrogrid.CeaApplication
Registry description of a CEA application.

 A CEA application describes a number of parameters ({@code p1},{@code p2},{@code p3} in the figure below).
 Each parameter is modelled by a {@link ParameterBean} which describes the
 name, type, and use of a parameter. The CEA application also describes a number of interfaces
 ({@code i1},{@code i2} in the figure}, which represent a set of parameters
 that the application can be invoked with. Each interface is modelled by a {@link InterfaceBean}, which contains
 a list of {@link ParameterReferenceBean} which refer back to the parameters.
 <br />
 {@textdiagram ceaParams

 /----------------\    /--\
 |                +----+i1+---+
 | CeaApplication |    \--/   |
 |                +--+        |
 \-+----+----+----/  | /--\   |
   |    |    |       +-+i2+-+ |
   |    |    |         \--/ | |
 /-+\ /-+\ /-+\         .   | |
 |p1| |p2| |p3| ...     .   | :
 \--/ \--/ \--/         .   : |
  ^     ^   ^^              | |
  |     |   ||              | |
  :     :   ::              | |
  |     +----*--------------+ |
  |         :                 |
  +---------*-----------------+


 }
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

/* data members from Application */
ListOfIvornOrURI applicationCapabilities;

/* data members from CeaApplication */
ListOfInterfaceBean interfaces;
ListOfParameterBean parameters;
JString applicationKind;

};

struct  CeaServerCapability {
       /*
        from
org.astrogrid.acr.astrogrid.CeaServerCapability
Registry description of a CEA server.

 Accessed from a {@link CeaService}.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface_Base interfaces;
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
Registry description of a CEA server.
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
A test query for a cone search service.
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
The capability to perform an IVOA Cone Search
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface_Base interfaces;
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
A service that can perform a cone search
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
Description of a data format that a service can return.
   */
    enum AcrType _type;

/* data members from Format */
JString value;

};

struct  DataCollection {
       /*
        from
org.astrogrid.acr.ivoa.resource.DataCollection
A logical grouping of data.
 <p />
 In general, this is composed of one
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


/* data members from DataCollection */
ListOfResourceName facilities;
ListOfResourceName instruments;
ListOfJString rights;
ListOfFormat formats;
struct Coverage coverage;
ListOfCatalog_Base catalogues;
struct AccessURL accessURL;

};

struct  DataService {
       /*
        from
org.astrogrid.acr.ivoa.resource.DataService
A service for accessing astronomical data.
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


/* data members from DataService */
ListOfResourceName facilities;
ListOfResourceName instruments;
struct Coverage coverage;

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
ListOfTableBean tables;

/* data members from DatabaseBean */

};

struct  ExecutionInformation {
       /*
        from
org.astrogrid.acr.astrogrid.ExecutionInformation
Description of  the progress of a remote process - e.g. a workflow job, or CEA application.

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
Capability of harvesting a registry.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface_Base interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

/* data members from RegistryCapability */

/* data members from HarvestCapability */
int maxRecords;

};

struct  HasCoverage {
       /*
        from
org.astrogrid.acr.ivoa.resource.HasCoverage
A resource that contains coverage information.
   */
    enum AcrType _type;

/* data members from HasCoverage */
struct Coverage coverage;

};

struct  SimpleDataType {
       /*
        from
org.astrogrid.acr.ivoa.resource.SimpleDataType
A datatype for an InputParam.
 These types do not imply a size nor precise format.
   */
    enum AcrType _type;

/* data members from SimpleDataType */
JString type;
JString arraysize;

};

struct  InputParam {
       /*
        from
org.astrogrid.acr.ivoa.resource.InputParam
A parameter to a HTTP service interface.
            <p />
            The allowed data type names do not imply a size or precise
            format.  This type is intended to be sufficient for describing
            an input parameter to a simple REST service.
   */
    enum AcrType _type;

/* data members from BaseParam */
JString name;
JString description;
JString unit;
JString ucd;

/* data members from InputParam */
JString use;
struct SimpleDataType dataType;

};

struct  NodeInformation {
       /*
        from
org.astrogrid.acr.astrogrid.NodeInformation
Metadata for a myspace 'node' - a file or folder.
 <p/>
 {@link #getId()} will return a myspace resouce locator - an ivorn of form
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
An organization that is involved in the virtual observatory.
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

struct  ParamHttpInterface {
       /*
        from
org.astrogrid.acr.ivoa.resource.ParamHttpInterface
A service invoked via an HTTP Query.

         The HTTP query may be of any form (i.e. Get or Post)
           with a set of arguments consisting of keyword name-value pairs.
   */
    enum AcrType _type;

/* data members from Interface */
ListOfAccessURL accessUrls;
JString role;
ListOfSecurityMethod securityMethods;
JString version;
JString type;

/* data members from ParamHttpInterface */
JString queryType;
JString resultType;
ListOfInputParam params;

};

struct  RegistryCapability {
       /*
        from
org.astrogrid.acr.ivoa.resource.RegistryCapability
Abstract baseclass for all Registry capabilities.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface_Base interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

/* data members from RegistryCapability */

};

struct  RegistryService {
       /*
        from
org.astrogrid.acr.ivoa.resource.RegistryService
An IVOA Registry Service
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
Base class for all resources returned from an IVOA Registry.


   An instance of this class can represent any entity or component of a VO application that is
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
Capability to query  a registry
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface_Base interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

/* data members from RegistryCapability */

/* data members from SearchCapability */
JString extensionSearchSupport;
int maxRecords;
ListOfJString optionalProtocol;

};

struct  Service {
       /*
        from
org.astrogrid.acr.ivoa.resource.Service
A resource that can be invoked to perform some action.
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
Object resolution information provided by Sesame
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
An extent in RA and Dec.
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
The limits for an image returned from a SIAP service.
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
A position in RA and Dec
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
A test query for a SIAP service
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
The capability to perform a Simple Image Access (SIAP) query.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface_Base interfaces;
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
An Image Access Service (SIAP)
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

struct  SsapCapability_PosParam {
       /*
        from
org.astrogrid.acr.ivoa.resource.SsapCapability.PosParam
The central coordinate of the spatial region to be searched.
   */
    enum AcrType _type;

/* data members from SsapCapability.PosParam */
double lon;
double lat;
JString refframe;

};

struct  SsapCapability_Query {
       /*
        from
org.astrogrid.acr.ivoa.resource.SsapCapability.Query
A test query for a SSAP service.
   */
    enum AcrType _type;

/* data members from SsapCapability.Query */
struct SsapCapability_PosParam pos;
double size;
JString queryDataCmd;

};

struct  SsapCapability {
       /*
        from
org.astrogrid.acr.ivoa.resource.SsapCapability
The capability to perform a Simple Spectral Access (SSAP) query.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface_Base interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

/* data members from SsapCapability */
JString complianceLevel;
ListOfJString dataSources;
ListOfJString creationTypes;
double maxSearchRadius;
int maxRecords;
int defaultMaxRecords;
double maxAperture;
ListOfJString supportedFrames;
int maxFileSize;
struct SsapCapability_Query testQuery;

};

struct  SsapService {
       /*
        from
org.astrogrid.acr.ivoa.resource.SsapService
A Spectral Access Service (SSAP).
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

/* data members from SsapService */

};

struct  StandardSTC {
       /*
        from
org.astrogrid.acr.ivoa.resource.StandardSTC
a description of standard space-time coordinate systems,
           positions, and regions.
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

/* data members from StandardSTC */
ListOfStcResourceProfile resourceProfiles;

};

struct  StapCapability_Query {
       /*
        from
org.astrogrid.acr.ivoa.resource.StapCapability.Query
A test query for a STAP service.

  Would like to document this more, but the original schema is not documented at the moment.
   */
    enum AcrType _type;

/* data members from StapCapability.Query */
JString start;
JString end;
struct SiapCapability_SkyPos pos;
struct SiapCapability_SkySize size;

};

struct  StapCapability {
       /*
        from
org.astrogrid.acr.ivoa.resource.StapCapability
The capability to perform a Simple Time Access (STAP) query.
   */
    enum AcrType _type;

/* data members from Capability */
JString description;
ListOfInterface_Base interfaces;
IvornOrURI standardID;
ListOfValidation validationLevel;
JString type;

/* data members from StapCapability */
ListOfJString supportedFormats;
int maxRecords;
struct StapCapability_Query testQuery;

};

struct  StapService {
       /*
        from
org.astrogrid.acr.ivoa.resource.StapService
A Time Access Service (STAP).
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

/* data members from StapService */

};

struct  TableBeanComparator {
       /*
        from
org.astrogrid.acr.astrogrid.TableBeanComparator
comparator for table beans.
   */
    enum AcrType _type;

/* data members from TableBeanComparator */

};

struct  TableService {
       /*
        from
org.astrogrid.acr.ivoa.resource.TableService
A service that interact with tables.
            <p/>
                        This type differs from a {@link CatalogService} in that the
            table or tables do not have any inherent coverage of the
            sky.
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

/* data members from TableService */
ListOfResourceName facilities;
ListOfResourceName instruments;
ListOfTableBean tables;

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

/* data members from DataCollection */
ListOfResourceName facilities;
ListOfResourceName instruments;
ListOfJString rights;
ListOfFormat formats;
struct Coverage coverage;
ListOfCatalog_Base catalogues;
struct AccessURL accessURL;

/* data members from TabularDB */
struct DatabaseBean database;

};

struct  UserInformation {
       /*
        from
org.astrogrid.acr.astrogrid.UserInformation
Metadata about the currently logged in user.
 <p/>
 Behaviour of superclass methods:
 <ul>
 <li>{@link #getId()} will return the user's ivorn</li>
 <li>{@link #getName()} will return be the user's name.</li>
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

struct  WebServiceInterface {
       /*
        from
org.astrogrid.acr.ivoa.resource.WebServiceInterface
A service invoked using SOAP.
   */
    enum AcrType _type;

/* data members from Interface */
ListOfAccessURL accessUrls;
JString role;
ListOfSecurityMethod securityMethods;
JString version;
JString type;

/* data members from WebServiceInterface */
ListOfIvornOrURI wsdlURLs;

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

   struct BaseParam_Base {
   enum AcrType _type;
   union {
       struct BaseParam baseparam;

   struct ParameterBean parameterbean;
   struct ColumnBean columnbean;
   struct InputParam inputparam;
   } d;
};

   struct Catalog_Base {
   enum AcrType _type;
   union {
       struct Catalog catalog;

   struct DatabaseBean databasebean;
   } d;
};

   struct Resource_Base {
   enum AcrType _type;
   union {
       struct Resource resource;

   struct Service service;
   struct CeaService ceaservice;
   struct TableService tableservice;
   struct StapService stapservice;
   struct SsapService ssapservice;
   struct SiapService siapservice;
   struct RegistryService registryservice;
   struct DataService dataservice;
   struct CatalogService catalogservice;
   struct ConeService coneservice;
   struct Application application;
   struct CeaApplication ceaapplication;
   struct DataCollection datacollection;
   struct TabularDB tabulardb;
   struct StandardSTC standardstc;
   struct Organisation organisation;
   struct Authority authority;
   } d;
};

   struct Service_Base {
   enum AcrType _type;
   union {
       struct Service service;

   struct CeaService ceaservice;
   struct TableService tableservice;
   struct StapService stapservice;
   struct SsapService ssapservice;
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

   struct Application_Base {
   enum AcrType _type;
   union {
       struct Application application;

   struct CeaApplication ceaapplication;
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
   struct StapCapability stapcapability;
   struct SsapCapability ssapcapability;
   struct SiapCapability siapcapability;
   struct RegistryCapability registrycapability;
   struct SearchCapability searchcapability;
   struct HarvestCapability harvestcapability;
   struct ConeCapability conecapability;
   } d;
};

   struct RegistryCapability_Base {
   enum AcrType _type;
   union {
       struct RegistryCapability registrycapability;

   struct SearchCapability searchcapability;
   struct HarvestCapability harvestcapability;
   } d;
};

   struct Interface_Base {
   enum AcrType _type;
   union {
       struct Interface interface;

   struct WebServiceInterface webserviceinterface;
   struct ParamHttpInterface paramhttpinterface;
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
    AR Service: Query remote databases and execute remote applications.


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

				org.astrogrid.acr.dialogs.ToolEditor

				org.astrogrid.acr.astrogrid.ExecutionInformation
			  */




/* function astrogrid_applications_list()list remote applications available in the registry.


	Returns ListOfIvornOrURI - a list of the registry identifiers of available applications
       */
ListOfIvornOrURI astrogrid_applications_list ( );


/* function astrogrid_applications_getQueryToListApplications()


	Returns JString -
       */
JString astrogrid_applications_getQueryToListApplications ( );


/* function astrogrid_applications_getRegistryQuery()helper method - returns the ADQL/s query that should be passed to the registry to
 list all available applications.

 can be used as a starting point to build up filters, etc.


	Returns JString - an adql query string.
       */
JString astrogrid_applications_getRegistryQuery ( );


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
    AR Service: Single sign-on and authentication.See:
				<a href='http://www.ivoa.net/Documents/latest/SSOAuthMech.html'>IVOA Single-Sign On Specification</a>
			  */




/* function astrogrid_community_login(username, password, community)login to virtual observatory.

 {@example login("EdwardWoodward","ewarwoowar","uk.ac.le.star") }

		username - user name (e.g. {@code fredbloggs})(JString)
		password - password for this user(JString)
		community - community the user is registered with (e.g. {@code uk.ac.astogrid} )(JString)

	Returns void -
       */
void astrogrid_community_login ( JString, JString, JString);


/* function astrogrid_community_getUserInformation()Access information about who the AR is currently logged in as.


	Returns struct UserInformation - information about the current user.
       */
struct UserInformation astrogrid_community_getUserInformation ( );


/* function astrogrid_community_logout()logout of the virtual observatory


	Returns void -
       */
void astrogrid_community_logout ( );


/* function astrogrid_community_isLoggedIn()check whether AR is currently logged in.


	Returns BOOL - true if the user is logged in
       */
BOOL astrogrid_community_isLoggedIn ( );


/* function astrogrid_community_guiLogin()display the login dialogue to prompt the user for input, and then log in.


	Returns void -
       */
void astrogrid_community_guiLogin ( );
      /* end class
      astrogrid.community
      */
   /* begin class astrogrid.jobs
    AR Service: Execute and control workflows on remote job servers.See:
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
    AR Service: A distributed storage system.

 <p/> Myspace is Astrogrid's prototype implementation of VOSpace.

 {@stickyWarning At present this interface doesn't contain suficient functionality to work with myspace in a truly efficient manner.
  Also, myspace is being replaced by an implementation of the VOSpace standard.
  Expect a cleaner, more efficient interface
 to VOSpace to be added later. However this interface and it's current methods will remain available, and won't be deprecated if at all possible.
 }See:
				#createFile Example of creating a file

				#getReadContentURL Example of reading from a file

				#getWriteContentURL Example of writing to a file

				NodeInformation

				MyspaceBrowser  Myspace file browser component

				ResourceChooser Dialogue for selecting myspace files
			  */




/* function astrogrid_myspace_getHome()Retrieve the identifier of the current user's home folder in myspace.

 Each user has a single root folder. this method returns the name of it.


	Returns IvornOrURI - uri of the home folder - typically has form  <tt>ivo://<i>Community-Id</i>/<i>User-Id</i>#</tt>
       */
IvornOrURI astrogrid_myspace_getHome ( );


/* function astrogrid_myspace_exists(filename)test whether a myspace resource exists.

		filename - uri to check (full or abridged form)(IvornOrURI)

	Returns BOOL - true if the resource exists
       */
BOOL astrogrid_myspace_exists ( IvornOrURI);


/* function astrogrid_myspace_getNodeInformation(filename)access metadata about a myspace resource.
 {@stickyWarning At the moment, this is a costly operation }

		filename - resource to investigate(IvornOrURI)

	Returns struct NodeInformation - a beanful of information
       */
struct NodeInformation astrogrid_myspace_getNodeInformation ( IvornOrURI);


/* function astrogrid_myspace_createFile(filename)create a new myspace file.

 Any parent folders that are missing will be created too.
 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
 ms = ar.astrogrid.myspace
  #call this function
 file = '#votable/a-new-file.vot'
 if not (ms.exists(file)):
    ms.createFile(file)
 }

 {@example "Java Example"
 import org.astrogrid.acr.*;
 import java.net.URI;
 import org.astrogrid.acr.astrogrid.Myspace;
 import org.astrogrid.acr.builtin.ACR
 Finder f = new Finder();
 ACR acr = f.find();
 Myspace ms = (Myspace)acr.getService(Myspace.class);
 URI file =new URI("#votable/a-new-file.vot");
 if (! ms.exists(file)) {
    ms.createFile(file)
 }
 }

		filename - the resource to create.(IvornOrURI)

	Returns void -
       */
void astrogrid_myspace_createFile ( IvornOrURI);


/* function astrogrid_myspace_createFolder(foldername)create a new myspace folder.

 Any parent folders that are missing will be created too.

		foldername - the resource to create.(IvornOrURI)

	Returns void -
       */
void astrogrid_myspace_createFolder ( IvornOrURI);


/* function astrogrid_myspace_createChildFolder(parentFolder, name)create a child folder of  the specified resource.

		parentFolder - parent of the new resource (must be a folder)(IvornOrURI)
		name - name of the new folder(JString)

	Returns IvornOrURI - the ivorn of the new folder
       */
IvornOrURI astrogrid_myspace_createChildFolder ( IvornOrURI, JString);


/* function astrogrid_myspace_createChildFile(parentFolder, name)create a child file of the specified resource.

		parentFolder - parent of the new resource (must be a folder)(IvornOrURI)
		name - name of the new file(JString)

	Returns IvornOrURI - the ivorn of the new file
       */
IvornOrURI astrogrid_myspace_createChildFile ( IvornOrURI, JString);


/* function astrogrid_myspace_getParent(filename)retrieve the ID  of the parent of a myspace resource.

		filename - uri of the resource to find parent for(IvornOrURI)

	Returns IvornOrURI - uri of the parent
       */
IvornOrURI astrogrid_myspace_getParent ( IvornOrURI);


/* function astrogrid_myspace_list(ivorn)list the names of the children (files and folders) of a myspace folder.

		ivorn - uri of the folder to inspect(IvornOrURI)

	Returns ListOfJString - an array of the names of the contents.
       */
ListOfJString astrogrid_myspace_list ( IvornOrURI);


/* function astrogrid_myspace_listIvorns(ivorn)list the identifiers of the children ( files and folders)  of a myspace folder.

		ivorn - uri of the folder to inspect(IvornOrURI)

	Returns ListOfIvornOrURI - an array of the ivorns of the contents.
       */
ListOfIvornOrURI astrogrid_myspace_listIvorns ( IvornOrURI);


/* function astrogrid_myspace_listNodeInformation(ivorn)list the node information objects for the children ( files and folders)  of a myspace folder.

 {@stickyWarning Expensive operation at present.}

		ivorn - uri of the folder to inspect(IvornOrURI)

	Returns ListOfNodeInformation - an array of the node information objects.
       */
ListOfNodeInformation astrogrid_myspace_listNodeInformation ( IvornOrURI);


/* function astrogrid_myspace_refresh(ivorn)refresh the metadata held about  a myspace resource with the server.
 {stickyNote
 For performance, metadata about myspace resources is used in a LRU cache. This method forces the ACR to re-query the myspace server
 about this resource.}

		ivorn - resource to refresh(IvornOrURI)

	Returns void -
       */
void astrogrid_myspace_refresh ( IvornOrURI);


/* function astrogrid_myspace_delete(ivorn)delete a myspace resource.

		ivorn - uri of the resource to delete(IvornOrURI)

	Returns void -
       */
void astrogrid_myspace_delete ( IvornOrURI);


/* function astrogrid_myspace_rename(srcIvorn, newName)rename a myspace resource.

		srcIvorn - uri of the resource to renam(IvornOrURI)
		newName - new name for this resource(JString)

	Returns IvornOrURI - uri pointing to the renamed resource (original uri may now be invalid)
       */
IvornOrURI astrogrid_myspace_rename ( IvornOrURI, JString);


/* function astrogrid_myspace_move(srcIvorn, newParentIvorn, newName)move a myspace resource.

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

		ivorn - resource to read(IvornOrURI)

	Returns JString - content of this resource (as a string)
       */
JString astrogrid_myspace_read ( IvornOrURI);


/* function astrogrid_myspace_write(ivorn, content)Write data to a myspace resource.

		ivorn - resource to write to(IvornOrURI)
		content - the data to write(JString)

	Returns void -
       */
void astrogrid_myspace_write ( IvornOrURI, JString);


/* function astrogrid_myspace_readBinary(ivorn)read the binary content of a myspace resource directly.

		ivorn - resource to read(IvornOrURI)

	Returns ListOfchar - byte array of the contents of this resource
       */
ListOfchar astrogrid_myspace_readBinary ( IvornOrURI);


/* function astrogrid_myspace_writeBinary(ivorn, content)Write binary data to a myspace resource.

		ivorn - resource to write to(IvornOrURI)
		content - the data to write(ListOfchar)

	Returns void -
       */
void astrogrid_myspace_writeBinary ( IvornOrURI, ListOfchar);


/* function astrogrid_myspace_getReadContentURL(ivorn)Compute a URL which can then be read to access the contents (data) of a myspace resource.

 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 from urllib2 import urlopen
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
 ms = ar.astrogrid.myspace
  #get the data url for a myspace file
 msfile = '#results/datafile.vot'
 dataUrl = ms.getReadContentURL(msfile)
  # read from the data url
 urlFile = urlopen(dataUrl)
 print urlFile
  }
 {@example  "Java Example"
 import org.astrogrid.acr.*;
 import java.net.URI;
 import org.astrogrid.acr.astrogrid.Myspace;
 import org.astrogrid.acr.builtin.ACR
 Finder f = new Finder();
 ACR acr = f.find();
 Myspace ms = (Myspace)acr.getService(Myspace.class);
 URI file =new URI("#results/datafile.vot");
 URL dataUrl = ms.getReadContentURL(file);
 InputStream is = dataUrl.openStream();
   // read in data..
 }

		ivorn - resource to read(IvornOrURI)

	Returns URLString - a url from which the contents of the resource can be read
       */
URLString astrogrid_myspace_getReadContentURL ( IvornOrURI);


/* function astrogrid_myspace_getWriteContentURL(ivorn)Compute  a URL which can then be written to set the contents (i.e. data) of a myspace resource.

 For the current filestore impleentation the result returned is a <tt>http://</tt> url, to which data should be <b>PUT</b> (not POST).
 {@stickyNote After the data has been written to the filestore, the filemanager needs to be notified that the data for this node has changed - by calling
 {@link #transferCompleted}
 }
 Here's how to PUT and then notify of completion:
 {@example "Java Example"
 import org.astrogrid.acr.*;
 import java.net.*;
 import org.astrogrid.acr.astrogrid.Myspace;
 import org.astrogrid.acr.builtin.ACR
 Finder f = new Finder();
 ACR acr = f.find();
 Myspace ms = (Myspace)acr.getService(Myspace.class);
 URI file =new URI("#results/datafile.vot");
  //get the output url
 URL url = ms.getWriteContentURL(file);
 HttpURLConnection conn  = (HttpURLConnection) url.openConnection() ;
 conn.setDoOutput(true) ;
 conn.setRequestMethod("PUT");
   // connect
 conn.connect();
 OutputStream os = conn.getOutputStream();
  //write the data
  ...
   //close
 os.close();
  // important - the URL connection class won't tranfer data unless you ask for a response - this is a nasty gotcha, not clear from the javadocs.
 conn.getResponseCode() // necessary to force the whole thing to happen
 ms.transferCompleted(file); // tell the filemanager that the content for this resource has changed.
 }

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
   /* begin class astrogrid.processManager
    AR Service: A general manager for the execution , monitoring, and control of all remote processes.

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

		executionId - identifier of the process to delete(IvornOrURI)

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
    AR Service: Query for <b>Time series data</b> from Simple Time Access Protocol (STAP) services (DAL).
 <p />
 {@stickyNote This class provides functions to construct a DAL query.
 To execute that query, see the examples and methods in the {@link Dal} class.
 }

 <h2>Example</h2>
 The following example constructs a queryURL, performs the query, and then downloads
 the first of the resulting datasets. See {@link Dal} for other things that
 can be done with a query URL.
 {@example "Query a Stap service and download a subset of the data (Python)"
# connect to the AR
from xmlrpc import Server, DateTime
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
stap = ar.ivoa.stap #take a reference to the AR STAP component

#the STAP service to query (selected using voexplorer)
service = "ivo://mssl.ucl.ac.uk/stap-lasco"

#build a query
start = DateTime('20000101T00:00:00')
end = DateTime('20000102T00:00:00')
query = stap.constructQuery(service,start,end)
print "QueryURL",query

#execute the query
rows = stap.execute(query)

#inspect what we've got.
print "Rows Returned",len(rows)
import pprint
pprint.pprint(rows[0])


#download first datasets into current directory
#compute url for current directory
from urlparse import urlunsplit
from os import getcwd
currentDirURL = urlunsplit(['file','',getcwd(),'',''])
print "Downloading data to",currentDirURL
stap.saveDatasetsSubset(query,currentDirURL,[0])
 }
 The output from this script is shown below.
 <pre>
 QueryURL http://msslxx.mssl.ucl.ac.uk:8080/stap-lasco/StapSearch?service=astrogrid_stap&START=2000-01-01T00%3A00%3A00&END=2000-01-02T00%3A00%3A00
Rows Returned 3
{'Concept': ' Coronal Mass Ejection',
 'Contact Email': ' eca@mssl.ucl.ac.uk',
 'Contact Name': ' Elizabeth Auden',
 'INST_ID': 'SOHO_LASCO',
 'IVORN': 'ivo://mssl.ucl.ac.uk/LASCO#LASCO20000101T065405_21',
 'Name': ' LASCO_20000101T065405_21',
 'Parameters': '  Central Position Angle=21 deg, Angular Width=76 deg, Linear Speed=337 km/s, Acceleration=8.8 m/s^2, Mass=5.0e+15 g, Kinetic Energy=2.8e+30 erg, Measurement Position Angle=11 deg',
 'References': '  C2 movie=http://lasco-www.nrl.navy.mil/daily_mpg/2000_01/000101_c2.mpg, C3 movie=http://lasco-www.nrl.navy.mil/daily_mpg/2000_01/000101_c3.mpg, SXT movie=http://cdaw.gsfc.nasa.gov/CME_list/daily_mpg/2000_01/sxt_almg.20000101.mpg, PHTX movie=http://cdaw.gsfc.nasa.gov/CME_list/daily_plots/sephtx/2000_01/sephtx.20000101.png, Java movie=http://cdaw.gsfc.nasa.gov/CME_list/daily_movies/2000/01/01, C2 movie / GOES light curve=http://cdaw.gsfc.nasa.gov/CME_list/UNIVERSAL/2000_01/jsmovies/2000_01/20000',
 'VOX:AccessReference': ' http://msslxx.mssl.ucl.ac.uk:8080/voevent/xml/LASCO/LASCO_20000101T065405_21.xml',
 'VOX:Format': 'VOEVENT',
 'meta': 'LASCO CME VOEvents',
 'meta.curation': 'CDAW (NASA/Goddard), via MSSL query',
 'meta.ref.url': 'http://cdaw.gsfc.nasa.gov/CME_list/',
 'meta.title': 'LASCO CMEs',
 'time.obs.end': '2000-01-01T06:54:05',
 'time.obs.start': '2000-01-01T06:54:05'}
Downloading data to file:///Users/noel/Documents/workspace/python
 </pre>See:
				<a href="http://wiki.astrogrid.org/bin/view/Astrogrid/SimpleTimeAccessProtocol">Proposed STAP Standard</a>

				Dal

				Sesame Sesame - resolves object names to RA,Dec positions

				#getRegistryXQuery() getRegistryXQuery() - a query to list all STAP Services.
			  */




/* function astrogrid_stap_constructQuery(service, start, end)Construct a STAP Query on Time.

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString astrogrid_stap_constructQuery ( IvornOrURI, ACRDate, ACRDate);


/* function astrogrid_stap_constructQueryF(service, start, end, format)Construct a STAP Query on Time and Format.

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		format - format of images or time series data e.g. {@code ALL}(JString)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString astrogrid_stap_constructQueryF ( IvornOrURI, ACRDate, ACRDate, JString);


/* function astrogrid_stap_constructQueryP(service, start, end, ra, dec, size)Construct a STAP Query on Time and Position

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension  e.g {@code 6.950}(double)
		dec - declination e.g. {@code -1.6}(double)
		size - radius of cone e.g. {@code 0.1}(double)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString astrogrid_stap_constructQueryP ( IvornOrURI, ACRDate, ACRDate, double, double, double);


/* function astrogrid_stap_constructQueryPF(service, start, end, ra, dec, size, format)Construct a STAP Query on Time, Position and Format

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension  e.g {@code 6.950}(double)
		dec - declination e.g. {@code -1.6}(double)
		size - radius of cone e.g. {@code 0.1}(double)
		format - format of images or time series data e.g. {@code ALL}(JString)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString astrogrid_stap_constructQueryPF ( IvornOrURI, ACRDate, ACRDate, double, double, double, JString);


/* function astrogrid_stap_constructQueryS(service, start, end, ra, dec, ra_size, dec_size)Construct a STAP Query on Time and full Position

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension  e.g {@code 6.950}(double)
		dec - declination e.g. {@code -1.6}(double)
		ra_size - size of ra e.g. {@code 0.1}(double)
		dec_size - size of dec  e.g. {@code 0.2}(double)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString astrogrid_stap_constructQueryS ( IvornOrURI, ACRDate, ACRDate, double, double, double, double);


/* function astrogrid_stap_constructQuerySF(service, start, end, ra, dec, ra_size, dec_size, format)Construct a STAP Query on Time, full Position, and Format

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension  e.g {@code 6.950}(double)
		dec - declination e.g. {@code -1.6}(double)
		ra_size - size of ra e.g. {@code 0.1}(double)
		dec_size - size of dec  e.g. {@code 0.2}(double)
		format - format of images or time series data e.g. {@code ALL}(JString)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString astrogrid_stap_constructQuerySF ( IvornOrURI, ACRDate, ACRDate, double, double, double, double, JString);


/* function astrogrid_stap_addOption(query, optionName, optionValue)Add an additional option to a previously constructed query.
 <p/>
 Sometimes neccessary, for some DAL protocols, to provide optional query parameters.

		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)

	Returns URLString - {@code query} with the option appended.
       */
URLString astrogrid_stap_addOption ( URLString, JString, JString);


/* function astrogrid_stap_execute(query)Execute a DAL query, returning a datastructure

		query - query url to execute(URLString)

	Returns ListOfACRKeyValueMap - The service response parsed as a list of  of rows. Each row is represented is a map between UCD or datamodel keys    and values from the response
       */
ListOfACRKeyValueMap astrogrid_stap_execute ( URLString);


/* function astrogrid_stap_executeVotable(query)Execute a DAL query, returning a Votable document.

		query - query url to execute(URLString)

	Returns XMLString - a votable document of results
       */
XMLString astrogrid_stap_executeVotable ( URLString);


/* function astrogrid_stap_executeAndSave(query, saveLocation)Execute a DAL query and save the resulting document.

		query - query url to execute(URLString)
		saveLocation - location to save result document - May be {@code file:/}, {@code ivo://} (myspace), {@code ftp://} location(IvornOrURI)

	Returns void -
       */
void astrogrid_stap_executeAndSave ( URLString, IvornOrURI);


/* function astrogrid_stap_saveDatasets(query, saveLocation)Execute a DAL query, and save the datasets referenced by the response.
 <p />
 Applies to those DAL protocols ({@link Siap}, {@link Ssap}, {@link Stap}) where the response points to external data files.

		query - query url to execute(URLString)
		saveLocation - location of a directory in which to save the datasets. May be a {@code file:/}, {@code ivo://}(myspace) or {@code ftp://} location.(IvornOrURI)

	Returns int - number of datasets saved.
       */
int astrogrid_stap_saveDatasets ( URLString, IvornOrURI);


/* function astrogrid_stap_saveDatasetsSubset(query, saveLocation, rows)Execute a DAL query, and save a subset of the datasets referenced by the response.
 <p />
 Applies to those DAL protocols ({@link Siap}, {@link Ssap}, {@link Stap}) where the response points to external data files.

		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. May be a {@code file:/}, {@code ivo://}(myspace) or {@code ftp://} location.(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset. (0= first row)(ACRList)

	Returns int - number of datasets saved.
       */
int astrogrid_stap_saveDatasetsSubset ( URLString, IvornOrURI, ACRList);


/* function astrogrid_stap_getRegistryXQuery()Return an XQuery that, when passed to the registry, will return all known services of that type.

 {@stickyWarning In the case of {@link Cone} the registry query will return far too many to be useful - it is necessary to use this xquery as a starting point
 for building a more tightly-constrained query.}
 {@example "Example of querying for cone services related to 'dwarf'"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
#call this method to get a query to list all Cone-search services.
coneQuery = ar.ivoa.cone.getRegistryXQuery()

#combine it into a more tightly contrained query
abellConeQuery = "let $cq := " + coneQuery + """
for $r in $cq
where contains($r/content/subject,'dwarf')
return $r
"""

# perform the query
rs = ar.ivoa.registry.xquerySearch(abellConeQuery)
#inspect the results
print len(rs)
for r in rs:
    print r['id']
 }
 the output of this script is
 <pre>
2
ivo://nasa.heasarc/rasswd
ivo://nasa.heasarc/mcksion
</pre>


	Returns JString - an xquery string
       */
JString astrogrid_stap_getRegistryXQuery ( );
      /* end class
      astrogrid.stap
      */

/* end package astrogrid */

/* begin package cds */

/* begin class cds.coordinate
    AR Service: Astronomical Coordinate Web Service, from CDSSee:
				<a href='http://cdsweb.u-strasbg.fr/cdsws/astroCoo.gml'>Webservice Description</a>
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
				the webservice interface at CDS this client calls doesn't seem to be maintained, and this component of AR is no longer provided
			Webservice to resolve  GLU  (Generateur de Liens Uniformes).tags.See:
				<a href='http://cdsweb.u-strasbg.fr/cdsws/glu_resolver.gml'>Webservice Descritption</a>
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
    AR Service: Sesame Object Name Resolver, from CDS.
 Resolve object  names to position by querying  Simbad and/or NED and/or VizieR.
 <p />
 {@link #resolve} resolves an object name to a datastructure containing position, error, aliases, etc.
 <p />
 {@link #sesame} and {@link #sesameChooseService}
 provide low-level access to the raw webservice.See:
				<a href="http://cdsweb.u-strasbg.fr/cdsws/name_resolver.gml">Sesame Webservice Description</a>

				#resolve Example of resolving object name to position.
			  */




/* function cds_sesame_resolve(name)Resolve an object name to a position using Sesame.

 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
  #call this function
 posBean = ar.cds.sesame.resolve('ngc123')
  #see what we get
 from pprint import pprint
 pprint(posBean)
  #access some variables
 print "Position",posBean['ra'],posBean['dec']
 }

 The output is
 <pre>
 {'OName': '{NGC} 123',
 'aliases': [],
 'dec': -1.6000000000000001,
 'decErr': 0.0,
 'posStr': '00:27.8     -01:36     ',
 'ra': 6.9500000000000002,
 'raErr': 0.0,
 'service': 'VizieR',
 'target': 'ngc123'}
 Position 6.95 -1.6
 </pre>

		name - the object name to resolve(JString)

	Returns struct SesamePositionBean - a datastructure of positional information about the object.
       */
struct SesamePositionBean cds_sesame_resolve ( JString);


/* function cds_sesame_sesame(name, resultType)resolve a name to position

		name - the name to resolve(JString)
		resultType - <dl>            <dt>{@code u}</dt><dd>usual (corresponding to the deprecated Sesame(String name) output)</dd>                 <dt>{@code H}</dt><dd>html</dd>                  <dt>{@code x}</dt><dd>XML</dd>         </dl>                 {@code p}(for plain (text/plain)) and {@code i} (for all identifiers) options can be added to {@code H} or {@code x}(JString)

	Returns JString - format depending on the resultType parameter
       */
JString cds_sesame_sesame ( JString, JString);


/* function cds_sesame_sesameChooseService(name, resultType, all, service)resolve a name, selecting which services to use.

		name - the name to resolve(JString)
		resultType - <dl>            <dt>{@code u}</dt><dd>usual (corresponding to the deprecated Sesame(String name) output)</dd>                 <dt>{@code H}</dt><dd>html</dd>                  <dt>{@code x}</dt><dd>XML</dd>         </dl>                 {@code p}(for plain (text/plain)) and {@code i} (for all identifiers) options can be added to {@code H} or {@code x}(JString)
		all - true if all identifiers wanted(BOOL)
		service - <dl>    <dt>{@code S}</dt><dd>Simbad</dd>         <dt>{@code N}</dt><dd>NED</dd>          <dt>{@code V}</dt><dd>VizieR</dd>         <dt>{@code A}</dt><dd>all</dd>         </dl>         (examples : {@code S} to query Simbad, {@code NS} to query  Ned if not found in Simbad,         A to query in Ned, Simbad and VizieR, ...)(JString)

	Returns JString - format depending on the resultTtype parameter
       */
JString cds_sesame_sesameChooseService ( JString, JString, BOOL, JString);
      /* end class
      cds.sesame
      */
   /* begin class cds.ucd
    AR Service: Web Service for manipulating
Unified Content Descriptors, from CDS.See:
				<a href="http://cdsweb.u-strasbg.fr/cdsws/ucdClient.gml">Webservice Description</a>
			  */




/* function cds_ucd_UCDList()Return a list of UCD1.


	Returns JString - html document containing all ucd1
       */
JString cds_ucd_UCDList ( );


/* function cds_ucd_resolveUCD(ucd)Resolve a UCD1 (won't work with UCD1+)

		ucd - the UCD1 to resolve (example : {@code PHOT_JHN_V})(JString)

	Returns JString - sentence corresponding to the UCD1 (example : {@code Johnson magnitude V (JHN)})
       */
JString cds_ucd_resolveUCD ( JString);


/* function cds_ucd_UCDofCatalog(catalog_designation)Access UCD1 for a catalogue

		catalog_designation - designes the catalog (example : {@code I/239})(JString)

	Returns JString - list of UCD1 (in raw text) contained in a given catalog
       */
JString cds_ucd_UCDofCatalog ( JString);


/* function cds_ucd_translate(ucd)Convert a legacy UCD1 to the equivalent UCD1+

		ucd - The argument is a UCD1 (not UCD1+ !).(JString)

	Returns JString - ucd. This function returns the default UCD1+ corresponding to an old-style UCD1.
       */
JString cds_ucd_translate ( JString);


/* function cds_ucd_upgrade(ucd)Upgrade a deprecated UCD1+.

		ucd - a deprecated UCD1+ (word or combination).                     Useful when the 'validate' method returns with code 2.(JString)

	Returns JString - ucd. This function returns a valid UCD1+ corresponding to a deprecated word.                       It is useful when some reference words of the UCD1+ vocabulary are changed,                       and ensures backward compatibility.
       */
JString cds_ucd_upgrade ( JString);


/* function cds_ucd_validate(ucd)validate a UCD1+.
 This function checks that a UCD is well-formed

		ucd - (e.g. {@code ivoa:phot.mag;em.opt.B})(JString)

	Returns JString - The first word of the return string is an error code, possibly followed by an explanation of the error.  A return value of 0 indicates no error (valid UCD).  The error-code results from the combination (logical OR) of the following values:     <ul>     <li>1: warning indicating use of non-standard namespace (not ivoa:)</li>     <li>2: use of deprecated word</li>     <li>4: use of non-existing word</li>     <li>8: syntax error (extra space or unallowed character)</li>      </ul>
       */
JString cds_ucd_validate ( JString);


/* function cds_ucd_explain(ucd)Find the description of a UCD1+

		ucd - (e.g. {@code ivoa:phot.mag;em.opt.B})(JString)

	Returns JString - This function gives a human-readable explanation for a UCD composed of one or several words
       */
JString cds_ucd_explain ( JString);


/* function cds_ucd_assign(descr)Find the UCD1+ associated with a description.

		descr - Plain text description of a parameter to be described(JString)

	Returns JString - This function returns the UCD1+ corresponding to the description
       */
JString cds_ucd_assign ( JString);
      /* end class
      cds.ucd
      */
   /* begin class cds.vizier
    AR Service: Access VizieR catalogues, from CDSSee:
				<a href="http://cdsweb.u-strasbg.fr/cdsws/vizierAccess.gml">Webservice Description</a>
			  */




/* function cds_vizier_cataloguesMetaData(target, radius, unit, text)Get metadata about catalogues.

		target - (example : {@code M32})(JString)
		radius - (example : {@code 0.1})(double)
		unit - (example : {@code arcsec})(JString)
		text - (author, ..., example : {@code Ochsenbein})(JString)

	Returns XMLString - metadata about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesMetaData ( JString, double, JString, JString);


/* function cds_vizier_cataloguesMetaDataWavelength(target, radius, unit, text, wavelength)Get metadata about catalogues, with wavelength criteria.

		target - (example : {@code M32})(JString)
		radius - (example : {@code 0.1})(double)
		unit - (example : {@code arcsec})(JString)
		text - (author, ..., example : {@code Ochsenbein})(JString)
		wavelength - (example : {@code optical, Radio} like in the VizieR Web interface)(JString)

	Returns XMLString - metadata about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesMetaDataWavelength ( JString, double, JString, JString, JString);


/* function cds_vizier_cataloguesData(target, radius, unit, text)Get catalogue data.

		target - (example : {@code M32})(JString)
		radius - (example : {@code 0.1})(double)
		unit - (example : {@code arcsec})(JString)
		text - (author, ..., example : {@code Ochsenbein})(JString)

	Returns XMLString - data about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesData ( JString, double, JString, JString);


/* function cds_vizier_cataloguesDataWavelength(target, radius, unit, text, wavelength)Get catalogue data, with wavelength criteria.

		target - (example : {@code M32})(JString)
		radius - (example : {@code 0.1})(double)
		unit - (example : {@code arcsec})(JString)
		text - (author, ..., example : {@code Ochsenbein})(JString)
		wavelength - (example : {@code optical, Radio}, like in the VizieR Web interface)(JString)

	Returns XMLString - data about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesDataWavelength ( JString, double, JString, JString, JString);


/* function cds_vizier_metaAll()get metadata for all catalogues.
 {@stickyWarning Will return a large amount of data. }


	Returns XMLString - all metadata about catalogues in VizieR (VOTable format)
       */
XMLString cds_vizier_metaAll ( );
      /* end class
      cds.vizier
      */

/* end package cds */

/* begin package dialogs */

/* begin class dialogs.registryGoogle
    AR Service: Dialogue that prompts the user to select a registry resourceSee:
				#selectResourcesFromList(String, boolean, URI[]) Example 1

				#selectResourcesXQueryFilter(String, boolean, String) Example 2

				Registry
			  */




/* function dialogs_registryGoogle_selectResourcesXQueryFilter(prompt, multiple, xqueryFilter)Display the resource chooser dialogue, populated with resources from an xquery.

 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
  #call this function
 xquery = """
 for $r in //vor:Resource[not (@status='inactive' or @status='deleted')]
 where $r/@xsi:type  &=  '*DataCollection'
 return $r
 """
 rs = ar.dialogs.registryGoogle.selectResourcesXQueryFilter("Choose a DataCollection",True,xquery)
 #list the selected identifiers
 for r in rs:
  print r['id']
  }

		prompt - message to prompt user for input.(JString)
		multiple - if true, allow multiple selections.(BOOL)
		xqueryFilter - a xquery to populate the resource chooser with. Same format as in an xquery list in voexplorer UI - which is convenient for  constructing these queries.(JString)

	Returns ListOfResource_Base - 0 or more selected resources. never null.
       */
ListOfResource_Base dialogs_registryGoogle_selectResourcesXQueryFilter ( JString, BOOL, JString);


/* function dialogs_registryGoogle_selectResourcesFromList(prompt, multiple, identifiers)Display the resource chooser dialogue, populated with a list of resources.

 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
  #call this function
 ids = ['ivo://irsa.ipac/2MASS-PSC'
         ,'ivo://mast.stsci/siap-cutout/goods.hst'
         ,'ivo://stecf.euro-vo/SSA/HST/FOS'
         ,'ivo://uk.ac.cam.ast/iphas-dsa-catalog/IDR'
         ,'ivo://uk.ac.cam.ast/IPHAS/images/SIAP'
         ]
 rs = ar.dialogs.registryGoogle.selectResourcesFromList("Choose a DataCollection",True,ids)
 #list the selected identifiers
 for r in rs:
  print r['id']
  }

		prompt - message to prompt user for input(JString)
		multiple - if true, allow multiple selections.(BOOL)
		identifiers - an array of resource identifiers that will be retrieved from registry and displayed in the dialogue.(ListOfIvornOrURI)

	Returns ListOfResource_Base - 0 or more selected resources. never null.
       */
ListOfResource_Base dialogs_registryGoogle_selectResourcesFromList ( JString, BOOL, ListOfIvornOrURI);
      /* end class
      dialogs.registryGoogle
      */
   /* begin class dialogs.resourceChooser
    AR Service: Dialogue that prompts the user to select a local file, vospace resource or URL.
  <p />
 This is a  generalisation of the typical filechooser dialogue that allows local and  remote ( myspace / vospace / URL) resources to be selected.See:
				org.astrogrid.acr.astrogrid.Myspace
			  */




/* function dialogs_resourceChooser_chooseResource(title, enableRemote)Prompt the user to select a file.

 Blocks until a file is selected, or user presses cancel.

		title - title for the dialogue - e.g.{@code choose file to open}(JString)
		enableRemote - - if true,allow selection of a remote resource (myspace / vospace / URL). Selection of local resources is enabled always.(BOOL)

	Returns IvornOrURI - URI of the selected resource, or null if the user pressed cancel
       */
IvornOrURI dialogs_resourceChooser_chooseResource ( JString, BOOL);


/* function dialogs_resourceChooser_chooseFolder(title, enableRemote)Prompt the user to select a folder.

 Blocks until a folder is selected, or user presses cancel.

		title - title for the dialogue - e.g.{@code choose file to open}(JString)
		enableRemote - - if true,allow selection of a remote resource (myspace / vospace / URL). Selection of local resources is enabled always.(BOOL)

	Returns IvornOrURI - URI of the selected folder, or null if the user pressed cancel.
       */
IvornOrURI dialogs_resourceChooser_chooseFolder ( JString, BOOL);


/* function dialogs_resourceChooser_fullChooseResource(title, enableVospace, enableLocal, enableURL)Fully-configurable resource chooser - a generalization of {@link #chooseResource}.

		title - title for the dialogue(JString)
		enableVospace - if true,allow selection of a remote myspace / vospace resource.(BOOL)
		enableLocal - if true, allow selection of local files(BOOL)
		enableURL - if true, enable the 'enter a URL' tab, so an arbitrary URL can be entered.(BOOL)

	Returns IvornOrURI - the URI of the selected resource, or null if the user cancelled
       */
IvornOrURI dialogs_resourceChooser_fullChooseResource ( JString, BOOL, BOOL, BOOL);


/* function dialogs_resourceChooser_fullChooseFolder(title, enableVospace, enableLocal, enableURL)Fully-configurable resource chooser - a generalization of {@link #chooseFolder}.

		title - title for the dialogue(JString)
		enableVospace - if true,allow selection of a remote myspace / vospace folder(BOOL)
		enableLocal - if true, allow selection of local folders(BOOL)
		enableURL - if true, enable the 'enter a URL' tab, so an arbitrary URL can be entered.   {@stickyWarning No verification that an entered URL <i>is</i> a folder is performed.}(BOOL)

	Returns IvornOrURI - the URI of the selected folder, or null if the user cancelled
       */
IvornOrURI dialogs_resourceChooser_fullChooseFolder ( JString, BOOL, BOOL, BOOL);
      /* end class
      dialogs.resourceChooser
      */
   /* begin class dialogs.toolEditor
    AR Service: Dialogue that allows user to select a remote application or database and fill in invocation parameters.

The output of the dialogue is a <i>Tool Document</i>, suitable for passing to the {@link Applications} service for execution.See:
				org.astrogrid.acr.astrogrid.Applications
			  */




/* function dialogs_toolEditor_edit(t)Prompt the user to edit a tool document.

		t - document conforming to Tool schema(XMLString)

	Returns XMLString - an edited copy of this document, or null if the user pressed cancel.
       */
XMLString dialogs_toolEditor_edit ( XMLString);


/* function dialogs_toolEditor_editStored(documentLocation)Prompt the user to edit a tool document.

		documentLocation - location the tool document is stored at (http://, ftp://, ivo://)(IvornOrURI)

	Returns XMLString - edited copy of this document, or null if the user pressed cancel.
       */
XMLString dialogs_toolEditor_editStored ( IvornOrURI);


/* function dialogs_toolEditor_selectAndBuild()prompt the user to select a remote application or database and construct an invocation/query against it.


	Returns XMLString - a new tool document
       */
XMLString dialogs_toolEditor_selectAndBuild ( );
      /* end class
      dialogs.toolEditor
      */

/* end package dialogs */

/* begin package ivoa */

/* begin class ivoa.adql
    AR Service: Support for working with ADQL queries. */




/* function ivoa_adql_s2x(s)convert an adql/s string to an adql/x document

		s - (JString)

	Returns XMLString - xml equivalent of the adql/s input
       */
XMLString ivoa_adql_s2x ( JString);
      /* end class
      ivoa.adql
      */
   /* begin class ivoa.cache
     */




/* function ivoa_cache_flush()flush all cached data - for example registry entries


	Returns void -
       */
void ivoa_cache_flush ( );
      /* end class
      ivoa.cache
      */
   /* begin class ivoa.cone
    AR Service: Query for <b>Catalogs</b> from Cone-Search Services (DAL).

 <p />
 {@stickyNote This class provides functions to construct a DAL query.
 To execute that query, see the examples and methods in the {@link Dal} class.
 }
 <h2>Constructing a Query</h2>
 The first stage in querying a cone-service is to select the service to query, find the position to query at, and then call the {@link #constructQuery(URI, double, double, double)}
 function:
 {@example "Contructing a Cone Query (Python)"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
cone = ar.ivoa.cone #take a reference to the AR Cone component

#the Cone service to query (selected using voexplorer)
service = "ivo://irsa.ipac/2MASS-PSC"
#resolve an object name to a position
pos = ar.cds.sesame.resolve('m54')
#build a query
query = cone.constructQuery(service,pos['ra'],pos['dec'],0.001)
print "QueryURL",query
}
This script produces a query URL (shown below), which can the be passed to the methods in the {@link Dal} class.
<blockquote><tt>
QueryURL http://irsa.ipac.caltech.edu/cgi-bin/Oasis/CatSearch/nph-catsearch?CAT=fp_psc&RA=283.7636667&DEC=-30.4785&SR=0.0010
</tt></blockquote>See:
				<a href='http://www.ivoa.net/Documents/latest/ConeSearch.html'>IVOA Cone Search Standard Document</a>

				Dal
			  */




/* function ivoa_cone_addOption(query, optionName, optionValue)Add an additional option to a previously constructed query.
 <p/>
 Sometimes neccessary, for some DAL protocols, to provide optional query parameters.

		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)

	Returns URLString - {@code query} with the option appended.
       */
URLString ivoa_cone_addOption ( URLString, JString, JString);


/* function ivoa_cone_execute(query)Execute a DAL query, returning a datastructure

		query - query url to execute(URLString)

	Returns ListOfACRKeyValueMap - The service response parsed as a list of  of rows. Each row is represented is a map between UCD or datamodel keys    and values from the response
       */
ListOfACRKeyValueMap ivoa_cone_execute ( URLString);


/* function ivoa_cone_executeVotable(query)Execute a DAL query, returning a Votable document.

		query - query url to execute(URLString)

	Returns XMLString - a votable document of results
       */
XMLString ivoa_cone_executeVotable ( URLString);


/* function ivoa_cone_executeAndSave(query, saveLocation)Execute a DAL query and save the resulting document.

		query - query url to execute(URLString)
		saveLocation - location to save result document - May be {@code file:/}, {@code ivo://} (myspace), {@code ftp://} location(IvornOrURI)

	Returns void -
       */
void ivoa_cone_executeAndSave ( URLString, IvornOrURI);


/* function ivoa_cone_saveDatasets(query, saveLocation)Execute a DAL query, and save the datasets referenced by the response.
 <p />
 Applies to those DAL protocols ({@link Siap}, {@link Ssap}, {@link Stap}) where the response points to external data files.

		query - query url to execute(URLString)
		saveLocation - location of a directory in which to save the datasets. May be a {@code file:/}, {@code ivo://}(myspace) or {@code ftp://} location.(IvornOrURI)

	Returns int - number of datasets saved.
       */
int ivoa_cone_saveDatasets ( URLString, IvornOrURI);


/* function ivoa_cone_saveDatasetsSubset(query, saveLocation, rows)Execute a DAL query, and save a subset of the datasets referenced by the response.
 <p />
 Applies to those DAL protocols ({@link Siap}, {@link Ssap}, {@link Stap}) where the response points to external data files.

		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. May be a {@code file:/}, {@code ivo://}(myspace) or {@code ftp://} location.(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset. (0= first row)(ACRList)

	Returns int - number of datasets saved.
       */
int ivoa_cone_saveDatasetsSubset ( URLString, IvornOrURI, ACRList);


/* function ivoa_cone_getRegistryXQuery()Return an XQuery that, when passed to the registry, will return all known services of that type.

 {@stickyWarning In the case of {@link Cone} the registry query will return far too many to be useful - it is necessary to use this xquery as a starting point
 for building a more tightly-constrained query.}
 {@example "Example of querying for cone services related to 'dwarf'"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
#call this method to get a query to list all Cone-search services.
coneQuery = ar.ivoa.cone.getRegistryXQuery()

#combine it into a more tightly contrained query
abellConeQuery = "let $cq := " + coneQuery + """
for $r in $cq
where contains($r/content/subject,'dwarf')
return $r
"""

# perform the query
rs = ar.ivoa.registry.xquerySearch(abellConeQuery)
#inspect the results
print len(rs)
for r in rs:
    print r['id']
 }
 the output of this script is
 <pre>
2
ivo://nasa.heasarc/rasswd
ivo://nasa.heasarc/mcksion
</pre>


	Returns JString - an xquery string
       */
JString ivoa_cone_getRegistryXQuery ( );


/* function ivoa_cone_constructQuery(service, ra, dec, sr)Construct a Cone-Search Query.

 The cone search standard allows queries on Right Ascension, Declination and Search Radius,
 all given in decimal degrees.

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the Cone Search service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://irsa.ipac/2MASS-XSC}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link ConeCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		ra - right ascension e.g {@code 6.950}(double)
		dec - declination e.g. {@code -1.6}(double)
		sr - search radius e.g. {@code 0.1}(double)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString ivoa_cone_constructQuery ( IvornOrURI, double, double, double);
      /* end class
      ivoa.cone
      */
   /* begin class ivoa.externalRegistry
    AR Service: Query an arbitrary IVOA Registry.

 This component gives access to a range of querying functions - for querying using keywords or XQuery.
 The functions either return a raw XML document, or a series of
 datastructures that contain the parsed information of the registry entries.

 {@stickyNote These functions are useful when you want to access records in a registry
 other than the 'system configured' registry,
 or if you wish to access the raw xml of the records.
 For other cases, we recommend using the simple {@link Registry} service.}

 The first parameter to each query method is the endpoint URL of the registry service to connect to.
 These functions will also accept the Resource Identifier of a registry service - which
 will then be resolved into an endpoint URL using the System Registry.See:
				<a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>

				<a href="http://www.ivoa.net/Documents/latest/VOResource.html">IVOA VOResource Definition</a>

				<a href='http://www.ivoa.net/Documents/latest/RegistryInterface.html'>IVOA Registry Interface Standard</a>

				<a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>

				org.astrogrid.acr.ivoa.Registry Registry - simpler interface to system registry
			  */




/* function ivoa_externalRegistry_adqlxSearchXML(registry, adqlx, identifiersOnly)Perform an ADQL/x query

		registry - (IvornOrURI)
		adqlx - (XMLString)
		identifiersOnly - (BOOL)

	Returns XMLString -
       */
XMLString ivoa_externalRegistry_adqlxSearchXML ( IvornOrURI, XMLString, BOOL);


/* function ivoa_externalRegistry_adqlxSearch(registry, adqlx)Perform an ADQL/x query, returning an array of datastructures.

		registry - (IvornOrURI)
		adqlx - (XMLString)

	Returns ListOfResource_Base -
       */
ListOfResource_Base ivoa_externalRegistry_adqlxSearch ( IvornOrURI, XMLString);


/* function ivoa_externalRegistry_adqlsSearchXML(registry, adqls, identifiersOnly)Perform a ADQL/s query.

		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		adqls - a string query (string form of ADQL)(JString)
		identifiersOnly - (BOOL)

	Returns XMLString - xml document of search results -  a series of matching registry records contained within an element  called <tt>VOResources</tt> in the namespace <tt>http://www.ivoa.net/wsdl/RegistrySearch/v1.0</tt>
       */
XMLString ivoa_externalRegistry_adqlsSearchXML ( IvornOrURI, JString, BOOL);


/* function ivoa_externalRegistry_adqlsSearch(registry, adqls)Perform an ADQL/s query, returning an array of datastructures.

		registry - (IvornOrURI)
		adqls - (JString)

	Returns ListOfResource_Base -
       */
ListOfResource_Base ivoa_externalRegistry_adqlsSearch ( IvornOrURI, JString);


/* function ivoa_externalRegistry_keywordSearchXML(registry, keywords, orValues)Perform a keyword search, returning an XML Document.

		registry - resource identifier or endpoint URL  of the registry to connect to(IvornOrURI)
		keywords - space separated list of keywords to search for(JString)
		orValues - - true to 'OR' together matches. false to 'AND' together matches(BOOL)

	Returns XMLString - xml document of search results -  A series of {@code VOResource} elements contained within an element  called {@code VOResources} in the namespace {@code http://www.ivoa.net/xml/RegistryInterface/v1.0}
       */
XMLString ivoa_externalRegistry_keywordSearchXML ( IvornOrURI, JString, BOOL);


/* function ivoa_externalRegistry_keywordSearch(registry, keywords, orValues)Perform a keyword search.
         <p/>
         A more convenient variant of {@link #keywordSearchXML}
 <p />
 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
  #call this function
 regEndpoint = 'http://www.my.registry.endpoint'
 rs = ar.ivoa.externalRegistry.keywordSearch(regEndpoint,"abell",False)
 #see what we've got
 print len(rs)
 #list first 10 identifiers
 for r in rs[:10]
    print r['id']:
 }
 The output is
 <pre>
150
ivo://CDS.VizieR/J/A+A/486/755
ivo://uk.ac.le.star.tmpledas/ledas/ledas/abell
ivo://nasa.heasarc/wblgalaxy
ivo://nasa.heasarc/wbl
ivo://nasa.heasarc/twosigma
ivo://nasa.heasarc/rassebcs
ivo://nasa.heasarc/noras
ivo://nasa.heasarc/eingalclus
ivo://nasa.heasarc/abell
ivo://CDS.VizieR/VII/96
 </pre>

		registry - resource identifier or endpoint URL  of the registry to connect to(IvornOrURI)
		keywords - space separated list of keywords to search for(JString)
		orValues - - true to 'OR' together matches. false to 'AND' together matches(BOOL)

	Returns ListOfResource_Base - list of matching resources.
       */
ListOfResource_Base ivoa_externalRegistry_keywordSearch ( IvornOrURI, JString, BOOL);


/* function ivoa_externalRegistry_getResourceXML(registry, id)Retrieve a named resource from a registry, as an XML document.

 {@stickyNote Try to use {@link #getResource(URI, URI)} instead, which
 returns a result in a more usable format}
 <p />
 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
  #call this function
 regEndpoint = 'http://www.my.registry.endpoint'
 resourceID = 'ivo://uk.ac.cam.ast/IPHAS/images/SIAP'
 xml = ar.ivoa.externalRegistry.getResourceXML(regEndpoint,resourceID)
 }

 {@example "Java Example"
 import org.astrogrid.acr.*;
 import java.net.URI;
 import org.astrogrid.acr.ivoa.ExternalRegistry;
 import org.astrogrid.acr.builtin.ACR
 Finder f = new Finder();
 ACR acr = f.find();
 ExternalRegistry reg = (ExternalRegistry)acr.getService(ExternalRegistry.class);
 URI regEndpoint = new URI("http://www.my.registry.endpoint");
 URI resourceID =new URI("ivo://uk.ac.cam.ast/IPHAS/images/SIAP");
 Document xml = reg.getResourceXML(regEndpoint,resourceID);
 }

 The output will look something like
 {@source
<ri:Resource xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0rc1" xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0" xmlns:va="http://www.ivoa.net/xml/VOApplication/v1.0rc1" xmlns:vg="http://www.ivoa.net/xml/VORegistry/v1.0" xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0" xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" created="2008-02-22T10:27:11" status="active" updated="2008-02-22T14:15:22" xsi:schemaLocation="http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/registry/RegistryInterface/v1.0/RegistryInterface.xsd http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd http://www.ivoa.net/xml/VOTable/v1.0 http://software.astrogrid.org/schema/vo-formats/VOTable/v1.0/VOTable.xsd" xsi:type="vr:Service">
        <title>IPHAS images</title>
        <identifier xmlns:sia="http://www.ivoa.net/xml/SIA/v1.0">ivo://uk.ac.cam.ast/IPHAS/images/SIAP</identifier>
        <curation>
            <publisher ivo-id="ivo://uk.ac.cam.ast/CASU">CASU</publisher>
            <creator>
                <name>IPHAS collaboration</name>
            </creator>
            <contact>
                <name>Guy Rixon</name>
                <email>gtr@ast.cam.ac.uk</email>
            </contact>
        </curation>
        <content>
            <subject>image, photometry, Halpha, INT-WFC</subject>
            <description>Images from the initial data release (IDR) of the INT Photometric Halpha Survey (IPHAS). The survey as a wholeis mapping the northern Galactic Plane in the latitude range |b|&lt;5 deg in the Halpha, r' and i' bands using the Wide Field Camera on the 2.5-m INT telescope at La Palma to a depth of r'=20 (10????). The IDR (Gonzalez-Solares et al. 2007) contains the data obtained between September 2003 and December 2005 during a total of 212 nights. Between these dates, approximately 60 percent of the total survey area was covered in terms of final survey quality.</description>
            <referenceURL>http://casu.ast.cam.ac.uk/surveys-projects/iphas</referenceURL>
            <type>Other</type>
            <contentLevel>Research</contentLevel>
        </content>
        <capability xmlns:sia="http://www.ivoa.net/xml/SIA/v1.0" standardID="ivo://ivoa.net/std/SIA" xsi:type="sia:SimpleImageAccess">
            <interface xsi:type="vs:ParamHTTP">
                <accessURL use="base">http://astrogrid.ast.cam.ac.uk/iphas/siap-atlas/queryImage?</accessURL>
            </interface>
            <imageServiceType>Pointed</imageServiceType>
            <maxQueryRegionSize>
                <long>360</long>
                <lat>360</lat>
            </maxQueryRegionSize>
            <maxImageExtent>
                <long>360</long>
                <lat>360</lat>
            </maxImageExtent>
            <maxImageSize>
                <long>4096</long>
                <lat>4096</lat>
            </maxImageSize>
            <maxFileSize>16800000</maxFileSize>
            <maxRecords>15000</maxRecords>
        </capability>
    </ri:Resource>
     }

		registry - resource identifier or endpoint URL  of the registry to connect to(IvornOrURI)
		id - identifier of the registry resource to retrieve. e.g.{@code ivo://uk.ac.cam.ast/IPHAS/images/SIAP}(IvornOrURI)

	Returns XMLString - xml document of the registry entry - a {@code Resource} document   in the {@code http://www.ivoa.net/xml/VOResource/v1.0} namespace
       */
XMLString ivoa_externalRegistry_getResourceXML ( IvornOrURI, IvornOrURI);


/* function ivoa_externalRegistry_getResource(registry, id)Retrieve a named resource from a registry.

 For most uses, it's better to use this method instead of {@link #getResourceXML}, because the result is easier to work with.

		registry - resource identifier or endpoint URL  of the registry to connect to(IvornOrURI)
		id - identifier of the registry entry to retrieve(IvornOrURI)

	Returns struct Resource_Base - a  datastructue representing the registry entry - will be a {@link Resource} or one of it's   subclasses depending on the registry entry type.
       */
struct Resource_Base ivoa_externalRegistry_getResource ( IvornOrURI, IvornOrURI);


/* function ivoa_externalRegistry_xquerySearchXML(registry, xquery)Search a registry using an XQuery, returning results as XML.
 <p />
 This method can accept an arbitrary XQuery, unlike {@link #xquerySearch(URI, String)}, which requires
 that the XQuery return a list of VOResource elements.

 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
  #call this function
 xquery ="""
         <ssap-wavebands>
          {
          (:find all spectral services :)
           let $ssap := //vor:Resource[capability/@standardID="ivo://ivoa.net/std/SSA"]
           (: find the distinct set of wavebands these services cover  (no duplicates) :)
           for $waveband in distinct-values($ssap/coverage/waveband)
           order by $waveband
           (: print each waveband in turn :)
           return <band name="{data($waveband)}">
                  {
                  (: list IDs of all services that cover this band :)
                    for $r in $ssap[coverage/waveband=$waveband]
                    return $r/identifier
                  }
                  </band>
          }
       </ssap-wavebands>
          """
 regEndpoint = "http://www.my.registry.endpoint"
 xml = ar.ivoa.externalRegistry.xquerySearchXML(regEndpoint,xquery)
 }

This will return the following result
    {@source
<ssap-wavebands>
    <band name="EUV">
        <identifier>ivo://iap.fr/FUSE/SSA</identifier>
        <identifier>ivo://www.g-vo.org/ssa.service.tmap</identifier>
    </band>
    <band name="Infrared">
        <identifier>ivo://archive.eso.org/ESO-SAF-SSAP</identifier>
        <identifier>ivo://basebe.obspm.fr/bess0.1</identifier>
        ...
    </band>
    <band name="Millimeter">
        <identifier>ivo://svo.laeff/models/dalessio</identifier>
        <identifier>ivo://voparis.obspm.gepi/BeStars/BeSS/SSAP</identifier>
    </band>
    <band name="Optical">
        <identifier>ivo://archive.eso.org/ESO-SAF-SSAP</identifier>
        <identifier>ivo://basebe.obspm.fr/bess0.1</identifier>
        ...
    </band>
    <band name="Radio">
        <identifier>ivo://obspm.fr/SSA_HIG</identifier>
        <identifier>ivo://voparis.obspm.gepi/BeStars/BeSS/SSAP</identifier>
    </band>
    <band name="UV">
        <identifier>ivo://archive.eso.org/ESO-SAF-SSAP</identifier>
        <identifier>ivo://basebe.obspm.fr/bess0.1</identifier>
        ...
    </band>
    <band name="X-ray">
        <identifier>ivo://svo.iaa/models/SSP/Xray</identifier>
        <identifier>ivo://www.g-vo.org/ssa.service.tmap</identifier>
    </band>
</ssap-wavebands>
     }

		registry - resource identifier or endpoint URL  of the registry to connect to(IvornOrURI)
		xquery - the query to perform. Must return a well-formed xml document - i.e. starting with a single root element.(JString)

	Returns XMLString - the result of executing this xquery over the specified registry - a document of arbitrary structure.
       */
XMLString ivoa_externalRegistry_xquerySearchXML ( IvornOrURI, JString);


/* function ivoa_externalRegistry_xquerySearch(registry, xquery)Search a registry using an XQuery.
 <p />
 {@stickyWarning This method returns an array of matching {@link Resource} objects - so the XQuery
 used must produce whole {@code VOResource} elements}

 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
  #call this function
 xquery ="//vor:Resource[@xsi:type &= '*DataCollection']"
 regEndpoint = "http://www.my.registry.endpoint"
 rs = ar.ivoa.externalRegistry.xquerySearch(regEndpoint,xquery)
 }

 The above XQuery could be written in a longer equivalent form, which is
 convenient when there are many filter clauses:
 {@source
        for $r in //vor:Resource
        where $r/@xsi:type  &=  '*DataCollection'
        return $r
 }

		registry - resource identifier or endpoint URL  of the registry to connect to(IvornOrURI)
		xquery - An XQuery that should return a document, or nodeset, containing whole {@code <Resource>} elements.   Results are not required to be single-rooted, and resource elements may be embedded within other elements - although the  parser will fail in extreme cases.(JString)

	Returns ListOfResource_Base - an array containing any registry records present in the query result.
       */
ListOfResource_Base ivoa_externalRegistry_xquerySearch ( IvornOrURI, JString);


/* function ivoa_externalRegistry_getIdentityXML(registry)Access the  resource  describing a registry itself, as an XML Document.

		registry - resource identifier or endpoint URL  of the registry to connect to(IvornOrURI)

	Returns XMLString - that registry's own service description - a single Resource documnt
       */
XMLString ivoa_externalRegistry_getIdentityXML ( IvornOrURI);


/* function ivoa_externalRegistry_getIdentity(registry)Access the  resource  describing a registry itself

		registry - resource identifier or endpoint URL  of the registry to connect to(IvornOrURI)

	Returns struct RegistryService - that registry's own service description
       */
struct RegistryService ivoa_externalRegistry_getIdentity ( IvornOrURI);


/* function ivoa_externalRegistry_buildResources(doc)Build an array of resource objects from an XML document.

		doc - an xml document of resources, for example one returned from a call to   {@link #keywordSearchXML(URI, String, boolean)}(XMLString)

	Returns ListOfResource_Base - the parsed resource objects
       */
ListOfResource_Base ivoa_externalRegistry_buildResources ( XMLString);
      /* end class
      ivoa.externalRegistry
      */
   /* begin class ivoa.registry
    AR Service: Query  the system-configured Registry.

 AstroRuntime uses an IVOA-compliant registry to retrieve details of available resources
  - servers, applications, catalogues, etc.
  <p/>
  The endpoint of this registry service can be inspected by calling {@link #getSystemRegistryEndpoint()}.
  In cases where this service is unavailable, registry queries will automatically fall-back to the
  backup registry service, whose endpoint is defined by {@link #getFallbackSystemRegistryEndpoint()}.
 The query functions in this interface are the equivalent to their counterparts in the
 {@link ExternalRegistry} interface, but operate against the System and Fallback registries.
  {@stickyNote These endpoints can be altered by using the UI preferences pane, or the web interface, or via commandline properties, or
  programmatically using the {@link Configuration} service.}See:
				<a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>

				<a href="http://www.ivoa.net/Documents/latest/VOResource.html">IVOA VOResource Definition</a>

				<a href='http://www.ivoa.net/Documents/latest/RegistryInterface.html'>IVOA Registry Interface Standard</a>

				<a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>

				ExternalRegistry External Registry - fuller interface to an arbitrary registry
			  */




/* function ivoa_registry_adqlxSearch(adqlx)Perform an ADQL/x registry search, return a list of matching resources

		adqlx - (XMLString)

	Returns ListOfResource_Base -
       */
ListOfResource_Base ivoa_registry_adqlxSearch ( XMLString);


/* function ivoa_registry_adqlsSearch(adqls)Perform an ADQL/s registry search, return a list of matching resources.

		adqls - a string query (string form of ADQL)(JString)

	Returns ListOfResource_Base - a list of matching resources
       */
ListOfResource_Base ivoa_registry_adqlsSearch ( JString);


/* function ivoa_registry_keywordSearch(keywords, orValues)Perform a keyword  search.
 <p/>
 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
  #call this function
 rs = ar.ivoa.registry.keywordSearch("abell",False)
 #see what we've got
 print len(rs)
 #list first 10 identifiers
 for r in rs[:10]
    print r['id']:
 }
 The output is
 <pre>
150
ivo://CDS.VizieR/J/A+A/486/755
ivo://uk.ac.le.star.tmpledas/ledas/ledas/abell
ivo://nasa.heasarc/wblgalaxy
ivo://nasa.heasarc/wbl
ivo://nasa.heasarc/twosigma
ivo://nasa.heasarc/rassebcs
ivo://nasa.heasarc/noras
ivo://nasa.heasarc/eingalclus
ivo://nasa.heasarc/abell
ivo://CDS.VizieR/VII/96
 </pre>

		keywords - space separated list of keywords to search for(JString)
		orValues - - true to 'OR' together matches. false to 'AND' together matches(BOOL)

	Returns ListOfResource_Base - list of matching resources.
       */
ListOfResource_Base ivoa_registry_keywordSearch ( JString, BOOL);


/* function ivoa_registry_getResource(id)Retrieve a named resource from the registry.

		id - identifier of the registry resource to retrieve. e.g.{@code ivo://uk.ac.cam.ast/IPHAS/images/SIAP}(IvornOrURI)

	Returns struct Resource_Base - a  datastructure representing the registry entry - will be a {@link Resource} or one of it's   subclasses depending on the registry entry type.
       */
struct Resource_Base ivoa_registry_getResource ( IvornOrURI);


/* function ivoa_registry_xquerySearch(xquery)Search the registry using an XQuery.
 <p/>
 {@stickyWarning This method returns an array of matching {@link Resource} objects - so the XQuery
 used must produce whole {@code VOResource} elements}

 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
  #call this function
 xquery ="//vor:Resource[@xsi:type &= '*DataCollection']"
 rs = ar.ivoa.registry.xquerySearch(xquery)
 }

 The above XQuery could be written in a longer equivalent form, which is
 convenient when there are many filter clauses:
 {@source
        for $r in //vor:Resource
        where $r/@xsi:type  &=  '*DataCollection'
        return $r
 }

		xquery - An XQuery that should return a document, or nodeset, containing whole {@code <Resource>} elements.   Results are not required to be single-rooted, and resource elements may be embedded within other elements - although the  parser will fail in extreme cases.(JString)

	Returns ListOfResource_Base - an array containing any registry records present in the query result.
       */
ListOfResource_Base ivoa_registry_xquerySearch ( JString);


/* function ivoa_registry_xquerySearchXML(xquery)Search the registry using an XQuery, returning results as XML.
 <p />
 This method can accept an arbitrary XQuery, unlike {@link #xquerySearch(String)}, which requires
 that the XQuery return a list of VOResource elements.

 {@example "Python Example"
  # connect to the AR
 from xmlrpc import Server
 from os.path import expanduser
 ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
  #call this function
 xquery ="""
     <ssap-wavebands>
          {
          (:find all spectral services :)
           let $ssap := //vor:Resource[capability/@standardID="ivo://ivoa.net/std/SSA"]
           (: find the distinct set of wavebands these services cover  (no duplicates) :)
           for $waveband in distinct-values($ssap/coverage/waveband)
           order by $waveband
           (: print each waveband in turn :)
           return <band name="{data($waveband)}">
                  {
                  (: list IDs of all services that cover this band :)
                    for $r in $ssap[coverage/waveband=$waveband]
                    return $r/identifier
                  }
                  </band>
          }
       </ssap-wavebands>
          """
 xml = ar.ivoa.registry.xquerySearchXML(regEndpoint,xquery)
 }

This will return the following result
    {@source
<ssap-wavebands>
    <band name="EUV">
        <identifier>ivo://iap.fr/FUSE/SSA</identifier>
        <identifier>ivo://www.g-vo.org/ssa.service.tmap</identifier>
    </band>
    <band name="Infrared">
        <identifier>ivo://archive.eso.org/ESO-SAF-SSAP</identifier>
        <identifier>ivo://basebe.obspm.fr/bess0.1</identifier>
        ...
    </band>
    <band name="Millimeter">
        <identifier>ivo://svo.laeff/models/dalessio</identifier>
        <identifier>ivo://voparis.obspm.gepi/BeStars/BeSS/SSAP</identifier>
    </band>
    <band name="Optical">
        <identifier>ivo://archive.eso.org/ESO-SAF-SSAP</identifier>
        <identifier>ivo://basebe.obspm.fr/bess0.1</identifier>
        ...
    </band>
    <band name="Radio">
        <identifier>ivo://obspm.fr/SSA_HIG</identifier>
        <identifier>ivo://voparis.obspm.gepi/BeStars/BeSS/SSAP</identifier>
    </band>
    <band name="UV">
        <identifier>ivo://archive.eso.org/ESO-SAF-SSAP</identifier>
        <identifier>ivo://basebe.obspm.fr/bess0.1</identifier>
        ...
    </band>
    <band name="X-ray">
        <identifier>ivo://svo.iaa/models/SSP/Xray</identifier>
        <identifier>ivo://www.g-vo.org/ssa.service.tmap</identifier>
    </band>
</ssap-wavebands>
     }

		xquery - the query to perform. Must return a well-formed xml document - i.e. starting with a single root element.(JString)

	Returns XMLString - the result of executing this xquery over the specified registry - a document of arbitrary structure.
       */
XMLString ivoa_registry_xquerySearchXML ( JString);


/* function ivoa_registry_getIdentity()Access the resource that describing the system registry itself.

 <p />
 This returned resource describes what search capabilities are provided by the registry


	Returns struct RegistryService -
       */
struct RegistryService ivoa_registry_getIdentity ( );


/* function ivoa_registry_getSystemRegistryEndpoint()Access the endpoint of the system registry


	Returns IvornOrURI -
       */
IvornOrURI ivoa_registry_getSystemRegistryEndpoint ( );


/* function ivoa_registry_getFallbackSystemRegistryEndpoint()Access the endpoint of the fallback system registry


	Returns IvornOrURI -
       */
IvornOrURI ivoa_registry_getFallbackSystemRegistryEndpoint ( );
      /* end class
      ivoa.registry
      */
   /* begin class ivoa.siap
    AR Service: Query for <b>Images</b> from Simple Image Access Protocol (SIAP) services (DAL).
 <p />
 {@stickyNote This class provides functions to construct a DAL query.
 To execute that query, see the examples and methods in the {@link Dal} class.
 }

 <h2>Example</h2>
 The following example constructs a query URL, performs the query, and then downloads the
 resulting images. See {@link Dal} for other things that can be done with a query URL.
 {@example "Query a SIAP service and download images (Python)"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
siap = ar.ivoa.siap #take a reference to the AR SIAP component

#the SIAP service to query (selected using voexplorer)
service = "ivo://irsa.ipac/MAST-Scrapbook"

#resolve an object name to a position
pos = ar.cds.sesame.resolve('m54')

#build a query
query = siap.constructQuery(service,pos['ra'],pos['dec'],0.001)
print "QueryURL",query

#execute the query
rows = siap.execute(query)

#inspect what we've got.
print "Rows Returned",len(rows)
print "Metadata Keys",rows[0].keys()
print "Image URLs"
for r in rows :
    print r['AccessReference']

#download these datasets into current directory
#compute url for current directory
from urlparse import urlunsplit
from os import getcwd
currentDirURL = urlunsplit(['file','',getcwd(),'',''])
print "Downloading images to",currentDirURL
siap.saveDatasets(query,currentDirURL)
 }
 The output from this script is shown below. The result is that 3 files ({@code data-0.fits}, {@code data-1.fits}, {@code data-2.fits}) are downloaded to the current directory.
 <blockquote><tt>
QueryURL http://irsa.ipac.caltech.edu/cgi-bin/Atlas/nph-atlas?mission=Scrapbook&hdr_location=%5CScrapbookDataPath%5C&collection_desc=The+MAST+Image%2FSpectra+Scrapbook+%28Scrapbook%29&SIAP_ACTIVE=1&POS=283.7636667%2C-30.4785&SIZE=0.0010<br />
Rows Returned 3<br />
Metadata Keys ['Scale', 'crpix1', 'crpix2', 'Title', 'inst_id', 'cd2_1', 'ctype2', 'ctype1', 'cd2_2', 'DEC', 'size', 'RADAR', 'Format', 'naxis1', 'naxis2', 'object_id', 'MAST', 'bandpass_id', 'fname', '2mass_fits', 'Naxes', 'RA', 'mjd', 'AccessReference', 'crval2', 'crval1', 'bandpass_unit', 'Naxis', 'GIF', 'ra4', 'bandpass_lolimit', 'ra2', 'ra3', 'ra1', 'dec4', 'dec1', 'dec2', 'dec3', 'cd1_2', 'VOX:WCS_CDMatrix', 'cd1_1', 'data_id', '2mass_jpeg', 'bandpass_refvalue', 'bandpass_hilimit']<br />
Image URLs<br />
http://archive.stsci.edu/cgi-bin/hst_preview_search?imfmt=fits&name=U37GA405R<br />
http://archive.stsci.edu/cgi-bin/hst_preview_search?imfmt=fits&name=U37GA40CR<br />
http://archive.stsci.edu/cgi-bin/hst_preview_search?imfmt=fits&name=O5HJT3DYQ<br />
Downloading images to file:///Users/noel/Documents/workspace/python
 </tt></blockquote>See:
				Dal

				<a href="http://www.ivoa.net/Documents/latest/SIA.html">IVOA SIAP Standard Document</a>

				Sesame Sesame - resolves object names to RA,Dec positions

				#getRegistryXQuery() getRegistryXQuery() - a query to list all SIAP Services.
			  */




/* function ivoa_siap_constructQuery(service, ra, dec, size)Construct a SIAP Query on Right Ascension, Declination and Search radius (decimal degrees).

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the SIAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://nasa.heasarc/skyview/halpha}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link SiapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		ra - right ascension  e.g {@code 6.950}(double)
		dec - declination e.g. {@code -1.6}(double)
		size - radius of cone  e.g. {@code 0.1}(double)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString ivoa_siap_constructQuery ( IvornOrURI, double, double, double);


/* function ivoa_siap_constructQueryF(service, ra, dec, size, format)Construct a SIAP Query on Right Ascension, Declination, Search radius (decimal degrees), and Format.

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the SIAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://nasa.heasarc/skyview/halpha}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link SiapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		ra - right ascension  e.g {@code 6.950}(double)
		dec - declination  e.g. {@code -1.6}(double)
		size - radius of cone  e.g. {@code 0.1}(double)
		format - format of images e.g. {@code FITS}(JString)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString ivoa_siap_constructQueryF ( IvornOrURI, double, double, double, JString);


/* function ivoa_siap_constructQueryS(service, ra, dec, ra_size, dec_size)Construct a SIAP Query on Right Ascension, Declination, and Search area in RA and Dec

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the SIAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://nasa.heasarc/skyview/halpha}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link SiapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		ra - right ascension  e.g {@code 6.950}(double)
		dec - declination  e.g. {@code -1.6}(double)
		ra_size - size of {@code ra}  e.g. {@code 0.1}(double)
		dec_size - size of {@code dec} e.g. {@code 0.2}(double)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString ivoa_siap_constructQueryS ( IvornOrURI, double, double, double, double);


/* function ivoa_siap_constructQuerySF(service, ra, dec, ra_size, dec_size, format)Construct a SIAP Query on Right Ascension, Declination, Search area in RA and Dec, and Format.

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the SIAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://nasa.heasarc/skyview/halpha}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link SiapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl>   </blockquote>(IvornOrURI)
		ra - right ascension  e.g {@code 6.950}(double)
		dec - declination  e.g. {@code -1.6}(double)
		ra_size - size of {@code ra} e.g. {@code 0.1}(double)
		dec_size - size of {@code dec} e.g. {@code 0.2}(double)
		format - format of images {@code FITS}(JString)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString ivoa_siap_constructQuerySF ( IvornOrURI, double, double, double, double, JString);


/* function ivoa_siap_addOption(query, optionName, optionValue)Add an additional option to a previously constructed query.
 <p/>
 Sometimes neccessary, for some DAL protocols, to provide optional query parameters.

		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)

	Returns URLString - {@code query} with the option appended.
       */
URLString ivoa_siap_addOption ( URLString, JString, JString);


/* function ivoa_siap_execute(query)Execute a DAL query, returning a datastructure

		query - query url to execute(URLString)

	Returns ListOfACRKeyValueMap - The service response parsed as a list of  of rows. Each row is represented is a map between UCD or datamodel keys    and values from the response
       */
ListOfACRKeyValueMap ivoa_siap_execute ( URLString);


/* function ivoa_siap_executeVotable(query)Execute a DAL query, returning a Votable document.

		query - query url to execute(URLString)

	Returns XMLString - a votable document of results
       */
XMLString ivoa_siap_executeVotable ( URLString);


/* function ivoa_siap_executeAndSave(query, saveLocation)Execute a DAL query and save the resulting document.

		query - query url to execute(URLString)
		saveLocation - location to save result document - May be {@code file:/}, {@code ivo://} (myspace), {@code ftp://} location(IvornOrURI)

	Returns void -
       */
void ivoa_siap_executeAndSave ( URLString, IvornOrURI);


/* function ivoa_siap_saveDatasets(query, saveLocation)Execute a DAL query, and save the datasets referenced by the response.
 <p />
 Applies to those DAL protocols ({@link Siap}, {@link Ssap}, {@link Stap}) where the response points to external data files.

		query - query url to execute(URLString)
		saveLocation - location of a directory in which to save the datasets. May be a {@code file:/}, {@code ivo://}(myspace) or {@code ftp://} location.(IvornOrURI)

	Returns int - number of datasets saved.
       */
int ivoa_siap_saveDatasets ( URLString, IvornOrURI);


/* function ivoa_siap_saveDatasetsSubset(query, saveLocation, rows)Execute a DAL query, and save a subset of the datasets referenced by the response.
 <p />
 Applies to those DAL protocols ({@link Siap}, {@link Ssap}, {@link Stap}) where the response points to external data files.

		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. May be a {@code file:/}, {@code ivo://}(myspace) or {@code ftp://} location.(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset. (0= first row)(ACRList)

	Returns int - number of datasets saved.
       */
int ivoa_siap_saveDatasetsSubset ( URLString, IvornOrURI, ACRList);


/* function ivoa_siap_getRegistryXQuery()Return an XQuery that, when passed to the registry, will return all known services of that type.

 {@stickyWarning In the case of {@link Cone} the registry query will return far too many to be useful - it is necessary to use this xquery as a starting point
 for building a more tightly-constrained query.}
 {@example "Example of querying for cone services related to 'dwarf'"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
#call this method to get a query to list all Cone-search services.
coneQuery = ar.ivoa.cone.getRegistryXQuery()

#combine it into a more tightly contrained query
abellConeQuery = "let $cq := " + coneQuery + """
for $r in $cq
where contains($r/content/subject,'dwarf')
return $r
"""

# perform the query
rs = ar.ivoa.registry.xquerySearch(abellConeQuery)
#inspect the results
print len(rs)
for r in rs:
    print r['id']
 }
 the output of this script is
 <pre>
2
ivo://nasa.heasarc/rasswd
ivo://nasa.heasarc/mcksion
</pre>


	Returns JString - an xquery string
       */
JString ivoa_siap_getRegistryXQuery ( );
      /* end class
      ivoa.siap
      */
   /* begin class ivoa.ssap
    AR Service: Query for <b>Spectra</b> from Simple Spectral Access Protool (SSAP) Services (DAL).
 <p />
 {@stickyNote This class provides functions to construct a DAL query.
 To execute that query, see the examples and methods in the {@link Dal} class.
 }

 <h2>Example</h2>
 The following example constructs a query URL, performs the query, and then downloads the
 first three spectra. See {@link Dal} for other things that can be done with a query URL.

 {@example "Query a SSAP service and download a subset of spectra (Python)"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
ssap = ar.ivoa.ssap #take a reference to the AR SSAP component

#the SSAP service to query (selected using voexplorer)
service = "ivo://archive.eso.org/ESO-SAF-SSAP"

#resolve an object name to a position
pos = ar.cds.sesame.resolve('m1')

#build a query
query = ssap.constructQuery(service,pos['ra'],pos['dec'],0.005)
print "QueryURL",query

#execute the query
rows = ssap.execute(query)

#inspect what we've got.
print "Rows Returned",len(rows)
print "Metadata Keys",rows[0].keys()

#download first three datasets into current directory
#compute url for current directory
from urlparse import urlunsplit
from os import getcwd
currentDirURL = urlunsplit(['file','',getcwd(),'',''])
print "Downloading images to",currentDirURL
ssap.saveDatasetsSubset(query,currentDirURL,[0,1,2])
 }
 The output of this script is shown below.
 <blockquote><tt>
QueryURL http://archive.eso.org/apps/ssaserver/EsoProxySsap?POS=83.6332083%2C22.0144722&SIZE=0.0050&REQUEST=queryData <br />
Rows Returned 63<br />
Metadata Keys ['meta.bib.bibcode', 'SpectralLocation', 'time.duration;obs.exposure', 'meta.display.url', 'pos.eq', 'AssocID', 'time;meta.dataset', 'meta.id;obs.seq', 'SpectralAxisUnit', 'TimeCalibration', 'meta.code.class;obs', 'meta.ref.url', 'FluxCalibration', 'time.duration;obs', 'instr.fov', 'FovRef', 'Format', 'PublisherDate', 'Collection', 'meta.id;meta.dataset', 'meta.curation', 'meta.id;obs', 'DataLength', 'FluxAxisUnit', 'DatasetType', 'pos.eq.ra;meta.main', 'meta.id;instr', 'meta.title;obs.proposal', 'instr.bandwidth', 'meta.code.class;obs.param', 'time.start;obs', 'time.epoch', 'meta.version;meta.dataset', 'pos.eq.dec;meta.main', 'SpectralCalibration', 'instr.bandpass', 'time.equinox;pos.frame', 'PosAngle', 'meta.title;meta.dataset', 'Creator', 'meta.code.class', 'em;stat.min', 'CreationType', 'instr.setup', 'SpatialCalibration', 'meta.code.class;instr.setup', 'meta.id;src', 'SpaceFrameName', 'meta.code;obs.proposal', 'em;stat.max', 'DataSource', 'meta.id.PI', 'time.end;obs', 'DataModel', 'meta.id;instr.tel']<br />
Downloading images to file:///Users/noel/Documents/workspace/python
 </tt></blockquote>See:
				Dal

				Sesame Sesame - resolves object names to RA,Dec positions

				#getRegistryXQuery() getRegistryXQuery() - a query to list all SSAP Services.

				<a href='http://www.ivoa.net/Documents/latest/SSA.html'>IVOA SSAP Standard Document</a>
			  */




/* function ivoa_ssap_constructQuery(service, ra, dec, size)Construct a SSAP Query on Right Ascension, Declination and Search radius (decimal degrees).

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the SSAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://archive.eso.org/ESO-SAF-SSAP]   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link SsapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		ra - right ascension  e.g {@code 6.950}(double)
		dec - declination e.g. {@code -1.6}(double)
		size - radius of cone e.g. {@code 0.1}(double)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString ivoa_ssap_constructQuery ( IvornOrURI, double, double, double);


/* function ivoa_ssap_constructQueryS(service, ra, dec, ra_size, dec_size)Construct a SSAP Query on Right Ascension, Declination, and Search area in RA and Dec

		service - Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.   <blockquote><dl>   <dt>Resource Identifier</dt><dd>   The resource ID of the SSAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://archive.eso.org/ESO-SAF-SSAP}   <br/>The {@link Registry} will be queried to    resolve the resource ID into a {@link Resource} object, from which the {@link SsapCapability} will be found, from which in turn the first   {@link AccessURL} will be used.   </dd>   <dt>URL of the Service</dt><dd>   The endpoint URL. Can be any {@code http://} URL.   </dd>   </dl></blockquote>(IvornOrURI)
		ra - right ascension e.g {@code 6.950}(double)
		dec - declination e.g. {@code -1.6}(double)
		ra_size - size of {@code ra} e.g. {@code 0.1}(double)
		dec_size - size of {@code dec} e.g. {@code 0.2}(double)

	Returns URLString - A query URL. The query can then be performed by either   <ul>  <li>  programmatically performing a HTTP GET on the query URL  </li>  <li>  passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}  </li>     </ul>
       */
URLString ivoa_ssap_constructQueryS ( IvornOrURI, double, double, double, double);


/* function ivoa_ssap_addOption(query, optionName, optionValue)Add an additional option to a previously constructed query.
 <p/>
 Sometimes neccessary, for some DAL protocols, to provide optional query parameters.

		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)

	Returns URLString - {@code query} with the option appended.
       */
URLString ivoa_ssap_addOption ( URLString, JString, JString);


/* function ivoa_ssap_execute(query)Execute a DAL query, returning a datastructure

		query - query url to execute(URLString)

	Returns ListOfACRKeyValueMap - The service response parsed as a list of  of rows. Each row is represented is a map between UCD or datamodel keys    and values from the response
       */
ListOfACRKeyValueMap ivoa_ssap_execute ( URLString);


/* function ivoa_ssap_executeVotable(query)Execute a DAL query, returning a Votable document.

		query - query url to execute(URLString)

	Returns XMLString - a votable document of results
       */
XMLString ivoa_ssap_executeVotable ( URLString);


/* function ivoa_ssap_executeAndSave(query, saveLocation)Execute a DAL query and save the resulting document.

		query - query url to execute(URLString)
		saveLocation - location to save result document - May be {@code file:/}, {@code ivo://} (myspace), {@code ftp://} location(IvornOrURI)

	Returns void -
       */
void ivoa_ssap_executeAndSave ( URLString, IvornOrURI);


/* function ivoa_ssap_saveDatasets(query, saveLocation)Execute a DAL query, and save the datasets referenced by the response.
 <p />
 Applies to those DAL protocols ({@link Siap}, {@link Ssap}, {@link Stap}) where the response points to external data files.

		query - query url to execute(URLString)
		saveLocation - location of a directory in which to save the datasets. May be a {@code file:/}, {@code ivo://}(myspace) or {@code ftp://} location.(IvornOrURI)

	Returns int - number of datasets saved.
       */
int ivoa_ssap_saveDatasets ( URLString, IvornOrURI);


/* function ivoa_ssap_saveDatasetsSubset(query, saveLocation, rows)Execute a DAL query, and save a subset of the datasets referenced by the response.
 <p />
 Applies to those DAL protocols ({@link Siap}, {@link Ssap}, {@link Stap}) where the response points to external data files.

		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. May be a {@code file:/}, {@code ivo://}(myspace) or {@code ftp://} location.(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset. (0= first row)(ACRList)

	Returns int - number of datasets saved.
       */
int ivoa_ssap_saveDatasetsSubset ( URLString, IvornOrURI, ACRList);


/* function ivoa_ssap_getRegistryXQuery()Return an XQuery that, when passed to the registry, will return all known services of that type.

 {@stickyWarning In the case of {@link Cone} the registry query will return far too many to be useful - it is necessary to use this xquery as a starting point
 for building a more tightly-constrained query.}
 {@example "Example of querying for cone services related to 'dwarf'"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
#call this method to get a query to list all Cone-search services.
coneQuery = ar.ivoa.cone.getRegistryXQuery()

#combine it into a more tightly contrained query
abellConeQuery = "let $cq := " + coneQuery + """
for $r in $cq
where contains($r/content/subject,'dwarf')
return $r
"""

# perform the query
rs = ar.ivoa.registry.xquerySearch(abellConeQuery)
#inspect the results
print len(rs)
for r in rs:
    print r['id']
 }
 the output of this script is
 <pre>
2
ivo://nasa.heasarc/rasswd
ivo://nasa.heasarc/mcksion
</pre>


	Returns JString - an xquery string
       */
JString ivoa_ssap_getRegistryXQuery ( );
      /* end class
      ivoa.ssap
      */
   /* begin class ivoa.vospace
    Work with VOSpace - a distributed storage system.

 VOSpace is the IVOA sanctioned distributed system @see <a href="http://www.ivoa.net/Documents/latest/VOSpace.html">VOSpace Specification</a>.

 All resources within VOSpace are identified by a <tt>vos: </tt> URI that identifies the VOSpace server and location within that server of the file in question. */




/* function ivoa_vospace_getHome()Retrieve the URI of the current user's home folder in VOSpace. Each user in a community is assigned a home folder somewhere in VOSpace.


	Returns IvornOrURI - The URI of the current user's home folder in VOSpace.
       */
IvornOrURI ivoa_vospace_getHome ( );


/* function ivoa_vospace_getNodeInformation(vosuri)access metadata about a VOSpace resource.

		vosuri - resource to investigate(IvornOrURI)

	Returns struct NodeInformation - a beanful of information
       */
struct NodeInformation ivoa_vospace_getNodeInformation ( IvornOrURI);


/* function ivoa_vospace_listVOSpaces()List the known VOSpaces. This will query the registry to discover the vospaces that are available.


	Returns ListOfService_Base - an array of service descriptions.
       */
ListOfService_Base ivoa_vospace_listVOSpaces ( );
      /* end class
      ivoa.vospace
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


/* function nvo_cone_getRegistryQuery()helper method - returns an ADQL/s query that should be passed to a registry to list all available cone services.

 Can be used as a starting point for building filters, etc


	Returns JString - an adql query string
       */
JString nvo_cone_getRegistryQuery ( );


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
    AR Service: Launch a new Task Runner / Query Builder GUISee:
				org.astrogrid.acr.astrogrid.Applications
			  */




/* function userInterface_applicationLauncher_show()display a new TaskRunner


	Returns void -
       */
void userInterface_applicationLauncher_show ( );
      /* end class
      userInterface.applicationLauncher
      */
   /* begin class userInterface.astroscope
    AR Servuce: Launch a new AstroScope GUI */




/* function userInterface_astroscope_show()Display a new instance of All-VO Astroscope


	Returns void -
       */
void userInterface_astroscope_show ( );
      /* end class
      userInterface.astroscope
      */
   /* begin class userInterface.fileManager
    AR Service: Launch a new FileManager GUI */




/* function userInterface_fileManager_show()Display a new instance of FileManager


	Returns void -
       */
void userInterface_fileManager_show ( );
      /* end class
      userInterface.fileManager
      */
   /* begin class userInterface.jobMonitor
    Deprecated:
				replaced by {@link Lookout}
			Control the Job Monitor GUI. (Unimplemented)See:
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
    Deprecated: Control the Lookout UI. (Unimplemented) */




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
			Control the  Myspace Browser UI.See:
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
    AR Service: Launch a new VOExplorer GUISee:
				org.astrogrid.acr.ivoa.Registry

				RegistryGoogle Registry Dialogue
			  */




/* function userInterface_registryBrowser_show()show a new instance of VOExplorer


	Returns void -
       */
void userInterface_registryBrowser_show ( );


/* function userInterface_registryBrowser_hide()Not implmented


	Returns void -
       */
void userInterface_registryBrowser_hide ( );


/* function userInterface_registryBrowser_search(xquery)show an new instance of the VOExplorer, and populate it using an xquery

		xquery - an xquery to populate the resource chooser with. Same format as in an xquery list in voexplorer UI - which is convenient for  constructing these queries.(JString)

	Returns void -
       */
void userInterface_registryBrowser_search ( JString);


/* function userInterface_registryBrowser_open(uri)Show a new instance of VOExplorer, and display a single resource record in it

		uri - the resource ID of the record to display(IvornOrURI)

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
				<a href="http://www.star.bris.ac.uk/~mbt/stil/">Stil Documentation</a>
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


/* function util_tables_convertFromFile(inLocation, inFormat, outFormat)Reads a table in a file into an in-memory table, converting between supported formats.

 {@stickyNote Will only give good results for text-based table formats.}

		inLocation - input location: may be a http://, file://, ivo:// , ftp://                      compressed using unix compress, gzip or bzip2(IvornOrURI)
		inFormat - input handler name: generally one of                        fits, votable, ascii, csv, ipac, wdc or null(JString)
		outFormat - output format: generally one of                       fits, fits-plus,                       votable, votable-tabledata, votable-binary-inline,                       votable-binary-href, votable-fits-inline,                        votable-fits-href,                       text, ascii, csv, html, html-element, latex,                       latex-document or null(JString)

	Returns JString - the converted representation of the table.
       */
JString util_tables_convertFromFile ( IvornOrURI, JString, JString);


/* function util_tables_convert(input, inFormat, outFormat)Converts an in-memory table between supported formats.

 {@stickyNote Will only give good results for text-based table formats.}

		input - the input table.(JString)
		inFormat - input handler name: generally one of                        fits, votable, ascii, csv, ipac, wdc or null(JString)
		outFormat - output format: generally one of                       fits, fits-plus,                       votable, votable-tabledata, votable-binary-inline,                       votable-binary-href, votable-fits-inline,                        votable-fits-href,                       text, ascii, csv, html, html-element, latex,                       latex-document or null(JString)

	Returns JString - a table in the requested format.
       */
JString util_tables_convert ( JString, JString, JString);


/* function util_tables_listOutputFormats()list the table formats this component can write out to


	Returns ListOfJString -
       */
ListOfJString util_tables_listOutputFormats ( );


/* function util_tables_listInputFormats()list the table formats this component can read in from


	Returns ListOfJString -
       */
ListOfJString util_tables_listInputFormats ( );
      /* end class
      util.tables
      */

/* end package util */

/* begin package util */


/* end package util */

/* begin package votech */

/* begin class votech.vomon
    Monitor service availability using the VoMon serviceSee:
				<a href="http://vomon.sourceforge.net">VoMon project page</a>
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


#ifdef __cplusplus
 }
#endif
#endif
