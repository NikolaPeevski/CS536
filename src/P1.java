public class P1 {
    public static void main(String[] args) {
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

        try {
            table.addDecl(potatoSym.getType(), potatoSym);
            table.addDecl(potatoSym.getType(), potatoSym);
            System.out.println("Adding duplicates work!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }

        try {
            table.addDecl(null, potatoSym);
            System.out.println("Adding null idName works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }

        try {
            table.addDecl("", potatoSym);
            System.out.println("Adding empty string idName works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        try {
            table.addDecl(" ", potatoSym);
            System.out.println("Adding whitespace idName works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!", e.getClass().toString(), e.getMessage()));
            e.printStackTrace();
        }
        try {
            table.addDecl("  ", potatoSym);
            System.out.println("Adding double whitespace idName works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        table.print();
        try {
            table.removeScope();
            table.addDecl(carrotSym.getType(), carrotSym);
            System.out.println("Adding while having to Hashmaps works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
        table.addScope();

        try {
            table.removeScope();
            table.removeScope();
            System.out.println("Removing when having no Hashmaps works!");
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }
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

            System.out.println(table.lookupLocal(potatoSym.getType()));
            System.out.println(table.lookupLocal(null));
            System.out.println(table.lookupLocal(""));
            System.out.println(table.lookupLocal(" "));
            System.out.println(table.lookupLocal("   "));
            System.out.println(table.lookupLocal("   \n"));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }

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

            System.out.println(table.lookupGlobal(garlicSym.getType()));
            System.out.println(table.lookupGlobal(null));
            System.out.println(table.lookupGlobal(""));
            System.out.println(table.lookupGlobal(" "));
            System.out.println(table.lookupGlobal("   "));
            System.out.println(table.lookupGlobal("   \n"));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }

        try {
            for(int i = 0; i < 100000; i++) {
                table.removeScope();
            }
            table.print();
            System.out.println(table.lookupGlobal(garlicSym.getType()));
            System.out.println(table.lookupGlobal(null));
            System.out.println(table.lookupGlobal(""));
            System.out.println(table.lookupGlobal(" "));
            System.out.println(table.lookupGlobal("   "));
            System.out.println(table.lookupGlobal("   \n"));
        } catch (Exception e) {
            System.out.println(String.format("%s Exception has %s message!",e.getClass().toString(),e.getMessage()));
            e.printStackTrace();
        }

    }
}
