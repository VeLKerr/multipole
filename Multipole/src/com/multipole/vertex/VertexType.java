
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
    },
    AND{
        @Override
        public String toString(){
            return "AND";
        }
    },
    OR{
        @Override
        public String toString(){
            return "OR";
        }
    },
    NOT{
        @Override
        public String toString(){
            return "NOT";
        }
    };
    
    @Override
    public abstract String toString();
    
    protected boolean isElement(){
        return !this.name().equals(INPUT.name());
    }
}
