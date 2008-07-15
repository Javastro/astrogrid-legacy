/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;


/** generate an xquery where all the constraints are within the [] clause
 * i.e. there's no 'for' statement.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 15, 200811:46:15 PM
 */
public class HeadClauseSRQLVisitor extends BasicRegistrySRQLVisitor {
    /**
     * 
     */
    public HeadClauseSRQLVisitor() {
        // adjust the default and other targets to remove reference to $r
        // initialized in superclass, but not as a static variable.
        adjustTargets(defaultTarget);
        for (String[] t : targets.values()) {
            adjustTargets(t);            
        }
    }
    /** remove any referenc to $r/
     * @param t
     */
    private void adjustTargets(String[] t) {
        for (int i = 0; i < t.length; i++) {
            String target = t[i];
            if (target.startsWith("$r/")) {
                t[i] = target.substring(3); 
            }
        }
    }
    public String build(SRQL q, String filter) {
        Object o = q.accept(this);
        StringBuffer sb = new StringBuffer();
        sb.append("//vor:Resource[(not (@status = 'inactive' or @status = 'deleted')) and (");
        if (filter != null) { // apply the filter first - as should restrict faster.
            sb.append(filter).append(") and (");
        }   
        sb.append(o);
        sb.append(")]");
        logger.debug(sb);
        return sb.toString();
    }
    
    // reject all incomplete queries.
    public Object visit(TermSRQL q) {
        if (q.getTerm() == null || q.getTerm().trim().length() == 0) {
            throw new IllegalArgumentException("Not a complete query");
        }
        return super.visit(q);
    }

}
