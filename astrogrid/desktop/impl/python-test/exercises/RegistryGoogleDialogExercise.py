#!/usr/bin/env python
#simple calls to exercise the registry google dialogue.

import unittest


def list() :
    rs = ar.dialogs.registryGoogle.selectResourcesFromList("Choose a resource",True
                ,['ivo://irsa.ipac/2MASS-PSC'
        ,'ivo://uk.ac.cam.ast/2dFGRS/object-catalogue/Object_catalogue_2dF_Galaxy_Redshift_Survey'
        ,'ivo://mast.stsci/siap-cutout/goods.hst'
        ,'ivo://stecf.euro-vo/SSA/HST/FOS'
        ,'ivo://uk.ac.cam.ast/iphas-dsa-catalog/IDR'
        ,'ivo://uk.ac.cam.ast/IPHAS/images/SIAP'
        ])
    print len(rs)
    for r in rs:
        print r['id']    


def query() :
    rs = ar.dialogs.registryGoogle.selectResourcesXQueryFilter("Choose a resource",True,"""
(:example query - list all cone searches:)
for $r in //vor:Resource[not (@status='inactive' or @status='deleted')] 
where $r/@xsi:type  &=  '*DataCollection' 
return $r
    """ )
    print len(rs)
    for r in rs:
        print r['id']   

if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    query()

