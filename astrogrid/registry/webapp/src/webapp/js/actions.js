var Xactions = {
	nodeSet: function(xPathString,xPathDoc) {
		
		//var xPathParser = new Dom3Xpath();
		var xPathResolver = document.createNSResolver(xPathDoc.documentElement);	
		var xPathResult = document.evaluate(xPathString, xPathDoc.documentElement, xPathResolver, 9, null);
	
		return xPathResult.singleNodeValue;
		
	},

	_reset: function () {

		var controls = _getElementsByTagNameNS("http://www.w3.org/2002/xforms","*");
				
		str='';
		for(var j=0; j < controls.length; j++) {
			str+=controls[j].tagName + ' ' + controls[j].getAttribute('xmlns') +'\n';
			var model=controls[j].getAttribute('model')?controls[j].getAttribute('model'):0;
			obj = this.nodeSet(controls[j].getAttribute('ref'),Xmodels[model]);
						
			if (obj) setFormValue(controls[j],obj.firstChild.nodeValue);
		}
		document.dispatchEvent(Xevents['xforms-reset']);
	},

	resetHandler: function (evt) {	
		var o = evt.target.control;
		Xactions._reset();
	},
	
	_recalculate: function () {
		Xvalues = new Array();
		
		var controls = _getElementsByTagNameNS("http://www.w3.org/2002/xforms","*");

		str='';
		for(var j=0; j < controls.length; j++) {
			str+=controls[j].tagName + ' ' + controls[j].getAttribute('xmlns') +'\n';
			obj = this.nodeSet(controls[j].getAttribute('ref'),Xmodels[0]);
			if (obj) getFormValue(controls[j],obj);
		}
		
		document.dispatchEvent(Xevents['xforms-recalculate']);
	},

	recalculateHandler: function (evt) {	
		var o = evt.target.control;
		Xactions._recalculate();
	},
	
	_refresh: function() {
		document.dispatchEvent(Xevents['xforms-refresh']);
	},
	
	refreshHandler: function (evt) {	
		Xactions._refresh();
	},

	_revalidate: function () {
		
		var controls = _getElementsByTagNameNS("http://www.w3.org/2002/xforms","*");

		str='';
		for(var j=0; j < controls.length; j++) {
			str+=controls[j].tagName + ' ' + controls[j].getAttribute('xmlns') +'\n';
			if (controls[j].bind) controls[j].bind.validate();
		}
		
		document.dispatchEvent(Xevents['xforms-revalidate']);
	},

	revalidateHandler: function (evt) {	
		var o = evt.target.control;
		Xactions._revalidate();	
	},
	
	_delete: function () {	
	
	},

	deleteHandler: function (evt) {	
		alert('delete');
	},

	_insert: function () {	
	
	},

	insertHandler: function (evt) {	
	
	},

	_dispatch : function (oAttributes) {
		
	},
		
	dispatchHandler: function (evt) {
		var o = evt.target;
		o.dispatchEvent(Xevents[o.control.attributes['name']]);
	},

	_setvalue: function(oAttributes) {
		//obj = this.nodeSet(oAttributes['ref'],document);
		//if (obj) alert('setvalue');
	},

	setvalueHandler: function (evt) {
		var o = evt.target;
	}

};
