import java.io.*;
import java.util.*;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class umlparser {
	public static final String INTERMEDIATE = "intermediate.txt";
	public static final String YUMLCOMMAND = "/usr/local/bin/yuml";

	public static void main(String[] args) throws Exception {
		Map<String, String> nameParsingMap = new HashMap<String, String> ();
		Map<String, ClassVisitor> nameVisitorMap = new HashMap<String, ClassVisitor> ();

		File[] files = null;
		String intermediate_code = null;
		File file = new File(INTERMEDIATE);
		FileWriter fw = null;
		BufferedWriter bw = null;

		File input_directory = new File(args[0]);
		String output_image = args[1];

		if (input_directory.isDirectory()) {
			files = input_directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".java");
				}
			});
		} else {
			System.err.println("Input Directory is not correct.");
		}

		if (files != null) 
			intermediate_code = parsing(files, nameParsingMap, nameVisitorMap);

		fw = new FileWriter(file);
		bw = new BufferedWriter(fw);
		bw.write(intermediate_code);
		if (bw != null)
			bw.close();
		if (fw != null)
			fw.close();

		// Using yuml to draw pictures. 
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(YUMLCOMMAND + " -i " + INTERMEDIATE + " -o " + output_image);	
	}

	public static String parsing(File[] files, Map<String, String> nameParsingMap, Map<String, ClassVisitor> nameVisitorMap) {
		CompilationUnit compilationUnit[] = new CompilationUnit[files.length];
		StringBuilder output_content = new StringBuilder();
		List<ClassVisitor> classVistors = new ArrayList<ClassVisitor> ();
		Set<String> classes = new HashSet<String> ();
		Map<String, String> twoWayMatching = new HashMap<String, String> ();
		String main_component = null;

		for (int i = 0; i < compilationUnit.length; i++) {
			try {
				compilationUnit[i] = JavaParser.parse(files[i]);
				ClassVisitor classVisitor = new ClassVisitor(compilationUnit[i]);
				classVisitor.visitInstance();
				classVistors.add(classVisitor);
				nameVisitorMap.put(classVisitor.getClassName(), classVisitor);
				nameParsingMap.put(classVisitor.getClassName(), classVisitor.toString());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (int i = 0; i < classVistors.size(); i++) {
			ClassVisitor currentVisitor = classVistors.get(i);
			if (currentVisitor.hasMain()) {
				for (String ele: classVistors.get(i).getMainComponents()) {
					if (nameParsingMap.containsKey(ele)) {
						main_component = currentVisitor.toString() + Relations.DEPENDENCIES.toString() + nameParsingMap.get(ele);
					}
				}
			}

			if (currentVisitor.getRelations().size() != 0) {
				for (int j = 0; j < currentVisitor.getRelations().size(); j++) {
					String name = currentVisitor.getRelations().get(j);
					if (currentVisitor.getRelationMap().get(name).contains(Relations.INHERIANCE.toString()) 
							|| currentVisitor.getRelationMap().get(name).contains(Relations.INTERFACE_INHERIANCE.toString())) {
						for (int k = 0; k < currentVisitor.getRelationMap().get(name).size(); k++) {
							String cur = null;
							if (currentVisitor.getRelationMap().get(name).get(k).equals(Relations.DEPENDENCIES.toString()))
								cur = currentVisitor.toString() + currentVisitor.getRelationMap().get(name).get(k) + nameParsingMap.get(name);
							else
								cur = nameParsingMap.get(name) + currentVisitor.getRelationMap().get(name).get(k) + currentVisitor.toString();
							if (twoWayMatching.containsKey(name)) {
								if (!output_content.toString().contains(cur) && (!twoWayMatching.get(name).equals(currentVisitor.getClassName()))) {
									output_content.append(cur);
									twoWayMatching.put(currentVisitor.getClassName(), name);
								}
							} else {
								if (!output_content.toString().contains(cur)) {
									output_content.append(cur);
									twoWayMatching.put(currentVisitor.getClassName(), name);
								}
							}
							if (k != currentVisitor.getRelationMap().get(name).size()-1) {
								output_content.append("\n");
							}
						}
					} else if (currentVisitor.getRelationMap().get(name).contains(Relations.DEPENDENCIES.toString())) {
						for (int k = 0; k < currentVisitor.getRelationMap().get(name).size(); k++) {
							String cur = null;
							if (currentVisitor.getRelationMap().get(name).get(k).equals(Relations.INHERIANCE.toString()) 
									|| currentVisitor.getRelationMap().get(name).get(k).equals(Relations.INTERFACE_INHERIANCE.toString())) 
								cur = nameParsingMap.get(name) + currentVisitor.getRelationMap().get(name).get(k) + currentVisitor.toString();
							else {
								if (nameVisitorMap.get(name) != null 
										&& (nameVisitorMap.get(name).isInterface() || currentVisitor.getRelationMap().get(name).get(k).equals(Relations.SIMPLEASSOCIATION.toString()))) {
									cur = currentVisitor.toString() + currentVisitor.getRelationMap().get(name).get(k) + nameParsingMap.get(name);
								}
							}
							if (cur != null) {
								if (twoWayMatching.containsKey(currentVisitor.getClassName())) {
									if (!output_content.toString().contains(cur) && (!twoWayMatching.get(currentVisitor.getClassName()).equals(name))) {
										output_content.append(cur);
										twoWayMatching.put(name, currentVisitor.getClassName());
									} 
								} else {
									if (!output_content.toString().contains(cur)) {
										output_content.append(cur);
										twoWayMatching.put(name, currentVisitor.getClassName());
									} 
								}
								if (k != currentVisitor.getRelationMap().get(name).size()-1) {
									output_content.append("\n");
								}
							}
						}
					} else {
						for (int k = 0; k < currentVisitor.getRelationMap().get(name).size(); k++) {
							String cur = null;
							if (currentVisitor.getRelationMap().get(name).get(k).equals(Relations.INHERIANCE.toString()) 
									|| currentVisitor.getRelationMap().get(name).get(k).equals(Relations.INTERFACE_INHERIANCE.toString())) 
								cur = nameParsingMap.get(name) + currentVisitor.getRelationMap().get(name).get(k) + currentVisitor.toString();
							else
								cur = currentVisitor.toString() + currentVisitor.getRelationMap().get(name).get(k) + nameParsingMap.get(name);
							if (twoWayMatching.containsKey(currentVisitor.getClassName())) {
								if (!output_content.toString().contains(cur) && (!twoWayMatching.get(currentVisitor.getClassName()).equals(name))) {
									output_content.append(cur);
									twoWayMatching.put(name, currentVisitor.getClassName());
								} 
							} else {
								if (!output_content.toString().contains(cur)) {
									output_content.append(cur);
									twoWayMatching.put(name, currentVisitor.getClassName());
								} 
							}
							if (k != currentVisitor.getRelationMap().get(name).size()-1) {
								output_content.append("\n");
							}

						}
					}
					if (j != currentVisitor.getRelations().size()-1) {
						output_content.append("\n");
					}
				}
			} else {
				if (!classes.contains(currentVisitor.getClassName())) {
					output_content.append(nameParsingMap.get(currentVisitor.getClassName()));
				}
			}
			if (i != classVistors.size()-1)
				output_content.append("\n");
		}

		if (main_component != null) {
			output_content.append("\n" + main_component);
		}
//		System.out.println(output_content);
		return output_content.toString();
	}
}
