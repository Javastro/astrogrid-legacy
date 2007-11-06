<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Coverage form</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
    </head>
    <body>
      
    <%@ include file="/style/header.xml" %>
    <%@ include file="/style/navigation.xml" %>
    
      <div id='bodyColumn'>
        
        <h1>Coverage of the resource</h1>
 
        <form action="Coverage" method="post">
	   <input type="hidden" name="IVORN" value="<%=request.getParameter("IVORN")%>"/>
           <h2>Frequency coverage</h2>
           <p>
             Please select all wavebands that apply to this resource.
           </p>
           <ul>
             <li><input type="checkbox" value="Radio" name="Radio"/>Radio</li> 
             <li><input type="checkbox" value="Millimeter" name="mm"/>Millimetre</li>
             <li><input type="checkbox" value="Infrared" name="IR"/>IR</li>
             <li><input type="checkbox" value="Optical" name="Optical"/>Optical</li>
             <li><input type="checkbox" value="UV" name="UV"/>UV</li>
             <li><input type="checkbox" value="EUV" name="EUV"/>EUV</li>
             <li><input type="checkbox" value="X-ray" name="X-ray"/>X-ray</li>
             <li><input type="checkbox" value="Gamma-ray" name="Gamma-ray"/>Gamma-ray</li>
           </ul>
           <h2>Spatial coverage</h2>
           <p>
             Please choose the region that most closely describes the
             distribution of your data (see notes below).
           </p>
           <ul>
             <li>
               <input type="radio" name="region" value="Allsky" checked="true">All-sky</input>
             </li>
             <li>
               <input type="radio" name="region" value="Box">Box</input>,
               centre = (<input type="text" name="box.C1" size="5"/>,
               <input type="text" name="box.C2" size="5"/>) degrees,
               size = <input type="text" name="box.S1" size="5"/> &#215;
               <input type="text" name="box.S2" size="5"/> degrees,
               coordinate system
               <input type="radio" name="box.coordsys" value="IRCS" checked="true">Equatorial</input>
               <input type="radio" name="box.coordsys" value="GALACTIC_II">Galactic</input>
               <input type="radio" name="box.coordsys" value="SUPERGALACTIC">Super-galactic</input>
             </li>
             <li>
               <input type="radio" name="region" value="Circle">Circle</input>,
               centre = (<input type="text" name="circle.C1" size="5"/>,
               <input type="text" name="circle.C2" size="5"/>) degrees,
               radius = <input type="text" name="circle.Radius" size="5"/> degrees,
               coordinate system
               <input type="radio" name="circle.coordsys" value="IRCS" checked="true">Equatorial</input>
               <input type="radio" name="circle.coordsys" value="GALACTIC_II" >Galactic</input>
               <input type="radio" name="circle.coordsys" value="SUPERGALACTIC">Super-galactic</input>
             </li>
           </ul>
          <p><input type="submit" value="Update the registry entry"/></p>
        </form>
        <hr/>
        <p>
          Spatial coverage is approximate. The main use for this information is
          to allow readers of the registry to exclude resources that
          <em>do not</em> cover a region of interest, assuming that all
          other resource <em>might</em> cover that region. Therefore,
          if you fill this in, choose a
          region on the sky that includes all the data of your resource rather
          than one that precisely describes where most of your data lie. If you
          don't want to specify anything "all sky" is the best answer.
        </p>
        <p>
          When specifying the equatorial position of a region, the coordinates
          should be given in the IRCS frame. Galactic positions should be in
          the seccond system of galactic coordinates. In practice, the scale and
          the uncertainties are so great that the differences between systems
          don't matter very much.
        </p>
        <p>
          If you need to specify more than one region then you could edit the
          XML of the registry entry directly.
        </p>
      </div>
      
    </body>
</html>
