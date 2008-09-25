"""
VOObject

Define VOObject abstract class.

Author: Francesco Pierfederici <fpierfed@eso.org>
Licensed under the Academic Free License version 2.0 (see LICENSE.txt).
"""
class VOObject(object):
    """
    VOObject

    Abstract class.
    Implement some handy utility code for object representation.
    """
    def __repr__(self):
        return(self.__str__())
    
    def __str__(self):
        """
        Override of the default __str__ method. Provide a nice format to each 
        instance string representation.
        
        It is never called explicitly, rather it is invoked by the cast to a 
        string.
        """
        # Create an initial string representation with the name of the class.
        strRepr = '%s\n' % (self.__class__.__name__)

        # Loop through the attributes and add them to strRepr, indenting them
        # as needed.
        for attr in self.__dict__.keys():
            value = getattr(self, attr)

            # Lists/tuples need extra indenting.
            if(isinstance(value, tuple) or isinstance(value, list)):
                if(len(value)):
                    subStr = ''
                    for element in value:
                        text = str(element).split('\n')
                        for s in text:
                            subStr += '     %s\n' % (str(s))
                    strRepr += '  %s:\n%s' % (attr, subStr)
                else:
                    strRepr += '  %s: []\n' % (attr)
            elif(isinstance(value, VOObject)):
                strRepr += '  %s(%s):\n' % (attr, value.__class__.__name__)
                subStr = ''
                element = str(value)
                
                # Indent subStr (remove the name of the class at first line).
                text = element.split('\n')[1:]
                
                for s in text:
                    subStr += '  %s\n' % (str(s))
                strRepr += subStr
            else:
                strRepr += '  %s: %s\n' % (attr, value)
            # <-- end if
        # <-- end for
        strRepr = strRepr[:-1]
        return(strRepr)
