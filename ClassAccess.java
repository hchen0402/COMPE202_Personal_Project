import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassAccess {
	/**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this 
             CompilationUnit, including inner class methods */
            System.out.println(n.getName());
            super.visit(n, arg);
        }
    }
    
    private static CompilationUnit createCU() {
        CompilationUnit cu = new CompilationUnit();
        // set the package
        
        
        
        
        cu.setPackageDeclaration(new PackageDeclaration(Name.parse("java.parser.test")));

        // or a shortcut
        cu.setPackageDeclaration("java.parser.test");

        // create the type declaration 
        ClassOrInterfaceDeclaration type = cu.addClass("GeneratedClass");

        // create a method
        EnumSet<Modifier> modifiers = EnumSet.of(Modifier.PUBLIC);
        MethodDeclaration method = new MethodDeclaration(modifiers, new VoidType(), "main");
        modifiers.add(Modifier.STATIC);
        method.setModifiers(modifiers);
        type.addMember(method);

        // or a shortcut
        MethodDeclaration main2 = type.addMethod("main2", Modifier.PUBLIC, Modifier.STATIC);

        // add a parameter to the method
        Parameter param = new Parameter(new ClassOrInterfaceType("String"), "args");
        param.setVarArgs(true);
        method.addParameter(param);

        // or a shortcut
        main2.addAndGetParameter(String.class, "args").setVarArgs(true);

        // add a body to the method
        BlockStmt block = new BlockStmt();
        method.setBody(block);

        // add a statement do the method body
        NameExpr clazz = new NameExpr("System");
        FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
        MethodCallExpr call = new MethodCallExpr(field, "println");
        call.addArgument(new StringLiteralExpr("Hello World!"));
        block.addStatement(call);

        return cu;
    }
}
