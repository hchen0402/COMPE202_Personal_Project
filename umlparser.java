import java.io.*;
import java.net.*;
import java.util.Optional;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
//import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;



public class umlparser {
	public static final String INTERMEDIATE = "intermediate.txt";
	public static final String YUMLCOMMAND = "/usr/local/bin/yuml";
	
	public static void main(String[] args) throws Exception {
//		File in = new File("test.java");
		
		
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
			intermediate_code = parsing(files);

        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(intermediate_code);
            if (bw != null)
            	bw.close();
            if (fw != null)
            	fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		// Using yuml to draw pictures. 
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(YUMLCOMMAND + " -i " + INTERMEDIATE + " -o " + output_image);	
	}
	
	public static String parsing(File[] files) {
		CompilationUnit compilationUnit[] = new CompilationUnit[files.length];
		StringBuilder output_content = new StringBuilder();

        for (int i = 0; i < compilationUnit.length; i++) {
                try {
					compilationUnit[i] = JavaParser.parse(files[i]);
					ClassVistor classVistor = new ClassVistor(compilationUnit[i]);
					classVistor.visitInstanceVariables();
					output_content.append(classVistor.toString());
					if (i != compilationUnit.length-1)
						output_content.append("\n");
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        System.out.println(output_content);
        
        return output_content.toString();
	}
	
	
}
