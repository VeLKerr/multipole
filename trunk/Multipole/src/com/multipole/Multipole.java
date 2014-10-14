package com.multipole;

import java.io.IOException;

/**
 *
 * @author Ivchenko Oleg (Kirius VeLKerr)
 */
public class Multipole {
    public static final String filename = "input.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Schema schema = new Schema();
        schema.generateSchema();
        schema.toTextFile(filename);
    }
}
