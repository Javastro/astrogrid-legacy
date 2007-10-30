/*
 * $Id: arcontainers.h,v 1.1 2007/10/30 15:54:29 pah Exp $
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
#include "XmlRpc.h"

template <class T>
class ListOf {
public:
	ListOf(XmlRpc::XmlRpcValue& xmlrpcval) : vec()  {
		if(xmlrpcval.size() > 0)
		{
			for (int i = 0; i < xmlrpcval.size(); ++i) {
				vec[i] = xmlrpcval[i];
			}
		}
	}
	
	int size() const {return vec.size();}
    T const& operator[](int i) const {  return vec.at(i); }
	
	
private:
	std::vector<T> vec;
};


template <class F, class T>
  T * copyArray(const ListOf<F>  & from)
{
   T * toArray = new T[from.size()]; 
   for (int i = 0; i < from.size(); ++i) {
	   toArray[i] = from[i];
	
}
   return toArray;
};

#endif /*ARCONTAINERS_H_*/
