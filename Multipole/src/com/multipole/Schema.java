
package com.multipole;

import com.multipole.vertex.Vertex;
import com.multipole.vertex.VertexType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.map.MultiValueMap;

/**
 *
 * @author Ivchenko Oleg (Kirius VeLKerr)
 */
public class Schema {
    public static final int VAR_CNT = 3;
    private final MultiValueMap<Integer, Integer> ajacencyMap;
    private final List<Vertex> vertices;
    
    public Schema(){
        this.ajacencyMap = new MultiValueMap<>();
        this.vertices = new ArrayList<>();
    }
    
    private int add1VarMulipole(){
        int initialSize = vertices.size();
        vertices.add(new Vertex(vertices.size(), VertexType.INPUT));
        vertices.add(new Vertex(vertices.size(), VertexType.NOT));
        ajacencyMap.put(initialSize, initialSize + 1);
        return initialSize;
    }
    
    private int[] generate1stLayer(){
        int[] inputIndexes = new int[VAR_CNT];
        for(int i=0; i<VAR_CNT; i++){
            inputIndexes[i] = add1VarMulipole();
        }
        return inputIndexes;
    }
    
    private int addBinaryElement(int oneIndex, int secIndex, VertexType vt){
        ajacencyMap.put(oneIndex, vertices.size());
        ajacencyMap.put(secIndex, vertices.size());
        vertices.add(new Vertex(vertices.size(), vt));
        return vertices.size() - 1;
    }
    
    public List<Integer> generate2stLayer(){ //Trere are many BUGs!!!
        int[] inputs = generate1stLayer();
        List<Integer> subOutputs = new ArrayList<>();
        List<Integer> outputs = new ArrayList<>();
        for(int i=0; i<2; i++){ //добавляем 1-й ряд конъюнкций
            for(int j=0; j<2; j++){
                subOutputs.add(addBinaryElement(inputs[0] + i, inputs[1] + j, VertexType.AND));
            }
        }
        for(int index: subOutputs){
            for(int j=0; j<2; j++){
                outputs.add(addBinaryElement(index, inputs[2] + j, VertexType.AND));
            }
        }
        return outputs;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<Integer, Integer>> it = ajacencyMap.iterator();
        while(it.hasNext()){
            Map.Entry<Integer, Integer> entry = it.next();
            sb.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }
        for(Vertex v: vertices){
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
    
    private List<Integer> getAll(VertexType vt){
        List<Integer> res = new ArrayList<>();
        for(Vertex v: vertices){
            if(v.getType().equals(vt)){
                res.add(v.getIndex());
            }
        }
        return res;
    }
}
