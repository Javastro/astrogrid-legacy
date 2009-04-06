/**
 * 
 */
package org.astrogrid.desktop.modules.system;

/** Simple tuple class, that can be used to pair things up
 *  - to return two results, etc.
 *  something missing from the standard libs.
 * @author Noel.Winstanley@manchester.ac.uk
 * @param <F> type of the first component of the tuple
 * @param <S> type of the second compont of the tuple
 * @since Apr 5, 200912:31:31 PM
 */
public class Tuple<F,S> {
    private final F first;
    private final S second;

    /** Create a new tuple
     * @param first first component of the tuple
     * @param second second compoonent of the tuple
     */
    public Tuple(final F first,final S second) {
        this.first = first;
        this.second = second;
    }
    
    /** return the first component of the tuple */
    public F fst() {
        return first;
    }
    
    /** return the second component of the tuple */
    public S snd() {
        return second;
    }
}
