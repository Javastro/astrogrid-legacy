package net.mchill.log;

import java.util.Vector;

/**
 * ForSourceFilter implements LogFilter, and is used to filter events by
 * those that are NOT from the given sources - ie, only messages with
 * a source not matching ANY in the source list will be kept.
 * Implemented as a NOT ForSourceFilter...
 *
 * Further (valid) sources can be added using addSource()
 * NB - watch out for circular references/tied ones - once you have
 * finished using resources bunched into a SourceFilter, remember to
 * destroy the sourceFilter so that the GC can get on with removing
 * the sources.  Unless we can do a weak reference?
 * @see ForSourceFilter
 *
 * @author M Hill
 */

public class NotSourceFilter extends ForSourceFilter
{
   /* Constructor - filters OUT all events FROM given source */
    public NotSourceFilter(Object aSource)
   {
      super(aSource);
   }
   
   /* Constructor - filters OUT all events FROM given source set */
   public NotSourceFilter(Vector sourceList)
   {
      super(sourceList);
   }
   
   /**
    * Returns true if the event is NOT from any of the
    * give sources */
   public boolean keepEvent(LogEvent event)
   {
      return (!super.keepEvent(event));
   }
}

