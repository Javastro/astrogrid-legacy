/*$Id: BaseBeanTest.java,v 1.1 2004/02/10 17:30:57 nw Exp $
 * Created on 10-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.common.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/** Test that exercises / demonstrates jxpath functionality of BaseBean
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Feb-2004
 *
 */
public class BaseBeanTest extends TestCase {
    /**
     * Constructor for BaseBeanTest.
     * @param arg0
     */
    public BaseBeanTest(String arg0) {
        super(arg0);
    }
    // first need to create mini object model
    protected void setUp() {
        car = new Car();
        car.setMaker("Renault");
        car.setModel("Clio");
        for (int i = 0 ; i < 4; i++) {
            Door d = new Door();
            d.setBackDoor(i > 1);
            d.setChildLock(i > 1);
            car.getDoors().add(d);
        }
        Engine e = new Engine();
        car.setEngine(e);
        e.setCapacity(1.2f);
        e.setFuel("unleaded");
        Cylinder[] arr = new Cylinder[4];
        for (int i = 0; i < 4; i++) {
            Cylinder c = new Cylinder();
            c.setFiringOrder(i+1);
            arr[i] = c;
        }
        e.setCylinders(arr);
        
    }
    protected Car car;
    
    public void testSimple() {
        assertEquals("Renault",car.findXPathValue("maker"));
        assertNull(car.findXPathValue("value")); // :)       
    }
    
    public void testNested() {
        assertEquals("unleaded",car.findXPathValue("engine/fuel"));       
    }
    
    public void testFindObject() {
        Object o = car.findXPathValue("engine/cylinders[firingOrder=1]");
        assertNotNull(o);
        assertTrue(o instanceof Cylinder);
        Cylinder c = (Cylinder)o;
        assertEquals(1,c.getFiringOrder());        
    }
    
    public void testListObjects() {
        Object o = car.findXPathValue("doors");
        assertNotNull(o);
        assertTrue(o instanceof List);
        List l = (List)o;
        assertEquals(car.getDoors().size(),l.size());
       
    }
    
    public void testFindAnotherObject() {
        Object o = car.findXPathValue("doors[childLock=1]");
        assertNotNull(o);
        assertTrue(o instanceof Door);
        Door d = (Door)o;
        assertTrue(d.isChildLock());
    }
    
    public void testFindSomeObjects() {
        Iterator i = car.findXPathIterator("doors[childLock=1]");
        int count = 0;
        for (; i.hasNext(); count++) {
            Object o = i.next();
            assertNotNull(o);
            assertTrue(o instanceof Door);
            Door d = (Door)o;
            assertTrue(d.isChildLock());   
        }
        assertEquals(2,count);
    }

    
    
    
    /** little micky-mouse object model */
    public static class Car extends BaseBean {
        private Engine engine;
        private List doors = new ArrayList();
        private String maker;
        private String model;

        public List getDoors() {
            return doors;
        }

        public Engine getEngine() {
            return engine;
        }

        public String getMaker() {
            return maker;
        }

        public String getModel() {
            return model;
        }

        public void setDoors(List list) {
            doors = list;
        }

        public void setEngine(Engine engine) {
            this.engine = engine;
        }

        public void setMaker(String string) {
            maker = string;
        }

        public void setModel(String string) {
            model = string;
        }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Car:");
        buffer.append(" engine: ");
        buffer.append(engine);
        buffer.append(" doors: ");
        buffer.append(doors);
        buffer.append(" maker: ");
        buffer.append(maker);
        buffer.append(" model: ");
        buffer.append(model);
        buffer.append("]");
        return buffer.toString();
    }
    }
    
    public static class Engine extends BaseBean {
        private float capacity;
        private String fuel;
        private Cylinder[] cylinders;

        public float getCapacity() {
            return capacity;
        }

        public Cylinder[] getCylinders() {
            return cylinders;
        }

        public String getFuel() {
            return fuel;
        }

        public void setCapacity(float f) {
            capacity = f;
        }

        public void setCylinders(Cylinder[] cylinders) {
            this.cylinders = cylinders;
        }

        public void setFuel(String string) {
            fuel = string;
        }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Engine:");
        buffer.append(" capacity: ");
        buffer.append(capacity);
        buffer.append(" fuel: ");
        buffer.append(fuel);
        buffer.append(" { ");
        for (int i0 = 0; cylinders != null && i0 < cylinders.length; i0++) {
            buffer.append(" cylinders[" + i0 + "]: ");
            buffer.append(cylinders[i0]);
        }
        buffer.append(" } ");
        buffer.append("]");
        return buffer.toString();
    }
    }
    
    public static class Cylinder extends BaseBean {
        private int firingOrder;

        public int getFiringOrder() {
            return firingOrder;
        }

        public void setFiringOrder(int i) {
            firingOrder = i;
        }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Cylinder:");
        buffer.append(" firingOrder: ");
        buffer.append(firingOrder);
        buffer.append("]");
        return buffer.toString();
    }
    }
    
    public static class Door extends BaseBean {
        boolean backDoor;
        boolean childLock;

        public boolean isBackDoor() {
            return backDoor;
        }

        public boolean isChildLock() {
            return childLock;
        }

        public void setBackDoor(boolean b) {
            backDoor = b;
        }

        public void setChildLock(boolean b) {
            childLock = b;
        }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Door:");
        buffer.append(" backDoor: ");
        buffer.append(backDoor);
        buffer.append(" childLock: ");
        buffer.append(childLock);
        buffer.append("]");
        return buffer.toString();
    }
    }

}


/* 
$Log: BaseBeanTest.java,v $
Revision 1.1  2004/02/10 17:30:57  nw
added base class for castor-generated object models that provides xpath querying
 
*/