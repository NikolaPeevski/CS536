package com.Carrot;
import jdk.internal.joptsimple.internal.Strings;

import java.util.*;

public class SymTable {
    private LinkedList<HashMap<String, Sym>> table = new LinkedList<HashMap<String, Sym>>();

    public SymTable() {
        table = new LinkedList<HashMap<String, Sym>>();
        //Separation for readability
        table.addFirst(new HashMap<String, Sym>());
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
        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }
        // If either idName or sym (or both) is null, throw a WrongArgumentException.
        ArgumentValidator(idName, sym);
        // If the first HashMap in the list already contains the given id name as a key, throw a DuplicateSymException.
        if(table.getFirst().containsKey(idName)) {
            throw new DuplicateSymException();
        }
        // Otherwise, add the given idName and sym to the first HashMap in the list.
        table.getFirst().put(idName, sym);
    }

    public void addScope() {
        //TODO: Implement this
        //Add a new, empty HashMap to the front of the list.
        table.addFirst(new HashMap<String, Sym>());
    }

    public Sym lookupLocal(String idName)
            throws
            EmptySymTableException {
        //TODO: Implement this
        if (Strings.isNullOrEmpty(idName)) {
            return null;
        }
        //If this SymTable's list is empty, throw an EmptySymTableException.
        HashMap<String, Sym> firstTable = table.getFirst();
        if (firstTable.isEmpty()) {
            throw new EmptySymTableException();
        }
        // Otherwise, if the first HashMap in the list contains id name as a key, return the associated Sym; otherwise, return null.
        return firstTable.get(idName);
    }

    public Sym lookupGlobal(String idName)
            throws
            EmptySymTableException {
        //TODO: Implement this
        if (Strings.isNullOrEmpty(idName)) {
            return null;
        }
        //If this SymTable's list is empty, throw an EmptySymTableException.
        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }
        for (HashMap<String, Sym> map : table) {
            if (map.containsKey(idName)) {
                return map.get(idName);
            }
        }
        // If any HashMap in the list contains idName as a key, return the first associated Sym
        // (i.e., the one from the HashMap that is closest to the front of the list); otherwise, return null.
        return null;
    }

    public void removeScope()
            throws
            EmptySymTableException {
        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }
        table.removeFirst();
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

    private void ArgumentValidator(String idName, Sym sym) throws WrongArgumentException {
        boolean validName = !Strings.isNullOrEmpty(idName.trim());
        boolean validSym = (sym == null);

        if (!validName && !validSym) {
            throw new WrongArgumentException(Constants.WRONG_ARGUMENT_EXCEPTION_LIST[2]);
        } else if (!validSym) {
            throw new WrongArgumentException(Constants.WRONG_ARGUMENT_EXCEPTION_LIST[1]);
        } else if (!validName) {
            throw new WrongArgumentException(Constants.WRONG_ARGUMENT_EXCEPTION_LIST[0]);
        }
    }
}
