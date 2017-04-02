import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.javaparser.ast.body.MethodDeclaration;
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
}
