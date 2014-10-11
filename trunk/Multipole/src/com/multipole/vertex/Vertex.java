
package com.multipole.vertex;

/**
 *
 * @author Ivchenko Oleg (Kirius VeLKerr)
 */
public class Vertex {
    private int index;
    private final VertexType type;
    private boolean value;

    public Vertex(int index, VertexType type) {
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public VertexType getType() {
        return type;
    }
    
    public boolean isElement(){
        return type.isElement();
    }
}
