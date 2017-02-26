import java.io.FileInputStream;
import java.util.Optional;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class umlparser {

	public static void main(String[] args) throws Exception {
		// creates an input stream for the file to be parsed
		FileInputStream in = new FileInputStream("test.java");

		CompilationUnit compilationUnit = JavaParser.parse(in);
		//	        compilationUnit.getNodesByType(FieldDeclaration.class).stream().
		//	        filter(f -> f.getModifiers().contains(PRIVATE)).
		//	        forEach(f -> System.out.println("Check field at line " + f.getBegin().get().line));

		//	        Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName("A");
		System.out.println(compilationUnit.toString());
	}

	
	private static class MethodVisitor extends VoidVisitorAdapter<Void> {
		@Override
		public void visit(MethodDeclaration n, Void arg) {
			/* here you can access the attributes of the method.
	             this method will be called for all methods in this 
	             CompilationUnit, including inner class methods */
			System.out.println(n.getName());
			super.visit(n, arg);
		}
	}
	
	private static class MethodChangerVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            // change the name of the method to upper case
            n.setName(n.getNameAsString().toUpperCase());

            // add a new parameter to the method
            n.addParameter("int", "value");
        }
    }
	
	private static void changeMethods(CompilationUnit cu) {
        // Go through all the types in the file
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
        for (TypeDeclaration<?> type : types) {
            // Go through all fields, methods, etc. in this type
            NodeList<BodyDeclaration<?>> members = type.getMembers();
            for (BodyDeclaration<?> member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    changeMethod(method);
                }
            }
        }
    }

    private static void changeMethod(MethodDeclaration n) {
        // change the name of the method to upper case
        n.setName(n.getNameAsString().toUpperCase());

 
    }


}
