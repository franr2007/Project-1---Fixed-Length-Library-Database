package Services;
import java.io.IOException;

import DB.PartsDB;
import DB.PartsDB.Part;

public class UsePartsDB {

    public static void main(String[] args) {

        PartsDB pdb = null;

        try {

            // Open the database
            pdb = new PartsDB("parts.db");

            // Populate the database if it is empty
            if (pdb.numRecs() == 0) {

                pdb.append(
                        "1-9009-3323-4x",
                        "Wiper Blade Micro Edge",
                        30,
                        2468
                );

                pdb.append(
                        "1-3233-44923-7j",
                        "Parking Brake Cable",
                        5,
                        1995
                );

                pdb.append(
                        "2-3399-6693-2m",
                        "Halogen Bulb H4 55/60W",
                        22,
                        813
                );

                pdb.append(
                        "2-599-2029-6k",
                        "Turbo Oil Line O-Ring",
                        26,
                        155
                );

                pdb.append(
                        "3-1299-3299-9u",
                        "Air Pump Electric",
                        9,
                        20200
                );
            }

            // Display all records
            dumpRecords(pdb);

            // Update record 1
            pdb.update(
                    1,
                    "1-3233-44923-7j",
                    "Parking Brake Cable",
                    10,
                    1995
            );

            // Display records again
            dumpRecords(pdb);

        } catch (IOException ioe) {

            System.err.println(ioe);

        } finally {

            // Close the database
            if (pdb != null) {
                pdb.close();
            }
        }
    }

    // Display all records
    static void dumpRecords(PartsDB pdb) throws IOException {

        for (int i = 0; i < pdb.numRecs(); i++) {

            PartsDB.Part part = pdb.select(i);

            System.out.print(
                    format(
                            part.getPartnum(),
                            PartsDB.PNUMLEN,
                            true
                    )
            );

            System.out.print(" | ");

            System.out.print(
                    format(
                            part.getDesc(),
                            PartsDB.DESCLEN,
                            true
                    )
            );

            System.out.print(" | ");

            System.out.print(
                    format(
                            "" + part.getQty(),
                            10,
                            false
                    )
            );

            System.out.print(" | ");

            // Unit cost is stored in cents
            String s = part.getUnitCost() / 100
                    + "."
                    + part.getUnitCost() % 100;

            // Add a leading zero if necessary
            if (s.charAt(s.length() - 2) == '.') {
                s += "0";
            }

            System.out.println(
                    format(
                            s,
                            10,
                            false
                    )
            );
        }

        System.out.println(
                "Number of records = " + pdb.numRecs()
        );

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
