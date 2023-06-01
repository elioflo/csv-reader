import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import static java.nio.file.Files.lines;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Lector {

  private Formato formato;

  public Lector(Formato formato) {
    this.formato = formato;
  }

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
      }else if(parameterTypes[index].equals(char.class)) {
        values[index] = arguments[index].charAt(0);
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

  private String[] separarDatos(String datos) {
    return Arrays.stream(datos.split(",")).map(String::trim).toArray(String[]::new);
  }
  public <T> T createObject(String data, String formato, Class<T> className) {
    String[] attributes = separarDatos(formato);
    String[] arguments = separarDatos(data);
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
        } else if(parameterTypes[index].equals(char.class)) {
          values[index] = arguments[index].charAt(0);
        } else {
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

  public <T> List<T> cargarLista(String nombreArchivo) {
    List<T> lista;
    try {
      formato.setUp(lines(Paths.get(nombreArchivo)).findFirst().get());
      lista = (List<T>) lines(Paths.get(nombreArchivo)).map(data -> formato.crearObjeto(data)).toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return lista;
  }
}
