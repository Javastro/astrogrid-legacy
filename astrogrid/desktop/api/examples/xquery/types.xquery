(: list the different xsi:types used in the registry :)
<types>
{
(: record-set worth searching :)
let $active := //vor:Resource[not (@status='inactive' or @status='deleted') ]

(: list of distinct xsi:types :)
let $xsiList := (
	(: necessary trickery to work around differing namespace prefixes in attribute :)
	for $i in distinct-values($active/@xsi:type)
	let $j := if (contains($i,":")) then substring-after($i,":") else string($i)
	order by $j
	return $j
	)
(:for each xsi type :)
for $xsi in distinct-values($xsiList)
order by $xsi
return <type>{$xsi}</type>
}
</types>