
package com.multipole.vertex;

import java.util.Comparator;

/**
 *
 * @author Ivchenko Oleg (Kirius VeLKerr)
 */
public class VertexComparator implements Comparator<Vertex>{

    @Override
    public int compare(Vertex v1, Vertex v2) {
        if(v1.getIndex() > v2.getIndex()){
            return 1;
        }
        else if(v1.getIndex() < v2.getIndex()){
            return -1;
        }
        return 0;
    }
    
}
