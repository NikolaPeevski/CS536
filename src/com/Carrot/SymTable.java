package com.Carrot;
import jdk.internal.joptsimple.internal.Strings;

import java.util.*;

public class SymTable {
    private LinkedList<HashMap<String, Sym>> table = new LinkedList<HashMap<String, Sym>>();

    /**
     *
     */
    public SymTable() {
        table = new LinkedList<HashMap<String, Sym>>();
        //Separation for readability
        table.addFirst(new HashMap<String, Sym>());
    }

    /**
     *
     * @param idName
     * @param sym
     * @throws DuplicateSymException
     * @throws EmptySymTableException
     * @throws WrongArgumentException
     */
    public void addDecl(String idName, Sym sym)
            throws
            DuplicateSymException,
            EmptySymTableException,
            WrongArgumentException {

        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }

        ArgumentValidator(idName, sym);

        if(table.getFirst().containsKey(idName)) {
            throw new DuplicateSymException();
        }

        table.getFirst().put(idName, sym);
    }

    /**
     *
     */
    public void addScope() {
        table.addFirst(new HashMap<String, Sym>());
    }

    /**
     *
     * @param idName
     * @return
     * @throws EmptySymTableException
     */
    public Sym lookupLocal(String idName)
            throws
            EmptySymTableException {

        if (Strings.isNullOrEmpty(idName)) {
            return null;
        }

        HashMap<String, Sym> firstTable = table.getFirst();
        if (firstTable.isEmpty()) {
            throw new EmptySymTableException();
        }

        return firstTable.get(idName);
    }

    /**
     *
     * @param idName
     * @return
     * @throws EmptySymTableException
     */
    public Sym lookupGlobal(String idName)
            throws
            EmptySymTableException {

        if (Strings.isNullOrEmpty(idName)) {
            return null;
        }

        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }
        for (HashMap<String, Sym> map : table) {
            if (map.containsKey(idName)) {
                return map.get(idName);
            }
        }

        return null;
    }

    /**
     *
     * @throws EmptySymTableException
     */
    public void removeScope()
            throws
            EmptySymTableException {
        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }
        table.removeFirst();
    }

    /**
     *
     */
    public void print() {
        //TODO: Implement this
        //This method is for debugging.
        //First, print “\n=== Sym Table ===\n”.

        //Then, for each HashMap M in the list, print M.toString() followed by a newline.
        //Finally, print one more newline. All output should go to System.out.
    }

    /**
     *
     * @param idName
     * @param sym
     * @throws WrongArgumentException
     */
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
