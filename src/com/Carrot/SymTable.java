package com.Carrot;

public class SymTable {
    public SymTable() {
        //TODO: Implement this
        //This is the constructor; it should initialize the SymTable's List field to contain a single, empty HashMap.
    }

    public void addDecl(String idName, Sym sym)
            throws
            DuplicateSymException,
            EmptySymTableException,
            WrongArgumentException {
        //TODO: Implement this
        //If this SymTable's list is empty, throw an EmptySymTableException.
        // If either idName or sym (or both) is null, throw a WrongArgumentException.
        // If the first HashMap in the list already contains the given id name as a key, throw a DuplicateSymException.
        // Otherwise, add the given idName and sym to the first HashMap in the list.
    }

    public void addScope() {
        //TODO: Implement this
        //Add a new, empty HashMap to the front of the list.
    }

    public Sym lookupLocal(String idName)
            throws
            EmptySymTableException {
        //TODO: Implement this
        //If this SymTable's list is empty, throw an EmptySymTableException.
        // Otherwise, if the first HashMap in the list contains id name as a key, return the associated Sym; otherwise, return null.

        return new Sym("Unimplemented");
    }

    public Sym lookupGlobal(String idName)
            throws
            EmptySymTableException {
        //TODO: Implement this
        //If this SymTable's list is empty, throw an EmptySymTableException.
        // If any HashMap in the list contains idName as a key, return the first associated Sym
        // (i.e., the one from the HashMap that is closest to the front of the list); otherwise, return null.
        return new Sym("Unimplemented");
    }

    public void removeScope()
            throws
            EmptySymTableException {
        //TODO: Implement this
        //If this SymTable's list is empty, throw an EmptySymTableException; otherwise, remove the HashMap from the front of the list.
        //To clarify, throw an exception only if before attempting to remove, the list is empty (i.e. there are no HashMaps to remove).
    }

    public void print() {
        //TODO: Implement this
        //This method is for debugging.
        //First, print “\n=== Sym Table ===\n”.
        //Then, for each HashMap M in the list, print M.toString() followed by a newline.
        //Finally, print one more newline. All output should go to System.out.
    }
}
