function ag_content_panelbar_tab_hideall()
{
  var panel_index = 1;
  var content_panel = document.getElementById('ag-content-panelbar-content-' + panel_index);
  while(content_panel) {
    content_panel.style.visibility = 'hidden';
    content_panel = document.getElementById('ag-content-panelbar-content-' + (++panel_index));
  }
}

function ag_content_panelbar_tab_click(content)
{
  var content_panel = document.getElementById(content);
  if(content_panel) {
    ag_content_panelbar_tab_hideall();
    
    var visibility = content_panel.style.visibility;
    if(visibility == 'visible') {
      content_panel.style.visibility = 'hidden';
    } else {
      content_panel.style.visibility = 'visible';
    }
  } else {
    alert('panel not found');
  }
}

