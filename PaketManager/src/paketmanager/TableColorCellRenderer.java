/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paketmanager;

import java.awt.Color;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author sedoox
 */
public class TableColorCellRenderer implements TableCellRenderer {
    private static final DefaultTableCellRenderer RENDERER = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        RENDERER.setHorizontalAlignment(JLabel.CENTER);
        Component c = RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        
        if (column == 5 && !(table.getModel() == Main.getForm().getArchiv())) {
			String data = (String) table.getModel().getValueAt(row, column);
			Color color = null;
			Calendar cal = Calendar.getInstance();
			Calendar heute = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
			try {
				cal.setTime(dateFormat.parse(data));
				heute.setTime(dateFormat.parse(dateFormat.format(new Date())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if (cal.compareTo(heute) > 0) {
				c.setForeground(color.green);
			} else if (cal.compareTo(heute) == 0) {
				c.setForeground(color.orange);
			} else {
				c.setForeground(color.red);
			}
			
			
			
		} else {
			c.setForeground(null);
		}
        
        return c;
    }
    
    
}
