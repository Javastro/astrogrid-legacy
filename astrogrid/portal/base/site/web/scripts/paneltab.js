function ag_alert(message)
{
  alert(message);
}

function ag_body_panelbar_toggle()
{
  var panelbar = document.getElementById('ag-body-panelbar');
  var content = document.getElementById('ag-body-content-text');
  var content_panel = document.getElementById('ag-body-panelbar-body-1');

  if(panelbar && content && content_panel) {
    var panelbar_visibility = panelbar.style.visibility;
    if(panelbar_visibility == 'hidden') {
      panelbar.style.visibility = 'visible';
      content_panel.style.visibility = 'visible';
      content.style.marginRight = "20%";
    } else {
      ag_body_panelbar_tab_hideall();
      panelbar.style.visibility = 'hidden';
      content.style.marginRight = "0%";
    }
  } else {
    alert('panelbar/content not found');
  }
}

function ag_body_panelbar_tab_hideall()
{
  var panel_index = 1;
  var content_panel = document.getElementById('ag-body-panelbar-body-' + panel_index);
  while(content_panel) {
    content_panel.style.visibility = 'hidden';
    content_panel = document.getElementById('ag-body-panelbar-body-' + (++panel_index));
  }
}

function ag_body_panelbar_tab_click(content)
{
  var content_panel = document.getElementById(content);
  if(content_panel) {
    ag_body_panelbar_tab_hideall();
    content_panel.style.visibility = 'visible';
  } else {
    alert('panel not found');
  }
}

function ag_menu_scroll()
{
  ag_alert('scrolled');
  
  return false;
}
