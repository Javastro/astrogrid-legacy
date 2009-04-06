package org.astrogrid.desktop.modules.ui.fileexplorer;

import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.matchers.MatcherEditor;

/** {@code MatcherEditor} that allows the matcher editor to be altered.
 * by default, starts with a 'pass thru' matcher
 *  */
public class MutableMatcherEditor<T> extends AbstractMatcherEditor<T> implements MatcherEditor<T> {
    public void setMatcher(final Matcher<? super T> m) {
        currentMatcher = (Matcher<T>)m;
        fireChanged(currentMatcher);
    }
}