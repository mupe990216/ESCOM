/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatmulticast;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author fnico
 */
public class Filtro extends DocumentFilter {
    private int tamMaxString = 128;
    private int actualLength;
    public Filtro(){
        actualLength = 0;
    }
    
    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException{
        if(minLength(string)){
            super.insertString(fb, offset, string, attr);
            actualLength = 0;
        }
    }
    
    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException{
        if(minLength(text)){
            super.insertString(fb, offset, text, attrs);
            actualLength += 1;
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        if(offset == 1){
            super.remove(fb, 0, length);
            actualLength = 0;
        }else{
            super.remove(fb, offset, length);
            actualLength -= 1;
        }
    }

    
    
    public void setActualLength(int actualLength) {
        this.actualLength = actualLength;
    }

    public int getTamMaxString() {
        return tamMaxString;
    }
    
    private boolean minLength(String s){
        if(s == null){
            return false;
        }
        
        return actualLength < tamMaxString;
    }
}
