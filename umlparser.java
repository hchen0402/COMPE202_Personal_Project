import java.io.*;
import java.util.*;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;

public class umlparser {
	public static final String INTERMEDIATE = "intermediate.txt";
	public static final String YUMLCOMMAND = "/usr/local/bin/yuml";

	public static void main(String[] args) throws Exception {
		Map<String, String> nameParsingMap = new HashMap<String, String> ();
		
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
			intermediate_code = parsing(files, nameParsingMap);

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

	public static String parsing(File[] files, Map<String, String> nameParsingMap) {
		CompilationUnit compilationUnit[] = new CompilationUnit[files.length];
		StringBuilder output_content = new StringBuilder();
		List<ClassVistor> classVistors = new ArrayList<ClassVistor> ();
		Set<String> classes = new HashSet<String> ();

		for (int i = 0; i < compilationUnit.length; i++) {
			try {
				compilationUnit[i] = JavaParser.parse(files[i]);
				ClassVistor classVistor = new ClassVistor(compilationUnit[i]);
				classVistor.visitInstance();
				classVistors.add(classVistor);
				nameParsingMap.put(classVistor.getClassName(), classVistor.toString());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < classVistors.size(); i++) {
			ClassVistor currentVisitor = classVistors.get(i);
			if (currentVisitor.getRelations().size() != 0) {
				classes.add(currentVisitor.getClassName());
				for (int j = 0; j < currentVisitor.getRelations().size(); j++) {
					String name = currentVisitor.getRelations().get(j);
					if (!classes.contains(name)) {
						output_content.append(currentVisitor.toString() + currentVisitor.getRelationMap().get(name) + nameParsingMap.get(name));
						classes.add(name);
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
		System.out.println(output_content);

		return output_content.toString();
	}


}
