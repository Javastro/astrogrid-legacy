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
   
   
#ifndef TESTINTF_H_
#define TESTINTF_H_

#include "arintf.h"
void printUserInformation( struct UserInformation s);
void printTableBeanComparator( struct TableBeanComparator s);
void printTableBean( struct TableBean s);
void printParameterReferenceBean( struct ParameterReferenceBean s);
void printParameterBean( struct ParameterBean s);
void printNodeInformation( struct NodeInformation s);
void printInterfaceBean( struct InterfaceBean s);
void printExecutionMessage( struct ExecutionMessage s);
void printExecutionInformation( struct ExecutionInformation s);
void printDatabaseBean( struct DatabaseBean s);
void printColumnBean( struct ColumnBean s);
void printCeaService( struct CeaService s);
void printCeaServerCapability( struct CeaServerCapability s);
void printCeaApplication( struct CeaApplication s);
void printAbstractInformation( struct AbstractInformation s);
void printSesamePositionBean( struct SesamePositionBean s);
void printSkyNodeTableBean( struct SkyNodeTableBean s);
void printSkyNodeColumnBean( struct SkyNodeColumnBean s);
void printFunctionBean( struct FunctionBean s);
void printAvailabilityBean( struct AvailabilityBean s);
void printValidation( struct Validation s);
void printTabularDB( struct TabularDB s);
void printSource( struct Source s);
void printSiapService( struct SiapService s);
void printSiapCapability( struct SiapCapability s);
void printSiapCapability_SkySize( struct SiapCapability_SkySize s);
void printSiapCapability_ImageSize( struct SiapCapability_ImageSize s);
void printSiapCapability_SkyPos( struct SiapCapability_SkyPos s);
void printSiapCapability_Query( struct SiapCapability_Query s);
void printService( struct Service s);
void printSecurityMethod( struct SecurityMethod s);
void printSearchCapability( struct SearchCapability s);
void printResourceName( struct ResourceName s);
void printResource( struct Resource s);
void printRelationship( struct Relationship s);
void printRegistryService( struct RegistryService s);
void printOrganisation( struct Organisation s);
void printInterface( struct Interface s);
void printHasCoverage( struct HasCoverage s);
void printHarvestCapability( struct HarvestCapability s);
void printHandler( struct Handler s);
void printFormat( struct Format s);
void printCurationDate( struct CurationDate s);
void printDataService( struct DataService s);
void printDataCollection( struct DataCollection s);
void printCuration( struct Curation s);
void printCreator( struct Creator s);
void printCoverage( struct Coverage s);
void printContent( struct Content s);
void printContact( struct Contact s);
void printConeService( struct ConeService s);
void printConeCapability( struct ConeCapability s);
void printConeCapability_Query( struct ConeCapability_Query s);
void printCatalogService( struct CatalogService s);
void printCatalog( struct Catalog s);
void printCapability( struct Capability s);
void printAuthority( struct Authority s);
void printAccessURL( struct AccessURL s);
void printVoMonBean( struct VoMonBean s);
void printAbstractInformation_Base( struct AbstractInformation_Base s);
void printTableBean_Base( struct TableBean_Base s);
void printCatalog_Base( struct Catalog_Base s);
void printColumnBean_Base( struct ColumnBean_Base s);
void printResource_Base( struct Resource_Base s);
void printCapability_Base( struct Capability_Base s);

#endif
        