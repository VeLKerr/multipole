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
    public static final String filename = "out.txt";
    public static final int varCnt = 3;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Schema schema = new Schema();
        schema.generate2stLayer();
        System.out.println(schema.toString());
//        SchemaTextView.test();
    }
    
}
