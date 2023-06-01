public interface Formato {
  void setUp(String nombreArchivo);

  <T> T crearObjeto(String data);
}
