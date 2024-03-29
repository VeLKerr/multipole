
package com.multipole;

import com.multipole.vertex.Vertex;
import com.multipole.vertex.VertexComparator;
import com.multipole.vertex.VertexType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.collections4.map.MultiValueMap;

/**
 *
 * @author Ivchenko Oleg (Kirius VeLKerr)
 */
public class Schema {
    public static final int UNDEFINED = -1;
    public static final int VAR_CNT = 3;
    private final MultiValueMap<Integer, Integer> ajacencyMap;
    private final Set<Vertex> vertices;
    private final Set<Vertex> sortedVertices;
    
    public Schema(){
        this.ajacencyMap = new MultiValueMap<>();
        this.vertices = new HashSet<>();
        this.sortedVertices = new TreeSet<>(new VertexComparator());
    }
    
    private Vertex[] genInputs(){
        Vertex[] res = new Vertex[VAR_CNT];
        for(int i=0; i<res.length; i++){
            res[i] = new Vertex(i, VertexType.INPUT, InitialMasives.getInitialValues(i));
            vertices.add(res[i]);
        }
        return res;
    }
    
    private List<Vertex[]> generate1stLayer(){
        Vertex[] inputs = genInputs();
        List<Vertex[]> res = new ArrayList<>();
        for(int i=0; i<inputs.length; i++){
            Vertex[] verts = new Vertex[2];
            verts[0] = inputs[i];
            verts[1] = new Vertex(vertices.size(), VertexType.NOT, verts[0]);
            ajacencyMap.put(verts[0].getIndex(), verts[1].getIndex());
            res.add(verts);
            vertices.add(verts[1]);
        }
        return res;
    }
    
    private Vertex addBinaryElement(Vertex vertex1, Vertex vertex2, VertexType vt){  
        Vertex newVertex = new Vertex(vertices.size(), vt, vertex1, vertex2);
        if(vertices.add(newVertex)){
            ajacencyMap.put(vertex1.getIndex(), vertices.size() - 1);
            ajacencyMap.put(vertex2.getIndex(), vertices.size() - 1);
            return newVertex;
        }
        return null;
    }
    
    private List<Vertex> generate2stLayer(){
        List<Vertex[]> inputs = generate1stLayer();
        List<Vertex> subOutputs = new ArrayList<>();
        List<Vertex> outputs = new ArrayList<>();
        for(int i=0; i<2; i++){ //добавляем 1-й ряд конъюнкций
            for(int j=0; j<2; j++){
                subOutputs.add(addBinaryElement(inputs.get(0)[i], inputs.get(1)[j], VertexType.AND));
            }
        }
        for(Vertex vert: subOutputs){ //2-й ряд
            for(int j=0; j<2; j++){
                outputs.add(addBinaryElement(vert, inputs.get(2)[j], VertexType.AND));
            }
        }
        return outputs;
    }
    
    public void generateSchema(){
        List<Vertex> inputs = generate2stLayer();
        List<Vertex> output = new ArrayList<>();
        for(int i=0; i<inputs.size(); i++){
            for(int j=i+1; j<inputs.size(); j++){
                Vertex vert = addBinaryElement(inputs.get(i), inputs.get(j), VertexType.OR);
                if(vert != null){
                    output.add(vert);
                }
            }
        }
        for(int i=0; i<7; i++){
            output = generateNextLayer(inputs, output);
        }
        List<Vertex> additional = findBy(new int[]{0, 3});
        addBinaryElement(additional.get(0), additional.get(1), VertexType.AND);
        sortedVertices.addAll(vertices);
    }
    
    private List<Vertex> generateNextLayer(List<Vertex> inputs, List<Vertex> outpPrevious){ //могут быть повторения!
        List<Vertex> res = new ArrayList<>();
        for(Vertex outP: outpPrevious){
            for(Vertex inp: inputs){
                if(outP.getParents().get(0).getIndex() != inp.getIndex() &&
                   outP.getParents().get(1).getIndex() != inp.getIndex()){
                    Vertex newV = addBinaryElement(outP, inp, VertexType.OR);
                    if(newV != null){
                        res.add(newV);
                    }
                }
            }
        }
        return res;
    }
    
    private List<Vertex> findBy(int[] indexes){
        List<Vertex> res = new ArrayList<>();
        Iterator<Vertex> it = vertices.iterator();
        while(it.hasNext()){
            Vertex v = it.next();
            for(int index: indexes){
                if(v.getIndex() == index){
                    res.add(v);
                }
            }
        }
        return res;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<Integer, Integer>> it = ajacencyMap.iterator();
        while(it.hasNext()){
            Map.Entry<Integer, Integer> entry = it.next();
            sb.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }
        for(Vertex v: sortedVertices){
            if(v.isElement()){
                sb.append(v.getIndex()).append(" ");
                sb.append(v.getType().toString()).append("\n");
            }
        }
        return sb.toString();
    }
    
    public void toTextFile(String filename) throws FileNotFoundException, IOException{
        System.out.println(toString());
        OutputStream os = new FileOutputStream(new File(filename));
        OutputStreamWriter osw = new OutputStreamWriter(os);
        osw.write(toString());
        osw.flush();
        osw.close();
    }
}
