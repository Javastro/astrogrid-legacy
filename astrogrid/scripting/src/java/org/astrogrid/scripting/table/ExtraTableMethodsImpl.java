/*$Id: ExtraTableMethodsImpl.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
 * Created on 06-Dec-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting.table;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.WrapperStarTable;

/** 
 * Implementation of the Extra table methods.
 * Done like this so that we can use an instance of this in each table class to simulate 'multiple inheritance'
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Dec-2004
 *
 */
abstract class ExtraTableMethodsImpl  implements ExtraTableMethods{
     static Class groovyCallableClass;
     static Class groovyColumnWrapper;
     static {
         try {
             groovyCallableClass = Class.forName("groovy.lang.Closure");
             groovyColumnWrapper = Class.forName("org.astrogrid.scripting.table.GroovyColumnWrapperTable");
         } catch (Exception e) {
             //not a problem.
             groovyCallableClass = null;
             groovyColumnWrapper = null;
         }
             
     }
 
    public ScriptStarTable addColumn(ColumnInfo meta, Object colValue) {
        // depends on type of colValue which impl we use.
        if (colValue instanceof Method) { // assume it's a static method
        return new MethodColumnWrapperTable(meta,(Method)colValue,getTable());
        }
        if (colValue instanceof ColumnFunction) {
            return new FunctionColumnWrapperTable(meta,(ColumnFunction)colValue,getTable());
        }
        if (groovyCallableClass != null && groovyCallableClass.isAssignableFrom(colValue.getClass())) {
            try {
                return (ScriptStarTable) groovyColumnWrapper.getConstructors()[0].newInstance(new Object[]{meta,colValue,getTable()});
            } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return new ConstantColumnWrapperTable(meta,colValue,getTable());   
    }
    public MutableScriptStarTable asMutableTable() throws IOException {
        MutableScriptStarTable result = new MutableScriptStarTable(getTable());
        RowSequence seq = getTable().getRowSequence();
        while (seq.hasNext()) {
            seq.next();
            result.addRow(seq.getRow());
        }
        return result;
    }
    public ScriptStarTable removeColumn(int index) {
        return new RemoveColumnWrapperTable(getTable(),index);
        
    }
    /** to be implemented by extenioins - defines where the table comes from */
    protected abstract StarTable getTable();
    

    public Iterator iterator() throws IOException {
        final RowSequence seq = getTable().getRowSequence();
        return new Iterator() {

            public void remove() {
                throw new UnsupportedOperationException("Can't remove from tables");
            }

            public boolean hasNext() {
                return seq.hasNext();
            }

            public Object next() {                
                try {
                    seq.next();
                    return Arrays.asList(seq.getRow());
                } catch (IOException e) {
                      throw new RuntimeException("StarTable.iterator().next() throw exception",e);
                }
            }
        };
    }
    

    public Iterator columnIterator(final int col) throws IOException {
        final RowSequence seq = getTable().getRowSequence();
        return new Iterator() {

            public void remove() {
                throw new UnsupportedOperationException("Can't remove from tables");
            }

            public boolean hasNext() {
                return seq.hasNext();
            }

            public Object next() {                
                try {
                    seq.next();
                    return seq.getCell(col);
                } catch (IOException e) {
                      throw new RuntimeException("StarTable.iterator().next() throw exception",e);
                }
            }
        };        
    }
    
}


/* 
$Log: ExtraTableMethodsImpl.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.

Revision 1.2  2004/12/06 20:03:03  clq2
nww_807a

Revision 1.1.2.1  2004/12/06 13:27:47  nw
fixes to improvide integration with external values and starTables.
 
*/