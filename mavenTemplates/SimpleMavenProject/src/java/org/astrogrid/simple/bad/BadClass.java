package org.astrogrid.simple.bad;
//these classes break every rule in the (green book)
public class BadClass { //missing javadoc
    //java docs errors rule 39
    private int noJavaDocPrivate;
    int noJavaDocDefault;
    protected int noJavaDocProtected;
    public int noJavaDocPublic;
    
    private void methodNoJavaDocPrivate(){}
    void methodNoJavaDocDefault(){}
    protected void methodNoJavaDocProtected(){}
    public void methodNoJavaDocPublic(){}
    
    /**
     * This method has incomplete javadoc
     * @param arg1
     * @return
     * @throws Exception
     */
    public int methodIncompleteJavaDoc(int arg1) throws Exception {
        return 0;
    }
    	//this line contains a tab
    
}

