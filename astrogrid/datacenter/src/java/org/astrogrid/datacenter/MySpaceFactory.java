package org.astrogrid.datacenter;

public interface MySpaceFactory {
	
    public Allocation allocateCacheSpace(String jobURN) throws AllocationException ;
    
    public void close( Allocation allocation ) throws AllocationException ;
    
    
}
