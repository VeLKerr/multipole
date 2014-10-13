
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
import java.util.Arrays;
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
    
    public Schema(){
        this.ajacencyMap = new MultiValueMap<>();
        this.vertices = new TreeSet<>(new VertexComparator());
    }
    
    private Vertex[] add1VarMulipole(int cnt){
        int initialSize = vertices.size();
        Vertex[] inputs = new Vertex[2];
        Vertex input = new Vertex(vertices.size(), VertexType.INPUT, InitialMasives.getInitialValues(cnt));
        inputs[0] = input;
        vertices.add(input);
        Vertex not = new Vertex(vertices.size(), VertexType.NOT, input);
        inputs[1] = not;
        vertices.add(not);
        ajacencyMap.put(initialSize, initialSize + 1);
        return inputs;
    }
    
    private List<Vertex[]> generate1stLayer(){
        List<Vertex[]> res = new ArrayList<>();
        for(int i=0; i<VAR_CNT; i++){
            res.add(add1VarMulipole(i));
        }
        return res;
    }
    
    private Vertex addBinaryElement(Vertex vertex1, Vertex vertex2, VertexType vt){  
        Vertex newVertex = new Vertex(vertices.size(), vt, vertex1, vertex2);
        if(vertices.add(newVertex)){
            ajacencyMap.put(vertex1.getIndex(), vertices.size());
            ajacencyMap.put(vertex2.getIndex(), vertices.size());
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
    
    public void generate3stLayer(){
        List<Vertex> inputs = generate2stLayer();
        List<Vertex> output = new ArrayList<>();
        for(int i=0; i<inputs.size(); i++){
            for(int j=i+1; j<inputs.size(); j++){
                output.add(addBinaryElement(inputs.get(i), inputs.get(j), VertexType.OR));
            }
        }
        for(int i=0; i<2; i++){
            output = generateNextLayer(inputs, output);
        }
        
        int k=0;
        for(Vertex v1: vertices){
            for(Vertex v2: vertices){
                if(v1.getIndex() != v2.getIndex() &&
                   Arrays.equals(v1.getValues(), v2.getValues())){
                    //System.err.println("КАРАУЛ!");
                    k++;
                }
            }
        }
        System.err.println(k / 2);
//        for(int i=0; i<ouptut2.size(); i++){
//            for(int j=i+1; j<ouptut2.size(); j++){
//                output4.add(addBinaryElement(i, j, VertexType.OR));
//            }
//        }
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
                sb.append(v.getType().toString()).append(v.valuesToString())
                        .append("\n");
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
    
    private Vertex findBy(int index){
        Iterator<Vertex> it = vertices.iterator();
        while(it.hasNext()){
            Vertex vert = it.next();
            if(vert.getIndex() == index){
                return vert;
            }
        }
        return null;
    }
}
