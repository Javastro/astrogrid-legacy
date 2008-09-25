(: list the different authorities and the number of records held from each in the registry :)
<authorities>
{
(: record-set worth searching :)
let $active := //vor:Resource[not (@status='inactive' or @status='deleted') ]

let $ids := (
	for $i in $active/identifier
	let $j := substring-before(substring-after($i,'ivo://'),'/')
	return $j
	)
for $x in distinct-values($ids)
order by $x
return <sz pub='{$x}'>{count($active[starts-with(identifier,concat('ivo://',$x))])}</sz>
}
</authorities>