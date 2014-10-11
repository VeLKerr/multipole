package com.multipole;

import com.multipole.vertex.VertexType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections4.map.MultiValueMap;
import static com.multipole.Multipole.filename;
import java.io.OutputStreamWriter;
import java.util.Collections;

/**
 * @author Ivchenko Oleg (Kirius VeLKerr)
 */
public class SchemaTextView {
    private MultiValueMap<Integer, Integer> invertedList;
    private Map<Integer, VertexType> elements;

    public SchemaTextView() {
        invertedList = new MultiValueMap<>();
        elements = new HashMap<>();
    }
    
    public int maxIndex(){
        return Collections.max(invertedList.keySet());
    }
    
    public void add1VarMultipole(){
        int maxIndex = maxIndex();
        elements.put(maxIndex + 1, VertexType.INPUT);
        elements.put(maxIndex + 2, VertexType.NOT);
        invertedList.put(maxIndex + 2, maxIndex + 1);
    }
    
    public static void test() throws IOException{
        SchemaTextView schema = new SchemaTextView();
        for(int i=1; i<4; i++){
            schema.invertedList.put(i, 0);
        }
        schema.invertedList.put(2, 1);
        schema.invertedList.put(3, 1);
        
        schema.elements.put(0, VertexType.INPUT);
        schema.elements.put(1, VertexType.NOT);
        schema.elements.put(2, VertexType.AND);
        schema.elements.put(3, VertexType.OR);
        
        schema.toTextFile(filename);
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<Integer, Integer>> it = invertedList.iterator();
        while(it.hasNext()){
            Entry<Integer, Integer> entry = it.next();
            sb.append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        for(Entry<Integer, VertexType> entry: elements.entrySet()){
//            if(entry.getValue().isElement()){
                sb.append(entry.getKey()).append(" ");
                sb.append(entry.getValue().toString()).append("\n");
//            }
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
