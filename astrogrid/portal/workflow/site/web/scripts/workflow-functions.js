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


    /*
     * showTable()
     *
     * Changes display setting of div
     *
     * @param id        id of div         
     */
    var table_selected=false;
	var previously_selected_table = null;
    function show_table(id)
    { 
        if (table_selected==false) 
        {
            table_selected=true;
            if (parent.document.getElementById(id))
            {
              parent.document.getElementById(id).style.display="";
		      previously_selected_table=id;
		    }
        }
        else
        {	
            if (previously_selected_table )
            {
		      parent.document.getElementById(previously_selected_table).style.display="none"; 
		    }
            parent.document.getElementById(id).style.display="";
		    previously_selected_table=id;
		    table_selected = true;		  
        }
    }
    
               
    function show_select(object)
    {
        if (parent.document.getElementById)
        {        
            if ( parent.document.getElementById(object) != null)
                node = parent.document.getElementById(object).style.display="";
        }
        else if (parent.document.layers)
        {        
            if (parent.document.layers[object] != null)
                parent.document.layers[object].style.display = "";
        }
        else if (parent.document.all)
        {        
            parent.document.all[object].style.display = "";
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
        if (parent.document.getElementById)
        {
            if (parent.document.getElementById(object) != null)
                node = parent.document.getElementById(object).style.display="none";
        }     
        else if (parent.document.layers)
        {
            if (parent.document.layers[object] != null)
                parent.document.layers[object].style.display = "none";
        }
        else if (parent.document.all)
            parent.document.all[object].style.display = "none";
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

    
    /*
     * setActivityDetails()
     *
     * Called from display-inner-frame.xsl. Sets values within insert_activity_form and remove_activity_form
     * (display-workflow-iframe.xsl). Values are used within desginAction() when submitted
     * from menu (workflow-menu.xml).
     *
     * @param activity_key      
     * @param parent_activity_key  activity key of activity container  
     * @param index                position of activity within activity container
     * @param activity_type        
     */
    function setActivityDetails(activity_key, parent_activity_key, index, activity_type)
    {
       parent.document.insert_activity_form.activity_index_key.value = index;
       parent.document.insert_activity_form.activity_key.value = activity_key;
       parent.document.insert_activity_form.parent_activity_key.value = parent_activity_key;
       parent.document.insert_activity_form.activity_type.value = activity_type;
       
       parent.document.remove_activity_form.activity_key.value = activity_key;       
       parent.document.remove_activity_form.parent_activity_key.value = parent_activity_key;       
       parent.document.remove_activity_form.activity_index_key.value = index;       
    }   
