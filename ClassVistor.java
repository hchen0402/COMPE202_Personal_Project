import java.util.*;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassVistor extends VoidVisitorAdapter<Void>{

	private CompilationUnit cu;
	private List<TypeDeclaration<?>> typeDeclaration;
	private StringBuilder intermediateCode;
	private PrimitiveAndStringType checkType;
	private boolean isInterface, addAttribute, addMethod;
	private String className;
	private List<String> attributes, methods;
	private List<String> relationList;
	private Map<String, String> relationMap;

	public ClassVistor(CompilationUnit cu) {
		this.cu = cu;
		this.typeDeclaration = cu.getTypes();
		this.intermediateCode = new StringBuilder();
		this.checkType = new PrimitiveAndStringType();
		this.isInterface = false;
		this.addAttribute = false;
		this.addMethod = false;
		this.className = null;
		this.intermediateCode.append("[");
		this.attributes = new ArrayList<String> ();
		this.methods = new ArrayList<String> ();
		this.relationList = new ArrayList<String> ();
		this.relationMap = new HashMap<String, String>();

		checkIsInterface();
	}
	
	public List<String> getRelations() {
		return this.relationList;
	}
	
	public Map<String, String> getRelationMap() {
		return this.relationMap;
	}
	
	public String getClassName() {
		return this.className;
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

	public void visitInstance() {
		
		if (this.isInterface) {
			this.intermediateCode.append("<<interface>>;" + this.className);
		} else {
			this.intermediateCode.append(this.className);
		}
		for (TypeDeclaration<?> typeDec : this.typeDeclaration) {
			List<BodyDeclaration<?>> members = typeDec.getMembers();
			if(members != null) {
				for (int i = 0; i < members.size(); i++) {
					BodyDeclaration<?> member = members.get(i);
					
					FieldDeclaration field = null;
					MethodDeclaration method = null;
					ConstructorDeclaration constructor = null;

					if (member instanceof FieldDeclaration) {
						field = (FieldDeclaration) member;
						this.visitAttributes(field);
					}

					if (member instanceof ConstructorDeclaration) {
						constructor = (ConstructorDeclaration) member;
						this.visitConstructor(constructor);
					}
					
					if (member instanceof MethodDeclaration) {
						method = (MethodDeclaration) member;
						this.visitMethods(method);
					}
				}  
			}
		}
		
		if (this.addAttribute) {
			if (this.isInterface)
				this.intermediateCode.append(";");
			else
				this.intermediateCode.append("|");
			for (int j = 0; j < this.attributes.size(); j++) {
				this.intermediateCode.append(this.attributes.get(j));
				if (j != this.attributes.size()-1) {
					this.intermediateCode.append(";");
				}
			}
		}
		
		if (this.addMethod) {
			if (this.isInterface)
				this.intermediateCode.append(";");
			else
				this.intermediateCode.append("|");
			for (int j = 0; j < this.methods.size(); j++) {
				this.intermediateCode.append(this.methods.get(j));
				if (j != this.methods.size()-1) {
					this.intermediateCode.append(";");
				}
			}
		}
		this.intermediateCode.append("]");
	}

	public void visitAttributes(FieldDeclaration field) {
		StringBuilder result = new StringBuilder();
		
		if (field != null) {
			String modifiers = null;
			Type type = field.getMaximumCommonType();

			if (this.checkType.isPrimitive(type)) {
				if (field.getModifiers().toString().equals("[PRIVATE]")) {
					if (!this.addAttribute) {
						this.addAttribute = true;
					}
					result.append("-");
				} else if (field.getModifiers().toString().equals("[PUBLIC]")) {
					if (!this.addAttribute) {
						this.addAttribute = true;
					}
					result.append("+");
				} else {
					return;
				}
				result.append(field.getVariables().get(0).getNameAsString());
				result.append(":");
				if (this.checkType.isPrimitiveArray(type)) {
					result.append(field.getElementType().toString() + "(*)");
				} else {
					result.append(field.getElementType().toString());
				}
				this.attributes.add(result.toString());
			} else if (type.toString().contains("Collection")) {
				String name = type.toString().substring(11, type.toString().length()-1); 
				this.relationList.add(name);
				this.relationMap.put(name, "1-0..*");
			} else {
				String name = type.toString();
				this.relationList.add(name);
				this.relationMap.put(name, "-1");
			}
		} 
	}
	
	public void visitConstructor(ConstructorDeclaration constructor) {
		StringBuilder result = new StringBuilder();
		
		if (constructor != null) {
			String modifiers = null;
			NodeList<Parameter> parameters = constructor.getParameters();						
			
			if (constructor.getModifiers().toString().equals("[PUBLIC]")) {
				if (!this.addMethod) {
					this.addMethod = true;
				}
				result.append("+");
			} else {
				return;
			}
			
			result.append(constructor.getNameAsString() + "(");
			
			for (int i = 0; i < parameters.size(); i++) {
				result.append(parameters.get(i).getNameAsString());
				result.append(":");
				Type type = parameters.get(i).getType();
				if (this.checkType.isPrimitiveArray(type)) {
					result.append(type.toString().substring(0, type.toString().length()-2) + "(*)");
				} else {
					result.append(type.toString());
				}
				if (i != parameters.size()-1) {
					result.append(",");
				}
			}
			result.append(")");

			this.methods.add(result.toString());
		}
		
	}
	
	public void visitMethods(MethodDeclaration method) {
		StringBuilder result = new StringBuilder();
		
		if (method != null) {
			String modifiers = null;
			NodeList<Parameter> parameters = method.getParameters();						

			if (method.getModifiers().toString().equals("[PUBLIC]") || method.getModifiers().toString().equals("[PUBLIC, ABSTRACT]")
					|| method.getModifiers().toString().equals("[PUBLIC, STATIC]")) {
				if (!this.addMethod) {
					this.addMethod = true;
				}
				result.append("+");
			} else {
				return;
			}
			
			result.append(method.getNameAsString() + "(");
			
			for (int i = 0; i < parameters.size(); i++) {
				result.append(parameters.get(i).getNameAsString());
				result.append(":");
				Type type = parameters.get(i).getType();
				if (this.checkType.isPrimitiveArray(type)) {
					result.append(type.toString().substring(0, type.toString().length()-2) + "(*)");
				} else {
					result.append(type.toString());
				}
				if (i != parameters.size()-1) {
					result.append(",");
				}
			}
			result.append(")");
			result.append(":" + method.getType());

			this.methods.add(result.toString());
		}
	}
}
