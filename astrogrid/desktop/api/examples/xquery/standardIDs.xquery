(: list the different standardIDs used in the registry :)
<types>
{
(: record-set worth searching :)
let $active := //vor:Resource[not (@status='inactive' or @status='deleted') ]

(:for each xsi type :)
for $id in distinct-values($active/capability/@standardID)
let $count := count($active[capability/@standardID = $id])
return <type count="{$count}">{$id}</type>
}
</types>