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


    function populate_tool_details(step_name, edit_condition, step_description, stepNumber, tool_name, tool_documentation)
    {                      
        document.properties_form.step_name.value = step_name;
        document.properties_form.edit_condition.value = edit_condition; 
        document.properties_form.step_description.value = step_description;  
        document.properties_form.activity_key.value = stepNumber;  
        document.properties_form.tool_name.value = tool_name;
        document.properties_form.tool_documentation.value = tool_documentation;                    
    }
 
 
    function populate_activity_container_insert_form(activityID,parentActivityID, index, activityType)
    {
       document.insert_sequence_form.activity_index_key.value = index;
       document.insert_sequence_form.activity_key.value = activityID;
       document.insert_sequence_form.parent_activity_key.value = parentActivityID;
       document.insert_sequence_form.activity_type.value = activityType;
       document.insert_flow_form.activity_index_key.value = index;
       document.insert_flow_form.activity_key.value = activityID;
       document.insert_flow_form.parent_activity_key.value = parentActivityID;
       document.insert_flow_form.activity_type.value = activityType;       
       document.insert_step_form.activity_index_key.value = index;
       document.insert_step_form.activity_key.value = activityID;
       document.insert_step_form.parent_activity_key.value = parentActivityID;
       document.insert_step_form.activity_type.value = activityType;              
       document.remove_activity_form.activity_index_key.value = index;
       document.remove_activity_form.activity_key.value = activityID; 
       document.remove_activity_form.parent_activity_key.value = parentActivityID;             
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
    
    
    function toggleColumn(jobIdColumn)
    {
        var i, id, target;
        for (i = 0; (link = document.links[i]); i++) 
        {              
            if (/\bjobIdColumn\b/.exec(link.className)) 
            {
                if (link.href == "javascript:void(0);") 
                {
                    toggle("full_column_heading");
                    toggle("short_column_heading");
                }
                else
                {
                    id = link.href.substring((link.href.lastIndexOf("jes")),(link.href.length));
                    toggle("full_"+id);
                    toggle("short_"+id);
                }
            }
        }
    }
    