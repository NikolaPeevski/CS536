import java.io.*;
import java.util.*;

// **********************************************************************
// The ErrorWriter class defines the error checking for the name analysis of the abstract-syntax tree that
// represents a Carrot program.
// **********************************************************************
class ErrorWriter {
    private boolean hasError = false;

    public void duplicate(IdNode id, TypeNode type) {
        ErrMsg.fatal(id.myLineNum, id.myCharNum, "Multiply declared identifier");
        setError();
    }
    public void undeclared(IdNode id) {
        ErrMsg.fatal(id.myLineNum, id.myCharNum, "Undeclared identifier");
        setError();
    }
    public void nonStructAccess(IdNode id) {
        ErrMsg.fatal(id.myLineNum, id.myCharNum, "Dot-access of non-struct type");
        setError();
    }
    public void invalidStructField(IdNode id) {
        ErrMsg.fatal(id.myLineNum, id.myCharNum, "Invalid struct field name");
        setError();
    }
    public void nonFuncVoided(IdNode id) {
        ErrMsg.fatal(id.myLineNum, id.myCharNum, "Non-function declared void");
        setError();
    }
    public void invalidStructType(IdNode id) {
        ErrMsg.fatal(id.myLineNum, id.myCharNum, "Invalid name of struct type");
        setError();
    }

    private void setError() {
        hasError = true;
    }

    public boolean checkError() {
        return hasError;
    }
}

// **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a Carrot program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a list (for nodes that may have a variable number of 
// children) or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have.  All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
//     Subclass            Kids
//     --------            ----
//     ProgramNode         DeclListNode
//     DeclListNode        linked list of DeclNode
//     DeclNode:
//       VarDeclNode       TypeNode, IdNode, int
//       FnDeclNode        TypeNode, IdNode, FormalsListNode, FnBodyNode
//       FormalDeclNode    TypeNode, IdNode
//       StructDeclNode    IdNode, DeclListNode
//
//     FormalsListNode     linked list of FormalDeclNode
//     FnBodyNode          DeclListNode, StmtListNode
//     StmtListNode        linked list of StmtNode
//     ExpListNode         linked list of ExpNode
//
//     TypeNode:
//       IntNode           -- none --
//       BoolNode          -- none --
//       VoidNode          -- none --
//       StructNode        IdNode
//
//     StmtNode:
//       AssignStmtNode      AssignNode
//       PostIncStmtNode     ExpNode
//       PostDecStmtNode     ExpNode
//       ReadStmtNode        ExpNode
//       WriteStmtNode       ExpNode
//       IfStmtNode          ExpNode, DeclListNode, StmtListNode
//       IfElseStmtNode      ExpNode, DeclListNode, StmtListNode,
//                                    DeclListNode, StmtListNode
//       WhileStmtNode       ExpNode, DeclListNode, StmtListNode
//       RepeatStmtNode      ExpNode, DeclListNode, StmtListNode
//       CallStmtNode        CallExpNode
//       ReturnStmtNode      ExpNode
//
//     ExpNode:
//       IntLitNode          -- none --
//       StrLitNode          -- none --
//       TrueNode            -- none --
//       FalseNode           -- none --
//       IdNode              -- none --
//       DotAccessNode       ExpNode, IdNode
//       AssignNode          ExpNode, ExpNode
//       CallExpNode         IdNode, ExpListNode
//       UnaryExpNode        ExpNode
//         UnaryMinusNode
//         NotNode
//       BinaryExpNode       ExpNode ExpNode
//         PlusNode     
//         MinusNode
//         TimesNode
//         DivideNode
//         AndNode
//         OrNode
//         EqualsNode
//         NotEqualsNode
//         LessNode
//         GreaterNode
//         LessEqNode
//         GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with linked lists of kids, or
// internal nodes with a fixed number of kids:
//
// (1) Leaf nodes:
//        IntNode,   BoolNode,  VoidNode,  IntLitNode,  StrLitNode,
//        TrueNode,  FalseNode, IdNode
//
// (2) Internal nodes with (possibly empty) linked lists of children:
//        DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of kids:
//        ProgramNode,     VarDeclNode,     FnDeclNode,     FormalDeclNode,
//        StructDeclNode,  FnBodyNode,      StructNode,     AssignStmtNode,
//        PostIncStmtNode, PostDecStmtNode, ReadStmtNode,   WriteStmtNode   
//        IfStmtNode,      IfElseStmtNode,  WhileStmtNode,  RepeatStmtNode,
//        CallStmtNode
//        ReturnStmtNode,  DotAccessNode,   AssignExpNode,  CallExpNode,
//        UnaryExpNode,    BinaryExpNode,   UnaryMinusNode, NotNode,
//        PlusNode,        MinusNode,       TimesNode,      DivideNode,
//        AndNode,         OrNode,          EqualsNode,     NotEqualsNode,
//        LessNode,        GreaterNode,     LessEqNode,     GreaterEqNode
//
// **********************************************************************

// **********************************************************************
// %%%ASTnode class (base class for all other kinds of nodes)
// **********************************************************************

abstract class ASTnode { 
    // every subclass must provide an unparse operation
    abstract public void unparse(PrintWriter p, int indent);
    // this method can be used by the unparse methods to do indenting
    protected void addIndent(PrintWriter p, int indent) {
        for (int k = 0; k < indent; k++) p.print(" ");
    }
}

// **********************************************************************
// ProgramNode,  DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode
// **********************************************************************

class ProgramNode extends ASTnode {
    public ProgramNode(DeclListNode L) {
        myDeclList = L;
        scopeTable = new SymTable();
        errorWriter = new ErrorWriter();
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
    }

    public boolean analyze() {
        myDeclList.analyze(errorWriter, scopeTable);

        return errorWriter.checkError();
    }
    // 1 kid
    private DeclListNode myDeclList;
    private SymTable scopeTable;
    private ErrorWriter errorWriter;
}

class DeclListNode extends ASTnode {
    public DeclListNode(List<DeclNode> S) {
        myDecls = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator it = myDecls.iterator();
        try {
            while (it.hasNext()) {
                ((DeclNode)it.next()).unparse(p, indent);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in DeclListNode.unparse");
            System.exit(-1);
        }
    }
    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        Iterator it = myDecls.iterator();
        try {

            while (it.hasNext()) {
                DeclNode curr = ((DeclNode)it.next());
                if (parent != "") {
                    curr.parent = parent;
                }
                scopeTable = curr.analyze(p, scopeTable);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in DeclListNode.analyze");
            System.exit(-1);
        }
        return scopeTable;
    }
    // list of kids (DeclNodes)
    private List<DeclNode> myDecls;
    public String parent = "";
}

class FormalsListNode extends ASTnode {
    public FormalsListNode(List<FormalDeclNode> S) {
        myFormals = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<FormalDeclNode> it = myFormals.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        } 
    }

    public LinkedList<String> listify() {
        LinkedList<String> res = new LinkedList<String>();
        Iterator<FormalDeclNode> it = myFormals.iterator();
        while (it.hasNext()) {
            res.add(it.next().getType());
        }
        return res;
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        Iterator it = myFormals.iterator();
        try {
            while (it.hasNext()) {
                scopeTable = ((FormalDeclNode)it.next()).analyze(p, scopeTable);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in FormalsListNode.analyze");
            System.exit(-1);
        }
        return scopeTable;
    }

    // list of kids (FormalDeclNodes)
    private List<FormalDeclNode> myFormals;
}

class FnBodyNode extends ASTnode {
    public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
        myDeclList = declList;
        myStmtList = stmtList;
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
        myStmtList.unparse(p, indent);
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        scopeTable = myDeclList.analyze(p, scopeTable);
        scopeTable = myStmtList.analyze(p, scopeTable);

        return scopeTable;
    }

    // 2 kids
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class StmtListNode extends ASTnode {
    public StmtListNode(List<StmtNode> S) {
        myStmts = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<StmtNode> it = myStmts.iterator();
        while (it.hasNext()) {
            it.next().unparse(p, indent);
        }
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        Iterator it = myStmts.iterator();
        try {
            while (it.hasNext()) {
                scopeTable = ((StmtNode)it.next()).analyze(p, scopeTable);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in StmtListNode.analyze");
            System.exit(-1);
        }
        return scopeTable;
    }

    // list of kids (StmtNodes)
    private List<StmtNode> myStmts;
}

class ExpListNode extends ASTnode {
    public ExpListNode(List<ExpNode> S) {
        myExps = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<ExpNode> it = myExps.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        } 
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        Iterator it = myExps.iterator();
        try {
            while (it.hasNext()) {
                scopeTable = ((ExpNode)it.next()).analyze(p, scopeTable);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in ExpListNode.analyze");
            System.exit(-1);
        }
        return scopeTable;
    }

    // list of kids (ExpNodes)
    private List<ExpNode> myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************

abstract class DeclNode extends ASTnode {
    abstract public SymTable analyze(ErrorWriter p, SymTable scopeTable);

    protected String parent = "";
}

class VarDeclNode extends DeclNode {
    public VarDeclNode(TypeNode type, IdNode id, int size) {
        myType = type;
        myId = id;
        mySize = size;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.println(";");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        if (myType.getClass().equals(VoidNode.class)) {
            p.nonFuncVoided(myId);
        }
        if (mySize == 0 && scopeTable.lookupGlobal(myType.toString()) == null) {
            p.invalidStructType(myId);
        }
        try {

            if (parent != "")
                scopeTable.addDecl(String.format("%s %s",parent, myId.myStrVal), new Sym(myType.toString(), parent));
            else
                scopeTable.addDecl(myId.myStrVal, new Sym(myType.toString()));
        } catch (DuplicateSymException e) {
            p.duplicate(myId, myType);
        } finally {
            return scopeTable;
        }
    }

    // 3 kids
    private TypeNode myType;
    private IdNode myId;
    private int mySize;  // use value NOT_STRUCT if this is not a struct type

    public static int NOT_STRUCT = -1;
}

class FnDeclNode extends DeclNode {
    public FnDeclNode(TypeNode type,
                      IdNode id,
                      FormalsListNode formalList,
                      FnBodyNode body) {
        myType = type;
        myId = id;
        myFormalsList = formalList;
        myBody = body;
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        try {
            scopeTable.addDecl(myId.myStrVal, new Sym(myType.toString(), myFormalsList.listify()));
        } catch (DuplicateSymException e) {
            p.duplicate(myId, myType);
        } finally {
            //Enter function
            scopeTable.addScope();
            //Parse formals as variables
            scopeTable = myFormalsList.analyze(p, scopeTable);
            //Parse body
            scopeTable = myBody.analyze(p, scopeTable);
            //Exit function, we don't care about it anymore
            try {
                scopeTable.removeScope();
            } catch (EmptySymTableException e) {
                System.out.println("Something went terribly wrong in a FUNCTION DECLARATION statement.");
                System.exit(0);
            }
            return scopeTable;
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.print("(");
        myFormalsList.unparse(p, 0);
        p.println(") {");
        myBody.unparse(p, indent+4);
        p.println("}\n");
    }

    // 4 kids
    private TypeNode myType;
    private IdNode myId;
    private FormalsListNode myFormalsList;
    private FnBodyNode myBody;
}

class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {

        try {
            scopeTable.addDecl(myId.myStrVal, new Sym(myType.toString()));
        } catch (DuplicateSymException e) {
            p.duplicate(myId, myType);
        } finally {
            return scopeTable;
        }
    }
    public String getType() {
        return myType.toString();
    }


    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}

class StructDeclNode extends DeclNode {
    public StructDeclNode(IdNode id, DeclListNode declList) {
        myId = id;
        myDeclList = declList;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("struct ");
        myId.unparse(p, 0);
        p.println("{");
        myDeclList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("};\n");

    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        myDeclList.parent = myId.myStrVal;
        try {
            scopeTable.addDecl(myId.myStrVal, new Sym("Struct"));
        } catch (DuplicateSymException e) {
            p.duplicate(myId, new StructNode(myId));
        } finally {
            scopeTable = myDeclList.analyze(p, scopeTable);
            return scopeTable;

        }
    }

    // 2 kids
    private IdNode myId;
    private DeclListNode myDeclList;
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************

abstract class TypeNode extends ASTnode {
}

class IntNode extends TypeNode {

    private static final String TYPE = "int";

    public IntNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(TYPE);
    }

    public String toString() {
        return TYPE;
    }
}

class BoolNode extends TypeNode {

    private static final String TYPE = "bool";

    public BoolNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(TYPE);
    }

    public String toString() {
        return TYPE;
    }
}

class VoidNode extends TypeNode {

    private static final String TYPE = "void";

    public VoidNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(TYPE);
    }

    public String toString() {
        return TYPE;
    }
}

class StructNode extends TypeNode {
    public StructNode(IdNode id) {
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("struct ");
        myId.unparse(p, 0);
    }

    public void analyze(ErrorWriter p, SymTable scopeTable) {
        System.out.println("Test");
    }

    public String toString() {
        return myId.myStrVal;
    }
    
    // 1 kid
    private IdNode myId;
}

// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************

abstract class StmtNode extends ASTnode {

    abstract public SymTable analyze(ErrorWriter p, SymTable scopeTable);
}

class AssignStmtNode extends StmtNode {
    public AssignStmtNode(AssignNode assign) {
        myAssign = assign;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myAssign.unparse(p, -1); // no parentheses
        p.println(";");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
//        scopeTable.print();print
        return myAssign.analyze(p, scopeTable);
    }

    // 1 kid
    private AssignNode myAssign;
}

class PostIncStmtNode extends StmtNode {
    public PostIncStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myExp.unparse(p, 0);
        p.println("++;");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        return myExp.analyze(p, scopeTable);
    }

    // 1 kid
    private ExpNode myExp;
}

class PostDecStmtNode extends StmtNode {
    public PostDecStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myExp.unparse(p, 0);
        p.println("--;");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        return myExp.analyze(p, scopeTable);
    }

    // 1 kid
    private ExpNode myExp;
}

class ReadStmtNode extends StmtNode {
    public ReadStmtNode(ExpNode e) {
        myExp = e;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("cin >> ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        return myExp.analyze(p, scopeTable);
    }

    // 1 kid (actually can only be an IdNode or an ArrayExpNode)
    private ExpNode myExp;
}

class WriteStmtNode extends StmtNode {
    public WriteStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("cout << ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        return myExp.analyze(p, scopeTable);
    }

    // 1 kid
    private ExpNode myExp;
}

class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myDeclList = dlist;
        myExp = exp;
        myStmtList = slist;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        //Validate expression
        scopeTable = myExp.analyze(p, scopeTable);
        //Enter if scope
        scopeTable.addScope();
        scopeTable = myDeclList.analyze(p, scopeTable);
        scopeTable = myStmtList.analyze(p, scopeTable);

        //Exit if scope
        try {
            scopeTable.removeScope();
        } catch (EmptySymTableException e) {
            System.out.println("Something went terribly wrong in an IF statement.");
            System.exit(0);
        }

        return scopeTable;
    }

    // e kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class IfElseStmtNode extends StmtNode {
    public IfElseStmtNode(ExpNode exp, DeclListNode dlist1,
                          StmtListNode slist1, DeclListNode dlist2,
                          StmtListNode slist2) {
        myExp = exp;
        myThenDeclList = dlist1;
        myThenStmtList = slist1;
        myElseDeclList = dlist2;
        myElseStmtList = slist2;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myThenDeclList.unparse(p, indent+4);
        myThenStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
        addIndent(p, indent);
        p.println("else {");
        myElseDeclList.unparse(p, indent+4);
        myElseStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");        
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        //Validate expression
        scopeTable = myExp.analyze(p, scopeTable);
        //Enter if scope
        scopeTable.addScope();
        scopeTable = myThenDeclList.analyze(p, scopeTable);
        scopeTable = myThenStmtList.analyze(p, scopeTable);

        //Exit if scope
        try {
            scopeTable.removeScope();
        } catch (EmptySymTableException e) {
            System.out.println("Something went terribly wrong in an IF ELSE statement.");
            System.exit(0);
        }

        //Enter else scope
        scopeTable.addScope();
        scopeTable = myElseDeclList.analyze(p, scopeTable);
        scopeTable = myElseStmtList.analyze(p, scopeTable);

        //Exit else scope
        try {
            scopeTable.removeScope();
        } catch (EmptySymTableException e) {
            System.out.println("Something went terribly wrong in an IF ELSE statement.");
            System.exit(0);
        }

        return scopeTable;
    }

    // 5 kids
    private ExpNode myExp;
    private DeclListNode myThenDeclList;
    private StmtListNode myThenStmtList;
    private StmtListNode myElseStmtList;
    private DeclListNode myElseDeclList;
}

class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }
    
    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("while (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        //Validate expression
        scopeTable = myExp.analyze(p, scopeTable);
        //Enter while scope
         scopeTable.addScope();
        scopeTable = myDeclList.analyze(p, scopeTable);
        scopeTable = myStmtList.analyze(p, scopeTable);

        //Exit if scope
        try {
            scopeTable.removeScope();
        } catch (EmptySymTableException e) {
            System.out.println("Something went terribly wrong in a WHILE statement.");
            System.exit(0);
        }

        return scopeTable;
    }

    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class RepeatStmtNode extends StmtNode {
    public RepeatStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }
	
    public void unparse(PrintWriter p, int indent) {
	addIndent(p, indent);
        p.print("repeat (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        //Validate expression
        scopeTable = myExp.analyze(p, scopeTable);
        //Enter while scope
        scopeTable.addScope();
        scopeTable = myDeclList.analyze(p, scopeTable);
        scopeTable = myStmtList.analyze(p, scopeTable);

        //Exit if scope
        try {
            scopeTable.removeScope();
        } catch (EmptySymTableException e) {
            System.out.println("Something went terribly wrong in a REPEAT statement.");
            System.exit(0);
        }

        return scopeTable;
    }

    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class CallStmtNode extends StmtNode {
    public CallStmtNode(CallExpNode call) {
        myCall = call;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myCall.unparse(p, indent);
        p.println(";");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        scopeTable = myCall.analyze(p, scopeTable);

        return scopeTable;
    }

    // 1 kid
    private CallExpNode myCall;
}

class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("return");
        if (myExp != null) {
            p.print(" ");
            myExp.unparse(p, 0);
        }
        p.println(";");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        scopeTable = myExp.analyze(p, scopeTable);

        return scopeTable;
    }

    // 1 kid
    private ExpNode myExp; // possibly null
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************

abstract class ExpNode extends ASTnode {

    abstract SymTable analyze(ErrorWriter p, SymTable scopeTable);
}

class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int charNum, int intVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myIntVal = intVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myIntVal);
    }


    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        return scopeTable;
    }

    private int myLineNum;
    private int myCharNum;
    private int myIntVal;
}

class StringLitNode extends ExpNode {
    public StringLitNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        return scopeTable;
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class TrueNode extends ExpNode {
    public TrueNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("true");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        return scopeTable;
    }

    private int myLineNum;
    private int myCharNum;
}

class FalseNode extends ExpNode {
    public FalseNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("false");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        return scopeTable;
    }

    private int myLineNum;
    private int myCharNum;
}

class IdNode extends ExpNode {
    public IdNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void unparse(PrintWriter p, int indent) {
        if (link != null) {
            p.print(myStrVal);
            p.print("(");
            p.print(link.getOrigin() == "FUNCTION" ? link.complexToString() : link.getType());
            p.print(")");
        } else
            p.print(myStrVal);
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        //TODO: MAke sure this is never called although ...
//        System.out.println("Id node analysis is not implemented." + " " + myStrVal);
//        System.out.println(myStrVal);

        if (link == null) {
            Sym s = scopeTable.lookupGlobal(myStrVal);
            if (s == null)
                p.undeclared(this);
            else
                link = s;
        }

        return scopeTable;
    }

    public int myLineNum;
    public int myCharNum;
    public String myStrVal;
    public Sym link;
}

class DotAccessExpNode extends ExpNode {
    public DotAccessExpNode(ExpNode loc, IdNode id) {
        myLoc = loc;    
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        myLoc.unparse(p, 0);
        p.print(".");
        myId.unparse(p, 0);
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        //TODO: Check if LHS (myId) has been declared
        Sym loc = scopeTable.lookupGlobal(((IdNode)myLoc).myStrVal);
        if (loc == null || scopeTable.lookupGlobal(loc.getType()) == null) {
            p.nonStructAccess((IdNode) myLoc);
        }

        Sym locVar = scopeTable.lookupGlobal(loc.getType() + " " + myId.myStrVal);

        if (locVar == null) {
            p.invalidStructField(myId);
        }

        myId.link = locVar;
        scopeTable = myLoc.analyze(p, scopeTable);
        scopeTable = myId.analyze(p, scopeTable);
        return scopeTable;
    }

    // 2 kids
    private ExpNode myLoc;    
    private IdNode myId;
}

class AssignNode extends ExpNode {
    public AssignNode(ExpNode lhs, ExpNode exp) {
        myLhs = lhs;
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        if (indent != -1)  p.print("(");
        myLhs.unparse(p, 0);
        p.print(" = ");
        myExp.unparse(p, 0);
        if (indent != -1)  p.print(")");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        //TODO: Check lhs if it has been declared
        //TODO: Check rhs based on the error table

        scopeTable = myLhs.analyze(p, scopeTable);
        scopeTable = myExp.analyze(p, scopeTable);

        return scopeTable;
    }

    // 2 kids
    private ExpNode myLhs;
    private ExpNode myExp;
}

class CallExpNode extends ExpNode {
    public CallExpNode(IdNode name, ExpListNode elist) {
        myId = name;
        myExpList = elist;
    }

    public CallExpNode(IdNode name) {
        myId = name;
        myExpList = new ExpListNode(new LinkedList<ExpNode>());
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
        myId.unparse(p, 0);
//        System.out.println(myId.myStrVal);
//        p.print(String.format("(%s)", myId.link.complexToString()));
        p.print("(");
        if (myExpList != null) {
            myExpList.unparse(p, 0);
        }
        p.print(")");
    }

    public SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        //TODO: Check if IdNode has been declared
        scopeTable = myId.analyze(p, scopeTable);

        if (myExpList != null) {

            scopeTable = myExpList.analyze(p, scopeTable);
        }

        return scopeTable;
    }

    // 2 kids
    private IdNode myId;
    private ExpListNode myExpList;  // possibly null
}

abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
        myExp = exp;
    }


    protected SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        scopeTable = myExp.analyze(p, scopeTable);

        return scopeTable;
    }

    // one child
    protected ExpNode myExp;
}

abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
        myExp1 = exp1;
        myExp2 = exp2;
    }


    protected SymTable analyze(ErrorWriter p, SymTable scopeTable) {
        scopeTable = myExp1.analyze(p, scopeTable);
        scopeTable = myExp2.analyze(p, scopeTable);

        return scopeTable;
    }

    // two kids
    protected ExpNode myExp1;
    protected ExpNode myExp2;
}

// **********************************************************************
// Subclasses of UnaryExpNode
// **********************************************************************

class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(-");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(!");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************

class PlusNode extends BinaryExpNode {
    public PlusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" + ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class MinusNode extends BinaryExpNode {
    public MinusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" - ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class TimesNode extends BinaryExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" * ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class DivideNode extends BinaryExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" / ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class AndNode extends BinaryExpNode {
    public AndNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" && ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class OrNode extends BinaryExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" || ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class EqualsNode extends BinaryExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" == ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class NotEqualsNode extends BinaryExpNode {
    public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" != ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessNode extends BinaryExpNode {
    public LessNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" < ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterNode extends BinaryExpNode {
    public GreaterNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" > ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessEqNode extends BinaryExpNode {
    public LessEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" <= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterEqNode extends BinaryExpNode {
    public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" >= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}
