var Xevents = new Array();
var Xmodels = new Array();

/**
 * xforms atom object
 */
function Xforms() {
		//this.element = document.getElementById("xforms");
		
		this.InitializationEventsList = new Array(	'xforms-model-construct','xforms-model-construct-done',
													'xforms-ready','xforms-model-destruct');
		this.InteractionEventsList = new Array(	'xforms-next','xforms-previous','xforms-focus','xforms-help',
												'xforms-hint','xforms-refresh','xforms-revalidate','xforms-recalculate',
												'xforms-rebuild','xforms-reset','xforms-submit');

    	this.NotificationEventsList = new Array('DOMActivate','xforms-activate','xforms-value-changed','xforms-select','xforms-deselect','xforms-scroll-first',
    											'xforms-scroll-last','xforms-insert','xforms-delete','xforms-valid','xforms-invalid',
        										'DOMFocusIn','DOMFocusOut','xforms-readonly','xforms-readwrite','xforms-required','xforms-optional',
        										'xforms-enabled','xforms-disabled','xforms-in-range','xforms-out-of-range','xforms-submit-done','xforms-submit-error');
    	
    	this.ErrorsEventsList = new Array('xforms-binding-exception','xforms-link-exception','xforms-link-error','xforms-compute-exception');
    	
    	this.registerEvents(this.InitializationEventsList);
    	this.registerEvents(this.InteractionEventsList);
    	this.registerEvents(this.NotificationEventsList);
    	this.registerEvents(this.ErrorsEventsList);
	
		/*
		for (var i=0; i < this.Events.length ;i++ ) {
			alert(this.Events[this.Events[i]]);
		}
		*/
		this.defaults();	
		
		this.childNodes = new Array();	
		this.nChildNodes= 0;
		
}

Xforms.prototype.appendModel = function(oChild) {
	document.dispatchEvent(Xevents['xforms-model-construct-done']);
	document.dispatchEvent(Xevents['xforms-ready']);
	this.childNodes[this.nChildNodes] = oChild;
	this.nChildNodes++;
	return oChild;
}


Xforms.prototype.defaults = function() {
	var a = document.getElementsByTagName('INPUT');
	for (var i=0;i < a.length; i++) {
		a[i].addEventListener('focus',focusHandler,false);	
		a[i].addEventListener('blur',blurHandler,false);
		a[i].addEventListener('keypress',keyHandler,false);
		a[i].addEventListener('change',changeHandler,false);
		if (a.type=='radio' || a.type=='checkbox') a[i].addEventListener('click',clickHandler2,false);		

	}

	var a = document.getElementsByTagName('TEXTAREA');
	for (var i=0;i < a.length; i++) {
		a[i].addEventListener('focus',focusHandler,false);	
		a[i].addEventListener('blur',blurHandler,false);
		a[i].addEventListener('click',clickHandler,false);		
	}
	
	var a = document.getElementsByTagName('BUTTON');
	for (var i=0;i < a.length; i++) {
		a[i].addEventListener('focus',focusHandler,false);	
		a[i].addEventListener('blur',blurHandler,false);
		a[i].addEventListener('click',clickHandler,false);
	}

	
	function clickHandler2(evt) {
		document.dispatchEvent(Xevents['DOMActivate']);
		var o= evt.currentTarget;
		if (o.checked)
			document.dispatchEvent(Xevents['xforms-select']);
		else
			document.dispatchEvent(Xevents['xforms-deselect']);
	}
		
	function clickHandler(evt) {
		var o= evt.target;
		o.dispatchEvent(Xevents['DOMActivate']);
		o.dispatchEvent(Xevents['xforms-activate']);

	}
	
	function changeHandler(evt) {
		var o = evt.currentTarget;
		Xactions.recalculateHandler(evt);
		Xactions.revalidateHandler(evt);
		document.dispatchEvent(Xevents['xforms-value-changed']);
	    Xactions.refreshHandler(evt);

	}

	function focusHandler(evt) {
		document.dispatchEvent(Xevents['DOMFocusIn']);
		document.dispatchEvent(Xevents['xforms-focus']);
		var o = evt.currentTarget;

		o.oldClassName = o.className;		
		o.className+='Selected';
	}

	function blurHandler(evt) {
		document.dispatchEvent(Xevents['DOMFocusOut']);
		var o = evt.currentTarget;
		o.className = o.oldClassName;
	}

	function keyHandler(evt) {
		switch (evt.keyCode) {
		case 9: //tab
			if (evt.shiftKey) 
				document.dispatchEvent(Xevents['xforms-previous']);
			else
				document.dispatchEvent(Xevents['xforms-next']);
			break;
		case 8:
			if (evt.target.readOnly) {
				evt.preventDefault();
				evt.stopPropagation();
			}
			break;
		}
		var o = evt.currentTarget;
	}

	
}

function myFired(evt) {
		var	info = 'Event Type: <b>' + evt.type + '</b><br>';
		debugDiv  = document.getElementById('debug');
		debugDiv.innerHTML+=info;
}


Xforms.prototype.registerEvents = function(oList) {
	for(var i=0; i< oList.length; i++) {
		Xevents[oList[i]] = document.createEvent("Events");
		Xevents[oList[i]].initEvent(oList[i],true,true);
		
		document.addEventListener(oList[i],myFired,false);
	}
	
}	

function Xdummy() {
	
}

/**
 * xforms model object
 */
function Xmodel(oAttributes) {
	document.dispatchEvent(Xevents['xforms-model-construct']);
	this.attributes = oAttributes
	this.id = this.attributes['id']; 
	this.instance = null;
	
	this.childEvent = new Array();
	this.childSubmission = new Array();
	this.childNodes = new Array();
	this.childBind = new Array();

}

Xmodel.prototype.appendChild = function(oChild) {
	oChild.parent = this;
	this.childNodes[this.childNodes.length] = oChild;
	return oChild
}

Xmodel.prototype.appendSubmission = function(oChild) {
	oChild.parent = this;
	this.childSubmission[this.childSubmission.length] = oChild;
	return oChild
}

Xmodel.prototype.appendBind = function(oChild) {
	oChild.parent = this;
	oChild.validate();
	this.childBind[this.childBind.length] = oChild;
	return oChild
}

Xmodel.prototype.appendInstance = function (oInstance) {
	Xmodels[Xmodels.length]=oInstance;
	if (this.id) Xmodels[this.id]=oInstance;
	
	this.instance = oInstance;
	Xactions._reset(this); 
	Xactions._revalidate(this);
}

/**
 * dump instance as url encoded form
 * @access private
 */
Xmodel.prototype._form= function (node) {
	var str = new String();
		for(var i=0;i<node.childNodes.length;i++) {
			switch (node.childNodes[i].nodeType) {
	 			case 1: // Element 
	 					if (node.childNodes[i].firstChild.nodeValue!=null) str+=node.childNodes[i].nodeName+'='+escape(node.childNodes[i].firstChild.nodeValue)+'&';
						str+=this._form(node.childNodes[i]);
					break;
				case 9: // Document
					str+=this._form(node.childNodes[i]);
					break;
			}
		}
	return str;
}

Xmodel.prototype.asXml = function () {
	Xactions._recalculate(this);
	return (this.instance.xml);
}

Xmodel.prototype.asXform = function () {
	Xactions._recalculate(this);
	str = new String();
	str+=this._form(this.instance);
	return str;

}


/**
 * xforms dom2 event object 
 */
function Xevent(oId,oAttributes,oFunction) {
	/*
	this.element = document.getElementById(oId);
	this.attributes = oAttributes
	
	var evt= createEvent(this.attributes['ev:event']); 
	var name= createEvent(this.attributes['name']); 

	this.element.control = this;
	this.element.addEventListener(evt,oFunction,false);	
	*/
}

function createEvent(evt) {
	if (evt && Xevents[evt]==undefined) {
		Xevents[evt] = document.createEvent("Events");
		Xevents[evt].initEvent(evt,true,true);
		document.addEventListener(evt,myFired,false);
	}
	return evt;
}

function _getElementsByAttributeNS(namespaceURI,attributeName,attributeValue) {
var elements = new Array();
var allElements = document.getElementsByTagName('*');

for (var i=0;i< allElements.length; i++) {
	if (allElements[i].getAttribute('xmlns')==namespaceURI &&  allElements[i].getAttribute(attributeName)==attributeValue) elements[elements.length]=allElements[i];
}

return elements
}

function _getElementsByTagNameNS(namespaceURI,localName) {
var elements = new Array();
var tag = localName ? localName:'*';
var allElements = document.getElementsByTagName(tag);

for (var i=0;i< allElements.length; i++) {
	if (allElements[i].getAttribute('xmlns')==namespaceURI) elements[elements.length]=allElements[i];
}

return elements
}
