package com.Carrot;

public class Sym {
    private String type = "";

    public Sym(String type) {
        if (type != null) {
            this.type = type;
        }
        //TODO: Implement this
        // This is the constructor; it should initialize the Sym to have the given type.
    }

    public String getType() {
        //TODO: Implement this
        //Return this Sym's type.
        return this.type;
    }

    public String toString() {
        //TODO: Implement this
        //Return this Sym's type. (This method will be changed later in a future project when more information is stored in a Sym.)
        return this.type;
    }
}
