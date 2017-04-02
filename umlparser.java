import java.io.*;
import java.net.*;
import java.util.Optional;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
//import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class umlparser {

	public static void main(String[] args) throws Exception {
		// creates an input stream for the file to be parsed
		FileInputStream in = new FileInputStream("/Users/hanchen/Dropbox/coding_interview/java/COMPE202_Personal_Project/test.java");

		CompilationUnit compilationUnit = JavaParser.parse(in);
		//	        compilationUnit.getNodesByType(FieldDeclaration.class).stream().
		//	        filter(f -> f.getModifiers().contains(PRIVATE)).
		//	        forEach(f -> System.out.println("Check field at line " + f.getBegin().get().line));

		//	        Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName("A");
//		compilationUnit.getNodesByType(FieldDeclaration.class).stream().
//        filter(f -> f.getModifiers().contains("public") && 
//                !f.getModifiers().contains("static")).
//        forEach(f -> System.out.println("Check field at line " + f.getBegin().get().line));
//		new MethodVisitor().visit(compilationUnit, null);
		
		// Using yuml to draw pictures. 
		Runtime rt = Runtime.getRuntime();
		// Make sure the framework are working fine
		Process pr = rt.exec("/usr/local/bin/yuml -i t.txt -o diagram.png");	
	}
	
//	private static void watch(final Process process) {
//	    new Thread() {
//	        public void run() {
//	            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
//	            String line = null; 
//	            try {
//	                while ((line = input.readLine()) != null) {
//	                    System.out.println(line);
//	                }
//	            } catch (IOException e) {
//	                e.printStackTrace();
//	            }
//	        }
//	    }.start();
//	}
//
//	
//	private static class MethodVisitor extends VoidVisitorAdapter<Void> {
//		@Override
//		public void visit(MethodDeclaration n, Void arg) {
//			/* here you can access the attributes of the method.
//	             this method will be called for all methods in this 
//	             CompilationUnit, including inner class methods */
//			System.out.println(n.getName());
//			super.visit(n, arg);
//		}
//	}
//	
//	private static class MethodChangerVisitor extends VoidVisitorAdapter<Void> {
//        @Override
//        public void visit(MethodDeclaration n, Void arg) {
//            // change the name of the method to upper case
//            n.setName(n.getNameAsString().toUpperCase());
//
//            // add a new parameter to the method
//            n.addParameter("int", "value");
//        }
//    }
//	
//	private static void changeMethods(CompilationUnit cu) {
//        // Go through all the types in the file
//        NodeList<TypeDeclaration<?>> types = cu.getTypes();
//        for (TypeDeclaration<?> type : types) {
//            // Go through all fields, methods, etc. in this type
//            NodeList<BodyDeclaration<?>> members = type.getMembers();
//            for (BodyDeclaration<?> member : members) {
//                if (member instanceof MethodDeclaration) {
//                    MethodDeclaration method = (MethodDeclaration) member;
//                    changeMethod(method);
//                }
//            }
//        }
//    }
//
//    private static void changeMethod(MethodDeclaration n) {
//        // change the name of the method to upper case
//        n.setName(n.getNameAsString().toUpperCase());
//    }
//    
//    private static CompilationUnit createCU() {
//        CompilationUnit cu = new CompilationUnit();
//        // set the package
//        
//        
//        
//        
//        cu.setPackageDeclaration(new PackageDeclaration(Name.parse("java.parser.test")));
//
//        // or a shortcut
//        cu.setPackageDeclaration("java.parser.test");
//
//        // create the type declaration 
//        ClassOrInterfaceDeclaration type = cu.addClass("GeneratedClass");
//
//        // create a method
//        EnumSet<Modifier> modifiers = EnumSet.of(Modifier.PUBLIC);
//        MethodDeclaration method = new MethodDeclaration(modifiers, new VoidType(), "main");
//        modifiers.add(Modifier.STATIC);
//        method.setModifiers(modifiers);
//        type.addMember(method);
//
//        // or a shortcut
//        MethodDeclaration main2 = type.addMethod("main2", Modifier.PUBLIC, Modifier.STATIC);
//
//        // add a parameter to the method
//        Parameter param = new Parameter(new ClassOrInterfaceType("String"), "args");
//        param.setVarArgs(true);
//        method.addParameter(param);
//
//        // or a shortcut
//        main2.addAndGetParameter(String.class, "args").setVarArgs(true);
//
//        // add a body to the method
//        BlockStmt block = new BlockStmt();
//        method.setBody(block);
//
//        // add a statement do the method body
//        NameExpr clazz = new NameExpr("System");
//        FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
//        MethodCallExpr call = new MethodCallExpr(field, "println");
//        call.addArgument(new StringLiteralExpr("Hello World!"));
//        block.addStatement(call);
//
//        return cu;
//    }
}
