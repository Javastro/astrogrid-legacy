var     winH, winW;

function getOzSize(){
	if (parseInt(navigator.appVersion)>3) {
		if (navigator.appName=="Netscape") {
			winW = window.innerWidth;
			winH = window.innerHeight;
		}
		if (navigator.appName.indexOf("Microsoft")!=-1) {
			winW = document.body.offsetWidth;
			winH = document.body.offsetHeight;
		}
	}
}

function findPosX(obj)
{
	var curleft = 0;
	if (obj.offsetParent) 
	{
		while (obj.offsetParent)
		{
			curleft += obj.offsetLeft
			obj = obj.offsetParent;
		}
	}
	else if (obj.x)
		curleft += obj.x;
	return curleft;
}

function findPosY(obj)
{
	var curtop = 0;
	if (obj.offsetParent)
	{
		while (obj.offsetParent)
		{
			curtop += obj.offsetTop
			obj = obj.offsetParent;
		}
	}
	else if (obj.y)
		curtop += obj.y;
	return curtop;
}

function hinter(elem){
	var idd = elem.id + "stick";
	var qposx = findPosX(elem);
	var qposy = findPosY(elem);
	getOzSize();
	var boxWidth = Math.round(winW * 0.4);
	var boxPos;
	if((qposx + 40 + boxWidth) < winW){
		boxPos = qposx+40;
	} else {
		boxPos = qposx-20 - boxWidth;
	}
	if(document.getElementById(idd)!= null) {
		var a = document.getElementById(idd);
		a.style.position="absolute";
		a.style.left= boxPos + "px";
		a.style.top= qposy+"px";
		a.style.width= boxWidth+"px";

		if(a.style.display == "none"){
			a.style.display = "";
		} else {
			a.style.display = "none";
		}
	}
}

/* ------------------------------------------------*/

function footer(){
	var fuss = document.getElementById("imgfoot");
	var qposx = findPosX(fuss);
	var qposy = findPosY(fuss);
	getOzSize();
	var gypos = winH-30;
	if(document.getElementById("imgfootbar")!= null) {
		var fussimg = document.getElementById("imgfootbar");
		if(qposy < gypos){
			fussimg.style.left = "0px";
			fussimg.style.top = gypos + "px";
			fussimg.style.display = "";
			fussimg.style.position="absolute";
		} else {
			fussimg.style.display = "";
		}
	}
}
