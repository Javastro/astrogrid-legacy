#test the siap services using Ray's validator
#Test when an error is received that
from astrogrid import acr
import urllib
import re

reqxq = acr.ivoa.siap.getRegistryXQuery()
verify="http://nvo.ncsa.uiuc.edu/dalvalidate/SIAValidater?runid=vsb54&RA=83.633208&DEC=22.014472&RASIZE=1.0&DECSIZE=1.0&FORMAT=ALL&format=xml&show=fail&show=warn&show=rec&op=Validate&endpoint="
remre=re.compile("(<\?[Xx][Mm][Ll][^>]*>)|(<!DOCTYPE[^>]+>)")
siaps = acr.ivoa.registry.xquerySearch(reqxq)
print "<tests>\n"
for siap in siaps:
    for cap in siap['capabilities']:
        if 'org.astrogrid.acr.ivoa.resource.SiapCapability' in cap['__interfaces']:
            url = cap['interfaces'][0]['accessUrls'][0]['value']
            urloriginal = url
            if not(url.endswith(('&','?'))):
                if (url.count("?") > 0):
                    url=url+"&"
                else:
                    url=url+"?"
            print '<resource id="'+siap['id']+'" title="'+siap['title']+'" url="'+url.replace('&','&amp;')+'" urlorig="'+urloriginal.replace('&','&amp;')+'">'
            a = urllib.urlopen(verify+urllib.quote_plus(url))
            print remre.sub("",a.read())
            a.close();
            metaurl = url+"FORMAT=METADATA"
            print '<metadata url="'+metaurl.replace('&','&amp;')+'">\n'
            try:
                a = urllib.urlopen(url+"FORMAT=METADATA")
                metadata = a.read();
                a.close()
                print remre.sub("",metadata)
            except:
                print "<failure/>"
            print "</metadata>\n"
            print "</resource>"
        else:
            print "id="+siap['id']+" not siap \n"

print "</tests>\n"
       