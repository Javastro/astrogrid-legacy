var Xvalues = new Array();

function inArray(oVal,oArray) {
	for(var i=0;i<oArray.length;i++) {
		if (oVal==oArray[i]) return true;
	}
	return false;
}

function  setFormValue(formControl,val) {
		
		var value = decode_utf8(val);
		var values = value.split(',');
		
		switch (formControl.tagName) {
		case 'INPUT':
				switch (formControl.type) {
					case 'password':
					case 'text':
						formControl.value = value;
						break;
					case 'checkbox':
					case 'radio':
						if (inArray(formControl.value,values)) formControl.checked=true;
						break;
					default:
				}
			break;

		case 'TEXTAREA':
			formControl.value = value;
			break;

		case 'SELECT':
			
			for (var j=0;j<formControl.options.length;j++) {
				if(inArray(formControl.item(j).value,values)) formControl.options[j].selected = true;
			}
			
			break;
	
		case 'SPAN':
		case 'DIV':
			if (formControl.className=='horizontal') {
				formControl.slider.setValue(value);				
			} else {
				formControl.innerHTML=value;
			}
			break;
		default:
			//window.status += formControl.tagName + '(' +formControl.type + ')=' + node.nodeName + '|';
		}
		
		
}

function getFormValue(formControl,node) {
		node = node.nodeType==2?node:node.firstChild;
		
		switch (formControl.tagName) {
		case 'INPUT':
				switch (formControl.type) {
					case 'password':
					case 'text':
						node.nodeValue = encode_utf8(formControl.value);
						break;
					case 'checkbox':
					case 'radio':
						if (formControl.checked) {
							if (!Xvalues[formControl.name]) { 
								Xvalues[formControl.name]=new String(formControl.value);
							}
							else Xvalues[formControl.name] += ','+formControl.value;
						}
						node.nodeValue = encode_utf8(Xvalues[formControl.name]?Xvalues[formControl.name]:'');
						break;
					default:
				}
			break;

		case 'TEXTAREA':
			node.nodeValue = encode_utf8(formControl.value);
			break;

		case 'SELECT':
			strValues= new String();
			for (var j=0;j<formControl.length;j++) {
				if(formControl.options[j].selected==true) strValues+=formControl.options[j].value+',';
			}
				
			node.nodeValue = encode_utf8(strValues.substr(0,strValues.length-1));
			break;
	
		case 'SPAN':
		case 'DIV':
			if (formControl.className=='horizontal') {
				node.nodeValue = formControl.slider._value; 
			} else {
				node.nodeValue =  encode_utf8(formControl.outerHTML);
			}

			break;
		
		default:
			//window.status += formControl.tagName + '(' +formControl.type + ')=' + node.nodeName + '|';
		}
}