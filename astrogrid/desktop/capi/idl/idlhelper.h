#ifndef IDLHELPER_H_
#define IDLHELPER_H_
// User defined Definition Files
#include "intf.h"
// IDL-Export Library Header (located in "$_IDL_DIR_$\external\include\")
#include "idl_export.h"

IDL_VPTR IDL_CDECL acridl_StrToSTRING(const char * s ) {
	return IDL_StrToSTRING(const_cast<char*>(s));
}


#endif /*IDLHELPER_H_*/
