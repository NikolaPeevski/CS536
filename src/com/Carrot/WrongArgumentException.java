package com.Carrot;

public class WrongArgumentException extends Exception {
    public WrongArgumentException(String message) {
        // Here, you will call Exception's constructor with message argument.

        //Implementation of addDecl(String idName, Sym sym) will throw WrongArgumentException with following messages:
        //
        //If idName is null, the message will be "Id name is null."
        //If sym is null, the message will be "Sym is null."
        //If both are null, the message will be "Id name and sym are null."
        new Exception(message);
    }
}
