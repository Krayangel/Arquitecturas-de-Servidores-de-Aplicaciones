package app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class Microspringboot2 {
    static Map<String, Method> controllerMethods = new HashMap<>();

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        System.out.println("Loading classes...");

        Class<?> c = Class.forName(args[0]);

        if(c.isAnnotationPresent(RestController.class)){

            for(Method m: c.getDeclaredMethods()){
                if(m.isAnnotationPresent(GetMapping.class)){
                    GetMapping a = m.getAnnotation(GetMapping.class);
                    controllerMethods.put(a.value(), m);
                }
            }
        }

        String path = args[1];

        System.out.println("Executing web method for path: ");

        Method m = controllerMethods.get(path);

        System.out.println(m.invoke(null));
    }

}
