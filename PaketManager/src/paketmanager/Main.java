package paketmanager;

import javax.swing.JFrame;

public class Main {
    
    private static PaketManagerForm form;
    
    public static void main(String[] args) throws Exception{
        
        form = new PaketManagerForm();
        form.setExtendedState(JFrame.MAXIMIZED_BOTH);
        form.setVisible(true);
    
    }
    
    public static PaketManagerForm getForm() {
        return form;
    }
}
