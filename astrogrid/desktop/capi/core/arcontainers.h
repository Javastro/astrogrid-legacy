/*
 * $Id: arcontainers.h,v 1.2 2007/11/22 20:51:27 pah Exp $
 * 
 * Created on 29 Oct 2007 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

#ifndef ARCONTAINERS_H_
#define ARCONTAINERS_H_
#include <set>
#include "XmlRpc.h"

template<class T> class ListOf {
public:
	ListOf(XmlRpc::XmlRpcValue& xmlrpcval) :
		vec() {

		if (xmlrpcval.getType() != xmlrpcval.TypeInvalid) {//empty vector
			if (xmlrpcval.size() > 0) {
				vec.reserve(xmlrpcval.size());
				for (int i = 0; i < xmlrpcval.size(); ++i) {
					vec.push_back(T(xmlrpcval[i]) );
				}
			}
		}
	}

	int size() const {
		return vec.size();
	}
	T const& operator[](int i) const {
		return vec.at(i);
	}

private:
	std::vector<T> vec;
};
template<class T> class ListOfBase {
public:
	ListOfBase(XmlRpc::XmlRpcValue& xmlrpcval) :
		vec() {

		if (xmlrpcval.getType() != xmlrpcval.TypeInvalid) {//empty vector
			if (xmlrpcval.size() > 0) {
				vec.reserve(xmlrpcval.size());
				for (int i = 0; i < xmlrpcval.size(); ++i) {
					vec.push_back(T::create(xmlrpcval[i]) );
				}
			}
		}
	}

	int size() const {
		return vec.size();
	}
	T*  operator[](int i) const {
		return vec.at(i);
	}

private:
	std::vector<T*> vec;
};

template<class F, class T> T * copyArray(const ListOf<F> & from) {
	T * toArray = new T[from.size()]; //should use malloc?
	for (int i = 0; i < from.size(); ++i) {
		toArray[i] = (from[i]);

	}
	return toArray;
}
;
template<class F, class T> T * copyArrayAsStruct(const ListOf<F> & from) {
	T * toArray = new T[from.size()]; //should use malloc?
	for (int i = 0; i < from.size(); ++i) {
		  from[i].asStruct(&toArray[i]);

	}
	return toArray;
}
;

typedef const char * JString_;
//convenience class to make it easier to write the string expressions in C++ (avoiding char * for as long as possible)
/*
 class JString_ {
 private:
 std::string str;
 public:
 JString_(){}
 JString_(XmlRpc::XmlRpcValue& v) : str(static_cast<const char *>(v)) {}
 operator const char *()  const {  return str.c_str();}   	 
 };
 */
typedef JString_ IvornOrURI_;
typedef JString_ URI_;
typedef JString_ URLString_;
typedef JString_ XMLString_;
typedef JString_ Document_;

#endif /*ARCONTAINERS_H_*/
