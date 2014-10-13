
package com.multipole.vertex;

/**
 *
 * @author Ivchenko Oleg (Kirius VeLKerr)
 */
public enum VertexType {
    INPUT{
        @Override
        public String toString(){
            return "";
        }

        @Override
        public boolean countValue(boolean... values) {
            return values[0];
        }
        
    },
    AND{
        @Override
        public String toString(){
            return "AND";
        }

        @Override
        public boolean countValue(boolean... values) {
            return values[0] & values[1];
        }
    },
    OR{
        @Override
        public String toString(){
            return "OR";
        }

        @Override
        public boolean countValue(boolean... values) {
            return values[0] | values[1];
        }
    },
    NOT{
        @Override
        public String toString(){
            return "NOT";
        }

        @Override
        public boolean countValue(boolean... values) {
            return !values[0];
        }
    };
    
    @Override
    public abstract String toString();
    
    protected abstract boolean countValue(boolean... values);
    
    protected boolean[] countValues(boolean[]... values){
        boolean[] res = new boolean[values[0].length];
        for(int i=0; i<res.length; i++){
            if(values.length == 1){
                res[i] = countValue(values[0][i]);
            }
            else{
                res[i] = countValue(values[0][i], values[1][i]);
            }
        }
        return res;
    }
    
    protected boolean isElement(){
        return !this.name().equals(INPUT.name());
    }
}
