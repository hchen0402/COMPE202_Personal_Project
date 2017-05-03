import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.type.Type;

public class PrimitiveAndStringType {
	private Set<String> primitiveType;
	private Set<String> primitiveTypeArray = new HashSet<String>();
	
	public PrimitiveAndStringType() {
		this.primitiveType = new HashSet<String>();
		this.primitiveTypeArray = new HashSet<String>();
		
		this.primitiveType.add("boolean");
		this.primitiveType.add("byte");
		this.primitiveType.add("char");
		this.primitiveType.add("short");
		this.primitiveType.add("int");
		this.primitiveType.add("long");
		this.primitiveType.add("float");
		this.primitiveType.add("double");
		this.primitiveType.add("boolean[]");
		this.primitiveType.add("byte[]");
		this.primitiveType.add("char[]");
		this.primitiveType.add("short[]");
		this.primitiveType.add("int[]");
		this.primitiveType.add("long[]");
		this.primitiveType.add("float[]");
		this.primitiveType.add("double[]");
		this.primitiveType.add("boolean[][]");
		this.primitiveType.add("byte[][]");
		this.primitiveType.add("char[][]");
		this.primitiveType.add("short[][]");
		this.primitiveType.add("int[][]");
		this.primitiveType.add("long[][]");
		this.primitiveType.add("float[][]");
		this.primitiveType.add("double[][]");
		
		this.primitiveTypeArray.add("boolean[]");
		this.primitiveTypeArray.add("byte[]");
		this.primitiveTypeArray.add("char[]");
		this.primitiveTypeArray.add("short[]");
		this.primitiveTypeArray.add("int[]");
		this.primitiveTypeArray.add("long[]");
		this.primitiveTypeArray.add("float[]");
		this.primitiveTypeArray.add("double[]");
		
		this.primitiveTypeArray.add("boolean[][]");
		this.primitiveTypeArray.add("byte[][]");
		this.primitiveTypeArray.add("char[][]");
		this.primitiveTypeArray.add("short[][]");
		this.primitiveTypeArray.add("int[][]");
		this.primitiveTypeArray.add("long[][]");
		this.primitiveTypeArray.add("float[][]");
		this.primitiveTypeArray.add("double[][]");
		
		this.primitiveType.add("String");
		this.primitiveType.add("String[]");
		this.primitiveType.add("String[][]");
		this.primitiveTypeArray.add("String[]");
		this.primitiveTypeArray.add("String[][]");
		
	}
	
	public boolean isPrimitive(Type type) {
		return this.primitiveType.contains(type.toString());
	}
	
	public boolean isPrimitiveArray(Type type) {
		return this.primitiveTypeArray.contains(type.toString());
	}
}
