/***********************************************
 * Dom Level 3 Xpath Wrapper
 */

ANY_TYPE = 0;
NUMBER_TYPE = 1;
STRING_TYPE = 2;
BOOLEAN_TYPE = 3;
UNORDERED_NODE_ITERATOR_TYPE = 4;
ORDERED_NODE_ITERATOR_TYPE = 5;
UNORDERED_SNAPSHOT_TYPE = 6;
ORDERED_SNAPSHOT_TYPE = 7;
ANY_UNORDERED_NODE_TYPE = 8;
FIRST_ORDERED_NODE_TYPE = 9;
 

document.evaluate = function (expression,contextNode,nsResolver,resultType, other) {
	this.parser = new XPathParser();
	this.xpath =  this.parser.parse(expression);
	
	this.context = new XPathContext(null,nsResolver,null);
	
	this.context.expressionContextNode = contextNode;

	this.result = this.xpath.evaluate(this.context);

	if (this.result.constructor === XString) {
 		this.stringValue = this.result.stringValue();
	}

	if (this.result.constructor === XNumber) {
 		this.numberValue = this.result.numberValue();
	}
	
	if (this.result.constructor === XBoolean) {
		this.booleanValue = this.result.booleanValue();
	}

	if (this.result.constructor === XNodeSet) {
 		this.singleNodeValue = this.result.toArray()[0];
 	}
	
	return this;
}

document.createNSResolver = function(contextNode) {
	
	return null;
	
}

