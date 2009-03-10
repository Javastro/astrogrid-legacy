(: list the different validators used in the registry  :)
<html>
<body>
<h1>Validators</h1>
{
(: record-set worth searching :)
let $active := //vor:Resource[not (@status='inactive' or @status='deleted') ]
(: CDS resources :)

(:subjects:)
let $validators := distinct-values($active/validationLevel/@validatedBy)
	
(:calculate sonme stats on subjects :)
for $v in $validators
	let $validated := $active[validationLevel/@validatedBy=$v]
	let $count := count($validated)
return <div><h2>{$v}</h2>
	 Number Validated {$count}
	 <h3>Validated resources from publishers:</h3>
	 <ul>
	 {
	 for $p in distinct-values($validated/curation/publisher)
	 	return <li>{$p}</li>
	 }
	 </ul>
	 </div>
	
}
</body>
</html>