

package org.astrogrid.datacenter;

public interface VOTableFactory {

    void stream(Query query, Allocation allocation) throws VOTableException ;

    VOTable createVOTable(Query query) throws VOTableException ;
}
