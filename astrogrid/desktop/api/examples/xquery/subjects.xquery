(: list the different subjects used in the registry - generate as a votable. :)
<VOTABLE>
<RESOURCE>
<TABLE>
<FIELD name="Subject" datatype="char" />
<FIELD name="Occurs"  datatype="int"/>
<FIELD name="CDS" datatype="int" />
<FIELD name="Elsewhere" datatype="int" />
<DATA>
<TABLEDATA>
{
(: record-set worth searching :)
let $active := //vor:Resource[not (@status='inactive' or @status='deleted') ]
(: CDS resources :)

let $cds := $active[curation/publisher/@ivo-id='ivo://CDS']

(:subjects:)
let $subjects := distinct-values($active/content/subject)
	
(:calculate sonme stats on subjects :)
for $subj in $subjects
	let $count := count($active[content/subject=$subj])
	let $cdsCount := count($cds[content/subject=$subj])
	order by $subj 
return <TR >
	<TD>{$subj}</TD>
	<TD>{$count}</TD>
	<TD>{$cdsCount}</TD>
	<TD>{$count - $cdsCount}</TD>
	</TR> 
}
</TABLEDATA>
</DATA>
</TABLE>
</RESOURCE>
</VOTABLE>