include('xpath.js');
include('dom3-xpath.js');
include('dom2.js');
include('dom2-drag.js');
include('core.js');
include('values.js');
include('utf8.js');
include('xmlextras.js');
include('submission.js');
include('bind.js');
include('switch.js');
include('hint.js');
include('actions.js');
include('httprequest.js');


function include(sFile) {
	document.write('<script type="text/javascript" src="js/'+sFile+'"></script>'); 
}
