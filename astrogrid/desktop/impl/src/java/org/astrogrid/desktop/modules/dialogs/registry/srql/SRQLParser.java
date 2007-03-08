/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry.srql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;

/** Parses a search string into a SRQL expression tree.
 * not thread safe
 * makes a best effort to be forgiving of errors.
 * 
 * 
 * srql ::= conjunction {['or'] conjunction}
 * conjunction ::= expr {'and' expr}
 * expr ::= '`' xpath '`' | target '=' primExp | primExp
 * xpath ::= any string
 * targett ::= term.
 * primExp ::= term | phrase | '(' srql ')' | 'not' expr | 
 * term ::= any string of letters and digits
 * phrase :=  ''' any string ''' | '"' any string '"'
 * 
* @author Noel Winstanley
 * @since Aug 9, 20062:47:14 PM
 */
public final class SRQLParser {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(SRQLParser.class);

	public SRQLParser(String s) {
		if (s == null) {
			throw new IllegalArgumentException("null not allowed");
		}
		Reader r = new BufferedReader(new StringReader(s));
		st = new StreamTokenizer(r);
		st.resetSyntax();
		st.wordChars('A','z');
		st.wordChars('+','<'); // includes numbers
		st.wordChars('>', '@');
		st.whitespaceChars(' ',' ');
		st.ordinaryChar('=');
		st.quoteChar('\'');
		st.quoteChar('\"');
		st.quoteChar('`');
		st.lowerCaseMode(true);
	}

	protected final StreamTokenizer st;
	
	
	public SRQL parse() throws InvalidArgumentException{
		try {
		st.nextToken();
		return srql();
		} catch (IOException e) {
			throw new InvalidArgumentException("Failed to parse search expression",e);
		}
	}
	
	private SRQL srql() throws IOException, InvalidArgumentException {
		SRQL result = null;
		OrSRQL accum = null;
		SRQL latest = null;
		while (st.ttype != StreamTokenizer.TT_EOF && st.ttype != ')') {
			if (st.ttype == StreamTokenizer.TT_WORD && st.sval.equals("or")) {
				st.nextToken();//flush
				continue;
			}
			SRQL q = conjunction();
			if (latest == null) {
				latest = q;
				result = q;
			} else if (accum == null) { // got two - add them together.
				accum = new OrSRQL();
				accum.setLeft(latest);
				result = accum;
				latest = q;
			} else { // add these two to what's alreeady accumulater
				OrSRQL accum1 = new OrSRQL();
				accum.setRight(accum1);
				accum = accum1;
				accum.setLeft(latest);
				latest = q;
			}
			//st.nextToken();//flush
		} // end while.
		
		// mop up the last one, if there..
		if (accum != null) {
			accum.setRight(latest);
		}
		
		return result;
	}
	
	private SRQL conjunction() throws IOException, InvalidArgumentException {
		SRQL latest = expr(); //primExp();
		if (latest == null) {
			return null;
		}
		AndSRQL accum = null;
		SRQL result = latest;
		while (st.ttype== StreamTokenizer.TT_WORD && st.sval.equals("and")) {
			st.nextToken(); //clears and
			SRQL right = expr(); //primExp();
			if (right == null) {
				break;
			}
			AndSRQL aq = new AndSRQL();
			if (accum == null) {
				accum = aq;
				accum.setLeft(latest);
				result = accum;
				latest = right;
			} else {
				accum.setRight(aq);
				accum = aq;
				accum.setLeft(latest);
				latest = right;
			}
		}// end while
		// mop up.
		if (accum != null) {
			accum.setRight(latest);
		}
		
		return result;
	}
	
	private SRQL expr() throws IOException, InvalidArgumentException {
		switch (st.ttype) {
		case '`' :
			XPathSRQL xp = new XPathSRQL();
			xp.setXpath(st.sval);
			st.nextToken();
			return xp;
		case '=' : // not expected to happen
			st.nextToken(); // flush it away.
			return null;
		default : // parse as a primExp, and check afterwards if it looks like a target
			SRQL latest = primExp();
			if (latest == null) {
				return null;
			}
			if (latest instanceof TermSRQL && st.ttype == '=') { // it's a target
				st.nextToken(); // remove the ':'
				TargettedSRQL t = new TargettedSRQL();
				t.setTarget(((TermSRQL)latest).getTerm());
				t.setChild(primExp());
				return t;
			} else {
				return latest;
			}
			
		}
	}

	private SRQL primExp() throws IOException, InvalidArgumentException  {
			switch (st.ttype) { 
			case StreamTokenizer.TT_NUMBER: 
				throw new InvalidArgumentException("Got a number - impossible");
			case StreamTokenizer.TT_WORD: 
					if (st.sval.equals("not")) {
						NotSRQL nq = new NotSRQL();
						st.nextToken();
						SRQL aq =expr();
						nq.setChild(aq);
						return nq;
					} else if (st.sval.equals("or") || st.sval.equals("and") || st.sval.equals("not")){ // check for a keyword in the wrong place
						st.nextToken(); // flush this keyword
						return null;
					} else {
					TermSRQL tq = new TermSRQL();
					tq.setTerm(st.sval);
					st.nextToken();
					return tq;
					}
			case '\'':
			case '\"':
				PhraseSRQL pq = new PhraseSRQL();
				pq.setPhrase(st.sval);
				st.nextToken();
				return pq;
			case '(':	
				st.nextToken();
				SRQL q =  srql();
					st.nextToken(); // flush the ')
				return q;
			case ')':
				st.nextToken();
					return null;
			default:
					st.nextToken();
				return null;
			} // end case

	}
}
