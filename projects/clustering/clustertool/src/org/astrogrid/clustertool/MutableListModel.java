/*
 * $Id: MutableListModel.java,v 1.1 2009/09/20 17:18:01 pah Exp $
 * 
 * Created on Sep 20, 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.clustertool;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;

@SuppressWarnings("serial")
public class MutableListModel extends AbstractListModel implements ListModel {
    
    private List<Double> data = new  ArrayList<Double>();

    public Object getElementAt(int index) {
        return data.get(index);
    }

    public int getSize() {
       return data.size();
    }
    
    public void setList(List<Double> newlist){
        data = newlist;
        fireContentsChanged(this, 0, data.size()-1);
    }
    public void setList(Vector v){
        data.clear();
        for (VectorEntry vectorEntry : v) {
            data.add(vectorEntry.get());
        }
        fireContentsChanged(this, 0, data.size()-1);
    }

}


/*
 * $Log: MutableListModel.java,v $
 * Revision 1.1  2009/09/20 17:18:01  pah
 * checking just prior to bham visit
 *
 */
