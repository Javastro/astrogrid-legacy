var openImg = new Image();
openImg.src = "/astrogrid-portal/icons/Folder.png";
var closedImg = new Image();
closedImg.src = "/astrogrid-portal/icons/Open.png";

function showBranch(branch){
  var objBranch = 
     document.getElementById(branch).style;
  if(objBranch.display=="block")
     objBranch.display="none";
  else
     objBranch.display="block";
  swapFolder('I' + branch);
}

function swapFolder(img){
  objImg = document.getElementById(img);
  if(objImg.src.indexOf('Open.png')>-1)
     objImg.src = openImg.src;
  else
     objImg.src = closedImg.src;
}

function setOldMySpaceName(oldMySpaceName){
  myspace_old_name = document.getElementById('myspace-src');
  myspace_old_name.value = oldMySpaceName;
}

function setNewMySpaceName(newMySpaceName){
  myspace_new_name = document.getElementById('myspace-dest');
  myspace_new_name.value = newMySpaceName;
}

function setIVORN(newIvorn){
  myspace_ivorn = document.getElementById('myspace-ivorn');
  myspace_ivorn.value = newIvorn;
}

function setParentIVORN(newIvorn){
  parentDoc = window.parent.document;
  
  alert('Parent Doc: <' + parentDoc + '>');
  
  parent_ivorn = parentDoc.getElementById('myspace-ivorn');
  myspace_ivorn = document.getElementById('myspace-ivorn');
  
  parent_ivorn.value = myspace_ivorn.value;
  
  window.close();
}
