    var image_selected=false;
	var previously_selected=null;
	var previously_selected_node = null;
    function change_image(id, node_type)
    { 
        if (image_selected==false) 
        {
            image_selected=true;
            document.getElementById(id).src="/astrogrid-portal/mount/workflow/"+node_type+"_selected.gif";
		    previously_selected=id;
		    previously_selected_node = node_type;
        }
        else
        {		
		    document.getElementById(previously_selected).src="/astrogrid-portal/mount/workflow/"+previously_selected_node+".gif";		       
            document.getElementById(id).src="/astrogrid-portal/mount/workflow/"+node_type+"_selected.gif";
		    previously_selected=id;		  
		    previously_selected_node = node_type;		  
        }
    }


    function populate_tool_details(step_name, join_condition, stepNumber, tool_name, tool_documentation)
    { 
        if (step_name == '') 
        {
            show_select('step_name_button');
        }
        else if (step_name != '')
        {
            hide_select('step_name_button');                         
        }
        if (tool_name == '') 
        {
            document.getElementById('tool_select_dropdown').style.display="";
        }
        else if (tool_name != '')
        {
            document.getElementById('tool_select_dropdown').style.display="none";
            document.getElementById('tool_select_button').style.display="none";
        }                     
        document.properties_form.step_name.value = step_name;
        document.properties_form.join_condition.value = join_condition; 
        document.properties_form.activity_key.value = stepNumber;  
        document.properties_form.tool_name.value = tool_name;
        document.properties_form.tool_documentation.value = tool_documentation;                    
    }
 
 
    function populate_container_insert_form(id, index)
    {
       document.container_insert_form.activity_index_key.value = index;
       document.container_insert_form.activity_key.value = id;
    } 
 
 
 
 
 
 
 
               
               
    function show_select(object)
    {
        if (document.getElementById)
        {
            if ( document.getElementById(object) != null)
                node = document.getElementById(object).style.visibility='visible';
        }
        else if (document.layers)
        {
            if (document.layers[object] != null)
                document.layers[object].visibility = 'visible';
        }
        else if (document.all)
        {
            document.all[object].style.visibility = 'visible';
        }
    }
               
               
    function toggle(id) 
    {
        var element = document.getElementById(id);
        with (element.style) 
        {
            if ( display == "none" )
            {
                display = ""
            } 
            else
            {
                display = "none"
            }
        }
    }               
                     
               

    function hide_select(object) 
    {
        if (document.getElementById)
        {
            if (document.getElementById(object) != null)
                node = document.getElementById(object).style.visibility='hidden';
        }     
        else if (document.layers)
        {
            if (document.layers[object] != null)
                document.layers[object].visibility = 'hidden';
        }
        else if (document.all)
            document.all[object].style.visibility = 'hidden';
    }
                   
                                   
    var popUpWin=0;
    function popUpWindow(URLStr, left, top, width, height)
    {
        if(popUpWin)
        {
            if(!popUpWin.closed) popUpWin.close();
        }
        popUpWin = open(URLStr, 'popUpWin', 'toolbar=no,location=no,directories=no,status=no,menub ar=no,scrollbar=yes,resizable=yes,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
    }