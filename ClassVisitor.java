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
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassVisitor extends VoidVisitorAdapter<Void>{

	private CompilationUnit cu;
	private List<TypeDeclaration<?>> typeDeclaration;
	private StringBuilder intermediateCode;
	private PrimitiveAndStringType checkType;
	private boolean isInterface, addAttribute, addMethod;
	private String className;
	private List<String> attributes, methods;
	private List<String> relationList;
	private Map<String, List<String>> relationMap;
	private NodeList<ClassOrInterfaceType> extendedTypes; 
	private NodeList<ClassOrInterfaceType> implementedTypes;
	private boolean hasMain;
	private List<String> mainComponents;

	public ClassVisitor(CompilationUnit cu) {
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
		this.relationMap = new HashMap<String, List<String>>();
		this.hasMain = false;
		this.mainComponents = new ArrayList<String>();

		checkClassRelations();
		if (this.cu.toString().contains("main")) {
			this.hasMain = true;
		}
		
		if (this.hasMain) {
			visitMain();
		}
	}

	public void visitMain() {
		String result[] = this.cu.toString().split("\\n");
		for(String r: result) {
			if (r.contains("=") && r.length() != 0) {
				String[] elements = r.split("\\=");
				for (int i = 0; i < elements.length; i++) {
					String[] parts = elements[i].split("\\s+"); 
					this.mainComponents.add(parts[1]);
					i += 1;
				}
			}
		}
	}
	
	public List<String> getMainComponents() {
		return this.mainComponents;
	}
	
	public boolean hasMain() {
		return this.hasMain;
	}
	
	public List<String> getRelations() {
		return this.relationList;
	}

	public Map<String, List<String>> getRelationMap() {
		return this.relationMap;
	}

	public NodeList<ClassOrInterfaceType> getExtendedTypes() {
		return this.extendedTypes;
	}

	public String getClassName() {
		return this.className;
	}
	
	public boolean isInterface() {
		return this.isInterface;
	}

	public CompilationUnit getCompilationUnit() {
		return this.cu;
	}

	public String toString() {
		return this.intermediateCode.toString();
	}

	public void checkClassRelations() {
		if (this.cu.getChildNodes() != null) {
			for (Node n : this.cu.getChildNodes()) {
				if (n instanceof ClassOrInterfaceDeclaration) {
					this.className = ((ClassOrInterfaceDeclaration) n).getNameAsString();
					if (((ClassOrInterfaceDeclaration) n).isInterface()) {
						this.isInterface = true;
					} 
					
					this.extendedTypes = ((ClassOrInterfaceDeclaration) n).getExtendedTypes();
					if (this.extendedTypes.size() != 0) {
						for (int i = 0; i < this.extendedTypes.size(); i++) {
							String extendedName = ((ClassOrInterfaceDeclaration) n).getExtendedTypes().get(i).getNameAsString();
							this.relationList.add(extendedName);
							if (this.relationMap.containsKey(extendedName)) {
								this.relationMap.get(extendedName).add(Relations.INHERIANCE.toString());
							} else {
								List<String> relation= new ArrayList<String>();
								relation.add(Relations.INHERIANCE.toString());
								this.relationMap.put(extendedName, relation);
							}
						}
					}
					
					this.implementedTypes = ((ClassOrInterfaceDeclaration) n).getImplementedTypes();
					if (this.implementedTypes.size() != 0) {
						for (int i = 0; i < this.implementedTypes.size(); i++) {
							String implementedName = ((ClassOrInterfaceDeclaration) n).getImplementedTypes().get(i).getNameAsString();
							this.relationList.add(implementedName);
							if (this.relationMap.containsKey(implementedName)) {
								this.relationMap.get(implementedName).add(Relations.INTERFACE_INHERIANCE.toString());
							} else {
								List<String> relation= new ArrayList<String>();
								relation.add(Relations.INTERFACE_INHERIANCE.toString());
								this.relationMap.put(implementedName, relation);
							}
						}
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
				if (this.relationMap.containsKey(name)) {
					this.relationMap.get(name).add(Relations.CARDINALITY.toString());
				} else {
					List<String> relation = new ArrayList<String>();
					relation.add(Relations.CARDINALITY.toString());
					this.relationMap.put(name, relation);
				}
			} else {
				String name = type.toString();
				this.relationList.add(name);
				if (this.relationMap.containsKey(name)) {
					
					this.relationMap.get(name).add(Relations.SIMPLEASSOCIATION.toString());
				} else {
					List<String> relation = new ArrayList<String>();
					relation.add(Relations.SIMPLEASSOCIATION.toString());
					this.relationMap.put(name, relation);
				}
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
					if (!this.checkType.isPrimitive(type) && !this.isInterface) {
						this.relationList.add(type.toString());
						if (this.relationMap.containsKey(type.toString())) {
							this.relationMap.get(type.toString()).add(Relations.DEPENDENCIES.toString());
						} else {
							List<String> relation= new ArrayList<String>();
							relation.add(Relations.DEPENDENCIES.toString());
							this.relationMap.put(type.toString(), relation);
						}
					}
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
					if (!this.checkType.isPrimitive(type) && !this.isInterface) {
						this.relationList.add(type.toString());
						if (this.relationMap.containsKey(type.toString())) {
							this.relationMap.get(type.toString()).add(Relations.DEPENDENCIES.toString());
						} else {
							List<String> relation= new ArrayList<String>();
							relation.add(Relations.DEPENDENCIES.toString());
							this.relationMap.put(type.toString(), relation);
						}
					}
					
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


