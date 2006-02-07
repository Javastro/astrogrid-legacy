package org.astrogrid.registry.server.http.servlets.helper;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import org.astrogrid.registry.server.query.ISearch;
import org.astrogrid.registry.server.query.QueryConfigExtractor;
import org.astrogrid.registry.server.query.QueryFactory;

public class JSPHelper {
    
    public static ISearch getQueryService(HttpServletRequest hsr)  throws IllegalAccessException, 
    InstantiationException, 
    ClassNotFoundException {
        String contractVersion = getQueryContractVersion(hsr);
        
        return QueryFactory.createQueryService(contractVersion);
    }
    
    public static String setQueryContractVersion(HttpServletRequest hsr, String contractVersion) {
        hsr.getSession().setAttribute("reg.query.defaultContractVersion",contractVersion);        
        return contractVersion;
    }
    
    private static String getQueryContractVersion(HttpServletRequest hsr) {
        return hsr.getSession().getAttribute("reg.query.defaultContractVersion") == null ? 
                setQueryContractVersion(hsr,QueryConfigExtractor.getDefaultContractVersion()) :
                (String)hsr.getSession().getAttribute("reg.query.defaultContractVersion");
    }
    
}