import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.net.URI;

public class gameController {
    public static void main(String[] args) {
        String code = "public class HelloWorld {" +
                "   public static void main(String[] args) {" +
                "       System.out.println(\"Hello, World!\");" +
                "   }" +
                "}";

        // Create a compilation task
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        // Create a source file
        JavaFileObject compilationUnit = new DynamicJavaSourceCodeObject("HelloWorld", code);

        Iterable<? extends JavaFileObject> compilationUnits = List.of(compilationUnit);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);

        // Perform the compilation
        boolean success = task.call();

        if (success) {
            // Run the compiled class
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(".").toURI().toURL()});
                Class<?> loadedClass = classLoader.loadClass("HelloWorld");
                loadedClass.getDeclaredMethod("main", String[].class).invoke(null, (Object) new String[]{});
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            // Display compilation errors
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                System.out.format("Error on line %d in %s%n",
                        diagnostic.getLineNumber(),
                        diagnostic.getSource().toUri());
            }
        }
    }
}