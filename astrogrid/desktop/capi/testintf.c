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
   
   
#include <stdio.h>            
#include "testintf.h"
void printUserInformation( struct UserInformation s) {
int i;
printf("structure UserInformation\n");
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","name", s.name);
printf(" value of %s = %s\n","community", s.community);
printf(" value of %s = %s\n","password", s.password);
}
void printTableBeanComparator( struct TableBeanComparator s) {
int i;
printf("structure TableBeanComparator\n");
}
void printTableBean( struct TableBean s) {
int i;
printf("structure TableBean\n");
for ( i = 0; i < s.columns.n ; i++ ){
printColumnBean(s.columns.list[i]);
}
                  printf(" value of %s = %s\n","description", s.description);
printf(" value of %s = %s\n","name", s.name);
printf(" value of %s = %s\n","role", s.role);
}
void printParameterReferenceBean( struct ParameterReferenceBean s) {
int i;
printf("structure ParameterReferenceBean\n");
printf(" value of %s = %d\n","max", s.max);
printf(" value of %s = %d\n","min", s.min);
printf(" value of %s = %s\n","ref", s.ref);
}
void printParameterBean( struct ParameterBean s) {
int i;
printf("structure ParameterBean\n");
printf(" value of %s = %s\n","name", s.name);
printf(" value of %s = %s\n","description", s.description);
printf(" value of %s = %s\n","unit", s.unit);
printf(" value of %s = %s\n","ucd", s.ucd);
printf(" value of %s = %s\n","id", s.id);
for ( i = 0; i < s.options.n ; i++ ){
/* simple type value of options type=ListOfJString not printed  */
}
                  printf(" value of %s = %s\n","type", s.type);
printf(" value of %s = %s\n","uiName", s.uiName);
for ( i = 0; i < s.defaultValues.n ; i++ ){
/* simple type value of defaultValues type=ListOfJString not printed  */
}
                  printf(" value of %s = %s\n","uType", s.uType);
printf(" value of %s = %s\n","mimeType", s.mimeType);
printf(" value of %s = %s\n","arraysize", s.arraysize);
}
void printNodeInformation( struct NodeInformation s) {
int i;
printf("structure NodeInformation\n");
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","name", s.name);
/* simple type value of attributes type=ACRKeyValueMap not printed  */
/* simple type value of createDate type=ACRDate not printed  */
/* simple type value of modifyDate type=ACRDate not printed  */
printf(" value of %s = %f\n","size", s.size);
printf(" value of %s = %s\n","contentLocation", s.contentLocation);
}
void printInterfaceBean( struct InterfaceBean s) {
int i;
printf("structure InterfaceBean\n");
for ( i = 0; i < s.inputs.n ; i++ ){
printParameterReferenceBean(s.inputs.list[i]);
}
                  printf(" value of %s = %s\n","name", s.name);
for ( i = 0; i < s.outputs.n ; i++ ){
printParameterReferenceBean(s.outputs.list[i]);
}
                  printf(" value of %s = %s\n","description", s.description);
}
void printExecutionMessage( struct ExecutionMessage s) {
int i;
printf("structure ExecutionMessage\n");
printf(" value of %s = %s\n","content", s.content);
printf(" value of %s = %s\n","level", s.level);
printf(" value of %s = %s\n","source", s.source);
printf(" value of %s = %s\n","status", s.status);
/* simple type value of timestamp type=ACRDate not printed  */
}
void printExecutionInformation( struct ExecutionInformation s) {
int i;
printf("structure ExecutionInformation\n");
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","name", s.name);
printf(" value of %s = %s\n","description", s.description);
/* simple type value of startTime type=ACRDate not printed  */
/* simple type value of finishTime type=ACRDate not printed  */
printf(" value of %s = %s\n","status", s.status);
}
void printDatabaseBean( struct DatabaseBean s) {
int i;
printf("structure DatabaseBean\n");
printf(" value of %s = %s\n","description", s.description);
printf(" value of %s = %s\n","name", s.name);
for ( i = 0; i < s.tables.n ; i++ ){
printTableBean(s.tables.list[i]);
}
                  }
void printColumnBean( struct ColumnBean s) {
int i;
printf("structure ColumnBean\n");
printf(" value of %s = %s\n","name", s.name);
printf(" value of %s = %s\n","description", s.description);
printf(" value of %s = %s\n","unit", s.unit);
printf(" value of %s = %s\n","ucd", s.ucd);
printTableDataType(s.columnDataType);
}
void printCeaService( struct CeaService s) {
int i;
printf("structure CeaService\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.capabilities.n ; i++ ){
printCapability_Base(s.capabilities.list[i]);
}
                  }
void printCeaServerCapability( struct CeaServerCapability s) {
int i;
printf("structure CeaServerCapability\n");
printf(" value of %s = %s\n","description", s.description);
for ( i = 0; i < s.interfaces.n ; i++ ){
printInterface_Base(s.interfaces.list[i]);
}
                  printf(" value of %s = %s\n","standardID", s.standardID);
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.managedApplications.n ; i++ ){
/* simple type value of managedApplications type=ListOfIvornOrURI not printed  */
}
                  }
void printCeaApplication( struct CeaApplication s) {
int i;
printf("structure CeaApplication\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.applicationCapabilities.n ; i++ ){
/* simple type value of applicationCapabilities type=ListOfIvornOrURI not printed  */
}
                  for ( i = 0; i < s.interfaces.n ; i++ ){
printInterfaceBean(s.interfaces.list[i]);
}
                  for ( i = 0; i < s.parameters.n ; i++ ){
printParameterBean(s.parameters.list[i]);
}
                  printf(" value of %s = %s\n","applicationKind", s.applicationKind);
}
void printAbstractInformation( struct AbstractInformation s) {
int i;
printf("structure AbstractInformation\n");
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","name", s.name);
}
void printSesamePositionBean( struct SesamePositionBean s) {
int i;
printf("structure SesamePositionBean\n");
printf(" value of %s = %f\n","dec", s.dec);
printf(" value of %s = %f\n","decErr", s.decErr);
printf(" value of %s = %s\n","oName", s.oName);
printf(" value of %s = %s\n","oType", s.oType);
printf(" value of %s = %s\n","posStr", s.posStr);
printf(" value of %s = %f\n","ra", s.ra);
printf(" value of %s = %f\n","raErr", s.raErr);
printf(" value of %s = %s\n","target", s.target);
printf(" value of %s = %s\n","service", s.service);
for ( i = 0; i < s.aliases.n ; i++ ){
/* simple type value of aliases type=ListOfJString not printed  */
}
                  }
void printFunctionBean( struct FunctionBean s) {
int i;
printf("structure FunctionBean\n");
printf(" value of %s = %s\n","description", s.description);
printf(" value of %s = %s\n","name", s.name);
for ( i = 0; i < s.parameters.n ; i++ ){
printParameterBean(s.parameters.list[i]);
}
                  }
void printAvailabilityBean( struct AvailabilityBean s) {
int i;
printf("structure AvailabilityBean\n");
printf(" value of %s = %s\n","location", s.location);
printf(" value of %s = %s\n","message", s.message);
printf(" value of %s = %s\n","serverName", s.serverName);
printf(" value of %s = %s\n","timeOnServer", s.timeOnServer);
printf(" value of %s = %s\n","upTime", s.upTime);
printf(" value of %s = %s\n","validTo", s.validTo);
}
void printWebServiceInterface( struct WebServiceInterface s) {
int i;
printf("structure WebServiceInterface\n");
for ( i = 0; i < s.accessUrls.n ; i++ ){
printAccessURL(s.accessUrls.list[i]);
}
                  printf(" value of %s = %s\n","role", s.role);
for ( i = 0; i < s.securityMethods.n ; i++ ){
printSecurityMethod(s.securityMethods.list[i]);
}
                  printf(" value of %s = %s\n","version", s.version);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.wsdlURLs.n ; i++ ){
/* simple type value of wsdlURLs type=ListOfIvornOrURI not printed  */
}
                  }
void printValidation( struct Validation s) {
int i;
printf("structure Validation\n");
printf(" value of %s = %s\n","validatedBy", s.validatedBy);
printf(" value of %s = %d\n","validationLevel", s.validationLevel);
}
void printTabularDB( struct TabularDB s) {
int i;
printf("structure TabularDB\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
printCoverage(s.coverage);
for ( i = 0; i < s.facilities.n ; i++ ){
printResourceName(s.facilities.list[i]);
}
                  for ( i = 0; i < s.instruments.n ; i++ ){
printResourceName(s.instruments.list[i]);
}
                  for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.formats.n ; i++ ){
printFormat(s.formats.list[i]);
}
                  printCoverage(s.coverage);
for ( i = 0; i < s.catalogues.n ; i++ ){
printCatalog_Base(s.catalogues.list[i]);
}
                  printAccessURL(s.accessURL);
printDatabaseBean(s.database);
}
void printTableService( struct TableService s) {
int i;
printf("structure TableService\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.capabilities.n ; i++ ){
printCapability_Base(s.capabilities.list[i]);
}
                  for ( i = 0; i < s.facilities.n ; i++ ){
printResourceName(s.facilities.list[i]);
}
                  for ( i = 0; i < s.instruments.n ; i++ ){
printResourceName(s.instruments.list[i]);
}
                  for ( i = 0; i < s.tables.n ; i++ ){
printTableBean(s.tables.list[i]);
}
                  }
void printTableDataType( struct TableDataType s) {
int i;
printf("structure TableDataType\n");
printf(" value of %s = %s\n","type", s.type);
printf(" value of %s = %s\n","arraysize", s.arraysize);
}
void printStcResourceProfile( struct StcResourceProfile s) {
int i;
printf("structure StcResourceProfile\n");
printf(" value of %s = %s\n","stcDocument", s.stcDocument);
}
void printStapService( struct StapService s) {
int i;
printf("structure StapService\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.capabilities.n ; i++ ){
printCapability_Base(s.capabilities.list[i]);
}
                  }
void printStapCapability( struct StapCapability s) {
int i;
printf("structure StapCapability\n");
printf(" value of %s = %s\n","description", s.description);
for ( i = 0; i < s.interfaces.n ; i++ ){
printInterface_Base(s.interfaces.list[i]);
}
                  printf(" value of %s = %s\n","standardID", s.standardID);
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.supportedFormats.n ; i++ ){
/* simple type value of supportedFormats type=ListOfJString not printed  */
}
                  printf(" value of %s = %d\n","maxRecords", s.maxRecords);
printStapCapability_Query(s.testQuery);
}
void printStapCapability_Query( struct StapCapability_Query s) {
int i;
printf("structure StapCapability_Query\n");
printf(" value of %s = %s\n","start", s.start);
printf(" value of %s = %s\n","end", s.end);
printSiapCapability_SkyPos(s.pos);
printSiapCapability_SkySize(s.size);
}
void printStandardSTC( struct StandardSTC s) {
int i;
printf("structure StandardSTC\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.resourceProfiles.n ; i++ ){
printStcResourceProfile(s.resourceProfiles.list[i]);
}
                  }
void printSsapService( struct SsapService s) {
int i;
printf("structure SsapService\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.capabilities.n ; i++ ){
printCapability_Base(s.capabilities.list[i]);
}
                  }
void printSsapCapability( struct SsapCapability s) {
int i;
printf("structure SsapCapability\n");
printf(" value of %s = %s\n","description", s.description);
for ( i = 0; i < s.interfaces.n ; i++ ){
printInterface_Base(s.interfaces.list[i]);
}
                  printf(" value of %s = %s\n","standardID", s.standardID);
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","type", s.type);
printf(" value of %s = %s\n","complianceLevel", s.complianceLevel);
for ( i = 0; i < s.dataSources.n ; i++ ){
/* simple type value of dataSources type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.creationTypes.n ; i++ ){
/* simple type value of creationTypes type=ListOfJString not printed  */
}
                  printf(" value of %s = %f\n","maxSearchRadius", s.maxSearchRadius);
printf(" value of %s = %d\n","maxRecords", s.maxRecords);
printf(" value of %s = %d\n","defaultMaxRecords", s.defaultMaxRecords);
printf(" value of %s = %f\n","maxAperture", s.maxAperture);
for ( i = 0; i < s.supportedFrames.n ; i++ ){
/* simple type value of supportedFrames type=ListOfJString not printed  */
}
                  printf(" value of %s = %d\n","maxFileSize", s.maxFileSize);
printSsapCapability_Query(s.testQuery);
}
void printSsapCapability_Query( struct SsapCapability_Query s) {
int i;
printf("structure SsapCapability_Query\n");
printSsapCapability_PosParam(s.pos);
printf(" value of %s = %f\n","size", s.size);
printf(" value of %s = %s\n","queryDataCmd", s.queryDataCmd);
}
void printSsapCapability_PosParam( struct SsapCapability_PosParam s) {
int i;
printf("structure SsapCapability_PosParam\n");
printf(" value of %s = %f\n","lon", s.lon);
printf(" value of %s = %f\n","lat", s.lat);
printf(" value of %s = %s\n","refframe", s.refframe);
}
void printSource( struct Source s) {
int i;
printf("structure Source\n");
printf(" value of %s = %s\n","format", s.format);
printf(" value of %s = %s\n","value", s.value);
}
void printSimpleDataType( struct SimpleDataType s) {
int i;
printf("structure SimpleDataType\n");
printf(" value of %s = %s\n","type", s.type);
printf(" value of %s = %s\n","arraysize", s.arraysize);
}
void printSiapService( struct SiapService s) {
int i;
printf("structure SiapService\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.capabilities.n ; i++ ){
printCapability_Base(s.capabilities.list[i]);
}
                  }
void printSiapCapability( struct SiapCapability s) {
int i;
printf("structure SiapCapability\n");
printf(" value of %s = %s\n","description", s.description);
for ( i = 0; i < s.interfaces.n ; i++ ){
printInterface_Base(s.interfaces.list[i]);
}
                  printf(" value of %s = %s\n","standardID", s.standardID);
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","type", s.type);
printf(" value of %s = %s\n","imageServiceType", s.imageServiceType);
printf(" value of %s = %d\n","maxFileSize", s.maxFileSize);
printSiapCapability_SkySize(s.maxImageExtent);
printSiapCapability_ImageSize(s.maxImageSize);
printSiapCapability_SkySize(s.maxQueryRegionSize);
printSiapCapability_Query(s.testQuery);
printf(" value of %s = %d\n","maxRecords", s.maxRecords);
}
void printSiapCapability_SkySize( struct SiapCapability_SkySize s) {
int i;
printf("structure SiapCapability_SkySize\n");
printf(" value of %s = %f\n","lat", s.lat);
printf(" value of %s = %f\n","lon", s.lon);
}
void printSiapCapability_ImageSize( struct SiapCapability_ImageSize s) {
int i;
printf("structure SiapCapability_ImageSize\n");
printf(" value of %s = %d\n","lat", s.lat);
printf(" value of %s = %d\n","lon", s.lon);
}
void printSiapCapability_SkyPos( struct SiapCapability_SkyPos s) {
int i;
printf("structure SiapCapability_SkyPos\n");
printf(" value of %s = %f\n","lat", s.lat);
printf(" value of %s = %f\n","lon", s.lon);
}
void printSiapCapability_Query( struct SiapCapability_Query s) {
int i;
printf("structure SiapCapability_Query\n");
printf(" value of %s = %s\n","extras", s.extras);
printSiapCapability_SkyPos(s.pos);
printSiapCapability_SkySize(s.size);
printf(" value of %s = %d\n","verb", s.verb);
}
void printService( struct Service s) {
int i;
printf("structure Service\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.capabilities.n ; i++ ){
printCapability_Base(s.capabilities.list[i]);
}
                  }
void printSecurityMethod( struct SecurityMethod s) {
int i;
printf("structure SecurityMethod\n");
printf(" value of %s = %s\n","standardID", s.standardID);
}
void printSearchCapability( struct SearchCapability s) {
int i;
printf("structure SearchCapability\n");
printf(" value of %s = %s\n","description", s.description);
for ( i = 0; i < s.interfaces.n ; i++ ){
printInterface_Base(s.interfaces.list[i]);
}
                  printf(" value of %s = %s\n","standardID", s.standardID);
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","type", s.type);
printf(" value of %s = %s\n","extensionSearchSupport", s.extensionSearchSupport);
printf(" value of %s = %d\n","maxRecords", s.maxRecords);
for ( i = 0; i < s.optionalProtocol.n ; i++ ){
/* simple type value of optionalProtocol type=ListOfJString not printed  */
}
                  }
void printResourceName( struct ResourceName s) {
int i;
printf("structure ResourceName\n");
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","value", s.value);
}
void printResource( struct Resource s) {
int i;
printf("structure Resource\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
}
void printRelationship( struct Relationship s) {
int i;
printf("structure Relationship\n");
for ( i = 0; i < s.relatedResources.n ; i++ ){
printResourceName(s.relatedResources.list[i]);
}
                  printf(" value of %s = %s\n","relationshipType", s.relationshipType);
}
void printRegistryService( struct RegistryService s) {
int i;
printf("structure RegistryService\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.capabilities.n ; i++ ){
printCapability_Base(s.capabilities.list[i]);
}
                  for ( i = 0; i < s.managedAuthorities.n ; i++ ){
/* simple type value of managedAuthorities type=ListOfJString not printed  */
}
                  }
void printRegistryCapability( struct RegistryCapability s) {
int i;
printf("structure RegistryCapability\n");
printf(" value of %s = %s\n","description", s.description);
for ( i = 0; i < s.interfaces.n ; i++ ){
printInterface_Base(s.interfaces.list[i]);
}
                  printf(" value of %s = %s\n","standardID", s.standardID);
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","type", s.type);
}
void printParamHttpInterface( struct ParamHttpInterface s) {
int i;
printf("structure ParamHttpInterface\n");
for ( i = 0; i < s.accessUrls.n ; i++ ){
printAccessURL(s.accessUrls.list[i]);
}
                  printf(" value of %s = %s\n","role", s.role);
for ( i = 0; i < s.securityMethods.n ; i++ ){
printSecurityMethod(s.securityMethods.list[i]);
}
                  printf(" value of %s = %s\n","version", s.version);
printf(" value of %s = %s\n","type", s.type);
printf(" value of %s = %s\n","queryType", s.queryType);
printf(" value of %s = %s\n","resultType", s.resultType);
for ( i = 0; i < s.params.n ; i++ ){
printInputParam(s.params.list[i]);
}
                  }
void printOrganisation( struct Organisation s) {
int i;
printf("structure Organisation\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.facilities.n ; i++ ){
printResourceName(s.facilities.list[i]);
}
                  for ( i = 0; i < s.instruments.n ; i++ ){
printResourceName(s.instruments.list[i]);
}
                  }
void printInterface( struct Interface s) {
int i;
printf("structure Interface\n");
for ( i = 0; i < s.accessUrls.n ; i++ ){
printAccessURL(s.accessUrls.list[i]);
}
                  printf(" value of %s = %s\n","role", s.role);
for ( i = 0; i < s.securityMethods.n ; i++ ){
printSecurityMethod(s.securityMethods.list[i]);
}
                  printf(" value of %s = %s\n","version", s.version);
printf(" value of %s = %s\n","type", s.type);
}
void printInputParam( struct InputParam s) {
int i;
printf("structure InputParam\n");
printf(" value of %s = %s\n","name", s.name);
printf(" value of %s = %s\n","description", s.description);
printf(" value of %s = %s\n","unit", s.unit);
printf(" value of %s = %s\n","ucd", s.ucd);
printf(" value of %s = %s\n","use", s.use);
printSimpleDataType(s.dataType);
}
void printHasCoverage( struct HasCoverage s) {
int i;
printf("structure HasCoverage\n");
printCoverage(s.coverage);
}
void printHarvestCapability( struct HarvestCapability s) {
int i;
printf("structure HarvestCapability\n");
printf(" value of %s = %s\n","description", s.description);
for ( i = 0; i < s.interfaces.n ; i++ ){
printInterface_Base(s.interfaces.list[i]);
}
                  printf(" value of %s = %s\n","standardID", s.standardID);
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","type", s.type);
printf(" value of %s = %d\n","maxRecords", s.maxRecords);
}
void printHandler( struct Handler s) {
int i;
printf("structure Handler\n");
}
void printFormat( struct Format s) {
int i;
printf("structure Format\n");
printf(" value of %s = %s\n","value", s.value);
}
void printCurationDate( struct CurationDate s) {
int i;
printf("structure CurationDate\n");
printf(" value of %s = %s\n","role", s.role);
printf(" value of %s = %s\n","value", s.value);
}
void printDataService( struct DataService s) {
int i;
printf("structure DataService\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.capabilities.n ; i++ ){
printCapability_Base(s.capabilities.list[i]);
}
                  printCoverage(s.coverage);
for ( i = 0; i < s.facilities.n ; i++ ){
printResourceName(s.facilities.list[i]);
}
                  for ( i = 0; i < s.instruments.n ; i++ ){
printResourceName(s.instruments.list[i]);
}
                  printCoverage(s.coverage);
}
void printDataCollection( struct DataCollection s) {
int i;
printf("structure DataCollection\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
printCoverage(s.coverage);
for ( i = 0; i < s.facilities.n ; i++ ){
printResourceName(s.facilities.list[i]);
}
                  for ( i = 0; i < s.instruments.n ; i++ ){
printResourceName(s.instruments.list[i]);
}
                  for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.formats.n ; i++ ){
printFormat(s.formats.list[i]);
}
                  printCoverage(s.coverage);
for ( i = 0; i < s.catalogues.n ; i++ ){
printCatalog_Base(s.catalogues.list[i]);
}
                  printAccessURL(s.accessURL);
}
void printCuration( struct Curation s) {
int i;
printf("structure Curation\n");
for ( i = 0; i < s.contacts.n ; i++ ){
printContact(s.contacts.list[i]);
}
                  for ( i = 0; i < s.contributors.n ; i++ ){
printResourceName(s.contributors.list[i]);
}
                  for ( i = 0; i < s.creators.n ; i++ ){
printCreator(s.creators.list[i]);
}
                  printResourceName(s.publisher);
printf(" value of %s = %s\n","version", s.version);
for ( i = 0; i < s.dates.n ; i++ ){
printCurationDate(s.dates.list[i]);
}
                  }
void printCreator( struct Creator s) {
int i;
printf("structure Creator\n");
printf(" value of %s = %s\n","logoURI", s.logoURI);
printResourceName(s.name);
}
void printCoverage( struct Coverage s) {
int i;
printf("structure Coverage\n");
printResourceName(s.footprint);
printStcResourceProfile(s.stcResourceProfile);
for ( i = 0; i < s.wavebands.n ; i++ ){
/* simple type value of wavebands type=ListOfJString not printed  */
}
                  }
void printContent( struct Content s) {
int i;
printf("structure Content\n");
for ( i = 0; i < s.contentLevel.n ; i++ ){
/* simple type value of contentLevel type=ListOfJString not printed  */
}
                  printf(" value of %s = %s\n","description", s.description);
printf(" value of %s = %s\n","referenceURI", s.referenceURI);
for ( i = 0; i < s.relationships.n ; i++ ){
printRelationship(s.relationships.list[i]);
}
                  for ( i = 0; i < s.subject.n ; i++ ){
/* simple type value of subject type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.type.n ; i++ ){
/* simple type value of type type=ListOfJString not printed  */
}
                  printSource(s.source);
}
void printContact( struct Contact s) {
int i;
printf("structure Contact\n");
printf(" value of %s = %s\n","address", s.address);
printf(" value of %s = %s\n","email", s.email);
printResourceName(s.name);
printf(" value of %s = %s\n","telephone", s.telephone);
}
void printConeService( struct ConeService s) {
int i;
printf("structure ConeService\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.capabilities.n ; i++ ){
printCapability_Base(s.capabilities.list[i]);
}
                  }
void printConeCapability( struct ConeCapability s) {
int i;
printf("structure ConeCapability\n");
printf(" value of %s = %s\n","description", s.description);
for ( i = 0; i < s.interfaces.n ; i++ ){
printInterface_Base(s.interfaces.list[i]);
}
                  printf(" value of %s = %s\n","standardID", s.standardID);
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","type", s.type);
printf(" value of %s = %d\n","maxRecords", s.maxRecords);
printf(" value of %s = %f\n","maxSR", s.maxSR);
printConeCapability_Query(s.testQuery);
}
void printConeCapability_Query( struct ConeCapability_Query s) {
int i;
printf("structure ConeCapability_Query\n");
printf(" value of %s = %s\n","catalog", s.catalog);
printf(" value of %s = %f\n","dec", s.dec);
printf(" value of %s = %s\n","extras", s.extras);
printf(" value of %s = %f\n","ra", s.ra);
printf(" value of %s = %f\n","sr", s.sr);
printf(" value of %s = %d\n","verb", s.verb);
}
void printCatalogService( struct CatalogService s) {
int i;
printf("structure CatalogService\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.rights.n ; i++ ){
/* simple type value of rights type=ListOfJString not printed  */
}
                  for ( i = 0; i < s.capabilities.n ; i++ ){
printCapability_Base(s.capabilities.list[i]);
}
                  printCoverage(s.coverage);
for ( i = 0; i < s.facilities.n ; i++ ){
printResourceName(s.facilities.list[i]);
}
                  for ( i = 0; i < s.instruments.n ; i++ ){
printResourceName(s.instruments.list[i]);
}
                  printCoverage(s.coverage);
for ( i = 0; i < s.tables.n ; i++ ){
printTableBean(s.tables.list[i]);
}
                  }
void printCatalog( struct Catalog s) {
int i;
printf("structure Catalog\n");
printf(" value of %s = %s\n","description", s.description);
printf(" value of %s = %s\n","name", s.name);
for ( i = 0; i < s.tables.n ; i++ ){
printTableBean(s.tables.list[i]);
}
                  }
void printCapability( struct Capability s) {
int i;
printf("structure Capability\n");
printf(" value of %s = %s\n","description", s.description);
for ( i = 0; i < s.interfaces.n ; i++ ){
printInterface_Base(s.interfaces.list[i]);
}
                  printf(" value of %s = %s\n","standardID", s.standardID);
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","type", s.type);
}
void printBaseParam( struct BaseParam s) {
int i;
printf("structure BaseParam\n");
printf(" value of %s = %s\n","name", s.name);
printf(" value of %s = %s\n","description", s.description);
printf(" value of %s = %s\n","unit", s.unit);
printf(" value of %s = %s\n","ucd", s.ucd);
}
void printAuthority( struct Authority s) {
int i;
printf("structure Authority\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
printResourceName(s.managingOrg);
}
void printApplication( struct Application s) {
int i;
printf("structure Application\n");
for ( i = 0; i < s.validationLevel.n ; i++ ){
printValidation(s.validationLevel.list[i]);
}
                  printf(" value of %s = %s\n","title", s.title);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %s\n","shortName", s.shortName);
printCuration(s.curation);
printContent(s.content);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","created", s.created);
printf(" value of %s = %s\n","updated", s.updated);
printf(" value of %s = %s\n","type", s.type);
for ( i = 0; i < s.applicationCapabilities.n ; i++ ){
/* simple type value of applicationCapabilities type=ListOfIvornOrURI not printed  */
}
                  }
void printAccessURL( struct AccessURL s) {
int i;
printf("structure AccessURL\n");
printf(" value of %s = %s\n","valueURI", s.valueURI);
printf(" value of %s = %s\n","use", s.use);
}
void printVoMonBean( struct VoMonBean s) {
int i;
printf("structure VoMonBean\n");
printf(" value of %s = %d\n","code", s.code);
printf(" value of %s = %s\n","id", s.id);
printf(" value of %s = %f\n","millis", s.millis);
printf(" value of %s = %s\n","status", s.status);
printf(" value of %s = %s\n","timestamp", s.timestamp);
}
void printAbstractInformation_Base( struct AbstractInformation_Base s){

         switch (s._type){
case UserInformation:
       printUserInformation(s.d.userinformation);

              break;
           case NodeInformation:
       printNodeInformation(s.d.nodeinformation);

              break;
           case ExecutionInformation:
       printExecutionInformation(s.d.executioninformation);

              break;
           
            default:
                printf("unknown type in Base\n");
         }
         
}
void printBaseParam_Base( struct BaseParam_Base s){

         switch (s._type){
case ParameterBean:
       printParameterBean(s.d.parameterbean);

              break;
           case ColumnBean:
       printColumnBean(s.d.columnbean);

              break;
           case InputParam:
       printInputParam(s.d.inputparam);

              break;
           
            default:
                printf("unknown type in Base\n");
         }
         
}
void printCatalog_Base( struct Catalog_Base s){

         switch (s._type){
case DatabaseBean:
       printDatabaseBean(s.d.databasebean);

              break;
           
            default:
                printf("unknown type in Base\n");
         }
         
}
void printResource_Base( struct Resource_Base s){

         switch (s._type){
case Service:
       printService(s.d.service);

              break;
           case CeaService:
       printCeaService(s.d.ceaservice);

              break;
           case TableService:
       printTableService(s.d.tableservice);

              break;
           case StapService:
       printStapService(s.d.stapservice);

              break;
           case SsapService:
       printSsapService(s.d.ssapservice);

              break;
           case SiapService:
       printSiapService(s.d.siapservice);

              break;
           case RegistryService:
       printRegistryService(s.d.registryservice);

              break;
           case DataService:
       printDataService(s.d.dataservice);

              break;
           case CatalogService:
       printCatalogService(s.d.catalogservice);

              break;
           case ConeService:
       printConeService(s.d.coneservice);

              break;
           case Application:
       printApplication(s.d.application);

              break;
           case CeaApplication:
       printCeaApplication(s.d.ceaapplication);

              break;
           case DataCollection:
       printDataCollection(s.d.datacollection);

              break;
           case TabularDB:
       printTabularDB(s.d.tabulardb);

              break;
           case StandardSTC:
       printStandardSTC(s.d.standardstc);

              break;
           case Organisation:
       printOrganisation(s.d.organisation);

              break;
           case Authority:
       printAuthority(s.d.authority);

              break;
           
            default:
                printf("unknown type in Base\n");
         }
         
}
void printCapability_Base( struct Capability_Base s){

         switch (s._type){
case CeaServerCapability:
       printCeaServerCapability(s.d.ceaservercapability);

              break;
           case StapCapability:
       printStapCapability(s.d.stapcapability);

              break;
           case SsapCapability:
       printSsapCapability(s.d.ssapcapability);

              break;
           case SiapCapability:
       printSiapCapability(s.d.siapcapability);

              break;
           case RegistryCapability:
       printRegistryCapability(s.d.registrycapability);

              break;
           case SearchCapability:
       printSearchCapability(s.d.searchcapability);

              break;
           case HarvestCapability:
       printHarvestCapability(s.d.harvestcapability);

              break;
           case ConeCapability:
       printConeCapability(s.d.conecapability);

              break;
           
            default:
                printf("unknown type in Base\n");
         }
         
}
void printInterface_Base( struct Interface_Base s){

         switch (s._type){
case WebServiceInterface:
       printWebServiceInterface(s.d.webserviceinterface);

              break;
           case ParamHttpInterface:
       printParamHttpInterface(s.d.paramhttpinterface);

              break;
           
            default:
                printf("unknown type in Base\n");
         }
         
}
