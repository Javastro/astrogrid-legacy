/*
 * $Id: MapDecoratedList.java,v 1.3 2008/09/18 09:13:39 pah Exp $
 * 
 * Created on 20 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * A decorated list that also stores all of its members in a map using the
 * {@link Identify#getId()} as a key. This allows for a simple retrieval of the
 * members using the {@link #get(String)} function. Note that this is the only
 * "map like" functionality that is exposed.
 * 
 * @param <E>
 *            the type to be stored in the list - must implement
 *            {@link Identify}.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 20 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class MapDecoratedList<E extends Identify> implements List<E> {

    protected final List<E> decoratedList;

    protected final Map<String, E> map;

    public MapDecoratedList() {
        decoratedList = new ArrayList<E>();
        map = new HashMap<String, E>();
    }

    public E get(String id) {
        return map.get(id);
    }

    public boolean containsKey(String id) {
        return map.containsKey(id);
    }

    public boolean add(E o) {
        boolean retval = decoratedList.add(o);
        map.put(o.getId(), o);
        return retval;
    }

    public void add(int index, E element) {
        decoratedList.add(index, element);
        map.put(element.getId(), element);
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean retval = decoratedList.addAll(c);

        for (E e : c) {
            map.put(e.getId(), e);
        }

        return retval;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        boolean retval = decoratedList.addAll(index, c);
        for (E e : c) {
            map.put(e.getId(), e);
        }
        return retval;
    }

    public void clear() {
        decoratedList.clear();
        map.clear();
    }

    public boolean contains(Object o) {
        boolean retval = decoratedList.contains(o);
        return retval;
    }

    public boolean containsAll(Collection<?> c) {
        return decoratedList.containsAll(c);
    }

    public E get(int index) {
        return decoratedList.get(index);
    }

    public int indexOf(Object o) {
        return decoratedList.indexOf(o);
    }

    public boolean isEmpty() {
        return decoratedList.isEmpty();
    }

    public Iterator<E> iterator() {
        return decoratedList.iterator();
    }

    public int lastIndexOf(Object o) {
        return decoratedList.lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return decoratedList.listIterator();
    }

    public ListIterator<E> listIterator(int index) {
        return decoratedList.listIterator(index);
    }

    public boolean remove(Object o) {
        boolean retval = decoratedList.remove(o);
        map.remove(((Identify) o).getId());
        return retval;
    }

    public E remove(int index) {
        E retval = decoratedList.remove(index);
        map.remove(retval.getId());
        return retval;
    }

    public boolean removeAll(Collection<?> c) {
        // TODO not sure if I can implement efficiently - need to look at he
        throw new UnsupportedOperationException(
                "MapDecoratedList.removeAll() not implemented");
    }

    public boolean retainAll(Collection<?> c) {
        // TODO not sure if I can implement efficiently
        throw new UnsupportedOperationException(
                "MapDecoratedList.retainAll() not implemented");
    }

    public E set(int index, E element) {
        E retval = decoratedList.set(index, element);
        map.put(retval.getId(), retval);
        return retval;
    }

    public int size() {
        return decoratedList.size();
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return decoratedList.subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return decoratedList.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return decoratedList.toArray(a);
    }

}

/*
 * $Log: MapDecoratedList.java,v $
 * Revision 1.3  2008/09/18 09:13:39  pah
 * improved javadoc
 * Revision 1.2 2008/09/03 14:18:43 pah result
 * of merge of pah_cea_1611 branch
 * 
 * Revision 1.1.2.1 2008/03/26 17:15:38 pah Unit tests pass
 */
