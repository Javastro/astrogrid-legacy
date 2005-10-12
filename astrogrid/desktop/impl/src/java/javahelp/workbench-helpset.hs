<?xml version='1.0' ?>
<!DOCTYPE helpset
PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN" "helpset_2_0.dtd">
<helpset version="1.0">
	<title>Workbench and ACR</title>
	<!-- points to a file that maps id's to locations in the helpset -->
	<maps>
		<homeID>top</homeID>
		<mapref location="Map.xml"/>
	</maps>
	
	<!-- points to a file that defines a view on the helpset -->
	
	<view>
		<name>TOC</name>
		<label>Table Of Contents</label>
		<type>javax.help.TOCView</type>
		<data>TOC.xml</data>
		<image>contentIcon</image>		
	</view>
	

	
	<!-- also possible to add glossary, index, etc later -->	
	<view>
		<name>Search</name>
		<label>Search</label>
		<type>javax.help.SearchView</type>
		<data>SearchData</data>
		<image>searchIcon</image>		
	</view>		

	<view>
		<name>favorites</name>
		<label>Favorites</label>
		<type>javax.help.FavoritesView</type>
		<image>bookmarkIcon</image>
	</view>	
	
	
	<presentation default="true">
<name>main window</name>
<size width="600" height="400"/>
<location x="0" y="0"/>
<title>Workbench and Astro Client Runtime Help </title>
<toolbar>
<helpaction image="backIcon">javax.help.BackAction</helpaction>
<helpaction image="forwardIcon">javax.help.ForwardAction</helpaction>
<helpaction image="homeIcon">javax.help.HomeAction</helpaction>
<helpaction image="reloadIcon">javax.help.ReloadAction</helpaction>
<helpaction image="addBookmarkIcon">javax.help.FavoritesAction</helpaction>
<helpaction image="printIcon">javax.help.PrintAction</helpaction>
<helpaction image="printSetupIcon">javax.help.PrintSetupAction</helpaction>
</toolbar>
</presentation>
	
</helpset>