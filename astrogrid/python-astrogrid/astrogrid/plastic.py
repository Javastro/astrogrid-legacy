__id__= '$Id: plastic.py,v 1.1 2009/02/12 12:29:15 egs Exp $'
__docformat__ = 'restructuredtext en'

from astrogrid import acr
from decorators import deprecated

# For compatibility
@deprecated
def broadcast(*args, **kwargs):
	return acr.plastic.broadcast(*args, **kwargs)
