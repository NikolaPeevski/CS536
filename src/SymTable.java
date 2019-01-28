import java.util.HashMap;
import java.util.LinkedList;

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
     * Adds a declaration to the first table
     * @param idName valid key
     * @param sym valid sym with sym type
     * @throws DuplicateSymException thrown when a key already exists in the first hashmap
     * @throws EmptySymTableException thrown when there is no hashmap present
     * @throws WrongArgumentException when either idName or Sym is invalid
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
     * Adds an empty hashmap to the first entry
     */
    public void addScope() {
        table.addFirst(new HashMap<String, Sym>());
    }

    /**
     * Searches the first entry of the list
     * @param idName valid idName
     * @return null or a Sym corresponding to the idName
     * @throws EmptySymTableException
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
     * Searches the whole collection for a corresponding key
     * @param idName valid IdName
     * @return null or corresponding Sym to the idName
     * @throws EmptySymTableException
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
     *
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
     * @throws WrongArgumentException Look up constants for corresponding exceptions
     */
    private void ArgumentValidator(String idName, Sym sym) throws WrongArgumentException {
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