
package com.multipole;

import java.util.List;
import static com.multipole.Schema.VAR_CNT;
import java.util.ArrayList;

/**
 * @author Ivchenko Oleg (Kirius VeLKerr)
 */
public abstract class InitialMasives {
    private static final List<boolean[]> initialValues = init();
    
    private static List<boolean[]> init(){
        List<boolean[]> list = new ArrayList<>();
        int size = (int)Math.pow(2, VAR_CNT);
        int invertIndex = size / 2;
        for(int i=0; i<VAR_CNT; i++){
            boolean[] res = new boolean[size];
            boolean value = true;
            for(int j=0; j<res.length; j++){
                if(j % (invertIndex / (Math.pow(2, i))) == 0){
                    value = invert(value);
                }
                res[j] = value;
            }
            list.add(res);
        }
        return list;
    }
    
    private static boolean invert(boolean value){
        return !value;
    }
    
    public static boolean[] getInitialValues(int varNumber){
        return initialValues.get(varNumber);
    }
}
