var skipcycle = false;
  
function fcsOnMe(){
  if (!skipcycle)
  {
    window.focus();
  }
  mytimer = setTimeout('fcsOnMe()', 500);
}
  
function loadUp() 
{
  mytimer = setTimeout('fcsOnMe()', 500);
}	  
