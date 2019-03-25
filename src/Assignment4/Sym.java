import java.util.*;
//TODO:LINK TO DEFINITION
public class Sym {
    private String type;
    private String origin = "NON-FUNCTION";
    private String parent = "";
    private LinkedList<String> params = new LinkedList<String>();
    
    public Sym(String type) {
        this.type = type;
        this.params = new LinkedList<String>();

        System.out.println(toString());
    }
    public Sym(String type, LinkedList<String> params) {
        this.type = type;
        this.params = params;
        this.origin = "FUNCTION";
    }
    public Sym(String type, String parent) {
        this.type = type;
        this.parent = parent;

        System.out.println(toString());
    }
    
    public String getType() {
        return type;
    }

    public String getParent() {
        return parent;
    }

    public String getOrigin() {
        return origin;
    }

    public String toString() {
        return type;
    }
    //TODO: Might need to rename this,
    // long term toString means something else derp.
    public String complexToString() {
        if (params.size() == 0)
            return origin != "FUNCTION" ? type : "-> " + type;

        String out = "";

        Iterator iterator = params.iterator();
        //Casting, necessary evil
        out = (String)iterator.next();

        while (iterator.hasNext()) {
            //Casting, necessary evil
            out += String.format(", %s", (String)iterator.next());
        }

        return String.format("%s -> %s", out, type);
    }
}
