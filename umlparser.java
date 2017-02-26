import java.io.FileInputStream;
import java.util.Optional;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;


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

	   
}
