
function hinter(elem){
	var idd = elem.id + "stick";
	if(document.getElementById(idd)!= null) {
		var a = document.getElementById(idd);
		a.style.position="absolute";
		a.style.left= "80px";
		a.style.top= "50px";

		if(a.style.display == "none"){
			a.style.display = "";
		} else {
			a.style.display = "none";
		}
	}
}
