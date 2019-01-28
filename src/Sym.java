public class Sym {

    private String type = "";

    /**
     *
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
     *
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     *
     * @return
     */
    public String toString() {
        return this.type;
    }
}
