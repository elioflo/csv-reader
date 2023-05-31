import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Lector {

  private String capitalize(String str) {
    return str.substring(0,1).toUpperCase().concat(str.substring(1));
  }

  public List<?> leerLista(String file, Class<? extends Object> className) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    String encabezado = "name, age";
    String[] attributes = Arrays.stream(encabezado.split(",")).map(String::trim).toArray(String[]::new);
    Method[] getters = Arrays.stream(attributes).map(attr -> {
      try {
        return className.getMethod("get"+capitalize(attr));
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }).toArray(Method[]::new);

    String data = "Jose, 25";
    String[] arguments = Arrays.stream(data.split(",")).map(String::trim).toArray(String[]::new);
    Class<?>[] parameterTypes = new Class[getters.length];
    Method[] setters = new Method[getters.length];

    for (int i = 0; i < parameterTypes.length; i++) {
      parameterTypes[i] = getters[i].getReturnType();
      setters[i] = className.getMethod("set"+capitalize(attributes[i]),parameterTypes[i]);
    }

    Object[] values = new Object[arguments.length];

    for (int index = 0; index < values.length; index++) {
      if(parameterTypes[index].equals(int.class)) {
        values[index] = Integer.parseInt(arguments[index]);
      }else{
        values[index] = arguments[index];
      }
    }

    Constructor<?> constructor = className.getConstructor();
    var object = constructor.newInstance();

    for (int i = 0; i < setters.length; i++) {
      setters[i].invoke(object,values[i]);
    }

    return List.of(object);
  }

  public <T> T createObject(String data, String format, Class<T> className) {
    String[] attributes = Arrays.stream(format.split(",")).map(String::trim).toArray(String[]::new);
    String[] arguments = Arrays.stream(data.split(",")).map(String::trim).toArray(String[]::new);
    Method[] getters = Arrays.stream(attributes).map(attr -> {
      try {
        return className.getMethod("get"+capitalize(attr));
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }).toArray(Method[]::new);
    Constructor<?> constructor = null;
    Object object = null;
    Class<?>[] parameterTypes = new Class[getters.length];
    Method[] setters = new Method[getters.length];
    Object[] values = new Object[arguments.length];

    try {
      constructor = className.getConstructor();
      object = constructor.newInstance();
      for (int i = 0; i < parameterTypes.length; i++) {
        parameterTypes[i] = getters[i].getReturnType();
        setters[i] = className.getMethod("set"+capitalize(attributes[i]),parameterTypes[i]);
      }

      for (int index = 0; index < values.length; index++) {
        if(parameterTypes[index].equals(int.class)) {
          values[index] = Integer.parseInt(arguments[index]);
        }else{
          values[index] = arguments[index];
        }
      }
      for (int i = 0; i < setters.length; i++) {
        setters[i].invoke(object,values[i]);
      }
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    return (T) object;
  }
}
