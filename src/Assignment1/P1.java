package Assignment1;

public class P1 {
    public static void main(String[] args) {
        /**
         * Test cases for:
         * Sym types (null,empty, whitespace, double whitespace)
         * Table functions (Adding,Removing,Looking up, Adding to empty,
         * Adding duplicate, Removing on empty
         */
        // Sym tests
        Sym nullSym = new Sym(null);
        Sym emptySym = new Sym("");
        Sym spaceSym = new Sym(" ");
        Sym twoSpaceSym = new Sym("  ");
        Sym trailSpaceSym = new Sym("                                 ");
        Sym newLineSym = new Sym("\n");

        //Dummy data
        Sym potatoSym = new Sym("Potato");
        Sym carrotSym = new Sym("Carrot");
        Sym tomatoSym = new Sym("Tomato");
        Sym cucumberSym = new Sym("Cucumber");
        Sym lettuceSym = new Sym("Lettuce");
        Sym onionSym = new Sym("Onion");
        Sym garlicSym = new Sym("Garlic");

        SymTable table = new SymTable();
        try {
            //Null
            System.out.println(nullSym.getType());
            System.out.println(nullSym.toString());

            //Empty
            System.out.println(emptySym.getType());
            System.out.println(emptySym.toString());

            //Space
            System.out.println(spaceSym.getType());
            System.out.println(spaceSym.toString());

            //TwoSpace
            System.out.println(twoSpaceSym.getType());
            System.out.println(twoSpaceSym.toString());

            //TrailSpaceSpace
            System.out.println(trailSpaceSym.getType());
            System.out.println(trailSpaceSym.toString());

            //NewLine
            System.out.println(newLineSym.getType());
            System.out.println(newLineSym.toString());

        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {

            System.out.println("Adding duplicates..");
            table.addDecl(potatoSym.getType(), potatoSym);
            table.addDecl(potatoSym.getType(), potatoSym);
            System.out.println("Adding duplicates works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Adding null idName..");
            table.addDecl(null, potatoSym);
            System.out.println("Adding null idName works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Adding empty string idName..");
            table.addDecl("", potatoSym);
            System.out.println("Adding empty string idName works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Adding whitespace idName..");
            table.addDecl(" ", potatoSym);
            System.out.println("Adding whitespace idName works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!", e.getClass().toString(), e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Adding double whitespace idName..");
            table.addDecl("  ", potatoSym);
            System.out.println("Adding double whitespace idName works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        table.print();
        System.out.println();
        try {
            System.out.println("Adding while having to Hashmaps..");
            table.removeScope();
            table.addDecl(carrotSym.getType(), carrotSym);
            System.out.println("Adding while having to Hashmaps works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        table.print();
        table.addScope();
        try {
            System.out.println("Removing when having no Hashmaps..");
            table.removeScope();
            table.removeScope();
            System.out.println("Removing when having no Hashmaps works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        table.addScope();
        try {
            table.addDecl(potatoSym.getType(), potatoSym);
            table.addDecl(carrotSym.getType(), carrotSym);
            table.addDecl(tomatoSym.getType(), tomatoSym);
            table.addDecl(cucumberSym.getType(), cucumberSym);
            table.addDecl(lettuceSym.getType(), lettuceSym);
            table.addDecl(onionSym.getType(), onionSym);
            table.addDecl(garlicSym.getType(), garlicSym);
            table.print();
            System.out.println(String.format("Looking up locally %s idName...", potatoSym.getType()));
            System.out.println(table.lookupLocal(potatoSym.getType()));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Looking up locally null idName...");
            System.out.println(table.lookupLocal(null));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Looking up locally empty idName...");
            System.out.println(table.lookupLocal(""));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Looking up locally whitespace idName...");
            System.out.println(table.lookupLocal(" "));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Looking up locally double whitespace idName...");
            System.out.println(table.lookupLocal("  "));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            for(int i = 0; i < 100000; i++) {
                table.addScope();
                table.addDecl(potatoSym.getType(), potatoSym);
                table.addDecl(carrotSym.getType(), carrotSym);
                table.addDecl(tomatoSym.getType(), tomatoSym);
                table.addDecl(cucumberSym.getType(), cucumberSym);
                table.addDecl(lettuceSym.getType(), lettuceSym);
                table.addDecl(onionSym.getType(), onionSym);
            }

            System.out.println(String.format("Looking up globally %s idName...", garlicSym.getType()));
            System.out.println(table.lookupGlobal(garlicSym.getType()));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Looking up globally null idName...");
            System.out.println(table.lookupGlobal(null));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Looking up globally empty idName...");
            System.out.println(table.lookupGlobal(""));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Looking up globally whitespace idName...");
            System.out.println(table.lookupGlobal(" "));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Looking up globally double whitespace idName...");
            System.out.println(table.lookupGlobal("  "));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }


        try {
            for(int i = 0; i < 100000; i++) {
                table.removeScope();
            }
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {
            System.out.println("Looking up in no tables locally..");
            table.removeScope();
            table.lookupLocal("DummyText");
            System.out.println("Looking up in no tables locally works!");

        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        System.out.println();
        try {

            System.out.println("Looking up in no tables globally..");
            table.lookupGlobal("DummyText");
            System.out.println("Looking up in no tables globally works!");

        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }


    }
}
