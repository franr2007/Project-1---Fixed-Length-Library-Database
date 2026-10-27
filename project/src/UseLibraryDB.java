
import DB.LibraryDB;
import java.io.IOException;

public class UseLibraryDB {

    public static void main(String[] args) {

        LibraryDB pdb = null;
        String registroBuscar;

        try {

            // Open the database
            pdb = new LibraryDB("library.db");

            // Populate the database if it is empty
            if (pdb.numRecs() == 0) {

                pdb.append(
                        "173482190375s",
                        "Rubius Japon 2009",
                        10,
                        3000);

                pdb.append(
                        "00004675239k",
                        "Cyberpunk edgerunners",
                        18,
                        2399);

                pdb.append(
                        "99215739456r",
                        "El mentalista",
                        45,
                        500);

                pdb.append(
                        "23723986095f",
                        "You",
                        2,
                        3500);

                pdb.append(
                        "34758020345p",
                        "The walking dead",
                        29,
                        4050);
            }

            // Display all records
            System.out.println("=== Libros iniciales ===");
            dumpRecords(pdb);

            // Update record 1
            pdb.update(
                    1,
                    "00004675239k",
                    "Cyberpunk edgerunners",
                    10,
                    1899);

            // Display records again
            System.out.println("=== Libros actualizados ===");
            dumpRecords(pdb);

            // buscar un registro entre todos
            registroBuscar = pdb.select(2).getTitle();
            System.out.println("Book in the regist 2: " + registroBuscar);

        } catch (IOException ioe) {

            System.err.println(ioe);

        } finally {

            // Close the database
            if (pdb != null) {
                try {
                    pdb.close();
                } catch (IOException ioe) {
                    System.err.println(ioe);
                }
            }
        }
    }

    // Display all records
    static void dumpRecords(LibraryDB pdb) throws IOException {

        System.out.println(
                format("ID", LibraryDB.IDLEN, true) + " | "
                        + format("Title", LibraryDB.TITLELEN, true) + " | "
                        + format("Quantity", 10, false) + " | "
                        + format("Price", 10, false));

        for (int i = 0; i < pdb.numRecs(); i++) {

            LibraryDB.Book part = pdb.select(i);

            System.out.print(
                    format(
                            part.getId(),
                            LibraryDB.IDLEN,
                            true));

            System.out.print(" | ");

            System.out.print(
                    format(
                            part.getTitle(),
                            LibraryDB.TITLELEN,
                            true));

            System.out.print(" | ");

            System.out.print(
                    format(
                            "" + part.getCopies(),
                            10,
                            false));

            System.out.print(" | ");

            // Unit cost is stored in cents
            String s = part.getPriceCents() / 100
                    + "."
                    + part.getPriceCents() % 100;

            // Add a leading zero if necessary
            if (s.charAt(s.length() - 2) == '.') {
                s += "0";
            }

            System.out.println(
                    format(
                            s,
                            10,
                            false));
        }

        System.out.println(
                "Number of records = " + pdb.numRecs());

        System.out.println();
    }

    // Format a string to a fixed width
    static String format(
            String value,
            int maxWidth,
            boolean leftAlign) {

        StringBuilder sb = new StringBuilder();

        int len = value.length();

        // Truncate if the value is too long
        if (len > maxWidth) {

            len = maxWidth;
            value = value.substring(0, len);
        }

        // Left-align
        if (leftAlign) {

            sb.append(value);

            for (int i = 0; i < maxWidth - len; i++) {
                sb.append(" ");
            }

            // Right-align
        } else {

            for (int i = 0; i < maxWidth - len; i++) {
                sb.append(" ");
            }

            sb.append(value);
        }

        return sb.toString();
    }
}
