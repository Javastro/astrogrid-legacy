if (document.images) {

	home_on= new Image(); 
    home_on.src="/astrogrid-portal/nav/on/home.gif";
	home_off= new Image(); 
    home_off.src="/astrogrid-portal/nav/off/home.gif";
	home_on_over= new Image(); 
    home_on_over.src="/astrogrid-portal/nav/on_over/home.gif";
	home_off_over= new Image(); 
    home_off_over.src="/astrogrid-portal/nav/off_over/home.gif";

	myspace_on= new Image(); 
    myspace_on.src="/astrogrid-portal/nav/on/myspace.gif";
	myspace_off= new Image(); 
    myspace_off.src="/astrogrid-portal/nav/off/myspace.gif";
	myspace_on_over= new Image(); 
    myspace_on_over.src="/astrogrid-portal/nav/on_over/myspace.gif";
	myspace_off_over= new Image(); 
    myspace_off_over.src="/astrogrid-portal/nav/off_over/myspace.gif";

	resources_on= new Image(); 
    resources_on.src="/astrogrid-portal/nav/on/resources.gif";
	resources_off= new Image(); 
    resources_off.src="/astrogrid-portal/nav/off/resources.gif";
	resources_on_over= new Image(); 
    resources_on_over.src="/astrogrid-portal/nav/on_over/resources.gif";
	resources_off_over= new Image(); 
    resources_off_over.src="/astrogrid-portal/nav/off_over/resources.gif";

	queries_on= new Image(); 
    queries_on.src="/astrogrid-portal/nav/on/queries.gif";
	queries_off= new Image(); 
    queries_off.src="/astrogrid-portal/nav/off/queries.gif";
	queries_on_over= new Image(); 
    queries_on_over.src="/astrogrid-portal/nav/on_over/queries.gif";
	queries_off_over= new Image(); 
    queries_off_over.src="/astrogrid-portal/nav/off_over/queries.gif";

	workflows_on= new Image(); 
    workflows_on.src="/astrogrid-portal/nav/on/workflows.gif";
	workflows_off= new Image(); 
    workflows_off.src="/astrogrid-portal/nav/off/workflows.gif";
	workflows_on_over= new Image(); 
    workflows_on_over.src="/astrogrid-portal/nav/on_over/workflows.gif";
	workflows_off_over= new Image(); 
    workflows_off_over.src="/astrogrid-portal/nav/off_over/workflows.gif";

	jobs_on= new Image(); 
    jobs_on.src="/astrogrid-portal/nav/on/jobs.gif";
	jobs_off= new Image(); 
    jobs_off.src="/astrogrid-portal/nav/off/jobs.gif";
	jobs_on_over= new Image(); 
    jobs_on_over.src="/astrogrid-portal/nav/on_over/jobs.gif";
	jobs_off_over= new Image(); 
    jobs_off_over.src="/astrogrid-portal/nav/off_over/jobs.gif";

	help_on= new Image(); 
    help_on.src="/astrogrid-portal/nav/on/help.gif";
	help_off= new Image(); 
    help_off.src="/astrogrid-portal/nav/off/help.gif";
	help_on_over= new Image(); 
    help_on_over.src="/astrogrid-portal/nav/on_over/help.gif";
	help_off_over= new Image(); 
    help_off_over.src="/astrogrid-portal/nav/off_over/help.gif";

	logout_on= new Image(); 
    logout_on.src="/astrogrid-portal/nav/on/logout.gif";
	logout_off= new Image(); 
    logout_off.src="/astrogrid-portal/nav/off/logout.gif";
	logout_on_over= new Image(); 
    logout_on_over.src="/astrogrid-portal/nav/on_over/logout.gif";
	logout_off_over= new Image(); 
    logout_off_over.src="/astrogrid-portal/nav/off_over/logout.gif";

}
function imageOn(imgName) {
	if (document.images) {
		imgOff=eval(imgName + "_on.src");
		document[imgName].src= imgOff;
	}
} 
function imageOff(imgName) {
	if (document.images) {
		imgOff=eval(imgName + "_off.src");
		document[imgName].src= imgOff;
	}
} 
function imageOverOn(imgName) {
	if (document.images) {
		imgOn=eval(imgName + "_on_over.src");
		document[imgName].src= imgOn;
	}
}
function imageOverOff(imgName) {
	if (document.images) {
		imgOn=eval(imgName + "_off_over.src");
		document[imgName].src= imgOn;
	}
}
