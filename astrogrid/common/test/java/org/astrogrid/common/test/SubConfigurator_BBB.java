package org.astrogrid.common.test ;

import org.astrogrid.Configurator ;


public class SubConfigurator_BBB extends org.astrogrid.Configurator {
    
    public static final String 
     /** Three letter acronym for this subsystem within the overall AstroGrid system...  */  
         SUBSYSTEM_ACRONYM = "BBB" ; 
            
     public static final String 
     /** Configuration file for this component. */  
         CONFIG_FILENAME = "BBB_malformedconfig.xml" ;    
        
    private static SubConfigurator_BBB
        singletonDTC = new SubConfigurator_BBB() ;
        
        
    private SubConfigurator_BBB(){
        super() ;
    }
    
    
    public static SubConfigurator_BBB getInstance() {
        return singletonDTC ;
    }
    
    
    /**
      *  
      * Static getter for properties from the component's configuration.
      * <p>
      * 
      * @param key - the property key within category
      * @param category - the category within the configuration
      * @return the String value of the property, or the empty string if null
      * 
      * @see org.jconfig.jConfig
      **/       
    public static String getProperty( String key, String category ) {
        return Configurator.getProperty( SUBSYSTEM_ACRONYM, key, category ) ;
    }
        
    protected String getConfigFileName() { return CONFIG_FILENAME ; }
    protected String getSubsystemAcronym() { return SUBSYSTEM_ACRONYM ; }


    /* (non-Javadoc)
     * @see org.astrogrid.Configurator#getJNDIName()
     */
    protected String getJNDIName() {
      
      return null;
    }         
        					   			    
} // end of class SubConfigurator_BBB
