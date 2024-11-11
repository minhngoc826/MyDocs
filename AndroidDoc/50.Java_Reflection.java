// 50.Reflection.java
// java.lang.reflect

@RequiresApi(api = 26)
public void invokeMethod() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class classObj = Class.forName("com.example.androidtraining.Temp"); // way 1
    classObj = classObj.getClass();     // way 2, from ready object
    // invoke method
    Method method = classObj.getMethod("methodName", String.class); // (methodName, parameters)
    method.invoke(classObj, "arg");  // (classObj, parameters)

    // other way
    Constructor[] constructors = classObj.getConstructors();
    Constructor constructor = constructors[0];
    Parameter[] parameters = constructor.getParameters();

    // methods
    Method[] methods = classObj.getMethods();   // get public methods, removed duplicated methods
    methods = classObj.getDeclaredMethods();    // get all methods

    // parameters
    Method method1 = methods[0];
    Parameter[] parameters1 = method1.getParameters();
    method1.getReturnType();

    Class[] classes = classObj.getDeclaredClasses();
    Annotation[] annotations = classObj.getAnnotations();
    Field[] fields = classObj.getFields();
}
