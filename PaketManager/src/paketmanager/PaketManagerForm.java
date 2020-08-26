/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paketmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Rectangle;

import datenbank.Datenbank;

/**
 *
 * @author sedoox
 */

public class PaketManagerForm extends javax.swing.JFrame {
    
    final Runnable errorSound = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
    DefaultTableModel tableModel;
    Datenbank datenbank;
    boolean clicked;
    DefaultTableModel archiv;
    
    public PaketManagerForm() {
        initComponents();
        
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("paket.png")));
        tableModel = (DefaultTableModel) jTable.getModel();
        jTable.setRowHeight(25);
        archiv = new DefaultTableModel(new Object[][] {}, new String[]  {"Rückgabe am", "Nachname", "Vorname", "Anschrift", "Nummer", "Express", "Kunden hat abgeholt", "Angekommen am"});
        jTable.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer() );
        datenbank = new Datenbank();
        jTable.setDefaultRenderer(Object.class, new TableColorCellRenderer());
        

        this.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent componentEvent){
                //resize();
            }
        });
        searchText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (jTable.getModel() == archiv) {
					datenbank.loadArchiv();
				} else {
					datenbank.loadPakete();
				}
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (jTable.getModel() == archiv) {
					datenbank.loadArchiv();
				} else {
					datenbank.loadPakete();
				}
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                System.out.println(".changedUpdate()");
            }
        });

        this.getContentPane().add(jScrollPane, BorderLayout.PAGE_START);
        this.getContentPane().add(lblArchiv, BorderLayout.PAGE_END);
        this.getContentPane().add(lblPakete, BorderLayout.PAGE_END);
        this.getContentPane().add(addButton, BorderLayout.PAGE_END);
        this.getContentPane().add(searchText, BorderLayout.PAGE_END);
        this.getContentPane().add(removeButton, BorderLayout.PAGE_END);
        this.getContentPane().add(lblPaketmanagerBySerdar, BorderLayout.PAGE_END);
        this.getContentPane().add(archivButton, BorderLayout.PAGE_END);

        this.pack();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
    	this.setTitle("PaketManager 2.0");
        jScrollPane = new javax.swing.JScrollPane();
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jTable = new javax.swing.JTable();
        jTable.setShowVerticalLines(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTable.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nummer", "Nachname", "Vorname", "Anschrift", "Express", "Rücksendung am"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.setToolTipText("");
        jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable.setFocusable(false);
        jTable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable.setMaximumSize(new java.awt.Dimension(2147483647, 100));
        jTable.setOpaque(false);
        jTable.setRowHeight(25);
        jTable.setSelectionBackground(new java.awt.Color(232, 57, 95));
        jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable.setShowGrid(true);
        jTable.getTableHeader().setResizingAllowed(false);
        jTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane.setViewportView(jTable);
        jScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        
        archivButton = new JToggleButton("archiv");
        archivButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if (jTable.getModel() == tableModel) {
                    jTable.setModel(archiv);
                    addButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    datenbank.loadArchiv();
                    archivButton.setBackground(Color.green);
                } else {
                    jTable.setModel(tableModel);
                    addButton.setEnabled(true);
                    removeButton.setEnabled(true);
                    datenbank.loadPakete();
                    archivButton.setBackground(Color.GRAY);
                }
        		searchText.setText("");
        	}
        });
        archivButton.setSelected(true);
        archivButton.setActionCommand("");
        searchText = new javax.swing.JTextField();
        
        searchText.setFont(new java.awt.Font("Segoe UI", 1, 14));
        addButton = new javax.swing.JButton();
        addButton.setBackground(new java.awt.Color(204, 204, 204));
        addButton.setText("Paket hinzufügen");
        addButton.setFocusPainted(false);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        removeButton = new javax.swing.JButton();
        removeButton.setBackground(new java.awt.Color(204, 204, 204));
        removeButton.setText("Paket löschen");
        removeButton.setFocusPainted(false);
        removeButton.setPreferredSize(new java.awt.Dimension(115, 22));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        
        lblPakete = new JLabel("anzahl Pakete: ");
        lblPakete.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblArchiv = new JLabel("Archiv");
        lblArchiv.setForeground(Color.RED);
        lblArchiv.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblArchiv.setHorizontalAlignment(SwingConstants.LEFT);
        
        lblPaketmanagerBySerdar = new JLabel("PaketManager by Serdar Tuzcu");
        lblPaketmanagerBySerdar.setFont(new Font("Segoe UI", Font.PLAIN, 11));

    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        NeuesPaketForm form = new NeuesPaketForm();
        form.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        form.setVisible(true);
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        datenbank.loadPakete();
        
        getTable().setDefaultRenderer(Object.class, new TableColorCellRenderer());
        getTable().setDefaultRenderer(Integer.class, new TableColorCellRenderer());
        
        System.out.println("Tabelle erfolgreich geladen");
    }

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTable.getSelectedRow() == -1) {
            errorSound.run();
            JOptionPane.showMessageDialog(null, "Du hast keine Zeile ausgewählt!", "Fehler", JOptionPane.OK_OPTION);
        } else {
            Object[] options = {"Paket wurde abgeholt", "Paket wurde zurückgesendet", "Paket war ein Fehler"};
            int input = JOptionPane.showOptionDialog(null,
                "Wähle eine Option für das Paket von " + jTable.getValueAt(jTable.getSelectedRow(), 2) + " " + jTable.getValueAt(jTable.getSelectedRow(), 1),
                "Paket entfernen",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
            if (input >= 0) {
	            if (input == 0) {
	                datenbank.addArchiv((Integer)(jTable.getValueAt(jTable.getSelectedRow(), 0)), 1);
				} else if (input == 1) {
					datenbank.addArchiv((Integer)(jTable.getValueAt(jTable.getSelectedRow(), 0)), 0);
				}
	        	// entfernt Datensatz von Datenbank
	            datenbank.remove(Integer.valueOf(jTable.getValueAt(jTable.getSelectedRow(), 0).toString()));
	            // entfernt Datensatz aus Tabelle
	            datenbank.loadPakete();
	            // setzt Suchtext auf leer
	            searchText.setText("");
            }
        }
    }

    private void resize() {
        int width = this.getBounds().width;
        int height = this.getBounds().height;

        // new Rectangle(int x, int y, int width, int height)

        removeButton.setBounds(new Rectangle(44, 19, width/10, height/30));
    } 
        
    public void addPaket(String nachname, String vorname, String anschrift, int express) {
        searchText.setText("");
        Calendar ankunft = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        datenbank.add(nachname, vorname, anschrift, express, dateFormat.format(ankunft.getTime()));
        datenbank.loadPakete();
    }
    
    public JTable getTable() {
        return jTable;
    }
    
    public DefaultTableModel getArchiv() {
        return archiv;
    }
    
    public String getSearchText() {
        return searchText.getText();
    }
    
    public Datenbank getDatenbank() {
        return datenbank;
    }

    public void setLabels(String tabelle, int anzahl) {
    	lblArchiv.setText(tabelle);
    	lblPakete.setText("anzahl Pakete: " + anzahl);
    }
    
    private JLabel lblArchiv;
    private JLabel lblPakete;
    private javax.swing.JButton addButton;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField searchText;
    private JScrollPane jScrollPane;
    private JToggleButton archivButton;
    private JButton removeButton;
    private JLabel lblPaketmanagerBySerdar;
}
