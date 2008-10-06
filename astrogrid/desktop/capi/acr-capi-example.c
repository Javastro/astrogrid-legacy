/*
 * Example of calling the ACR from C.
 * @author Paul Harrison
 */

#include <stdio.h>
#include "arintf.h"
#include "testintf.h"


int main(int argc, char* argv[]) {
    init_ar();

    /* some examples.. */
    struct SesamePositionBean  result = cds_sesame_resolve("Crab");

    double ra = result.ra;
    double dec = result.dec;
    double size = 0.1;

    /* doing a cone search */
    char * ivorn = "ivo://wfau.roe.ac.uk/twomass-dsa/wsa";
//    ivorn ="ivo://irsa.ipac/2MASS-PSC";
    struct Resource_Base registry;
    /* TODO what about if the resource structure is some subclass - unions..  */
     registry = ivoa_registry_getResource(ivorn);

    int type = registry._type;
    type++;

    char * desc = registry.d.resource.content.description;


    JString query = ivoa_cone_constructQuery(ivorn, ra, dec, size);
    char * votable = ivoa_cone_executeVotable(query);


    ivorn = "ivo://org.astrogrid/SExtractor";
    registry = ivoa_registry_getResource(ivorn);
    printResource_Base(registry);

    ListOfService_Base servers = astrogrid_applications_listServersProviding(ivorn);
    int nservices = servers.n;
    struct Service ceaserver = servers.list[0].d.service;
    printService(ceaserver);
    ivorn = "ivo://irsa.ipac/2MASS-ASKYW-AT";
    query = ivoa_siap_constructQuery(ivorn, ra, dec, size);
    ListOfACRKeyValueMap siapresult = ivoa_siap_execute ( query) ;
    printf ("%d siap results\n first result\n", siapresult.n);
    int i;
    ACRKeyValueMap res = siapresult.list[0];
    printf("number of siap keys = %d \n",res.n);
    for ( i = 0; i < res.n; i++) {
	    printf("key=%s  val=%s \n",res.map[i].key, res.map[i].val);
	}


}
