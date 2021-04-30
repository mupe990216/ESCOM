package memoramacliente;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author fnico
 */
class Carta extends JLabel{
    private boolean presionado;
    private int id;
    private int x_pos, y_pos;
    private boolean parejaEncontrada;
    private Icon iconoOculto;
    private Icon iconoNormal;

    public Carta(int id) {
        this.id = id; 

        this.presionado = false;
        this.parejaEncontrada = false;
    }
    
    public Carta(int id, int x_pos, int y_pos) {
        this.id = id; 
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.presionado = false;
        this.parejaEncontrada = false;
    }
    
            

    public int getX_pos() {
        return x_pos;
    }

    public void setX_pos(int x_pos) {
        this.x_pos = x_pos;
    }

    public int getY_pos() {
        return y_pos;
    }

    public void setY_pos(int y_pos) {
        this.y_pos = y_pos;
    }
    
    


    public void setIconoNormal(Icon iconoNormal) {
        this.iconoNormal = iconoNormal;
    }

    public Icon getIconoNormal() {
        return iconoNormal;
    }
    
    public void setIconoOculto(Icon iconoOculto){
        this.iconoOculto = iconoOculto;
    }
    
    public Icon getIconoOculto(){
        return iconoOculto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPresionado(boolean presionado) {
        this.presionado = presionado;
    }
    
    public boolean getPresionado(){
        return presionado;
    }   

    public boolean getParejaEncontrada() {
        return parejaEncontrada;
    }

    public void setParejaEncontrada(boolean parejaEncontrada) {
        this.parejaEncontrada = parejaEncontrada;
    }
}
