import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumSet;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Relationship {
	private static void watch(final Process process) {
		new Thread() {
			public void run() {
				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null; 
				try {
					while ((line = input.readLine()) != null) {
						System.out.println(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
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
