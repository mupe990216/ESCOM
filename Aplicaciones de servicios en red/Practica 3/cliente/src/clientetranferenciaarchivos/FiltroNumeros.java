package clientetranferenciaarchivos;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author fnico
 */
public class FiltroNumeros extends DocumentFilter {
    public FiltroNumeros(){
        
    }
    
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException{
        if(esNumeroEntero(string)){
            super.insertString(fb, offset, string, attr);
        }
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException{
        if(esNumeroEntero(text)){
            super.insertString(fb, offset, text, attrs);
        }
    }
    
    private boolean esNumeroEntero(String s){
        if(s == null || s.equals("")){
            return false;
        }
        
        for(int i = 0; i < s.length(); i++){
            if(!Character.isDigit(s.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
