package DB;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PartsDB {

    // Fixed field lengths
    public static final int PNUMLEN = 20;
    public static final int DESCLEN = 30;
    public static final int QUANLEN = 4;
    public static final int COSTLEN = 4;

    // Size of one record
    private static final int RECLEN =
            2 * PNUMLEN + 2 * DESCLEN + QUANLEN + COSTLEN;

    private RandomAccessFile raf;

    // Constructor
    public PartsDB(String path) throws IOException {
        raf = new RandomAccessFile(path, "rw");
    }

    // Add a record at the end of the file
    public void append(String partnum, String partdesc,
                       int qty, int ucost) throws IOException {

        raf.seek(raf.length());

        write(partnum, partdesc, qty, ucost);
    }

    // Close the database
    public void close() {
        try {
            raf.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    // Return the number of records
    public int numRecs() throws IOException {
        return (int) (raf.length() / RECLEN);
    }

    // Read a record
    public Part select(int recno) throws IOException {

        if (recno < 0 || recno >= numRecs()) {
            throw new IllegalArgumentException(
                    recno + " out of range");
        }

        raf.seek(recno * RECLEN);

        return read();
    }

    // Update an existing record
    public void update(int recno, String partnum,
                       String partdesc, int qty,
                       int ucost) throws IOException {

        if (recno < 0 || recno >= numRecs()) {
            throw new IllegalArgumentException(
                    recno + " out of range");
        }

        raf.seek(recno * RECLEN);

        write(partnum, partdesc, qty, ucost);
    }

    // Read one record from the current file position
    private Part read() throws IOException {

        StringBuilder sb = new StringBuilder();

        // Read part number
        for (int i = 0; i < PNUMLEN; i++) {
            sb.append(raf.readChar());
        }

        String partnum = sb.toString().trim();

        // Read description
        sb.setLength(0);

        for (int i = 0; i < DESCLEN; i++) {
            sb.append(raf.readChar());
        }

        String partdesc = sb.toString().trim();

        // Read quantity and unit cost
        int qty = raf.readInt();
        int ucost = raf.readInt();

        return new Part(partnum, partdesc, qty, ucost);
    }

    // Write one record
    private void write(String partnum, String partdesc,
                       int qty, int ucost) throws IOException {

        // Prepare part number
        StringBuilder sb = new StringBuilder(partnum);

        if (sb.length() > PNUMLEN) {
            sb.setLength(PNUMLEN);
        } else {
            while (sb.length() < PNUMLEN) {
                sb.append(" ");
            }
        }

        raf.writeChars(sb.toString());

        // Prepare description
        sb = new StringBuilder(partdesc);

        if (sb.length() > DESCLEN) {
            sb.setLength(DESCLEN);
        } else {
            while (sb.length() < DESCLEN) {
                sb.append(" ");
            }
        }

        raf.writeChars(sb.toString());

        // Write numeric fields
        raf.writeInt(qty);
        raf.writeInt(ucost);
    }

    // Represents one part
    public static class Part {

        private String partnum;
        private String desc;
        private int qty;
        private int ucost;

        public Part(String partnum, String desc,
                    int qty, int ucost) {

            this.partnum = partnum;
            this.desc = desc;
            this.qty = qty;
            this.ucost = ucost;
        }

        public String getPartnum() {
            return partnum;
        }

        public String getDesc() {
            return desc;
        }

        public int getQty() {
            return qty;
        }

        public int getUnitCost() {
            return ucost;
        }

        @Override
        public String toString() {
            return "Part number: " + partnum
                    + ", Description: " + desc
                    + ", Quantity: " + qty
                    + ", Unit cost: " + ucost;
        }
    }

    // Main method so the program can be executed
    public static void main(String[] args) {

        try {

            PartsDB db = new PartsDB("parts.dat");

            // Add some records
            db.append("P001", "Keyboard", 10, 2500);
            db.append("P002", "Mouse", 20, 1500);
            db.append("P003", "Monitor", 5, 12000);

            System.out.println("Number of records: "
                    + db.numRecs());

            // Read records
            System.out.println("\nReading records:");

            for (int i = 0; i < db.numRecs(); i++) {
                System.out.println(db.select(i));
            }

            // Update record 1
            db.update(1, "P002", "Wireless Mouse", 25, 2000);

            System.out.println("\nAfter updating record 1:");

            System.out.println(db.select(1));

            db.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}