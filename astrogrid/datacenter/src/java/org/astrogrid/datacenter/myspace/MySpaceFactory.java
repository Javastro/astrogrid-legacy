package org.astrogrid.datacenter.myspace;

public interface MySpaceFactory {
	
    public Allocation allocateCacheSpace(String jobURN) throws AllocationException ;
    
    public void close( Allocation allocation ) throws AllocationException ;
    
    
}
