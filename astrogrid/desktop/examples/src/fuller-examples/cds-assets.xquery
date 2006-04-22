<html>
{
  for $x in //vor:Resource
   where $x/vr:identifier &=  'ivo://CDS/*'
   and $x/vr:title  &=  '*&amp;*' 
  return <li>{$x/vr:identifier}</li>
}
</html>

</b><br />
Tabular Sky Services from CDS: <b>
{
let $hits := (
  for $x in //vor:Resource
   where $x/vr:identifier &=  'ivo://CDS/*'
   and $x/@xsi:type  &=  '*Resource' 
  return $x)
return count($hits)
}
</b><br />
</html>


<foo>
Active <b>
{
count(//vor:Resource[@status='active'])
}
</b>
Unmarked <b>
{
count(//vor:Resource[empty(@status)])
}
</b>
Inactive <b>
{
count(//vor:Resource[@status='inactive'])
}
</b>
Deleted <b>
{
count(//vor:Resource[@status='deleted'])
}
</b>
<foo>
Other <b>
{
count(//vor:Resource[not (@status='deleted' or @status='inactive' or @status='active' or empty(@status))])
}
</b>
</foo>



<b>
{
let $hits := (
  for $x in //vor:Resource
   where $x/vr:identifier &=  'ivo://CDS/*' and $x/@status='active'
   and $x/@xsi:type  &=  '*TabularSkyService' 
  return $x)
return count($hits)
}
</b>


url</th></tr>
<results>
{
for $x in //vor:Resource
 where $x/vr:shortName &= '*437/883'
return $x
}
</results>