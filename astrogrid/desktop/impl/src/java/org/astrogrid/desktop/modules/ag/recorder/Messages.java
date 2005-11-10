/*$Id: Messages.java,v 1.1 2005/11/10 12:05:43 nw Exp $
 * Created on 07-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.recorder;

import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.MessageContainer;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import jdbm.RecordManager;
import jdbm.btree.BTree;
import jdbm.helper.LongComparator;
import jdbm.helper.Tuple;
import jdbm.helper.TupleBrowser;

/**
 * implementation of a table of messages, and management of their storage.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Nov-2005
 *
 */
public class Messages extends AbstractTableModel implements TableModel {

    public Messages(RecordManager rec) {
        this.rec = rec;
        this.messageList = new ArrayList();
        this.comp = new LongComparator();
        this.df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT);
    }
    private final DateFormat df;
    private final RecordManager rec;
    private final List messageList; // the actual model.
    private final Comparator comp;
    private BTree currentFolder = null;
    public void displayFolder(URI key) throws IOException {
        this.currentFolder = findStoreFor(key);
        messageList.clear();
        populateList(this.currentFolder,messageList);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {               
         fireTableDataChanged(); 
            }
        });
     }
            
    private BTree findStoreFor(URI id) throws IOException {
        long code = rec.getNamedObject(id.toString());
        if (code == 0) {
            BTree b= BTree.createInstance(rec,comp);
            rec.setNamedObject(id.toString(),b.getRecid());
            return b;
        } else {
            return BTree.load(rec,code);
        }
    }
    private void populateList(BTree b,List l) throws IOException {
        TupleBrowser tb = b.browse();
        Tuple t = new Tuple();
        while (tb.getNext(t)) {
            l.add(t.getValue());
        }
        
    }

    public org.astrogrid.desktop.modules.ag.MessageRecorderImpl.MessageContainer[] listFolder(URI key) throws IOException {
        List l = new ArrayList();
        BTree b = findStoreFor(key);
        populateList(b,l);
        return (MessageContainer[])l.toArray(new MessageContainer[]{});
    }

    public MessageContainer getMessage(int row) {
        return (MessageContainer)messageList.get(row);
    }

    public synchronized void deleteFolder(URI key) throws IOException {
        long code = rec.getNamedObject(key.toString());
        if (code == 0) { 
            return ; // not there.
        }
        rec.delete(code);
        currentFolder = null;
        messageList.clear();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {               
        fireTableDataChanged();
            }
        });
    }
    
    public synchronized void addMessage(URI id,MessageContainer m) throws IOException {
        BTree b = findStoreFor(id);
        final int oldSize = b.size();
        Long key = new Long(System.currentTimeMillis()); 
        // check key is free.
        while (b.find(key) != null) {
            key = new Long(key.longValue() + 1); // scan forwards.
        }
        ((MessageContainerImpl)m).setKey(key);
        b.insert(key,m,false);
        rec.commit();
        if (currentFolder != null && b.getRecid() == currentFolder.getRecid()) {
            messageList.add(m);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {                   
            fireTableRowsInserted(oldSize,oldSize);
                }
            });
        }
    }
    
    public synchronized void updateMessage(MessageContainer m) throws IOException {
        currentFolder.insert(((MessageContainerImpl)m).getKey(),m,true);            
        rec.commit();
        // probably will have updated messageList already, due to aliasing. howver, to be sure..
        final int pos = messageList.indexOf(m);
        messageList.set(pos,m);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {               
        fireTableRowsUpdated(pos,pos);
            }
        });
    }
    
    public synchronized void deleteMessage(final int row) throws IOException {
        MessageContainerImpl msg = (MessageContainerImpl)messageList.remove(row);
        currentFolder.remove(msg.getKey());
        rec.commit();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {               
        fireTableRowsDeleted(row,row);
            }
        });
    }
            
    // table model methods.
    public int getColumnCount() {
        return 3;
    }
    
    public int getRowCount() {
        return messageList.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        MessageContainer m = (MessageContainer)messageList.get(rowIndex);
        switch (columnIndex) {
            case 2: return m.getMessage().getSource();
            case 1: return df.format(m.getMessage().getTimestamp());
            case 0:return m.getSummary();
           default:
               return null;
        }
    }
    

    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2: return String.class;
            case 1: return String.class;
            case 0: return String.class;
            default: return null;
        }
    }
    public String getColumnName(int column) {
        switch(column) {
            case 2: return "From";
            case 1: return "Date";
            case 0: return "Subject";
            default: return "";
        }
    }
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // does nothing - not editable.
    }
}

/* 
$Log: Messages.java,v $
Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/