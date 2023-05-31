import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    Lector lector = new Lector();
    Person person = lector.createObject("Elio, 30", "name, age", Person.class);
    System.out.println(person.getName()+" tiene "+person.getAge());

    Perro perro = lector.createObject("Rambo, Pastor Malinois, 8","nombre, raza, edad", Perro.class);
    System.out.println(perro);
  }
}