package org.astrogrid.desktop.modules.ui.fileexplorer;

import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;

/** matcher editor which allows the matcher editor to be altered
 * by default, starts with a 'pass thru' matcher
 *  */
public class MutableMatcherEditor extends AbstractMatcherEditor {
    public void setMatcher(Matcher m) {
        currentMatcher = m;
        fireChanged(currentMatcher);
    }
}