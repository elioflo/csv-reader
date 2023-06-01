import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CSV implements Formato{
  private final Class<?> clase;
  private Constructor<?> constructor;
  private String[] attributes;
  private Method[] setters;
  private Method[] getters;
  private Class<?>[] tipos;

  public CSV(Class<?> clase) {
    super();
    this.clase = clase;
  }

  private String[] separarDatos(String datos) {
    return Arrays.stream(datos.split(",")).map(String::trim).toArray(String[]::new);
  }

  private Object[] values(String[] arguments) {
    Object[] resultado = new Object[arguments.length];
    for (int index = 0; index < tipos.length; index++) {
      if(tipos[index].equals(int.class)) {
        resultado[index] = Integer.parseInt(arguments[index]);
      } else if(tipos[index].equals(char.class)) {
        resultado[index] = arguments[index].charAt(0);
      } else {
        resultado[index] = arguments[index];
      }
    }
    return resultado;
  }

  private String capitalize(String str) {
    return str.substring(0,1).toUpperCase().concat(str.substring(1));
  }

  @Override
  public void setUp(String formato) {
    attributes = separarDatos(formato);
    getters = new Method[attributes.length];
    setters = new Method[attributes.length];
    tipos = new Class<?>[attributes.length];
    try {
      constructor = clase.getConstructor();
      for (int indice = 0; indice < attributes.length; indice++) {
        getters[indice] = clase.getMethod("get"+capitalize(attributes[indice]));
        tipos[indice] = getters[indice].getReturnType();
        setters[indice] = clase.getMethod("set"+capitalize(attributes[indice]),tipos[indice]);
      }
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> T crearObjeto(String data) {
    Object objeto;
    String[] arguments = separarDatos(data);
    Object[] values = values(arguments);
    try {
      objeto = constructor.newInstance();
      for (int method = 0; method < setters.length; method++) {
        setters[method].invoke(objeto, values[method]);
      }
    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return (T) objeto;
  }
}
