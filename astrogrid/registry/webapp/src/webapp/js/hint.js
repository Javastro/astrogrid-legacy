function Hint(oElement,oHtml) {
	this.element = oElement;
	this.element.hint = this;
	this.html = oHtml;
	
	this.element.addEventListener('mouseover',this.show,false);
	this.element.addEventListener('mouseout',this.hide,false);
	this.element.addEventListener('click',this.hide,false);


	this.tip = document.createElement("DIV");
	this.tip.style.visibility = "hidden";
	this.tip.className = "help-tooltip";
	document.body.appendChild(this.tip);		
	this.tip.innerHTML = this.html;
} 

Hint.prototype.show  = function (evt) {
	document.dispatchEvent(Xevents['xforms-hint']);
	var o = evt.target.hint.tip;	
	evt.target.style.cursor = 'help';
	
	o.style.visibility = "visible";

		
	// width
	if (o.offsetWidth >= window.innerWidth)
		o.stylevt.width = window.innerWidth - 10 + "px";
	else
		o.style.width = "";
	
	o.style.left = evt.pageX+10 +"px";
	o.style.top = evt.pageY+10+"px";
			

   var info = '';
   info += 'Event Type: ' + evt.type + '<br>';
   info += 'Target: ' + evt.target.id + '<br>';
   info += 'Current Target: ' + evt.currentTarget.id + '<br>';
   info += 'Timestamp: ' + evt.timeStamp + '<br>';
   info += 'Does this event bubble: ' + evt.bubbles + '<br>';
   info += 'Is this event cancelable: ' + evt.cancelable + '<br>';
   info += 'Number of clicks: ' + evt.detail + '<br>';
   info += 'Button used: ' + (evt.button == 0 ? 'left' : (evt.button == 1 ? 'center' : 'right')) + '<br>';
   info += 'Alt held down?: ' + evt.altKey + '<br>';
   info += 'Ctrl held down?: ' + evt.ctrlKey + '<br>';
   info += 'Shift held down?: ' + evt.shiftKey + '<br>';
   info += 'X position with respect to client area: ' + evt.clientX + '<br>';
   info += 'Y position with respect to client area: ' + evt.clientY + '<br>';
   info += 'X position with respect to screen: ' + evt.screenX + '<br>';
   info += 'Y position with respect to screen: ' + evt.screenY + '<br>';
   info += 'X position with respect to document: ' + evt.pageX + '<br>';
   info += 'Y position with respect to document: ' + evt.pageY + '<br>';

  o.innerHTML = evt.target.hint.html;


}

Hint.prototype.hide  = function (evt) {
	var o = evt.target.hint.tip;	
	o.style.visibility = "hidden";
	
	
}
