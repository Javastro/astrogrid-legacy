package net.mchill.log;

import java.util.Vector;

/**
 * ForSourceFilter implements LogFilter, and is used to filter events by
 * those that match (are FOR) the given sources - ie, only messages that
 * match the source list will be kept.
 * <P>
 * Further (valid) sources can be added using addSource()
 * <P>
 * Some messages will not have sources given - to allow these to pass
 * as well, add a (null) source using addSource(null)
 * <P>
 * NB - watch out for circular references/tied ones - once you have
 * finished using resources bunched into a SourceFilter, remember to
 * destroy the sourceFilter so that the GC can get on with removing
 * the sources.  Unless we can do a weak reference?
 *
 * @author M Hill
 */

public class ForSourceFilter implements LogFilter
{
   private Vector sources = null;
   
   public ForSourceFilter(Object aSource)
   {
      sources = new Vector(1);
      sources.add(aSource);
   }
   
   public ForSourceFilter(Vector sourceList)
   {
      sources = sourceList;
   }

   public void addSource(Object aSource)
   {
      sources.add(aSource);
   }
   
   public void addSource(Vector sourceList)
   {
      sources.add(sourceList);
   }

   public void setSource(Vector sourceList)
   {
      sources = sourceList;
   }

   /**
    * Returns true if the given event has a source matching
    * one of the sources added to this filter.  To include
    * all non-sourced events (ie events with a null source)
    * use addSource(null) on this filter
    */
   public boolean keepEvent(LogEvent event)
   {
      Object[] listCopy = sources.toArray();
      for (int i = 0; i<listCopy.length; i++)
      {
         if (event.getSource() == listCopy[i])
            return true;
      }
      return false;
   }
}

