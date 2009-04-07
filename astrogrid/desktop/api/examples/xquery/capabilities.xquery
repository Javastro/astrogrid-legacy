(: list the different xsi:types and standardIds used in capabilities :)
<results>

<standards>
{
(: record-set worth searching :)
let $active := //vor:Resource[not (@status='inactive' or @status='deleted') ]

(:for each xsi type :)
for $s in distinct-values($active/capability/@standardID)
order by $s
return <standard>{$s}</standard>
}
</standards>
</results>