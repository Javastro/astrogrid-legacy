/**
 *
 */
function Xsubmission (oAttributes) { 
	this.attributes = oAttributes;
	
	this.action = this.attributes['action'];
	this.method = this.attributes['method'];
	this.replace = this.attributes['replace'];
	this.parent = null;
	this.element = document.getElementById(this.attributes['id']);
	this.element.submission = this;
	this.element.addEventListener('click',this.sendHandler,false);
}	

Xsubmission.prototype.sendHandler= function(evt) {
	var httpRequest = new XMLHttpRequest(); 
	var submission = evt.currentTarget.submission;
	var responseReplace = submission.replace;
	
	document.dispatchEvent(Xevents['xforms-submit']);
	
	httpRequest.onreadystatechange = function() { 
		if (httpRequest.readyState == 4) {
			switch (httpRequest.status) {
				case 200:
					switch (responseReplace) {
						case 'all': 
							document.write(httpRequest.responseText);
							document.dispatchEvent(Xevents['xforms-submit-done']);	
							break;
			
						case 'instance':
							break;
				
						case 'none':
							break;

						case undefined:
						case null:
							alert(httpRequest.responseText);
							document.dispatchEvent(Xevents['xforms-submit-done']);	
							break;
							
						default:
							var elementReplace = document.getElementById(responseReplace);
							if (elementReplace) {
								elementReplace.innerHTML=httpRequest.responseText;
								document.dispatchEvent(Xevents['xforms-submit-done']);	
							}
					}
					break;
				default:
					document.dispatchEvent(Xevents['xforms-submit-error']);
				}
		}
	}
	
		switch (submission.method) {
			case 'get':
				httpRequest.open('GET', submission.action+'?'+submission.parent.asXform());
				httpRequest.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
				httpRequest.setRequestHeader('Referer',window.location.href);
				httpRequest.send(null); 
				break;

			case 'form-data-post':
				// multipart not implemented yet
				httpRequest.open('POST',  submission.action); 
				httpRequest.setRequestHeader('Content-Type','multipart/form-data');
  				httpRequest.send(submission.parent.asXform()); 
				break;
				
			case 'multipart-post':
				// multipart not implemented yet
				httpRequest.open('POST', submission.action);
				httpRequest.setRequestHeader('Content-Type','multipart/related');
				httpRequest.send(submission.parent.asMultipart());
				break;
			case 'post':
			default:
				httpRequest.open('POST',  submission.action); 
				httpRequest.setRequestHeader('Content-Type','application/xml');
  				httpRequest.send(submission.parent.asXml()); 
  				
		}


}

