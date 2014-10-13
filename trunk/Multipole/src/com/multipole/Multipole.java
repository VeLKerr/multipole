/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
