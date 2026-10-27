
package DB;

import java.io.IOException;
import java.io.RandomAccessFile;

public class LibraryDB implements AutoCloseable {

    public static final int IDLEN = 12;
    public static final int TITLELEN = 40;
    public static final int QUANLEN = 4;
    public static final int COSTLEN = 4;

    private static final int RECLEN =
            2 * IDLEN + 2 * TITLELEN + QUANLEN + COSTLEN;

    private RandomAccessFile raf;

    public LibraryDB(String path) throws IOException {
        raf = new RandomAccessFile(path, "rw");
    }

    // Añadir un libro
    public void append(String id, String title,
                       int copies, int priceCents) throws IOException {
        raf.seek(raf.length());
        write(id, title, copies, priceCents);
    }

    // Cerrar el archivo
    @Override
    public void close() throws IOException {
        raf.close();
    }

    // Número de registros
    public int numRecs() throws IOException {
        return (int) (raf.length() / RECLEN);
    }

    // Leer un libro
    public Book select(int recno) throws IOException {
        if (recno < 0 || recno >= numRecs()) {
            throw new IllegalArgumentException(recno + " out of range");
        }

        raf.seek((long) recno * RECLEN);
        return read();
    }

    // Actualizar un libro
    public void update(int recno, String id, String title,
                       int copies, int priceCents) throws IOException {
        if (recno < 0 || recno >= numRecs()) {
            throw new IllegalArgumentException(recno + " out of range");
        }

        raf.seek((long) recno * RECLEN);
        write(id, title, copies, priceCents);
    }

    // Leer un registro
    private Book read() throws IOException {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < IDLEN; i++) {
            sb.append(raf.readChar());
        }
        String id = sb.toString().trim();

        sb.setLength(0);

        for (int i = 0; i < TITLELEN; i++) {
            sb.append(raf.readChar());
        }
        String title = sb.toString().trim();

        int copies = raf.readInt();
        int priceCents = raf.readInt();

        return new Book(id, title, copies, priceCents);
    }

    // Escribir un registro
    private void write(String id, String title,
                       int copies, int priceCents) throws IOException {

        StringBuilder sb = new StringBuilder(id);

        if (sb.length() > IDLEN) {
            sb.setLength(IDLEN);
        }
        while (sb.length() < IDLEN) {
            sb.append(" ");
        }
        raf.writeChars(sb.toString());

        sb = new StringBuilder(title);

        if (sb.length() > TITLELEN) {
            sb.setLength(TITLELEN);
        }
        while (sb.length() < TITLELEN) {
            sb.append(" ");
        }
        raf.writeChars(sb.toString());

        raf.writeInt(copies);
        raf.writeInt(priceCents);
    }

    // Clase Book
    public static class Book {
        private String id;
        private String title;
        private int copies;
        private int priceCents;

        public Book(String id, String title,
                    int copies, int priceCents) {
            this.id = id;
            this.title = title;
            this.copies = copies;
            this.priceCents = priceCents;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getCopies() {
            return copies;
        }

        public int getPriceCents() {
            return priceCents;
        }

        @Override
        public String toString() {
            return "ID: " + id
                    + ", Título: " + title
                    + ", Ejemplares: " + copies
                    + ", Precio: " + priceCents + " céntimos";
        }
    }
}
