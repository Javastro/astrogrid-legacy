/*$Id: ReflectionHelper.java,v 1.8 2009/03/26 18:04:12 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Helpler methods for working for working with java.lang.reflect;
 * @todo either add more here, or move this code elsewhere.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 31-Jan-2005
 */
public class ReflectionHelper {

    private ReflectionHelper() {
    	// do nothing.
    }


    
    /** get a method by name from a class 
     * @throws NoSuchMethodException
     * */
    public static Method getMethodByName(Class clazz, String name) throws NoSuchMethodException {
        Method[] ms = clazz.getMethods();
        for (int i = 0; i < ms.length; i++) {
            if (ms[i].getName().equalsIgnoreCase(name.trim()) ) {
                return ms[i];
            }
        }
        throw new NoSuchMethodException("method " + name + " not found");
    }
    
	public static  Object callStatic(Class c, String methodName, Object ...params ) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method m = getMethodByName(c,methodName);
		return m.invoke(null,params);
	}

	

	public static Object call(Object o, String methodName,Object... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	    Method m =  getMethodByName(o.getClass(),methodName);
		return m.invoke(o,params);
	}


}


/* 
$Log: ReflectionHelper.java,v $
Revision 1.8  2009/03/26 18:04:12  nw
source code improvements - cleaned imports, @override, etc.

Revision 1.7  2008/04/23 10:50:33  nw
1.5 improvements.

Revision 1.6  2007/09/04 18:50:50  nw
Event Dispatch thread related fixes.

Revision 1.5  2007/06/27 11:10:30  nw
additional reflection methods.

Revision 1.4  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.3  2007/01/09 16:22:47  nw
minor

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.4  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/05 11:41:48  nw
added 'hidden.modules',
allowed more methods to be called from ui.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/