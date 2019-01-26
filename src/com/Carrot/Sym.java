package com.Carrot;

import jdk.internal.joptsimple.internal.Strings;

public class Sym {

    private String type = "";

    /**
     * C
     * @param type - String
     */
    public Sym(String type) {
        if (!Strings.isNullOrEmpty(type)) {
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
