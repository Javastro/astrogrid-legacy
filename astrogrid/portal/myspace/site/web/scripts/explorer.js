function reset_action(form, action) {
  myspace_explorer_form = document.getElementById(form);
  if(myspace_explorer_form) {
    myspace_explorer_form.action = action;
  }
}
