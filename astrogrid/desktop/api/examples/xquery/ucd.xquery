(: list the different UCD used in the registry - generate as a votable. :)
<VOTABLE>
<RESOURCE>
<TABLE>
<FIELD name="UCD" datatype="char" arraysize="*"  />
<FIELD name="Occurs"  datatype="int"/>
<DATA>
<TABLEDATA>
{
(: record-set worth searching :)
let $rs := //vor:Resource[not (@status='inactive' or @status='deleted')]
	
	
(:	and not (curation/publisher/@ivo-id='ivo://CDS')] :)


(:ucds:)
let $ucds := $rs/table/column/ucd[starts-with(.,'P')] 
		| $rs/catalog/table/column/ucd[starts-with(.,'P')]

(:calculate sonme stats on ucds :)
for $u in distinct-values($ucds)
	let $count := count($ucds[. = $u])
	
return <TR >
	<TD>{$u}</TD>
	<TD>{$count}</TD>
	</TR> 
}
</TABLEDATA>
</DATA>
</TABLE>
</RESOURCE>
</VOTABLE>