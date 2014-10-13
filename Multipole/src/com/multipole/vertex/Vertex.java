
package com.multipole.vertex;

import static com.multipole.Schema.VAR_CNT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Ivchenko Oleg (Kirius VeLKerr)
 */
public class Vertex{   
    private Vertex parent1;
    private Vertex parent2;
    private int index;
    private final VertexType type;
    private boolean[] values;
    
    public Vertex(int index, VertexType type, boolean[] values) {
        this.index = index;
        this.type = type;
        this.parent1 = null;
        this.parent2 = null;
        this.values = values;
    }
    
    public Vertex(int index, VertexType type, Vertex parent) {
        this.index = index;
        this.type = type;
        this.parent1 = parent;
        this.parent2 = null;
        this.values = type.countValues(parent1.values);
    }

    public Vertex(int index, VertexType type, Vertex parent1, Vertex parent2) {
        this.index = index;
        this.type = type;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.values = type.countValues(parent1.values, parent2.values);
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

    @Override
    public int hashCode() {
        int hash = 0;
        for(int i=0; i<values.length; i++){
            if(values[i]){
                hash += Math.pow(10, i);
            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vertex other = (Vertex) obj;
        return Arrays.equals(this.values, other.values);
    }
    
    public List<Vertex> getParents(){
        List<Vertex> parents = new ArrayList<>();
        parents.add(parent1);
        parents.add(parent2);
        return parents;
    }
    
    public String _valuesToString(){
        StringBuilder sb = new StringBuilder(" ");
        for(boolean v: values){
            if(v){
                sb.append(1);
            }
            else{
                sb.append(0);
            }
            sb.append(" ");
        }
        return sb.toString();
    }

    public boolean[] _getValues() {
        return values;
    }
}
