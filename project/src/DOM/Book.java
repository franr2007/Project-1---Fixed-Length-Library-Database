package DOM;

public class Book {
    private String id;
    private String titulo;
    private int copias;
    private int precio;

    public Book(String id, String titulo, int copias, int precio) {
        this.id = id;
        this.titulo = titulo;
        this.copias = copias;
        this.precio = precio;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String gettitulo() {
        return titulo;
    }

    public int getcopias() {
        return copias;
    }

    public int getprecio() {
        return precio;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void settitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setcopias(int copias) {
        this.copias = copias;
    }

    public void setprecio(int precio) {
        this.precio = precio;
    }
}
