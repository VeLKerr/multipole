/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.multipole;

import java.io.IOException;
import static com.multipole.Schema.VAR_CNT;
import java.util.Arrays;

/**
 *
 * @author Ivchenko Oleg (Kirius VeLKerr)
 */
public class Multipole {
    public static final String filename = "out.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Schema schema = new Schema();
        schema.generate3stLayer();
        System.out.println(schema.toString());
        for(int i=0; i<VAR_CNT; i++){
        System.out.println(Arrays.toString(InitialMasives.getInitialValues(i)));
        }
    }
}
