/*
 * $Id: acrtypes.h,v 1.1 2007/10/23 14:06:52 pah Exp $
 * 
 * Created on 6 Sep 2007 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

/* types used in the generated interface */
#ifndef ACRTYPES_H_
#define ACRTYPES_H_

#include <time.h>
#ifdef __cplusplus
 extern "C" {
#endif
	/*
	 * init the AR - fundamentatal function for connecting to the ar
	 * TODO perhaps nicer to return some sort of "handle" - should be called "finder" to match with the Java version.
	 */
	extern int init_ar();


/* some typedefs from ACR specific to simple types 
 * note that these are all defined as const to be compatible with the C++ std::string.c_str() member function for returning a C style string - the core of the code is written in C++*/
typedef const char* JString;
typedef const char* IvornOrURI;
typedef const char* URI;
typedef const char* URLString;
typedef const char* XMLString;
typedef const char* Document; /* for returning a org.w3c.dom.Document TODO check org.w3c.dom.Document return*/
/*TODO  perhaps an enum would be better */
typedef int BOOL;

typedef void * Object;

/* types for a primitive map - strings only */
struct keyval {
	const char * key;
	const char * val;
};

typedef  struct acrmap {
	int n; 
	struct keyval   *map;	
} ACRKeyValueMap;

typedef struct {
	int n;
	ACRKeyValueMap * list[];
} ListOfACRKeyValueMap;

/* types for a primitive list */

/* TODO - this is probably not appropriate in all cases that List is used - assuming list of strings...*/
typedef struct {
	int n;
	JString * list[];
} ACRList;

typedef struct {
	int n;
	IvornOrURI * list[];
} ListOfIvornOrURI;

typedef struct {
	int n;
	time_t * list[];
} ListOftime_t;

typedef struct {
    int n;
    void *list[];
   } ListOfbyte;

typedef struct {
    int n;
    URI * list[];
  } ListOfURI;

typedef struct {
          int n;
    JString * list[];
    } ListOfJString;

 typedef struct {
	 int n;
      Object * list[];
    } ListOfObject;

#ifdef __cplusplus
 }
#endif

#endif /*ACRTYPES_H_*/
