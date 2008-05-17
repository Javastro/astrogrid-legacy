/*
 * $Id: idlhelper.cpp,v 1.1 2008/05/17 16:20:35 pah Exp $
 * 
 * Created on 9 Dec 2007 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 
#include <stdio.h>
#include "idlhelper.h"


IDL_VPTR IDL_CDECL acridl_StrToSTRING(const char * s ) {
	return IDL_StrToSTRING(const_cast<char*>(s));
}
