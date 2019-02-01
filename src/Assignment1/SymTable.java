package Assignment1;

import java.util.HashMap;
import java.util.LinkedList;

public class SymTable {
    private LinkedList<HashMap<String, Sym>> table = new LinkedList<HashMap<String, Sym>>();

    /**
     * Initializes a Symtable with an empty scope
     */
    public SymTable() {
        table = new LinkedList<HashMap<String, Sym>>();
        //Separation for readability
        table.addFirst(new HashMap<String, Sym>());
    }

    /**
     * Adds a declaration to the local scope
     * @param idName valid key, non-null,non-empty, non-whitespace
     * @param sym valid sym with sym type, non-null
     * @throws DuplicateSymException thrown when a key already exists in the local scope
     * @throws EmptySymTableException thrown when the SymTable has no scope
     * @throws WrongArgumentException when either idName or Sym is invalid, Constants present the argument messages.
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

        idName = idName.trim();

        if (idName.isEmpty()) {
            //Do something...
        }

        if(table.getFirst().containsKey(idName)) {
            throw new DuplicateSymException();
        }

        table.getFirst().put(idName, sym);
    }

    /**
     * Adds an empty scope to the first entry of the table
     */
    public void addScope() {
        table.addFirst(new HashMap<String, Sym>());
    }

    /**
     * Searches the local scope for a corresponding key
     * Works in a first comes, first serves manner
     * @param idName valid idName
     * @return null or a Sym corresponding to the idName
     * @throws EmptySymTableException thrown when the SymTable has no scope
     */
    public Sym lookupLocal(String idName)
            throws
            EmptySymTableException {

        if (Utils.isNullOrEmpty(idName)) {
            return null;
        }

        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }
        HashMap<String, Sym> firstTable = table.getFirst();

        return firstTable.get(idName);
    }

    /**
     * Searches the scope for a corresponding key
     * Works in a first comes, first serves manner
     * @param idName valid IdName
     * @return null or corresponding Sym to the idName
     * @throws EmptySymTableException thrown when the SymTable has no scope
     */
    public Sym lookupGlobal(String idName)
            throws
            EmptySymTableException {

        if (Utils.isNullOrEmpty(idName)) {
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
     * Removes the first entry of the collection
     * @throws EmptySymTableException thrown when the collection is empty
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
     * Used for debugging
     * Prints the whole collection
     */
    public void print() {
        System.out.println("\n=== Sym Table ===\n");
        for(HashMap<String, Sym> m : table) {
            System.out.println(String.format("%s \n", m.toString()));
        }
        System.out.println("\n");
    }

    /**
     * Validates idName and Sym and throws appropriate exception if invalid
     * @param idName valid idName
     * @param sym valid Sym
     * @throws WrongArgumentException Look up constants for corresponding exception messages
     */
    private void ArgumentValidator(String idName, Sym sym)
            throws
            WrongArgumentException {
        boolean invalidName = Utils.isNullOrEmpty(idName);
        boolean invalidSym = (sym == null);

        if (invalidName && invalidSym) {
            throw new WrongArgumentException(Constants.WRONG_ARGUMENT_EXCEPTION_LIST[2]);
        } else if (invalidSym) {
            throw new WrongArgumentException(Constants.WRONG_ARGUMENT_EXCEPTION_LIST[1]);
        } else if (invalidName) {
            throw new WrongArgumentException(Constants.WRONG_ARGUMENT_EXCEPTION_LIST[0]);
        }
    }
}