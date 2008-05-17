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
   
   
#include <stdio.h>
#include "idlhelper.h"

using namespace std;

// bean definitions
 static void * s_UserInformation;
static void * s_TableBeanComparator;
static void * s_TableBean;
static void * s_ParameterReferenceBean;
static void * s_ParameterBean;
static void * s_NodeInformation;
static void * s_InterfaceBean;
static void * s_ExecutionMessage;
static void * s_ExecutionInformation;
static void * s_DatabaseBean;
static void * s_ColumnBean;
static void * s_CeaService;
static void * s_CeaServerCapability;
static void * s_CeaApplication;
static void * s_AbstractInformation;
static void * s_SesamePositionBean;
static void * s_SkyNodeTableBean;
static void * s_SkyNodeColumnBean;
static void * s_FunctionBean;
static void * s_AvailabilityBean;
static void * s_Validation;
static void * s_TabularDB;
static void * s_Source;
static void * s_SiapService;
static void * s_SiapCapability;
static void * s_SiapCapability_SkySize;
static void * s_SiapCapability_ImageSize;
static void * s_SiapCapability_SkyPos;
static void * s_SiapCapability_Query;
static void * s_Service;
static void * s_SecurityMethod;
static void * s_SearchCapability;
static void * s_ResourceName;
static void * s_Resource;
static void * s_Relationship;
static void * s_RegistryService;
static void * s_Organisation;
static void * s_Interface;
static void * s_HasCoverage;
static void * s_HarvestCapability;
static void * s_Handler;
static void * s_Format;
static void * s_CurationDate;
static void * s_DataService;
static void * s_DataCollection;
static void * s_Curation;
static void * s_Creator;
static void * s_Coverage;
static void * s_Content;
static void * s_Contact;
static void * s_ConeService;
static void * s_ConeCapability;
static void * s_ConeCapability_Query;
static void * s_CatalogService;
static void * s_Catalog;
static void * s_Capability;
static void * s_Authority;
static void * s_AccessURL;
static void * s_VoMonBean;
static IDL_VPTR IDL_astrogrid_stap_constructQuery(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   ACRDate start;
//FIXME type too complex for IDL? - need to think of way of representing....
   ACRDate end;
//FIXME type too complex for IDL? - need to think of way of representing....
retval = astrogrid_stap_constructQuery(service, start, end);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_constructQueryF(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   ACRDate start;
//FIXME type too complex for IDL? - need to think of way of representing....
   ACRDate end;
//FIXME type too complex for IDL? - need to think of way of representing....
   JString format;
   format = argv[3]->value.str.s;
retval = astrogrid_stap_constructQueryF(service, start, end, format);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_constructQueryP(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   ACRDate start;
//FIXME type too complex for IDL? - need to think of way of representing....
   ACRDate end;
//FIXME type too complex for IDL? - need to think of way of representing....
   double ra;
   ra = argv[3]->value.d;
   double dec;
   dec = argv[4]->value.d;
   double size;
   size = argv[5]->value.d;
retval = astrogrid_stap_constructQueryP(service, start, end, ra, dec, size);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_constructQueryPF(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   ACRDate start;
//FIXME type too complex for IDL? - need to think of way of representing....
   ACRDate end;
//FIXME type too complex for IDL? - need to think of way of representing....
   double ra;
   ra = argv[3]->value.d;
   double dec;
   dec = argv[4]->value.d;
   double size;
   size = argv[5]->value.d;
   JString format;
   format = argv[6]->value.str.s;
retval = astrogrid_stap_constructQueryPF(service, start, end, ra, dec, size, format);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_constructQueryS(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   ACRDate start;
//FIXME type too complex for IDL? - need to think of way of representing....
   ACRDate end;
//FIXME type too complex for IDL? - need to think of way of representing....
   double ra;
   ra = argv[3]->value.d;
   double dec;
   dec = argv[4]->value.d;
   double ra_size;
   ra_size = argv[5]->value.d;
   double dec_size;
   dec_size = argv[6]->value.d;
retval = astrogrid_stap_constructQueryS(service, start, end, ra, dec, ra_size, dec_size);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_constructQuerySF(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   ACRDate start;
//FIXME type too complex for IDL? - need to think of way of representing....
   ACRDate end;
//FIXME type too complex for IDL? - need to think of way of representing....
   double ra;
   ra = argv[3]->value.d;
   double dec;
   dec = argv[4]->value.d;
   double ra_size;
   ra_size = argv[5]->value.d;
   double dec_size;
   dec_size = argv[6]->value.d;
   JString format;
   format = argv[7]->value.str.s;
retval = astrogrid_stap_constructQuerySF(service, start, end, ra, dec, ra_size, dec_size, format);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_addOption(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      URLString query;
   query = argv[0]->value.str.s;
   JString optionName;
   optionName = argv[1]->value.str.s;
   JString optionValue;
   optionValue = argv[2]->value.str.s;
retval = astrogrid_stap_addOption(query, optionName, optionValue);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_execute(int argc, IDL_VPTR *argv, char* argk)
{
ListOfACRKeyValueMap retval;
      URLString query;
   query = argv[0]->value.str.s;
retval = astrogrid_stap_execute(query);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_executeVotable(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      URLString query;
   query = argv[0]->value.str.s;
retval = astrogrid_stap_executeVotable(query);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_astrogrid_stap_executeAndSave(int argc, IDL_VPTR *argv, char* argk)
{
   URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
astrogrid_stap_executeAndSave(query, saveLocation);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_stap_saveDatasets(int argc, IDL_VPTR *argv, char* argk)
{
int retval;
      URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
retval = astrogrid_stap_saveDatasets(query, saveLocation);

       IDL_VPTR idl_retval;
 idl_retval = IDL_GettmpInt(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_saveDatasetsSubset(int argc, IDL_VPTR *argv, char* argk)
{
int retval;
      URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
   ACRList rows;
//FIXME type too complex for IDL? - need to think of way of representing....
retval = astrogrid_stap_saveDatasetsSubset(query, saveLocation, rows);

       IDL_VPTR idl_retval;
 idl_retval = IDL_GettmpInt(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_getRegistryAdqlQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = astrogrid_stap_getRegistryAdqlQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = astrogrid_stap_getRegistryXQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_list(int argc, IDL_VPTR *argv, char* argk)
{
ListOfIvornOrURI retval;
   retval = astrogrid_processManager_list();

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_submit(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      XMLString document;
   document = argv[0]->value.str.s;
retval = astrogrid_processManager_submit(document);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_submitTo(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      XMLString document;
   document = argv[0]->value.str.s;
   IvornOrURI server;
   server = argv[1]->value.str.s;
retval = astrogrid_processManager_submitTo(document, server);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_submitStored(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI documentLocation;
   documentLocation = argv[0]->value.str.s;
retval = astrogrid_processManager_submitStored(documentLocation);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_submitStoredTo(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI documentLocation;
   documentLocation = argv[0]->value.str.s;
   IvornOrURI server;
   server = argv[1]->value.str.s;
retval = astrogrid_processManager_submitStoredTo(documentLocation, server);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_astrogrid_processManager_halt(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI executionId;
   executionId = argv[0]->value.str.s;
astrogrid_processManager_halt(executionId);

       IDL_VPTR idl_retval;

   };
static void IDL_astrogrid_processManager_delete(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI executionId;
   executionId = argv[0]->value.str.s;
astrogrid_processManager_delete(executionId);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_processManager_getExecutionInformation(int argc, IDL_VPTR *argv, char* argk)
{
struct ExecutionInformation retval;
      IvornOrURI executionId;
   executionId = argv[0]->value.str.s;
retval = astrogrid_processManager_getExecutionInformation(executionId);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_ExecutionInformation, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 0, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.description);
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 1, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  startTime type=ACRDate
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 2, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  finishTime type=ACRDate
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 3, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.status);

     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_getMessages(int argc, IDL_VPTR *argv, char* argk)
{
ListOfExecutionMessage retval;
      IvornOrURI executionId;
   executionId = argv[0]->value.str.s;
retval = astrogrid_processManager_getMessages(executionId);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_getResults(int argc, IDL_VPTR *argv, char* argk)
{
ACRKeyValueMap retval;
      IvornOrURI executionid;
   executionid = argv[0]->value.str.s;
retval = astrogrid_processManager_getResults(executionid);

       IDL_VPTR idl_retval;
 idl_retval = (retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_getSingleResult(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      IvornOrURI executionId;
   executionId = argv[0]->value.str.s;
   JString resultName;
   resultName = argv[1]->value.str.s;
retval = astrogrid_processManager_getSingleResult(executionId, resultName);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_astrogrid_publish_register(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI registry;
   registry = argv[0]->value.str.s;
   XMLString entry;
   entry = argv[1]->value.str.s;
astrogrid_publish_register(registry, entry);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_myspace_getHome(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
   retval = astrogrid_myspace_getHome();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_exists(int argc, IDL_VPTR *argv, char* argk)
{
BOOL retval;
      IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
retval = astrogrid_myspace_exists(ivorn);

       IDL_VPTR idl_retval;
 idl_retval = IDL_GettmpInt(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_getNodeInformation(int argc, IDL_VPTR *argv, char* argk)
{
struct NodeInformation retval;
      IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
retval = astrogrid_myspace_getNodeInformation(ivorn);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_NodeInformation, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_NodeInformation, 0, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  attributes type=ACRKeyValueMap
offset = IDL_StructTagInfoByIndex(s_NodeInformation, 1, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  createDate type=ACRDate
offset = IDL_StructTagInfoByIndex(s_NodeInformation, 2, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  modifyDate type=ACRDate
offset = IDL_StructTagInfoByIndex(s_NodeInformation, 3, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  size type=long
offset = IDL_StructTagInfoByIndex(s_NodeInformation, 4, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  contentLocation type=IvornOrURI

     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_createFile(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
astrogrid_myspace_createFile(ivorn);

       IDL_VPTR idl_retval;

   };
static void IDL_astrogrid_myspace_createFolder(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
astrogrid_myspace_createFolder(ivorn);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_myspace_createChildFolder(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI parentIvorn;
   parentIvorn = argv[0]->value.str.s;
   JString name;
   name = argv[1]->value.str.s;
retval = astrogrid_myspace_createChildFolder(parentIvorn, name);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_createChildFile(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI parentIvorn;
   parentIvorn = argv[0]->value.str.s;
   JString name;
   name = argv[1]->value.str.s;
retval = astrogrid_myspace_createChildFile(parentIvorn, name);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_getParent(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
retval = astrogrid_myspace_getParent(ivorn);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_list(int argc, IDL_VPTR *argv, char* argk)
{
ListOfJString retval;
      IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
retval = astrogrid_myspace_list(ivorn);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_listIvorns(int argc, IDL_VPTR *argv, char* argk)
{
ListOfIvornOrURI retval;
      IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
retval = astrogrid_myspace_listIvorns(ivorn);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_listNodeInformation(int argc, IDL_VPTR *argv, char* argk)
{
ListOfNodeInformation retval;
      IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
retval = astrogrid_myspace_listNodeInformation(ivorn);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_refresh(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
astrogrid_myspace_refresh(ivorn);

       IDL_VPTR idl_retval;

   };
static void IDL_astrogrid_myspace_delete(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
astrogrid_myspace_delete(ivorn);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_myspace_rename(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI srcIvorn;
   srcIvorn = argv[0]->value.str.s;
   JString newName;
   newName = argv[1]->value.str.s;
retval = astrogrid_myspace_rename(srcIvorn, newName);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_move(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI srcIvorn;
   srcIvorn = argv[0]->value.str.s;
   IvornOrURI newParentIvorn;
   newParentIvorn = argv[1]->value.str.s;
   JString newName;
   newName = argv[2]->value.str.s;
retval = astrogrid_myspace_move(srcIvorn, newParentIvorn, newName);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_changeStore(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI srcIvorn;
   srcIvorn = argv[0]->value.str.s;
   IvornOrURI storeIvorn;
   storeIvorn = argv[1]->value.str.s;
astrogrid_myspace_changeStore(srcIvorn, storeIvorn);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_myspace_copy(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI srcIvorn;
   srcIvorn = argv[0]->value.str.s;
   IvornOrURI newParentIvorn;
   newParentIvorn = argv[1]->value.str.s;
   JString newName;
   newName = argv[2]->value.str.s;
retval = astrogrid_myspace_copy(srcIvorn, newParentIvorn, newName);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_read(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
retval = astrogrid_myspace_read(ivorn);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_write(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
   JString content;
   content = argv[1]->value.str.s;
astrogrid_myspace_write(ivorn, content);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_myspace_readBinary(int argc, IDL_VPTR *argv, char* argk)
{
ListOfchar retval;
      IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
retval = astrogrid_myspace_readBinary(ivorn);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_writeBinary(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
   ListOfchar content;
//FIXME type too complex for IDL? - need to think of way of representing....
astrogrid_myspace_writeBinary(ivorn, content);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_myspace_getReadContentURL(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
retval = astrogrid_myspace_getReadContentURL(ivorn);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_getWriteContentURL(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
retval = astrogrid_myspace_getWriteContentURL(ivorn);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_transferCompleted(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
astrogrid_myspace_transferCompleted(ivorn);

       IDL_VPTR idl_retval;

   };
static void IDL_astrogrid_myspace_copyContentToURL(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI ivorn;
   ivorn = argv[0]->value.str.s;
   URLString destination;
   destination = argv[1]->value.str.s;
astrogrid_myspace_copyContentToURL(ivorn, destination);

       IDL_VPTR idl_retval;

   };
static void IDL_astrogrid_myspace_copyURLToContent(int argc, IDL_VPTR *argv, char* argk)
{
   URLString src;
   src = argv[0]->value.str.s;
   IvornOrURI ivorn;
   ivorn = argv[1]->value.str.s;
astrogrid_myspace_copyURLToContent(src, ivorn);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_myspace_listStores(int argc, IDL_VPTR *argv, char* argk)
{
ListOfService_Base retval;
   retval = astrogrid_myspace_listStores();

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_list(int argc, IDL_VPTR *argv, char* argk)
{
ListOfIvornOrURI retval;
   retval = astrogrid_jobs_list();

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_listFully(int argc, IDL_VPTR *argv, char* argk)
{
ListOfExecutionInformation retval;
   retval = astrogrid_jobs_listFully();

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_createJob(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
   retval = astrogrid_jobs_createJob();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_wrapTask(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      XMLString taskDocument;
   taskDocument = argv[0]->value.str.s;
retval = astrogrid_jobs_wrapTask(taskDocument);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_getJobTranscript(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI jobURN;
   jobURN = argv[0]->value.str.s;
retval = astrogrid_jobs_getJobTranscript(jobURN);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_getJobInformation(int argc, IDL_VPTR *argv, char* argk)
{
struct ExecutionInformation retval;
      IvornOrURI jobURN;
   jobURN = argv[0]->value.str.s;
retval = astrogrid_jobs_getJobInformation(jobURN);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_ExecutionInformation, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 0, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.description);
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 1, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  startTime type=ACRDate
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 2, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  finishTime type=ACRDate
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 3, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.status);

     return idl_retval;
    
   };
static void IDL_astrogrid_jobs_cancelJob(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI jobURN;
   jobURN = argv[0]->value.str.s;
astrogrid_jobs_cancelJob(jobURN);

       IDL_VPTR idl_retval;

   };
static void IDL_astrogrid_jobs_deleteJob(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI jobURN;
   jobURN = argv[0]->value.str.s;
astrogrid_jobs_deleteJob(jobURN);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_jobs_submitJob(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      XMLString workflow;
   workflow = argv[0]->value.str.s;
retval = astrogrid_jobs_submitJob(workflow);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_submitStoredJob(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI workflowReference;
   workflowReference = argv[0]->value.str.s;
retval = astrogrid_jobs_submitStoredJob(workflowReference);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_astrogrid_community_login(int argc, IDL_VPTR *argv, char* argk)
{
   JString username;
   username = argv[0]->value.str.s;
   JString password;
   password = argv[1]->value.str.s;
   JString community;
   community = argv[2]->value.str.s;
astrogrid_community_login(username, password, community);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_community_getUserInformation(int argc, IDL_VPTR *argv, char* argk)
{
struct UserInformation retval;
   retval = astrogrid_community_getUserInformation();

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_UserInformation, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_UserInformation, 0, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.community);
offset = IDL_StructTagInfoByIndex(s_UserInformation, 1, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.password);

     return idl_retval;
    
   };
static void IDL_astrogrid_community_logout(int argc, IDL_VPTR *argv, char* argk)
{
astrogrid_community_logout();

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_community_isLoggedIn(int argc, IDL_VPTR *argv, char* argk)
{
BOOL retval;
   retval = astrogrid_community_isLoggedIn();

       IDL_VPTR idl_retval;
 idl_retval = IDL_GettmpInt(retval);
     return idl_retval;
    
   };
static void IDL_astrogrid_community_guiLogin(int argc, IDL_VPTR *argv, char* argk)
{
astrogrid_community_guiLogin();

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_applications_list(int argc, IDL_VPTR *argv, char* argk)
{
ListOfIvornOrURI retval;
   retval = astrogrid_applications_list();

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getRegistryAdqlQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = astrogrid_applications_getRegistryAdqlQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = astrogrid_applications_getRegistryXQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getCeaApplication(int argc, IDL_VPTR *argv, char* argk)
{
struct CeaApplication retval;
      IvornOrURI applicationName;
   applicationName = argv[0]->value.str.s;
retval = astrogrid_applications_getCeaApplication(applicationName);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_CeaApplication, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_CeaApplication, 0, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  interfaces type=ListOfInterfaceBean
offset = IDL_StructTagInfoByIndex(s_CeaApplication, 1, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  parameters type=ListOfParameterBean

     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getDocumentation(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      IvornOrURI applicationName;
   applicationName = argv[0]->value.str.s;
retval = astrogrid_applications_getDocumentation(applicationName);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_createTemplateDocument(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI applicationName;
   applicationName = argv[0]->value.str.s;
   JString interfaceName;
   interfaceName = argv[1]->value.str.s;
retval = astrogrid_applications_createTemplateDocument(applicationName, interfaceName);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_createTemplateStruct(int argc, IDL_VPTR *argv, char* argk)
{
ACRKeyValueMap retval;
      IvornOrURI applicationName;
   applicationName = argv[0]->value.str.s;
   JString interfaceName;
   interfaceName = argv[1]->value.str.s;
retval = astrogrid_applications_createTemplateStruct(applicationName, interfaceName);

       IDL_VPTR idl_retval;
 idl_retval = (retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_convertDocumentToStruct(int argc, IDL_VPTR *argv, char* argk)
{
ACRKeyValueMap retval;
      XMLString document;
   document = argv[0]->value.str.s;
retval = astrogrid_applications_convertDocumentToStruct(document);

       IDL_VPTR idl_retval;
 idl_retval = (retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_convertStructToDocument(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      ACRKeyValueMap structure;
//FIXME type too complex for IDL? - need to think of way of representing....
retval = astrogrid_applications_convertStructToDocument(structure);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_astrogrid_applications_validate(int argc, IDL_VPTR *argv, char* argk)
{
   XMLString document;
   document = argv[0]->value.str.s;
astrogrid_applications_validate(document);

       IDL_VPTR idl_retval;

   };
static void IDL_astrogrid_applications_validateStored(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI documentLocation;
   documentLocation = argv[0]->value.str.s;
astrogrid_applications_validateStored(documentLocation);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_applications_listServersProviding(int argc, IDL_VPTR *argv, char* argk)
{
ListOfService_Base retval;
      IvornOrURI applicationId;
   applicationId = argv[0]->value.str.s;
retval = astrogrid_applications_listServersProviding(applicationId);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_submit(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      XMLString document;
   document = argv[0]->value.str.s;
retval = astrogrid_applications_submit(document);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_submitTo(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      XMLString document;
   document = argv[0]->value.str.s;
   IvornOrURI server;
   server = argv[1]->value.str.s;
retval = astrogrid_applications_submitTo(document, server);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_submitStored(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI documentLocation;
   documentLocation = argv[0]->value.str.s;
retval = astrogrid_applications_submitStored(documentLocation);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_submitStoredTo(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      IvornOrURI documentLocation;
   documentLocation = argv[0]->value.str.s;
   IvornOrURI server;
   server = argv[1]->value.str.s;
retval = astrogrid_applications_submitStoredTo(documentLocation, server);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_astrogrid_applications_cancel(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI executionId;
   executionId = argv[0]->value.str.s;
astrogrid_applications_cancel(executionId);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_astrogrid_applications_getExecutionInformation(int argc, IDL_VPTR *argv, char* argk)
{
struct ExecutionInformation retval;
      IvornOrURI executionId;
   executionId = argv[0]->value.str.s;
retval = astrogrid_applications_getExecutionInformation(executionId);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_ExecutionInformation, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 0, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.description);
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 1, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  startTime type=ACRDate
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 2, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  finishTime type=ACRDate
offset = IDL_StructTagInfoByIndex(s_ExecutionInformation, 3, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.status);

     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getResults(int argc, IDL_VPTR *argv, char* argk)
{
ACRKeyValueMap retval;
      IvornOrURI executionid;
   executionid = argv[0]->value.str.s;
retval = astrogrid_applications_getResults(executionid);

       IDL_VPTR idl_retval;
 idl_retval = (retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_vizier_cataloguesMetaData(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      JString target;
   target = argv[0]->value.str.s;
   double radius;
   radius = argv[1]->value.d;
   JString unit;
   unit = argv[2]->value.str.s;
   JString text;
   text = argv[3]->value.str.s;
retval = cds_vizier_cataloguesMetaData(target, radius, unit, text);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_vizier_cataloguesMetaDataWavelength(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      JString target;
   target = argv[0]->value.str.s;
   double radius;
   radius = argv[1]->value.d;
   JString unit;
   unit = argv[2]->value.str.s;
   JString text;
   text = argv[3]->value.str.s;
   JString wavelength;
   wavelength = argv[4]->value.str.s;
retval = cds_vizier_cataloguesMetaDataWavelength(target, radius, unit, text, wavelength);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_vizier_cataloguesData(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      JString target;
   target = argv[0]->value.str.s;
   double radius;
   radius = argv[1]->value.d;
   JString unit;
   unit = argv[2]->value.str.s;
   JString text;
   text = argv[3]->value.str.s;
retval = cds_vizier_cataloguesData(target, radius, unit, text);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_vizier_cataloguesDataWavelength(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      JString target;
   target = argv[0]->value.str.s;
   double radius;
   radius = argv[1]->value.d;
   JString unit;
   unit = argv[2]->value.str.s;
   JString text;
   text = argv[3]->value.str.s;
   JString wavelength;
   wavelength = argv[4]->value.str.s;
retval = cds_vizier_cataloguesDataWavelength(target, radius, unit, text, wavelength);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_vizier_metaAll(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
   retval = cds_vizier_metaAll();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_UCDList(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = cds_ucd_UCDList();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_resolveUCD(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString ucd;
   ucd = argv[0]->value.str.s;
retval = cds_ucd_resolveUCD(ucd);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_UCDofCatalog(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString catalog_designation;
   catalog_designation = argv[0]->value.str.s;
retval = cds_ucd_UCDofCatalog(catalog_designation);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_translate(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString ucd;
   ucd = argv[0]->value.str.s;
retval = cds_ucd_translate(ucd);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_upgrade(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString ucd;
   ucd = argv[0]->value.str.s;
retval = cds_ucd_upgrade(ucd);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_validate(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString ucd;
   ucd = argv[0]->value.str.s;
retval = cds_ucd_validate(ucd);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_explain(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString ucd;
   ucd = argv[0]->value.str.s;
retval = cds_ucd_explain(ucd);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_assign(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString descr;
   descr = argv[0]->value.str.s;
retval = cds_ucd_assign(descr);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_sesame_resolve(int argc, IDL_VPTR *argv, char* argk)
{
struct SesamePositionBean retval;
      JString name;
   name = argv[0]->value.str.s;
retval = cds_sesame_resolve(name);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_SesamePositionBean, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_SesamePositionBean, 0, IDL_MSG_LONGJMP, NULL);
 *((double*)(idl_retval->value.s.arr->data +offset)) = retval.dec;
offset = IDL_StructTagInfoByIndex(s_SesamePositionBean, 1, IDL_MSG_LONGJMP, NULL);
 *((double*)(idl_retval->value.s.arr->data +offset)) = retval.decErr;
offset = IDL_StructTagInfoByIndex(s_SesamePositionBean, 2, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.oName);
offset = IDL_StructTagInfoByIndex(s_SesamePositionBean, 3, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.oType);
offset = IDL_StructTagInfoByIndex(s_SesamePositionBean, 4, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.posStr);
offset = IDL_StructTagInfoByIndex(s_SesamePositionBean, 5, IDL_MSG_LONGJMP, NULL);
 *((double*)(idl_retval->value.s.arr->data +offset)) = retval.ra;
offset = IDL_StructTagInfoByIndex(s_SesamePositionBean, 6, IDL_MSG_LONGJMP, NULL);
 *((double*)(idl_retval->value.s.arr->data +offset)) = retval.raErr;
offset = IDL_StructTagInfoByIndex(s_SesamePositionBean, 7, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.target);
offset = IDL_StructTagInfoByIndex(s_SesamePositionBean, 8, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.service);
offset = IDL_StructTagInfoByIndex(s_SesamePositionBean, 9, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  aliases type=ListOfJString

     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_sesame_sesame(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString name;
   name = argv[0]->value.str.s;
   JString resultType;
   resultType = argv[1]->value.str.s;
retval = cds_sesame_sesame(name, resultType);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_sesame_sesameChooseService(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString name;
   name = argv[0]->value.str.s;
   JString resultType;
   resultType = argv[1]->value.str.s;
   BOOL all;
   all = argv[2]->value.i;
   JString service;
   service = argv[3]->value.str.s;
retval = cds_sesame_sesameChooseService(name, resultType, all, service);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_glu_getURLfromTag(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString id;
   id = argv[0]->value.str.s;
retval = cds_glu_getURLfromTag(id);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_coordinate_convert(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      double x;
   x = argv[0]->value.d;
   double y;
   y = argv[1]->value.d;
   double z;
   z = argv[2]->value.d;
   int precision;
   precision = argv[3]->value.i;
retval = cds_coordinate_convert(x, y, z, precision);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_coordinate_convertL(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      double lon;
   lon = argv[0]->value.d;
   double lat;
   lat = argv[1]->value.d;
   int precision;
   precision = argv[2]->value.i;
retval = cds_coordinate_convertL(lon, lat, precision);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_coordinate_convertE(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      int frame1;
   frame1 = argv[0]->value.i;
   int frame2;
   frame2 = argv[1]->value.i;
   double x;
   x = argv[2]->value.d;
   double y;
   y = argv[3]->value.d;
   double z;
   z = argv[4]->value.d;
   int precision;
   precision = argv[5]->value.i;
   double equinox1;
   equinox1 = argv[6]->value.d;
   double equinox2;
   equinox2 = argv[7]->value.d;
retval = cds_coordinate_convertE(frame1, frame2, x, y, z, precision, equinox1, equinox2);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_coordinate_convertLE(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      int frame1;
   frame1 = argv[0]->value.i;
   int frame2;
   frame2 = argv[1]->value.i;
   double lon;
   lon = argv[2]->value.d;
   double lat;
   lat = argv[3]->value.d;
   int precision;
   precision = argv[4]->value.i;
   double equinox1;
   equinox1 = argv[5]->value.d;
   double equinox2;
   equinox2 = argv[6]->value.d;
retval = cds_coordinate_convertLE(frame1, frame2, lon, lat, precision, equinox1, equinox2);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_toolEditor_edit(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      XMLString t;
   t = argv[0]->value.str.s;
retval = dialogs_toolEditor_edit(t);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_toolEditor_editStored(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI documentLocation;
   documentLocation = argv[0]->value.str.s;
retval = dialogs_toolEditor_editStored(documentLocation);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_toolEditor_selectAndBuild(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
   retval = dialogs_toolEditor_selectAndBuild();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_resourceChooser_chooseResource(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      JString title;
   title = argv[0]->value.str.s;
   BOOL enableRemote;
   enableRemote = argv[1]->value.i;
retval = dialogs_resourceChooser_chooseResource(title, enableRemote);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_resourceChooser_chooseFolder(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      JString title;
   title = argv[0]->value.str.s;
   BOOL enableRemote;
   enableRemote = argv[1]->value.i;
retval = dialogs_resourceChooser_chooseFolder(title, enableRemote);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_resourceChooser_fullChooseResource(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      JString title;
   title = argv[0]->value.str.s;
   BOOL enableVospace;
   enableVospace = argv[1]->value.i;
   BOOL enableLocal;
   enableLocal = argv[2]->value.i;
   BOOL enableURL;
   enableURL = argv[3]->value.i;
retval = dialogs_resourceChooser_fullChooseResource(title, enableVospace, enableLocal, enableURL);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_resourceChooser_fullChooseFolder(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
      JString title;
   title = argv[0]->value.str.s;
   BOOL enableVospace;
   enableVospace = argv[1]->value.i;
   BOOL enableLocal;
   enableLocal = argv[2]->value.i;
   BOOL enableURL;
   enableURL = argv[3]->value.i;
retval = dialogs_resourceChooser_fullChooseFolder(title, enableVospace, enableLocal, enableURL);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_registryGoogle_selectResources(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      JString prompt;
   prompt = argv[0]->value.str.s;
   BOOL multiple;
   multiple = argv[1]->value.i;
retval = dialogs_registryGoogle_selectResources(prompt, multiple);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_registryGoogle_selectResourcesAdqlFilter(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      JString prompt;
   prompt = argv[0]->value.str.s;
   BOOL multiple;
   multiple = argv[1]->value.i;
   JString adqlFilter;
   adqlFilter = argv[2]->value.str.s;
retval = dialogs_registryGoogle_selectResourcesAdqlFilter(prompt, multiple, adqlFilter);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_registryGoogle_selectResourcesXQueryFilter(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      JString prompt;
   prompt = argv[0]->value.str.s;
   BOOL multiple;
   multiple = argv[1]->value.i;
   JString xqueryFilter;
   xqueryFilter = argv[2]->value.str.s;
retval = dialogs_registryGoogle_selectResourcesXQueryFilter(prompt, multiple, xqueryFilter);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_constructQuery(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   double ra;
   ra = argv[1]->value.d;
   double dec;
   dec = argv[2]->value.d;
   double size;
   size = argv[3]->value.d;
retval = ivoa_ssap_constructQuery(service, ra, dec, size);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_constructQueryS(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   double ra;
   ra = argv[1]->value.d;
   double dec;
   dec = argv[2]->value.d;
   double ra_size;
   ra_size = argv[3]->value.d;
   double dec_size;
   dec_size = argv[4]->value.d;
retval = ivoa_ssap_constructQueryS(service, ra, dec, ra_size, dec_size);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_addOption(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      URLString query;
   query = argv[0]->value.str.s;
   JString optionName;
   optionName = argv[1]->value.str.s;
   JString optionValue;
   optionValue = argv[2]->value.str.s;
retval = ivoa_ssap_addOption(query, optionName, optionValue);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_execute(int argc, IDL_VPTR *argv, char* argk)
{
ListOfACRKeyValueMap retval;
      URLString query;
   query = argv[0]->value.str.s;
retval = ivoa_ssap_execute(query);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_executeVotable(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      URLString query;
   query = argv[0]->value.str.s;
retval = ivoa_ssap_executeVotable(query);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_ivoa_ssap_executeAndSave(int argc, IDL_VPTR *argv, char* argk)
{
   URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
ivoa_ssap_executeAndSave(query, saveLocation);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_ivoa_ssap_saveDatasets(int argc, IDL_VPTR *argv, char* argk)
{
int retval;
      URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
retval = ivoa_ssap_saveDatasets(query, saveLocation);

       IDL_VPTR idl_retval;
 idl_retval = IDL_GettmpInt(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_saveDatasetsSubset(int argc, IDL_VPTR *argv, char* argk)
{
int retval;
      URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
   ACRList rows;
//FIXME type too complex for IDL? - need to think of way of representing....
retval = ivoa_ssap_saveDatasetsSubset(query, saveLocation, rows);

       IDL_VPTR idl_retval;
 idl_retval = IDL_GettmpInt(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_getRegistryAdqlQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = ivoa_ssap_getRegistryAdqlQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = ivoa_ssap_getRegistryXQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_skyNode_getRegistryAdqlQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = ivoa_skyNode_getRegistryAdqlQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_skyNode_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = ivoa_skyNode_getRegistryXQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_skyNode_getMetadata(int argc, IDL_VPTR *argv, char* argk)
{
ListOfSkyNodeTableBean retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
retval = ivoa_skyNode_getMetadata(service);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_skyNode_getFormats(int argc, IDL_VPTR *argv, char* argk)
{
ListOfJString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
retval = ivoa_skyNode_getFormats(service);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_skyNode_getFunctions(int argc, IDL_VPTR *argv, char* argk)
{
ListOfFunctionBean retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
retval = ivoa_skyNode_getFunctions(service);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_skyNode_getResults(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   XMLString adqlx;
   adqlx = argv[1]->value.str.s;
retval = ivoa_skyNode_getResults(service, adqlx);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_ivoa_skyNode_saveResults(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI service;
   service = argv[0]->value.str.s;
   XMLString adqlx;
   adqlx = argv[1]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[2]->value.str.s;
ivoa_skyNode_saveResults(service, adqlx, saveLocation);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_ivoa_skyNode_getResultsF(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   XMLString adqlx;
   adqlx = argv[1]->value.str.s;
   JString format;
   format = argv[2]->value.str.s;
retval = ivoa_skyNode_getResultsF(service, adqlx, format);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_ivoa_skyNode_saveResultsF(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI service;
   service = argv[0]->value.str.s;
   XMLString adqlx;
   adqlx = argv[1]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[2]->value.str.s;
   JString format;
   format = argv[3]->value.str.s;
ivoa_skyNode_saveResultsF(service, adqlx, saveLocation, format);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_ivoa_skyNode_getFootprint(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   XMLString region;
   region = argv[1]->value.str.s;
retval = ivoa_skyNode_getFootprint(service, region);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_skyNode_estimateQueryCost(int argc, IDL_VPTR *argv, char* argk)
{
double retval;
      long planId;
   planId = argv[0]->value.i;
   XMLString adql;
   adql = argv[1]->value.str.s;
retval = ivoa_skyNode_estimateQueryCost(planId, adql);

       IDL_VPTR idl_retval;
 idl_retval = (retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_skyNode_getAvailability(int argc, IDL_VPTR *argv, char* argk)
{
struct AvailabilityBean retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
retval = ivoa_skyNode_getAvailability(service);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_AvailabilityBean, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_AvailabilityBean, 0, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.location);
offset = IDL_StructTagInfoByIndex(s_AvailabilityBean, 1, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.message);
offset = IDL_StructTagInfoByIndex(s_AvailabilityBean, 2, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.serverName);
offset = IDL_StructTagInfoByIndex(s_AvailabilityBean, 3, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.timeOnServer);
offset = IDL_StructTagInfoByIndex(s_AvailabilityBean, 4, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.upTime);
offset = IDL_StructTagInfoByIndex(s_AvailabilityBean, 5, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.validTo);

     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_constructQuery(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   double ra;
   ra = argv[1]->value.d;
   double dec;
   dec = argv[2]->value.d;
   double size;
   size = argv[3]->value.d;
retval = ivoa_siap_constructQuery(service, ra, dec, size);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_constructQueryF(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   double ra;
   ra = argv[1]->value.d;
   double dec;
   dec = argv[2]->value.d;
   double size;
   size = argv[3]->value.d;
   JString format;
   format = argv[4]->value.str.s;
retval = ivoa_siap_constructQueryF(service, ra, dec, size, format);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_constructQueryS(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   double ra;
   ra = argv[1]->value.d;
   double dec;
   dec = argv[2]->value.d;
   double ra_size;
   ra_size = argv[3]->value.d;
   double dec_size;
   dec_size = argv[4]->value.d;
retval = ivoa_siap_constructQueryS(service, ra, dec, ra_size, dec_size);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_constructQuerySF(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   double ra;
   ra = argv[1]->value.d;
   double dec;
   dec = argv[2]->value.d;
   double ra_size;
   ra_size = argv[3]->value.d;
   double dec_size;
   dec_size = argv[4]->value.d;
   JString format;
   format = argv[5]->value.str.s;
retval = ivoa_siap_constructQuerySF(service, ra, dec, ra_size, dec_size, format);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_addOption(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      URLString query;
   query = argv[0]->value.str.s;
   JString optionName;
   optionName = argv[1]->value.str.s;
   JString optionValue;
   optionValue = argv[2]->value.str.s;
retval = ivoa_siap_addOption(query, optionName, optionValue);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_execute(int argc, IDL_VPTR *argv, char* argk)
{
ListOfACRKeyValueMap retval;
      URLString query;
   query = argv[0]->value.str.s;
retval = ivoa_siap_execute(query);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_executeVotable(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      URLString query;
   query = argv[0]->value.str.s;
retval = ivoa_siap_executeVotable(query);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_ivoa_siap_executeAndSave(int argc, IDL_VPTR *argv, char* argk)
{
   URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
ivoa_siap_executeAndSave(query, saveLocation);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_ivoa_siap_saveDatasets(int argc, IDL_VPTR *argv, char* argk)
{
int retval;
      URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
retval = ivoa_siap_saveDatasets(query, saveLocation);

       IDL_VPTR idl_retval;
 idl_retval = IDL_GettmpInt(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_saveDatasetsSubset(int argc, IDL_VPTR *argv, char* argk)
{
int retval;
      URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
   ACRList rows;
//FIXME type too complex for IDL? - need to think of way of representing....
retval = ivoa_siap_saveDatasetsSubset(query, saveLocation, rows);

       IDL_VPTR idl_retval;
 idl_retval = IDL_GettmpInt(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_getRegistryAdqlQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = ivoa_siap_getRegistryAdqlQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = ivoa_siap_getRegistryXQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryXQueryBuilder_allRecords(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = ivoa_registryXQueryBuilder_allRecords();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryXQueryBuilder_fullTextSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryXQueryBuilder_fullTextSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryXQueryBuilder_summaryTextSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryXQueryBuilder_summaryTextSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryXQueryBuilder_identifierSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryXQueryBuilder_identifierSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryXQueryBuilder_shortNameSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryXQueryBuilder_shortNameSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryXQueryBuilder_titleSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryXQueryBuilder_titleSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryXQueryBuilder_descriptionSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryXQueryBuilder_descriptionSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryAdqlBuilder_allRecords(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = ivoa_registryAdqlBuilder_allRecords();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryAdqlBuilder_fullTextSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryAdqlBuilder_fullTextSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryAdqlBuilder_summaryTextSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryAdqlBuilder_summaryTextSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryAdqlBuilder_identifierSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryAdqlBuilder_identifierSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryAdqlBuilder_shortNameSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryAdqlBuilder_shortNameSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryAdqlBuilder_titleSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryAdqlBuilder_titleSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registryAdqlBuilder_descriptionSearch(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString recordSet;
   recordSet = argv[0]->value.str.s;
   JString searchTerm;
   searchTerm = argv[1]->value.str.s;
retval = ivoa_registryAdqlBuilder_descriptionSearch(recordSet, searchTerm);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_adqlxSearch(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      XMLString adqlx;
   adqlx = argv[0]->value.str.s;
retval = ivoa_registry_adqlxSearch(adqlx);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_adqlsSearch(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      JString adqls;
   adqls = argv[0]->value.str.s;
retval = ivoa_registry_adqlsSearch(adqls);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_keywordSearch(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      JString keywords;
   keywords = argv[0]->value.str.s;
   BOOL orValues;
   orValues = argv[1]->value.i;
retval = ivoa_registry_keywordSearch(keywords, orValues);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_getResource(int argc, IDL_VPTR *argv, char* argk)
{
struct Resource_Base retval;
      IvornOrURI id;
   id = argv[0]->value.str.s;
retval = ivoa_registry_getResource(id);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_Resource, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_Resource, 0, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  validationLevel type=ListOfValidation
offset = IDL_StructTagInfoByIndex(s_Resource, 1, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.title);
offset = IDL_StructTagInfoByIndex(s_Resource, 2, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  id type=IvornOrURI
offset = IDL_StructTagInfoByIndex(s_Resource, 3, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.shortName);
offset = IDL_StructTagInfoByIndex(s_Resource, 4, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  curation type=struct Curation
offset = IDL_StructTagInfoByIndex(s_Resource, 5, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  content type=struct Content
offset = IDL_StructTagInfoByIndex(s_Resource, 6, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.status);
offset = IDL_StructTagInfoByIndex(s_Resource, 7, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.created);
offset = IDL_StructTagInfoByIndex(s_Resource, 8, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.updated);
offset = IDL_StructTagInfoByIndex(s_Resource, 9, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.type);

     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_xquerySearch(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      JString xquery;
   xquery = argv[0]->value.str.s;
retval = ivoa_registry_xquerySearch(xquery);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_xquerySearchXML(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      JString xquery;
   xquery = argv[0]->value.str.s;
retval = ivoa_registry_xquerySearchXML(xquery);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_getIdentity(int argc, IDL_VPTR *argv, char* argk)
{
struct RegistryService retval;
   retval = ivoa_registry_getIdentity();

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_RegistryService, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_RegistryService, 0, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  managedAuthorities type=ListOfJString

     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_getSystemRegistryEndpoint(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
   retval = ivoa_registry_getSystemRegistryEndpoint();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_getFallbackSystemRegistryEndpoint(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
   retval = ivoa_registry_getFallbackSystemRegistryEndpoint();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_adqlxSearchXML(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
   XMLString adqlx;
   adqlx = argv[1]->value.str.s;
   BOOL identifiersOnly;
   identifiersOnly = argv[2]->value.i;
retval = ivoa_externalRegistry_adqlxSearchXML(registry, adqlx, identifiersOnly);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_adqlxSearch(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
   XMLString adqlx;
   adqlx = argv[1]->value.str.s;
retval = ivoa_externalRegistry_adqlxSearch(registry, adqlx);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_adqlsSearchXML(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
   JString adqls;
   adqls = argv[1]->value.str.s;
   BOOL identifiersOnly;
   identifiersOnly = argv[2]->value.i;
retval = ivoa_externalRegistry_adqlsSearchXML(registry, adqls, identifiersOnly);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_adqlsSearch(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
   JString adqls;
   adqls = argv[1]->value.str.s;
retval = ivoa_externalRegistry_adqlsSearch(registry, adqls);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_keywordSearchXML(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
   JString keywords;
   keywords = argv[1]->value.str.s;
   BOOL orValues;
   orValues = argv[2]->value.i;
retval = ivoa_externalRegistry_keywordSearchXML(registry, keywords, orValues);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_keywordSearch(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
   JString keywords;
   keywords = argv[1]->value.str.s;
   BOOL orValues;
   orValues = argv[2]->value.i;
retval = ivoa_externalRegistry_keywordSearch(registry, keywords, orValues);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_getResourceXML(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
   IvornOrURI id;
   id = argv[1]->value.str.s;
retval = ivoa_externalRegistry_getResourceXML(registry, id);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_getResource(int argc, IDL_VPTR *argv, char* argk)
{
struct Resource_Base retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
   IvornOrURI id;
   id = argv[1]->value.str.s;
retval = ivoa_externalRegistry_getResource(registry, id);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_Resource, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_Resource, 0, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  validationLevel type=ListOfValidation
offset = IDL_StructTagInfoByIndex(s_Resource, 1, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.title);
offset = IDL_StructTagInfoByIndex(s_Resource, 2, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  id type=IvornOrURI
offset = IDL_StructTagInfoByIndex(s_Resource, 3, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.shortName);
offset = IDL_StructTagInfoByIndex(s_Resource, 4, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  curation type=struct Curation
offset = IDL_StructTagInfoByIndex(s_Resource, 5, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  content type=struct Content
offset = IDL_StructTagInfoByIndex(s_Resource, 6, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.status);
offset = IDL_StructTagInfoByIndex(s_Resource, 7, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.created);
offset = IDL_StructTagInfoByIndex(s_Resource, 8, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.updated);
offset = IDL_StructTagInfoByIndex(s_Resource, 9, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.type);

     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_xquerySearchXML(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
   JString xquery;
   xquery = argv[1]->value.str.s;
retval = ivoa_externalRegistry_xquerySearchXML(registry, xquery);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_xquerySearch(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
   JString xquery;
   xquery = argv[1]->value.str.s;
retval = ivoa_externalRegistry_xquerySearch(registry, xquery);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_getIdentityXML(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
retval = ivoa_externalRegistry_getIdentityXML(registry);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_getIdentity(int argc, IDL_VPTR *argv, char* argk)
{
struct RegistryService retval;
      IvornOrURI registry;
   registry = argv[0]->value.str.s;
retval = ivoa_externalRegistry_getIdentity(registry);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_RegistryService, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_RegistryService, 0, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  managedAuthorities type=ListOfJString

     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_buildResources(int argc, IDL_VPTR *argv, char* argk)
{
ListOfResource_Base retval;
      XMLString doc;
   doc = argv[0]->value.str.s;
retval = ivoa_externalRegistry_buildResources(doc);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_getRegistryOfRegistriesEndpoint(int argc, IDL_VPTR *argv, char* argk)
{
IvornOrURI retval;
   retval = ivoa_externalRegistry_getRegistryOfRegistriesEndpoint();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_addOption(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      URLString query;
   query = argv[0]->value.str.s;
   JString optionName;
   optionName = argv[1]->value.str.s;
   JString optionValue;
   optionValue = argv[2]->value.str.s;
retval = ivoa_cone_addOption(query, optionName, optionValue);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_execute(int argc, IDL_VPTR *argv, char* argk)
{
ListOfACRKeyValueMap retval;
      URLString query;
   query = argv[0]->value.str.s;
retval = ivoa_cone_execute(query);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_executeVotable(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      URLString query;
   query = argv[0]->value.str.s;
retval = ivoa_cone_executeVotable(query);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_ivoa_cone_executeAndSave(int argc, IDL_VPTR *argv, char* argk)
{
   URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
ivoa_cone_executeAndSave(query, saveLocation);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_ivoa_cone_saveDatasets(int argc, IDL_VPTR *argv, char* argk)
{
int retval;
      URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
retval = ivoa_cone_saveDatasets(query, saveLocation);

       IDL_VPTR idl_retval;
 idl_retval = IDL_GettmpInt(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_saveDatasetsSubset(int argc, IDL_VPTR *argv, char* argk)
{
int retval;
      URLString query;
   query = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
   ACRList rows;
//FIXME type too complex for IDL? - need to think of way of representing....
retval = ivoa_cone_saveDatasetsSubset(query, saveLocation, rows);

       IDL_VPTR idl_retval;
 idl_retval = IDL_GettmpInt(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_getRegistryAdqlQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = ivoa_cone_getRegistryAdqlQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = ivoa_cone_getRegistryXQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_constructQuery(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   double ra;
   ra = argv[1]->value.d;
   double dec;
   dec = argv[2]->value.d;
   double sr;
   sr = argv[3]->value.d;
retval = ivoa_cone_constructQuery(service, ra, dec, sr);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_ivoa_cache_flush(int argc, IDL_VPTR *argv, char* argk)
{
ivoa_cache_flush();

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_ivoa_adql_s2x(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      JString s;
   s = argv[0]->value.str.s;
retval = ivoa_adql_s2x(s);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_nvo_cone_constructQuery(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      IvornOrURI service;
   service = argv[0]->value.str.s;
   double ra;
   ra = argv[1]->value.d;
   double dec;
   dec = argv[2]->value.d;
   double sr;
   sr = argv[3]->value.d;
retval = nvo_cone_constructQuery(service, ra, dec, sr);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_nvo_cone_addOption(int argc, IDL_VPTR *argv, char* argk)
{
URLString retval;
      URLString coneQuery;
   coneQuery = argv[0]->value.str.s;
   JString optionName;
   optionName = argv[1]->value.str.s;
   JString optionValue;
   optionValue = argv[2]->value.str.s;
retval = nvo_cone_addOption(coneQuery, optionName, optionValue);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_nvo_cone_getResults(int argc, IDL_VPTR *argv, char* argk)
{
XMLString retval;
      URLString coneQuery;
   coneQuery = argv[0]->value.str.s;
retval = nvo_cone_getResults(coneQuery);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_nvo_cone_saveResults(int argc, IDL_VPTR *argv, char* argk)
{
   URLString coneQuery;
   coneQuery = argv[0]->value.str.s;
   IvornOrURI saveLocation;
   saveLocation = argv[1]->value.str.s;
nvo_cone_saveResults(coneQuery, saveLocation);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_nvo_cone_getRegistryAdqlQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = nvo_cone_getRegistryAdqlQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_nvo_cone_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
   retval = nvo_cone_getRegistryXQuery();

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static void IDL_userInterface_registryBrowser_show(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_registryBrowser_show();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_registryBrowser_hide(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_registryBrowser_hide();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_registryBrowser_search(int argc, IDL_VPTR *argv, char* argk)
{
   JString s;
   s = argv[0]->value.str.s;
userInterface_registryBrowser_search(s);

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_registryBrowser_open(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI uri;
   uri = argv[0]->value.str.s;
userInterface_registryBrowser_open(uri);

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_queryBuilder_show(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_queryBuilder_show();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_myspaceBrowser_show(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_myspaceBrowser_show();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_myspaceBrowser_hide(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_myspaceBrowser_hide();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_lookout_show(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_lookout_show();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_lookout_hide(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_lookout_hide();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_lookout_refresh(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_lookout_refresh();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_jobMonitor_show(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_jobMonitor_show();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_jobMonitor_hide(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_jobMonitor_hide();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_jobMonitor_refresh(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_jobMonitor_refresh();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_jobMonitor_addApplication(int argc, IDL_VPTR *argv, char* argk)
{
   JString name;
   name = argv[0]->value.str.s;
   IvornOrURI executionId;
   executionId = argv[1]->value.str.s;
userInterface_jobMonitor_addApplication(name, executionId);

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_jobMonitor_displayApplicationTab(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_jobMonitor_displayApplicationTab();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_jobMonitor_displayJobTab(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_jobMonitor_displayJobTab();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_fileManager_show(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_fileManager_show();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_astroscope_show(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_astroscope_show();

       IDL_VPTR idl_retval;

   };
static void IDL_userInterface_applicationLauncher_show(int argc, IDL_VPTR *argv, char* argk)
{
userInterface_applicationLauncher_show();

       IDL_VPTR idl_retval;

   };
static void IDL_util_tables_convertFiles(int argc, IDL_VPTR *argv, char* argk)
{
   IvornOrURI inLocation;
   inLocation = argv[0]->value.str.s;
   JString inFormat;
   inFormat = argv[1]->value.str.s;
   IvornOrURI outLocation;
   outLocation = argv[2]->value.str.s;
   JString outFormat;
   outFormat = argv[3]->value.str.s;
util_tables_convertFiles(inLocation, inFormat, outLocation, outFormat);

       IDL_VPTR idl_retval;

   };
static void IDL_util_tables_convertToFile(int argc, IDL_VPTR *argv, char* argk)
{
   JString input;
   input = argv[0]->value.str.s;
   JString inFormat;
   inFormat = argv[1]->value.str.s;
   IvornOrURI outLocation;
   outLocation = argv[2]->value.str.s;
   JString outFormat;
   outFormat = argv[3]->value.str.s;
util_tables_convertToFile(input, inFormat, outLocation, outFormat);

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_util_tables_convertFromFile(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      IvornOrURI inLocation;
   inLocation = argv[0]->value.str.s;
   JString inFormat;
   inFormat = argv[1]->value.str.s;
   JString outFormat;
   outFormat = argv[2]->value.str.s;
retval = util_tables_convertFromFile(inLocation, inFormat, outFormat);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_util_tables_convert(int argc, IDL_VPTR *argv, char* argk)
{
JString retval;
      JString input;
   input = argv[0]->value.str.s;
   JString inFormat;
   inFormat = argv[1]->value.str.s;
   JString outFormat;
   outFormat = argv[2]->value.str.s;
retval = util_tables_convert(input, inFormat, outFormat);

       IDL_VPTR idl_retval;
 idl_retval = acridl_StrToSTRING(retval);
     return idl_retval;
    
   };
static IDL_VPTR IDL_util_tables_listOutputFormats(int argc, IDL_VPTR *argv, char* argk)
{
ListOfJString retval;
   retval = util_tables_listOutputFormats();

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_VPTR IDL_util_tables_listInputFormats(int argc, IDL_VPTR *argv, char* argk)
{
ListOfJString retval;
   retval = util_tables_listInputFormats();

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of simple type - do not know how to yet 
     return idl_retval;
    
   };
static void IDL_votech_vomon_reload(int argc, IDL_VPTR *argv, char* argk)
{
votech_vomon_reload();

       IDL_VPTR idl_retval;

   };
static IDL_VPTR IDL_votech_vomon_checkAvailability(int argc, IDL_VPTR *argv, char* argk)
{
struct VoMonBean retval;
      IvornOrURI id;
   id = argv[0]->value.str.s;
retval = votech_vomon_checkAvailability(id);

       IDL_VPTR idl_retval;
 IDL_MEMINT offset;
               IDL_MakeTempStructVector(s_VoMonBean, (IDL_MEMINT)1, &idl_retval, IDL_TRUE);
offset = IDL_StructTagInfoByIndex(s_VoMonBean, 0, IDL_MSG_LONGJMP, NULL);
 *((int*)(idl_retval->value.s.arr->data +offset)) = retval.code;
offset = IDL_StructTagInfoByIndex(s_VoMonBean, 1, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  id type=IvornOrURI
offset = IDL_StructTagInfoByIndex(s_VoMonBean, 2, IDL_MSG_LONGJMP, NULL);
//FIXME data member not transferred  millis type=long
offset = IDL_StructTagInfoByIndex(s_VoMonBean, 3, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.status);
offset = IDL_StructTagInfoByIndex(s_VoMonBean, 4, IDL_MSG_LONGJMP, NULL);
IDL_StrStore((IDL_STRING*)(idl_retval->value.s.arr->data +offset), (char *)retval.timestamp);

     return idl_retval;
    
   };
static IDL_VPTR IDL_votech_vomon_checkCeaAvailability(int argc, IDL_VPTR *argv, char* argk)
{
ListOfVoMonBean retval;
      IvornOrURI id;
   id = argv[0]->value.str.s;
retval = votech_vomon_checkCeaAvailability(id);

       IDL_VPTR idl_retval;
 idl_retval = 0; //FIXME this is an array of struct type - do not know how to yet 
     return idl_retval;
    
   };
static IDL_SYSFUN_DEF2 procedure_addr[] = {
 { (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_executeAndSave,"ASTROGRID_STAP_EXECUTEANDSAVE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_halt,"ASTROGRID_PROCESSMANAGER_HALT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_delete,"ASTROGRID_PROCESSMANAGER_DELETE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_publish_register,"ASTROGRID_PUBLISH_REGISTER", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_createFile,"ASTROGRID_MYSPACE_CREATEFILE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_createFolder,"ASTROGRID_MYSPACE_CREATEFOLDER", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_refresh,"ASTROGRID_MYSPACE_REFRESH", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_delete,"ASTROGRID_MYSPACE_DELETE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_changeStore,"ASTROGRID_MYSPACE_CHANGESTORE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_write,"ASTROGRID_MYSPACE_WRITE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_writeBinary,"ASTROGRID_MYSPACE_WRITEBINARY", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_transferCompleted,"ASTROGRID_MYSPACE_TRANSFERCOMPLETED", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_copyContentToURL,"ASTROGRID_MYSPACE_COPYCONTENTTOURL", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_copyURLToContent,"ASTROGRID_MYSPACE_COPYURLTOCONTENT", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_cancelJob,"ASTROGRID_JOBS_CANCELJOB", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_deleteJob,"ASTROGRID_JOBS_DELETEJOB", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_community_login,"ASTROGRID_COMMUNITY_LOGIN", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_community_logout,"ASTROGRID_COMMUNITY_LOGOUT", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_community_guiLogin,"ASTROGRID_COMMUNITY_GUILOGIN", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_validate,"ASTROGRID_APPLICATIONS_VALIDATE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_validateStored,"ASTROGRID_APPLICATIONS_VALIDATESTORED", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_cancel,"ASTROGRID_APPLICATIONS_CANCEL", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_executeAndSave,"IVOA_SSAP_EXECUTEANDSAVE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_saveResults,"IVOA_SKYNODE_SAVERESULTS", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_saveResultsF,"IVOA_SKYNODE_SAVERESULTSF", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_executeAndSave,"IVOA_SIAP_EXECUTEANDSAVE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_executeAndSave,"IVOA_CONE_EXECUTEANDSAVE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cache_flush,"IVOA_CACHE_FLUSH", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_saveResults,"NVO_CONE_SAVERESULTS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_registryBrowser_show,"USERINTERFACE_REGISTRYBROWSER_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_registryBrowser_hide,"USERINTERFACE_REGISTRYBROWSER_HIDE", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_registryBrowser_search,"USERINTERFACE_REGISTRYBROWSER_SEARCH", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_registryBrowser_open,"USERINTERFACE_REGISTRYBROWSER_OPEN", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_queryBuilder_show,"USERINTERFACE_QUERYBUILDER_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_myspaceBrowser_show,"USERINTERFACE_MYSPACEBROWSER_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_myspaceBrowser_hide,"USERINTERFACE_MYSPACEBROWSER_HIDE", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_lookout_show,"USERINTERFACE_LOOKOUT_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_lookout_hide,"USERINTERFACE_LOOKOUT_HIDE", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_lookout_refresh,"USERINTERFACE_LOOKOUT_REFRESH", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_show,"USERINTERFACE_JOBMONITOR_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_hide,"USERINTERFACE_JOBMONITOR_HIDE", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_refresh,"USERINTERFACE_JOBMONITOR_REFRESH", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_addApplication,"USERINTERFACE_JOBMONITOR_ADDAPPLICATION", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_displayApplicationTab,"USERINTERFACE_JOBMONITOR_DISPLAYAPPLICATIONTAB", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_displayJobTab,"USERINTERFACE_JOBMONITOR_DISPLAYJOBTAB", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_fileManager_show,"USERINTERFACE_FILEMANAGER_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_astroscope_show,"USERINTERFACE_ASTROSCOPE_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_applicationLauncher_show,"USERINTERFACE_APPLICATIONLAUNCHER_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_convertFiles,"UTIL_TABLES_CONVERTFILES", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_convertToFile,"UTIL_TABLES_CONVERTTOFILE", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_votech_vomon_reload,"VOTECH_VOMON_RELOAD", 0, 0, 0, 0 },

      { (IDL_SYSRTN_GENERIC) IDL_ar_init, "AR_INIT", 0, 0, 0, 0},
      { (IDL_SYSRTN_GENERIC) NULL, "", 0, 0, 0, 0}
  
};

static IDL_SYSFUN_DEF2 function_addr[] = {
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQuery,"ASTROGRID_STAP_CONSTRUCTQUERY", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQueryF,"ASTROGRID_STAP_CONSTRUCTQUERYF", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQueryP,"ASTROGRID_STAP_CONSTRUCTQUERYP", 6, 6, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQueryPF,"ASTROGRID_STAP_CONSTRUCTQUERYPF", 7, 7, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQueryS,"ASTROGRID_STAP_CONSTRUCTQUERYS", 7, 7, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQuerySF,"ASTROGRID_STAP_CONSTRUCTQUERYSF", 8, 8, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_addOption,"ASTROGRID_STAP_ADDOPTION", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_execute,"ASTROGRID_STAP_EXECUTE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_executeVotable,"ASTROGRID_STAP_EXECUTEVOTABLE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_saveDatasets,"ASTROGRID_STAP_SAVEDATASETS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_saveDatasetsSubset,"ASTROGRID_STAP_SAVEDATASETSSUBSET", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_getRegistryAdqlQuery,"ASTROGRID_STAP_GETREGISTRYADQLQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_getRegistryXQuery,"ASTROGRID_STAP_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_list,"ASTROGRID_PROCESSMANAGER_LIST", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_submit,"ASTROGRID_PROCESSMANAGER_SUBMIT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_submitTo,"ASTROGRID_PROCESSMANAGER_SUBMITTO", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_submitStored,"ASTROGRID_PROCESSMANAGER_SUBMITSTORED", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_submitStoredTo,"ASTROGRID_PROCESSMANAGER_SUBMITSTOREDTO", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_getExecutionInformation,"ASTROGRID_PROCESSMANAGER_GETEXECUTIONINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_getMessages,"ASTROGRID_PROCESSMANAGER_GETMESSAGES", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_getResults,"ASTROGRID_PROCESSMANAGER_GETRESULTS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_getSingleResult,"ASTROGRID_PROCESSMANAGER_GETSINGLERESULT", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_getHome,"ASTROGRID_MYSPACE_GETHOME", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_exists,"ASTROGRID_MYSPACE_EXISTS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_getNodeInformation,"ASTROGRID_MYSPACE_GETNODEINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_createChildFolder,"ASTROGRID_MYSPACE_CREATECHILDFOLDER", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_createChildFile,"ASTROGRID_MYSPACE_CREATECHILDFILE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_getParent,"ASTROGRID_MYSPACE_GETPARENT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_list,"ASTROGRID_MYSPACE_LIST", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_listIvorns,"ASTROGRID_MYSPACE_LISTIVORNS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_listNodeInformation,"ASTROGRID_MYSPACE_LISTNODEINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_rename,"ASTROGRID_MYSPACE_RENAME", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_move,"ASTROGRID_MYSPACE_MOVE", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_copy,"ASTROGRID_MYSPACE_COPY", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_read,"ASTROGRID_MYSPACE_READ", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_readBinary,"ASTROGRID_MYSPACE_READBINARY", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_getReadContentURL,"ASTROGRID_MYSPACE_GETREADCONTENTURL", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_getWriteContentURL,"ASTROGRID_MYSPACE_GETWRITECONTENTURL", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_listStores,"ASTROGRID_MYSPACE_LISTSTORES", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_list,"ASTROGRID_JOBS_LIST", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_listFully,"ASTROGRID_JOBS_LISTFULLY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_createJob,"ASTROGRID_JOBS_CREATEJOB", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_wrapTask,"ASTROGRID_JOBS_WRAPTASK", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_getJobTranscript,"ASTROGRID_JOBS_GETJOBTRANSCRIPT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_getJobInformation,"ASTROGRID_JOBS_GETJOBINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_submitJob,"ASTROGRID_JOBS_SUBMITJOB", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_submitStoredJob,"ASTROGRID_JOBS_SUBMITSTOREDJOB", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_community_getUserInformation,"ASTROGRID_COMMUNITY_GETUSERINFORMATION", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_community_isLoggedIn,"ASTROGRID_COMMUNITY_ISLOGGEDIN", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_list,"ASTROGRID_APPLICATIONS_LIST", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getRegistryAdqlQuery,"ASTROGRID_APPLICATIONS_GETREGISTRYADQLQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getRegistryXQuery,"ASTROGRID_APPLICATIONS_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getCeaApplication,"ASTROGRID_APPLICATIONS_GETCEAAPPLICATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getDocumentation,"ASTROGRID_APPLICATIONS_GETDOCUMENTATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_createTemplateDocument,"ASTROGRID_APPLICATIONS_CREATETEMPLATEDOCUMENT", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_createTemplateStruct,"ASTROGRID_APPLICATIONS_CREATETEMPLATESTRUCT", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_convertDocumentToStruct,"ASTROGRID_APPLICATIONS_CONVERTDOCUMENTTOSTRUCT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_convertStructToDocument,"ASTROGRID_APPLICATIONS_CONVERTSTRUCTTODOCUMENT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_listServersProviding,"ASTROGRID_APPLICATIONS_LISTSERVERSPROVIDING", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_submit,"ASTROGRID_APPLICATIONS_SUBMIT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_submitTo,"ASTROGRID_APPLICATIONS_SUBMITTO", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_submitStored,"ASTROGRID_APPLICATIONS_SUBMITSTORED", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_submitStoredTo,"ASTROGRID_APPLICATIONS_SUBMITSTOREDTO", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getExecutionInformation,"ASTROGRID_APPLICATIONS_GETEXECUTIONINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getResults,"ASTROGRID_APPLICATIONS_GETRESULTS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_vizier_cataloguesMetaData,"CDS_VIZIER_CATALOGUESMETADATA", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_vizier_cataloguesMetaDataWavelength,"CDS_VIZIER_CATALOGUESMETADATAWAVELENGTH", 5, 5, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_vizier_cataloguesData,"CDS_VIZIER_CATALOGUESDATA", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_vizier_cataloguesDataWavelength,"CDS_VIZIER_CATALOGUESDATAWAVELENGTH", 5, 5, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_vizier_metaAll,"CDS_VIZIER_METAALL", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_UCDList,"CDS_UCD_UCDLIST", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_resolveUCD,"CDS_UCD_RESOLVEUCD", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_UCDofCatalog,"CDS_UCD_UCDOFCATALOG", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_translate,"CDS_UCD_TRANSLATE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_upgrade,"CDS_UCD_UPGRADE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_validate,"CDS_UCD_VALIDATE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_explain,"CDS_UCD_EXPLAIN", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_assign,"CDS_UCD_ASSIGN", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_sesame_resolve,"CDS_SESAME_RESOLVE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_sesame_sesame,"CDS_SESAME_SESAME", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_sesame_sesameChooseService,"CDS_SESAME_SESAMECHOOSESERVICE", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_glu_getURLfromTag,"CDS_GLU_GETURLFROMTAG", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_coordinate_convert,"CDS_COORDINATE_CONVERT", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_coordinate_convertL,"CDS_COORDINATE_CONVERTL", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_coordinate_convertE,"CDS_COORDINATE_CONVERTE", 8, 8, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_coordinate_convertLE,"CDS_COORDINATE_CONVERTLE", 7, 7, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_toolEditor_edit,"DIALOGS_TOOLEDITOR_EDIT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_toolEditor_editStored,"DIALOGS_TOOLEDITOR_EDITSTORED", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_toolEditor_selectAndBuild,"DIALOGS_TOOLEDITOR_SELECTANDBUILD", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_resourceChooser_chooseResource,"DIALOGS_RESOURCECHOOSER_CHOOSERESOURCE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_resourceChooser_chooseFolder,"DIALOGS_RESOURCECHOOSER_CHOOSEFOLDER", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_resourceChooser_fullChooseResource,"DIALOGS_RESOURCECHOOSER_FULLCHOOSERESOURCE", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_resourceChooser_fullChooseFolder,"DIALOGS_RESOURCECHOOSER_FULLCHOOSEFOLDER", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_registryGoogle_selectResources,"DIALOGS_REGISTRYGOOGLE_SELECTRESOURCES", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_registryGoogle_selectResourcesAdqlFilter,"DIALOGS_REGISTRYGOOGLE_SELECTRESOURCESADQLFILTER", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_registryGoogle_selectResourcesXQueryFilter,"DIALOGS_REGISTRYGOOGLE_SELECTRESOURCESXQUERYFILTER", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_constructQuery,"IVOA_SSAP_CONSTRUCTQUERY", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_constructQueryS,"IVOA_SSAP_CONSTRUCTQUERYS", 5, 5, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_addOption,"IVOA_SSAP_ADDOPTION", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_execute,"IVOA_SSAP_EXECUTE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_executeVotable,"IVOA_SSAP_EXECUTEVOTABLE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_saveDatasets,"IVOA_SSAP_SAVEDATASETS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_saveDatasetsSubset,"IVOA_SSAP_SAVEDATASETSSUBSET", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_getRegistryAdqlQuery,"IVOA_SSAP_GETREGISTRYADQLQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_getRegistryXQuery,"IVOA_SSAP_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_getRegistryAdqlQuery,"IVOA_SKYNODE_GETREGISTRYADQLQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_getRegistryXQuery,"IVOA_SKYNODE_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_getMetadata,"IVOA_SKYNODE_GETMETADATA", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_getFormats,"IVOA_SKYNODE_GETFORMATS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_getFunctions,"IVOA_SKYNODE_GETFUNCTIONS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_getResults,"IVOA_SKYNODE_GETRESULTS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_getResultsF,"IVOA_SKYNODE_GETRESULTSF", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_getFootprint,"IVOA_SKYNODE_GETFOOTPRINT", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_estimateQueryCost,"IVOA_SKYNODE_ESTIMATEQUERYCOST", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_skyNode_getAvailability,"IVOA_SKYNODE_GETAVAILABILITY", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_constructQuery,"IVOA_SIAP_CONSTRUCTQUERY", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_constructQueryF,"IVOA_SIAP_CONSTRUCTQUERYF", 5, 5, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_constructQueryS,"IVOA_SIAP_CONSTRUCTQUERYS", 5, 5, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_constructQuerySF,"IVOA_SIAP_CONSTRUCTQUERYSF", 6, 6, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_addOption,"IVOA_SIAP_ADDOPTION", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_execute,"IVOA_SIAP_EXECUTE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_executeVotable,"IVOA_SIAP_EXECUTEVOTABLE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_saveDatasets,"IVOA_SIAP_SAVEDATASETS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_saveDatasetsSubset,"IVOA_SIAP_SAVEDATASETSSUBSET", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_getRegistryAdqlQuery,"IVOA_SIAP_GETREGISTRYADQLQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_getRegistryXQuery,"IVOA_SIAP_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryXQueryBuilder_allRecords,"IVOA_REGISTRYXQUERYBUILDER_ALLRECORDS", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryXQueryBuilder_fullTextSearch,"IVOA_REGISTRYXQUERYBUILDER_FULLTEXTSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryXQueryBuilder_summaryTextSearch,"IVOA_REGISTRYXQUERYBUILDER_SUMMARYTEXTSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryXQueryBuilder_identifierSearch,"IVOA_REGISTRYXQUERYBUILDER_IDENTIFIERSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryXQueryBuilder_shortNameSearch,"IVOA_REGISTRYXQUERYBUILDER_SHORTNAMESEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryXQueryBuilder_titleSearch,"IVOA_REGISTRYXQUERYBUILDER_TITLESEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryXQueryBuilder_descriptionSearch,"IVOA_REGISTRYXQUERYBUILDER_DESCRIPTIONSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryAdqlBuilder_allRecords,"IVOA_REGISTRYADQLBUILDER_ALLRECORDS", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryAdqlBuilder_fullTextSearch,"IVOA_REGISTRYADQLBUILDER_FULLTEXTSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryAdqlBuilder_summaryTextSearch,"IVOA_REGISTRYADQLBUILDER_SUMMARYTEXTSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryAdqlBuilder_identifierSearch,"IVOA_REGISTRYADQLBUILDER_IDENTIFIERSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryAdqlBuilder_shortNameSearch,"IVOA_REGISTRYADQLBUILDER_SHORTNAMESEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryAdqlBuilder_titleSearch,"IVOA_REGISTRYADQLBUILDER_TITLESEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registryAdqlBuilder_descriptionSearch,"IVOA_REGISTRYADQLBUILDER_DESCRIPTIONSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_adqlxSearch,"IVOA_REGISTRY_ADQLXSEARCH", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_adqlsSearch,"IVOA_REGISTRY_ADQLSSEARCH", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_keywordSearch,"IVOA_REGISTRY_KEYWORDSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_getResource,"IVOA_REGISTRY_GETRESOURCE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_xquerySearch,"IVOA_REGISTRY_XQUERYSEARCH", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_xquerySearchXML,"IVOA_REGISTRY_XQUERYSEARCHXML", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_getIdentity,"IVOA_REGISTRY_GETIDENTITY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_getSystemRegistryEndpoint,"IVOA_REGISTRY_GETSYSTEMREGISTRYENDPOINT", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_getFallbackSystemRegistryEndpoint,"IVOA_REGISTRY_GETFALLBACKSYSTEMREGISTRYENDPOINT", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_adqlxSearchXML,"IVOA_EXTERNALREGISTRY_ADQLXSEARCHXML", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_adqlxSearch,"IVOA_EXTERNALREGISTRY_ADQLXSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_adqlsSearchXML,"IVOA_EXTERNALREGISTRY_ADQLSSEARCHXML", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_adqlsSearch,"IVOA_EXTERNALREGISTRY_ADQLSSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_keywordSearchXML,"IVOA_EXTERNALREGISTRY_KEYWORDSEARCHXML", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_keywordSearch,"IVOA_EXTERNALREGISTRY_KEYWORDSEARCH", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_getResourceXML,"IVOA_EXTERNALREGISTRY_GETRESOURCEXML", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_getResource,"IVOA_EXTERNALREGISTRY_GETRESOURCE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_xquerySearchXML,"IVOA_EXTERNALREGISTRY_XQUERYSEARCHXML", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_xquerySearch,"IVOA_EXTERNALREGISTRY_XQUERYSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_getIdentityXML,"IVOA_EXTERNALREGISTRY_GETIDENTITYXML", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_getIdentity,"IVOA_EXTERNALREGISTRY_GETIDENTITY", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_buildResources,"IVOA_EXTERNALREGISTRY_BUILDRESOURCES", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_getRegistryOfRegistriesEndpoint,"IVOA_EXTERNALREGISTRY_GETREGISTRYOFREGISTRIESENDPOINT", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_addOption,"IVOA_CONE_ADDOPTION", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_execute,"IVOA_CONE_EXECUTE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_executeVotable,"IVOA_CONE_EXECUTEVOTABLE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_saveDatasets,"IVOA_CONE_SAVEDATASETS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_saveDatasetsSubset,"IVOA_CONE_SAVEDATASETSSUBSET", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_getRegistryAdqlQuery,"IVOA_CONE_GETREGISTRYADQLQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_getRegistryXQuery,"IVOA_CONE_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_constructQuery,"IVOA_CONE_CONSTRUCTQUERY", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_adql_s2x,"IVOA_ADQL_S2X", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_constructQuery,"NVO_CONE_CONSTRUCTQUERY", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_addOption,"NVO_CONE_ADDOPTION", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_getResults,"NVO_CONE_GETRESULTS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_getRegistryAdqlQuery,"NVO_CONE_GETREGISTRYADQLQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_getRegistryXQuery,"NVO_CONE_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_convertFromFile,"UTIL_TABLES_CONVERTFROMFILE", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_convert,"UTIL_TABLES_CONVERT", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_listOutputFormats,"UTIL_TABLES_LISTOUTPUTFORMATS", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_listInputFormats,"UTIL_TABLES_LISTINPUTFORMATS", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_votech_vomon_checkAvailability,"VOTECH_VOMON_CHECKAVAILABILITY", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_votech_vomon_checkCeaAvailability,"VOTECH_VOMON_CHECKCEAAVAILABILITY", 1, 1, 0, 0 },

     { (IDL_SYSRTN_GENERIC) NULL, "", 0, 0, 0, 0}
 
};

/*****************************************************************************/


extern "C" {
//    __declspec(dllexport) int IDL_Load(void)
     int IDL_Load(void)
    {
IDL_STRUCT_TAG_DEF s_tags_UserInformation[] = {
         { "COMMUNITY", 0, (void *)IDL_TYP_STRING},
         { "PASSWORD", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_UserInformation = IDL_MakeStruct("USERINFORMATION", s_tags_UserInformation);

IDL_STRUCT_TAG_DEF s_tags_TableBeanComparator[] = {

           { 0 }
          };    
    s_TableBeanComparator = IDL_MakeStruct("TABLEBEANCOMPARATOR", s_tags_TableBeanComparator);

IDL_STRUCT_TAG_DEF s_tags_TableBean[] = {
         { "COLUMNS", 0, (void *)},
         { "DESCRIPTION", 0, (void *)IDL_TYP_STRING},
         { "NAME", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_TableBean = IDL_MakeStruct("TABLEBEAN", s_tags_TableBean);

IDL_STRUCT_TAG_DEF s_tags_ParameterReferenceBean[] = {
         { "MAX", 0, (void *)IDL_TYP_INT},
         { "MIN", 0, (void *)IDL_TYP_INT},
         { "REF", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_ParameterReferenceBean = IDL_MakeStruct("PARAMETERREFERENCEBEAN", s_tags_ParameterReferenceBean);

IDL_STRUCT_TAG_DEF s_tags_ParameterBean[] = {
         { "DEFAULTVALUE", 0, (void *)IDL_TYP_STRING},
         { "DESCRIPTION", 0, (void *)IDL_TYP_STRING},
         { "NAME", 0, (void *)IDL_TYP_STRING},
         { "OPTIONS", 0, (void *)IDL_TYP_STRING},
         { "SUBTYPE", 0, (void *)IDL_TYP_STRING},
         { "TYPE", 0, (void *)IDL_TYP_STRING},
         { "UCD", 0, (void *)IDL_TYP_STRING},
         { "UINAME", 0, (void *)IDL_TYP_STRING},
         { "UNITS", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_ParameterBean = IDL_MakeStruct("PARAMETERBEAN", s_tags_ParameterBean);

IDL_STRUCT_TAG_DEF s_tags_NodeInformation[] = {
         { "ATTRIBUTES", 0, (void *)},
         { "CREATEDATE", 0, (void *)},
         { "MODIFYDATE", 0, (void *)},
         { "SIZE", 0, (void *)IDL_TYP_INT},
         { "CONTENTLOCATION", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_NodeInformation = IDL_MakeStruct("NODEINFORMATION", s_tags_NodeInformation);

IDL_STRUCT_TAG_DEF s_tags_InterfaceBean[] = {
         { "INPUTS", 0, (void *)},
         { "NAME", 0, (void *)IDL_TYP_STRING},
         { "OUTPUTS", 0, (void *)},

           { 0 }
          };    
    s_InterfaceBean = IDL_MakeStruct("INTERFACEBEAN", s_tags_InterfaceBean);

IDL_STRUCT_TAG_DEF s_tags_ExecutionMessage[] = {
         { "CONTENT", 0, (void *)IDL_TYP_STRING},
         { "LEVEL", 0, (void *)IDL_TYP_STRING},
         { "SOURCE", 0, (void *)IDL_TYP_STRING},
         { "STATUS", 0, (void *)IDL_TYP_STRING},
         { "TIMESTAMP", 0, (void *)},

           { 0 }
          };    
    s_ExecutionMessage = IDL_MakeStruct("EXECUTIONMESSAGE", s_tags_ExecutionMessage);

IDL_STRUCT_TAG_DEF s_tags_ExecutionInformation[] = {
         { "DESCRIPTION", 0, (void *)IDL_TYP_STRING},
         { "STARTTIME", 0, (void *)},
         { "FINISHTIME", 0, (void *)},
         { "STATUS", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_ExecutionInformation = IDL_MakeStruct("EXECUTIONINFORMATION", s_tags_ExecutionInformation);

IDL_STRUCT_TAG_DEF s_tags_DatabaseBean[] = {

           { 0 }
          };    
    s_DatabaseBean = IDL_MakeStruct("DATABASEBEAN", s_tags_DatabaseBean);

IDL_STRUCT_TAG_DEF s_tags_ColumnBean[] = {
         { "DATATYPE", 0, (void *)IDL_TYP_STRING},
         { "DESCRIPTION", 0, (void *)IDL_TYP_STRING},
         { "NAME", 0, (void *)IDL_TYP_STRING},
         { "UCD", 0, (void *)IDL_TYP_STRING},
         { "UNIT", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_ColumnBean = IDL_MakeStruct("COLUMNBEAN", s_tags_ColumnBean);

IDL_STRUCT_TAG_DEF s_tags_CeaService[] = {

           { 0 }
          };    
    s_CeaService = IDL_MakeStruct("CEASERVICE", s_tags_CeaService);

IDL_STRUCT_TAG_DEF s_tags_CeaServerCapability[] = {
         { "MANAGEDAPPLICATIONS", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_CeaServerCapability = IDL_MakeStruct("CEASERVERCAPABILITY", s_tags_CeaServerCapability);

IDL_STRUCT_TAG_DEF s_tags_CeaApplication[] = {
         { "INTERFACES", 0, (void *)},
         { "PARAMETERS", 0, (void *)},

           { 0 }
          };    
    s_CeaApplication = IDL_MakeStruct("CEAAPPLICATION", s_tags_CeaApplication);

IDL_STRUCT_TAG_DEF s_tags_AbstractInformation[] = {
         { "ID", 0, (void *)IDL_TYP_STRING},
         { "NAME", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_AbstractInformation = IDL_MakeStruct("ABSTRACTINFORMATION", s_tags_AbstractInformation);

IDL_STRUCT_TAG_DEF s_tags_SesamePositionBean[] = {
         { "DEC", 0, (void *)IDL_TYP_DOUBLE},
         { "DECERR", 0, (void *)IDL_TYP_DOUBLE},
         { "ONAME", 0, (void *)IDL_TYP_STRING},
         { "OTYPE", 0, (void *)IDL_TYP_STRING},
         { "POSSTR", 0, (void *)IDL_TYP_STRING},
         { "RA", 0, (void *)IDL_TYP_DOUBLE},
         { "RAERR", 0, (void *)IDL_TYP_DOUBLE},
         { "TARGET", 0, (void *)IDL_TYP_STRING},
         { "SERVICE", 0, (void *)IDL_TYP_STRING},
         { "ALIASES", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_SesamePositionBean = IDL_MakeStruct("SESAMEPOSITIONBEAN", s_tags_SesamePositionBean);

IDL_STRUCT_TAG_DEF s_tags_SkyNodeTableBean[] = {
         { "PRIMARYKEY", 0, (void *)IDL_TYP_STRING},
         { "RANK", 0, (void *)IDL_TYP_INT},
         { "RELATIONS", 0, (void *)},
         { "ROWS", 0, (void *)IDL_TYP_INT},

           { 0 }
          };    
    s_SkyNodeTableBean = IDL_MakeStruct("SKYNODETABLEBEAN", s_tags_SkyNodeTableBean);

IDL_STRUCT_TAG_DEF s_tags_SkyNodeColumnBean[] = {
         { "BYTESIZE", 0, (void *)IDL_TYP_INT},
         { "PRECISION", 0, (void *)IDL_TYP_INT},
         { "RANK", 0, (void *)IDL_TYP_INT},

           { 0 }
          };    
    s_SkyNodeColumnBean = IDL_MakeStruct("SKYNODECOLUMNBEAN", s_tags_SkyNodeColumnBean);

IDL_STRUCT_TAG_DEF s_tags_FunctionBean[] = {
         { "DESCRIPTION", 0, (void *)IDL_TYP_STRING},
         { "NAME", 0, (void *)IDL_TYP_STRING},
         { "PARAMETERS", 0, (void *)},

           { 0 }
          };    
    s_FunctionBean = IDL_MakeStruct("FUNCTIONBEAN", s_tags_FunctionBean);

IDL_STRUCT_TAG_DEF s_tags_AvailabilityBean[] = {
         { "LOCATION", 0, (void *)IDL_TYP_STRING},
         { "MESSAGE", 0, (void *)IDL_TYP_STRING},
         { "SERVERNAME", 0, (void *)IDL_TYP_STRING},
         { "TIMEONSERVER", 0, (void *)IDL_TYP_STRING},
         { "UPTIME", 0, (void *)IDL_TYP_STRING},
         { "VALIDTO", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_AvailabilityBean = IDL_MakeStruct("AVAILABILITYBEAN", s_tags_AvailabilityBean);

IDL_STRUCT_TAG_DEF s_tags_Validation[] = {
         { "VALIDATEDBY", 0, (void *)IDL_TYP_STRING},
         { "VALIDATIONLEVEL", 0, (void *)IDL_TYP_INT},

           { 0 }
          };    
    s_Validation = IDL_MakeStruct("VALIDATION", s_tags_Validation);

IDL_STRUCT_TAG_DEF s_tags_TabularDB[] = {
         { "DATABASE", 0, (void *)},

           { 0 }
          };    
    s_TabularDB = IDL_MakeStruct("TABULARDB", s_tags_TabularDB);

IDL_STRUCT_TAG_DEF s_tags_Source[] = {
         { "FORMAT", 0, (void *)IDL_TYP_STRING},
         { "VALUE", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_Source = IDL_MakeStruct("SOURCE", s_tags_Source);

IDL_STRUCT_TAG_DEF s_tags_SiapService[] = {

           { 0 }
          };    
    s_SiapService = IDL_MakeStruct("SIAPSERVICE", s_tags_SiapService);

IDL_STRUCT_TAG_DEF s_tags_SiapCapability[] = {
         { "IMAGESERVICETYPE", 0, (void *)IDL_TYP_STRING},
         { "MAXFILESIZE", 0, (void *)IDL_TYP_INT},
         { "MAXIMAGEEXTENT", 0, (void *)},
         { "MAXIMAGESIZE", 0, (void *)},
         { "MAXQUERYREGIONSIZE", 0, (void *)},
         { "TESTQUERY", 0, (void *)},
         { "MAXRECORDS", 0, (void *)IDL_TYP_INT},

           { 0 }
          };    
    s_SiapCapability = IDL_MakeStruct("SIAPCAPABILITY", s_tags_SiapCapability);

IDL_STRUCT_TAG_DEF s_tags_SiapCapability_SkySize[] = {
         { "LAT", 0, (void *)IDL_TYP_DOUBLE},
         { "LON", 0, (void *)IDL_TYP_DOUBLE},

           { 0 }
          };    
    s_SiapCapability_SkySize = IDL_MakeStruct("SIAPCAPABILITY_SKYSIZE", s_tags_SiapCapability_SkySize);

IDL_STRUCT_TAG_DEF s_tags_SiapCapability_ImageSize[] = {
         { "LAT", 0, (void *)IDL_TYP_INT},
         { "LON", 0, (void *)IDL_TYP_INT},

           { 0 }
          };    
    s_SiapCapability_ImageSize = IDL_MakeStruct("SIAPCAPABILITY_IMAGESIZE", s_tags_SiapCapability_ImageSize);

IDL_STRUCT_TAG_DEF s_tags_SiapCapability_SkyPos[] = {
         { "LAT", 0, (void *)IDL_TYP_DOUBLE},
         { "LON", 0, (void *)IDL_TYP_DOUBLE},

           { 0 }
          };    
    s_SiapCapability_SkyPos = IDL_MakeStruct("SIAPCAPABILITY_SKYPOS", s_tags_SiapCapability_SkyPos);

IDL_STRUCT_TAG_DEF s_tags_SiapCapability_Query[] = {
         { "EXTRAS", 0, (void *)IDL_TYP_STRING},
         { "POS", 0, (void *)},
         { "SIZE", 0, (void *)},
         { "VERB", 0, (void *)IDL_TYP_INT},

           { 0 }
          };    
    s_SiapCapability_Query = IDL_MakeStruct("SIAPCAPABILITY_QUERY", s_tags_SiapCapability_Query);

IDL_STRUCT_TAG_DEF s_tags_Service[] = {
         { "RIGHTS", 0, (void *)IDL_TYP_STRING},
         { "CAPABILITIES", 0, (void *)},

           { 0 }
          };    
    s_Service = IDL_MakeStruct("SERVICE", s_tags_Service);

IDL_STRUCT_TAG_DEF s_tags_SecurityMethod[] = {
         { "STANDARDID", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_SecurityMethod = IDL_MakeStruct("SECURITYMETHOD", s_tags_SecurityMethod);

IDL_STRUCT_TAG_DEF s_tags_SearchCapability[] = {
         { "EXTENSIONSEARCHSUPPORT", 0, (void *)IDL_TYP_STRING},
         { "MAXRECORDS", 0, (void *)IDL_TYP_INT},
         { "OPTIONALPROTOCOL", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_SearchCapability = IDL_MakeStruct("SEARCHCAPABILITY", s_tags_SearchCapability);

IDL_STRUCT_TAG_DEF s_tags_ResourceName[] = {
         { "ID", 0, (void *)IDL_TYP_STRING},
         { "VALUE", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_ResourceName = IDL_MakeStruct("RESOURCENAME", s_tags_ResourceName);

IDL_STRUCT_TAG_DEF s_tags_Resource[] = {
         { "VALIDATIONLEVEL", 0, (void *)},
         { "TITLE", 0, (void *)IDL_TYP_STRING},
         { "ID", 0, (void *)IDL_TYP_STRING},
         { "SHORTNAME", 0, (void *)IDL_TYP_STRING},
         { "CURATION", 0, (void *)},
         { "CONTENT", 0, (void *)},
         { "STATUS", 0, (void *)IDL_TYP_STRING},
         { "CREATED", 0, (void *)IDL_TYP_STRING},
         { "UPDATED", 0, (void *)IDL_TYP_STRING},
         { "TYPE", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_Resource = IDL_MakeStruct("RESOURCE", s_tags_Resource);

IDL_STRUCT_TAG_DEF s_tags_Relationship[] = {
         { "RELATEDRESOURCES", 0, (void *)},
         { "RELATIONSHIPTYPE", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_Relationship = IDL_MakeStruct("RELATIONSHIP", s_tags_Relationship);

IDL_STRUCT_TAG_DEF s_tags_RegistryService[] = {
         { "MANAGEDAUTHORITIES", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_RegistryService = IDL_MakeStruct("REGISTRYSERVICE", s_tags_RegistryService);

IDL_STRUCT_TAG_DEF s_tags_Organisation[] = {
         { "FACILITIES", 0, (void *)},
         { "INSTRUMENTS", 0, (void *)},

           { 0 }
          };    
    s_Organisation = IDL_MakeStruct("ORGANISATION", s_tags_Organisation);

IDL_STRUCT_TAG_DEF s_tags_Interface[] = {
         { "ACCESSURLS", 0, (void *)},
         { "ROLE", 0, (void *)IDL_TYP_STRING},
         { "SECURITYMETHODS", 0, (void *)},
         { "VERSION", 0, (void *)IDL_TYP_STRING},
         { "TYPE", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_Interface = IDL_MakeStruct("INTERFACE", s_tags_Interface);

IDL_STRUCT_TAG_DEF s_tags_HasCoverage[] = {
         { "COVERAGE", 0, (void *)},

           { 0 }
          };    
    s_HasCoverage = IDL_MakeStruct("HASCOVERAGE", s_tags_HasCoverage);

IDL_STRUCT_TAG_DEF s_tags_HarvestCapability[] = {
         { "MAXRECORDS", 0, (void *)IDL_TYP_INT},

           { 0 }
          };    
    s_HarvestCapability = IDL_MakeStruct("HARVESTCAPABILITY", s_tags_HarvestCapability);

IDL_STRUCT_TAG_DEF s_tags_Handler[] = {

           { 0 }
          };    
    s_Handler = IDL_MakeStruct("HANDLER", s_tags_Handler);

IDL_STRUCT_TAG_DEF s_tags_Format[] = {
         { "VALUE", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_Format = IDL_MakeStruct("FORMAT", s_tags_Format);

IDL_STRUCT_TAG_DEF s_tags_CurationDate[] = {
         { "ROLE", 0, (void *)IDL_TYP_STRING},
         { "VALUE", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_CurationDate = IDL_MakeStruct("CURATIONDATE", s_tags_CurationDate);

IDL_STRUCT_TAG_DEF s_tags_DataService[] = {
         { "FACILITIES", 0, (void *)},
         { "INSTRUMENTS", 0, (void *)},

           { 0 }
          };    
    s_DataService = IDL_MakeStruct("DATASERVICE", s_tags_DataService);

IDL_STRUCT_TAG_DEF s_tags_DataCollection[] = {
         { "FACILITIES", 0, (void *)},
         { "INSTRUMENTS", 0, (void *)},
         { "RIGHTS", 0, (void *)IDL_TYP_STRING},
         { "FORMATS", 0, (void *)},
         { "CATALOGUES", 0, (void *)},
         { "ACCESSURL", 0, (void *)},

           { 0 }
          };    
    s_DataCollection = IDL_MakeStruct("DATACOLLECTION", s_tags_DataCollection);

IDL_STRUCT_TAG_DEF s_tags_Curation[] = {
         { "CONTACTS", 0, (void *)},
         { "CONTRIBUTORS", 0, (void *)},
         { "CREATORS", 0, (void *)},
         { "PUBLISHER", 0, (void *)},
         { "VERSION", 0, (void *)IDL_TYP_STRING},
         { "DATES", 0, (void *)},

           { 0 }
          };    
    s_Curation = IDL_MakeStruct("CURATION", s_tags_Curation);

IDL_STRUCT_TAG_DEF s_tags_Creator[] = {
         { "LOGO", 0, (void *)IDL_TYP_STRING},
         { "LOGOURI", 0, (void *)IDL_TYP_STRING},
         { "NAME", 0, (void *)},

           { 0 }
          };    
    s_Creator = IDL_MakeStruct("CREATOR", s_tags_Creator);

IDL_STRUCT_TAG_DEF s_tags_Coverage[] = {
         { "FOOTPRINT", 0, (void *)},
         { "STCRESOURCEPROFILE", 0, (void *)IDL_TYP_STRING},
         { "WAVEBANDS", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_Coverage = IDL_MakeStruct("COVERAGE", s_tags_Coverage);

IDL_STRUCT_TAG_DEF s_tags_Content[] = {
         { "CONTENTLEVEL", 0, (void *)IDL_TYP_STRING},
         { "DESCRIPTION", 0, (void *)IDL_TYP_STRING},
         { "REFERENCEURL", 0, (void *)IDL_TYP_STRING},
         { "REFERENCEURI", 0, (void *)IDL_TYP_STRING},
         { "RELATIONSHIPS", 0, (void *)},
         { "SUBJECT", 0, (void *)IDL_TYP_STRING},
         { "TYPE", 0, (void *)IDL_TYP_STRING},
         { "SOURCE", 0, (void *)},

           { 0 }
          };    
    s_Content = IDL_MakeStruct("CONTENT", s_tags_Content);

IDL_STRUCT_TAG_DEF s_tags_Contact[] = {
         { "ADDRESS", 0, (void *)IDL_TYP_STRING},
         { "EMAIL", 0, (void *)IDL_TYP_STRING},
         { "NAME", 0, (void *)},
         { "TELEPHONE", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_Contact = IDL_MakeStruct("CONTACT", s_tags_Contact);

IDL_STRUCT_TAG_DEF s_tags_ConeService[] = {

           { 0 }
          };    
    s_ConeService = IDL_MakeStruct("CONESERVICE", s_tags_ConeService);

IDL_STRUCT_TAG_DEF s_tags_ConeCapability[] = {
         { "MAXRECORDS", 0, (void *)IDL_TYP_INT},
         { "MAXSR", 0, (void *)IDL_TYP_DOUBLE},
         { "TESTQUERY", 0, (void *)},

           { 0 }
          };    
    s_ConeCapability = IDL_MakeStruct("CONECAPABILITY", s_tags_ConeCapability);

IDL_STRUCT_TAG_DEF s_tags_ConeCapability_Query[] = {
         { "CATALOG", 0, (void *)IDL_TYP_STRING},
         { "DEC", 0, (void *)IDL_TYP_DOUBLE},
         { "EXTRAS", 0, (void *)IDL_TYP_STRING},
         { "RA", 0, (void *)IDL_TYP_DOUBLE},
         { "SR", 0, (void *)IDL_TYP_DOUBLE},
         { "VERB", 0, (void *)IDL_TYP_INT},

           { 0 }
          };    
    s_ConeCapability_Query = IDL_MakeStruct("CONECAPABILITY_QUERY", s_tags_ConeCapability_Query);

IDL_STRUCT_TAG_DEF s_tags_CatalogService[] = {
         { "TABLES", 0, (void *)},

           { 0 }
          };    
    s_CatalogService = IDL_MakeStruct("CATALOGSERVICE", s_tags_CatalogService);

IDL_STRUCT_TAG_DEF s_tags_Catalog[] = {
         { "DESCRIPTION", 0, (void *)IDL_TYP_STRING},
         { "NAME", 0, (void *)IDL_TYP_STRING},
         { "TABLES", 0, (void *)},

           { 0 }
          };    
    s_Catalog = IDL_MakeStruct("CATALOG", s_tags_Catalog);

IDL_STRUCT_TAG_DEF s_tags_Capability[] = {
         { "DESCRIPTION", 0, (void *)IDL_TYP_STRING},
         { "INTERFACES", 0, (void *)},
         { "STANDARDID", 0, (void *)IDL_TYP_STRING},
         { "VALIDATIONLEVEL", 0, (void *)},
         { "TYPE", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_Capability = IDL_MakeStruct("CAPABILITY", s_tags_Capability);

IDL_STRUCT_TAG_DEF s_tags_Authority[] = {
         { "MANAGINGORG", 0, (void *)},

           { 0 }
          };    
    s_Authority = IDL_MakeStruct("AUTHORITY", s_tags_Authority);

IDL_STRUCT_TAG_DEF s_tags_AccessURL[] = {
         { "VALUE", 0, (void *)IDL_TYP_STRING},
         { "VALUEURI", 0, (void *)IDL_TYP_STRING},
         { "USE", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_AccessURL = IDL_MakeStruct("ACCESSURL", s_tags_AccessURL);

IDL_STRUCT_TAG_DEF s_tags_VoMonBean[] = {
         { "CODE", 0, (void *)IDL_TYP_INT},
         { "ID", 0, (void *)IDL_TYP_STRING},
         { "MILLIS", 0, (void *)IDL_TYP_INT},
         { "STATUS", 0, (void *)IDL_TYP_STRING},
         { "TIMESTAMP", 0, (void *)IDL_TYP_STRING},

           { 0 }
          };    
    s_VoMonBean = IDL_MakeStruct("VOMONBEAN", s_tags_VoMonBean);

    
    
    
        return IDL_SysRtnAdd(function_addr, TRUE, IDL_CARRAY_ELTS(function_addr)-1)
            && IDL_SysRtnAdd(procedure_addr, FALSE, IDL_CARRAY_ELTS(procedure_addr)-1);
    }
   
}

 