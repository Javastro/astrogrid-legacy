var menu;
var theTop = 30;
var old = theTop;

function init()
{
	menu = new getObj('ag-menu');
  movemenu();
}

function movemenu()
{
  pos = getPos();
  
	if (pos < theTop) pos = theTop;
	else pos += 30;
	if (pos == old)
	{
		menu.style.top = '' + pos;
	}
	old = pos;
	temp = setTimeout('movemenu()',500);
}

function getPos()
{
	if (window.innerHeight)
	{
		  pos = window.pageYOffset
	}
	else if (document.documentElement && document.documentElement.scrollTop)
	{
		pos = document.documentElement.scrollTop
	}
	else if (document.body)
	{
		  pos = document.body.scrollTop
	}
  
  return pos;
}

function getObj(name)
{
  if (document.getElementById)
  {
  	this.obj = document.getElementById(name);
    this.style = document.getElementById(name).style;
  }
  else if (document.all)
  {
	this.obj = document.all[name];
	this.style = document.all[name].style;
  }
  else if (document.layers)
  {
   	this.obj = document.layers[name];
   	this.style = document.layers[name];
  }
}

