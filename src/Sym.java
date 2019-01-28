public class Sym {

    private String type = "";

    /**
     * Initializes Sym with a non-null, non-empty type
     * @param type - String
     */
    public Sym(String type) {
        if (!Utils.isNullOrEmpty(type)) {
            type = type.trim();

            if (type.isEmpty()) {
                // Do something.. I guess..
            }

            this.type = type;
        }
    }

    /**
     *  Returns the type of Sym
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Currently returns the type of Sym
     * @return
     */
    public String toString() {
        return this.type;
    }
}
