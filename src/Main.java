import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {
//    Lector lector = new Lector();
//    Entidad entidad = lector.createObject("Subte, Transporte", "nombre, tipo", Entidad.class);
//    System.out.println(entidad.getNombre()+" es de tipo "+entidad.getTipo());

    Class<?> clase = Perro.class;
    Lector lector = new Lector(new CSV(clase));
    List<Perro> perros = lector.cargarLista("perritos.csv");
    perros.forEach(perro -> System.out.println(perro.getNombre()+ " es " + perro.getRaza() + ". Tiene " + perro.getEdad()));

  }

}