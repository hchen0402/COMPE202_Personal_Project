import java.util.*;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassVistor extends VoidVisitorAdapter<Void>{

	private CompilationUnit cu;
	private List<TypeDeclaration<?>> typeDeclaration;
	private StringBuilder intermediateCode;
	private PrimitiveAndStringType checkType;
	private boolean isInterface, addAttribute;
	private String className;

	private Map<String, String> instanceVariablesMap = new HashMap<String, String>();

	public ClassVistor(CompilationUnit cu) {
		this.cu = cu;
		this.typeDeclaration = cu.getTypes();
		this.intermediateCode = new StringBuilder();
		this.checkType = new PrimitiveAndStringType();
		this.isInterface = false;
		this.addAttribute = false;
		this.className = null;
		this.intermediateCode.append("[");

		checkIsInterface();


	}

	public CompilationUnit getCompilationUnit() {
		return this.cu;
	}

	public String toString() {
		return this.intermediateCode.toString();
	}

	public void checkIsInterface() {
		if (this.cu.getChildNodes() != null) {
			for (Node n : this.cu.getChildNodes()) {
				if (n instanceof ClassOrInterfaceDeclaration) {
					this.className = ((ClassOrInterfaceDeclaration) n).getNameAsString();
					if (((ClassOrInterfaceDeclaration) n).isInterface()) {
						this.isInterface = true;
					}
				}
			}
		}
	}

	public void visitInstanceVariables() {
		if (this.isInterface) {
			this.intermediateCode.append("<<interface>>;" + this.className);
		} else {
			this.intermediateCode.append(this.className);
		}
		for (TypeDeclaration<?> typeDec : this.typeDeclaration) {
			List<BodyDeclaration<?>> members = typeDec.getMembers();
			if(members != null) {
				for (BodyDeclaration<?> member : members) {
				

					FieldDeclaration field = null;
					if (member instanceof FieldDeclaration) {
						field = (FieldDeclaration) member;

					}

					if (field != null) {
						String modifiers = null;
						

						Type type = field.getMaximumCommonType();

						if (this.checkType.isPrimitive(type)) {
							if (!this.addAttribute) {
								this.intermediateCode.append("|");
								this.addAttribute = true;
							}
							if (field.getModifiers().toString().equals("[PRIVATE]")) {
								this.intermediateCode.append("-");
							} else if (field.getModifiers().toString().equals("[PUBLIC]")) {
								this.intermediateCode.append("+");
							} 
							this.intermediateCode.append(field.getVariables().get(0).getNameAsString());
							this.intermediateCode.append(":");
							if (this.checkType.isPrimitiveArray(type)) {
								this.intermediateCode.append(field.getElementType().toString() + "(*)");

							} else {
								this.intermediateCode.append(field.getElementType().toString());
							}
							this.intermediateCode.append(";");
						}


						//		        		System.out.println(field.getModifiers());
						//		        		System.out.println(field.getMaximumCommonType());
						//		        		System.out.println(field.getVariables().get(0).getNameAsString());
						//		        		System.out.println();

					}
					//		        //Print the field's name 
					//		        System.out.println(field.getVariables().get(0).getId().getName());
					//		        //Print the field's init value, if not null
					//		        Object initValue = field.getVariables().get(0).getInit();
					//		        if(initValue != null) {
					//		             System.out.println(field.getVariables().get(0).getInit().toString());
				}  
			}
			this.intermediateCode.append("]");
		}
	}

	private static class MethodVisitor extends VoidVisitorAdapter<Void> {
		@Override
		public void visit(MethodDeclaration n, Void arg) {
			/* here you can access the attributes of the method.
             this method will be called for all methods in this 
             CompilationUnit, including inner class methods */
			System.out.println(n.getName() + "here");
			super.visit(n, arg);
		}
	}
}
