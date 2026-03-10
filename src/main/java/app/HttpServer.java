package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private static final Map<String, Method> services = new HashMap<>();
    private static final Map<String, Object> controllers = new HashMap<>();
    private static String staticFilesPath = "webroot";
    private static int port = 8080;

    public static void staticfiles(String folder) {
        // Fallback: si existe target/classes (local), usarlo. Si no (AWS), usar carpeta
        // actual.
        if (folder.startsWith("/"))
            folder = folder.substring(1); // Quitar '/' inicial
        File localTarget = new File("target/classes/" + folder);
        if (localTarget.exists()) {
            staticFilesPath = "target/classes/" + folder;
        } else {
            staticFilesPath = folder; // Para AWS: busca en la carpeta actual
        }
    }

    public static void setPort(int p) {
        port = p;
    }

    public static void main(String[] args) {
        start();
    }

    public static void addService(String path, Method method, Object controller) {
        services.put(path, method);
        controllers.put(path, controller);
    }

    public static void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en el puerto: " + port);
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleRequest(clientSocket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void handleRequest(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String firstLine = in.readLine();
        if (firstLine == null)
            return;

        String[] parts = firstLine.split(" ");
        if (parts.length < 2)
            return;

        String fullPath = parts[1];
        String path = fullPath;
        String queryString = null;

        if (fullPath.contains("?")) {
            int queryIdx = fullPath.indexOf("?");
            path = fullPath.substring(0, queryIdx);
            queryString = fullPath.substring(queryIdx + 1);
        }

        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (services.containsKey(path)) {
            try {
                Method method = services.get(path);
                Object controller = controllers.get(path);
                HttpRequest request = new HttpRequest(queryString);

                Object[] args = prepareArgs(method, request);
                String responseBody = (String) method.invoke(controller, args);

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html; charset=UTF-8");
                out.println();
                out.println(responseBody);
            } catch (Exception e) {
                out.println("HTTP/1.1 500 Internal Server Error");
                out.println();
                out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            serveStaticFile(path, clientSocket);
        }
    }

    private static Object[] prepareArgs(Method method, HttpRequest request) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter p = parameters[i];
            if (p.isAnnotationPresent(RequestParam.class)) {
                RequestParam ann = p.getAnnotation(RequestParam.class);
                String value = request.getValues(ann.value());
                if (value == null || value.equals("desconocido")) {
                    value = ann.defaultValue();
                }
                args[i] = value;
            } else if (p.getType().equals(HttpRequest.class)) {
                args[i] = request;
            } else {
                args[i] = null;
            }
        }
        return args;
    }

    private static void serveStaticFile(String path, Socket clientSocket) throws IOException {
        File file = new File(staticFilesPath + path);

        if (file.exists() && !file.isDirectory()) {
            String contentType = Files.probeContentType(file.toPath());
            byte[] fileContent = Files.readAllBytes(file.toPath());

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + (contentType != null ? contentType : "text/plain"));
            out.println("Content-Length: " + fileContent.length);
            out.println();
            clientSocket.getOutputStream().write(fileContent);
        } else {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("HTTP/1.1 404 Not Found");
            out.println();
            out.println("<h1>No encontrado</h1><p>El recurso " + (staticFilesPath + path) + " no existe.</p>");
        }
    }
}
