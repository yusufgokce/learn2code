package sandbox;

class Sandbox {
    private String untrustedCode;

    public Sandbox(String untrustedCode) {
        this.untrustedCode = untrustedCode;
    }

    public void executeUntrustedCode() {
        // Create a separate ClassLoader for untrusted code
        ClassLoader sandboxClassLoader = new SandboxClassLoader();

        // Load and execute untrusted code using the sandbox ClassLoader
        try {
            // Create the untrusted class dynamically within the sandbox environment
            Class<?> untrustedClass = ((SandboxClassLoader) sandboxClassLoader).defineClass("UntrustedCode", untrustedCode);
            Runnable untrustedInstance = (Runnable) untrustedClass.getDeclaredConstructor().newInstance();
            untrustedInstance.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Custom ClassLoader for loading and defining untrusted code
class SandboxClassLoader extends ClassLoader {
    // Define a method to compile and load the untrusted class
    public Class<?> defineClass(String name, String code) {
        byte[] byteCode = compileCode(code);
        return defineClass(name, byteCode, 0, byteCode.length);
    }

    // Simulate compilation (you might want a more robust compiler)
    private byte[] compileCode(String code) {
        // Simulated compilation: Convert the code string to bytecode
        // In a real scenario, use a compiler API or tools like JavaCompiler
        // Here, for simplicity, just converting the string to bytes
        return code.getBytes();
    }
}
class SandboxRunner {
    public static void main(String[] args) {
        // Provide the untrusted code as a string
        String untrustedCode = "public class UntrustedCode implements Runnable {" +
                "   public void run() {" +
                "       System.out.println(\"Untrusted code executed\");" +
                "   }" +
                "}";

        // Create a Sandbox instance with the untrusted code
        Sandbox sandbox = new Sandbox(untrustedCode);
        sandbox.executeUntrustedCode();
    }
}
