package datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import paketmanager.Main;

/**
 *
 * @author sedoox
 */

public class Datenbank {
    
    private Connection connection;
    private JTable table;
    private boolean database;
    
    public Datenbank() {
        try {
            connection = getConnection();
        } catch (Exception ex) {
            Logger.getLogger(Datenbank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Connection getConnection() throws Exception {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost/paketmanager?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String username = "root";
            String password = "galatasaray";
            Class.forName(driver);
            
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("connected");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        return null;
    }
    
    public void add(String nachname, String vorname, String anschrift, int express, String ankunft) {
        try {
            PreparedStatement posted = connection.prepareStatement("INSERT INTO aktuellepakete VALUES ('" + getNextNumber() + "', '" + nachname + "', '" + vorname + "', '" + anschrift + "', '" + express + "', '" + ankunft + "')");
            posted.execute();
        } catch (SQLException ex) {
            System.out.println("Fehler beim speichern");
        }
    }
    
    public void addArchiv(int nummer, int abgeholt) {
        try {
        	Statement statement = connection.createStatement();
        	ResultSet resultset = statement.executeQuery("SELECT * FROM aktuellepakete WHERE nummer = " + nummer + " ORDER BY nummer ASC");
        	
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	Calendar rückgabe = Calendar.getInstance();
        	while(resultset.next()) {
	        	PreparedStatement posted = connection.prepareStatement("INSERT INTO archiv VALUES ('" + dateFormat.format(rückgabe.getTime()) + "', '" + resultset.getString("nachname") + "', '" + resultset.getString("vorname") + "', '" + resultset.getString("anschrift") + "', '" + nummer + "', '" + resultset.getInt("express") + "', '" + abgeholt + "', '" + resultset.getDate("ankunft") + "')");
	            posted.execute();
        	}
        } catch (SQLException ex) {
        	ex.printStackTrace();
            System.out.println("Fehler beim speichern");
        }
    }

    public void remove(int paketnummer) {
        try {
        	PreparedStatement posted = connection.prepareStatement("DELETE FROM aktuellepakete WHERE nummer = " + paketnummer);
            posted.execute();
        } catch (SQLException ex) {
            System.out.println("Fehler beim entfernen");
        }
    }
    
    public void loadPakete() { 
    	table = paketmanager.Main.getForm().getTable();
        String search = paketmanager.Main.getForm().getSearchText();
        ((DefaultTableModel) table.getModel()).setRowCount(0);
        
        try {
        	Main.getForm().setLabels("aktuelle Pakete", getCount("aktuellepakete"));
            Statement statement = connection.createStatement();
            ResultSet resultset;
            if (search.equals("")) {
                resultset = statement.executeQuery("SELECT * FROM aktuellepakete ORDER BY nummer ASC");
            } else {
                resultset = statement.executeQuery("SELECT * FROM aktuellepakete WHERE nachname LIKE '%" + search + "%' OR vorname LIKE '%" + search + "%' OR anschrift LIKE '%" + search + "%' OR nummer LIKE '%" + search + "%' ORDER BY nummer ASC");
            }
        	
            while(resultset.next()) {
            	String express;
            	if (resultset.getInt("express") == 1) {
    				express = "X";
    			} else {
    				express = "-";
    			}
            	
            	String nachname = resultset.getString("nachname");
            	if (nachname.equals("")) {
					nachname = "-";
				}
            	
            	String vorname = resultset.getString("vorname");
            	if (vorname.equals("")) {
					vorname = "-";
				}
            	
            	String anschrift = resultset.getString("anschrift");
            	if (anschrift.equals("")) {
					anschrift = "-";
				}
            	
            	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            	
            	Calendar ankunft = Calendar.getInstance();
            	ankunft.setTime(dateFormat.parse(resultset.getString("ankunft")));
            	ankunft.add(Calendar.DAY_OF_MONTH, 9);
            	
            	dateFormat = new SimpleDateFormat("dd.MM.yy");
            	
                Object[] data = {resultset.getInt("nummer"), nachname, vorname, anschrift, express, dateFormat.format(ankunft.getTime())};
                ((DefaultTableModel) table.getModel()).addRow(data);
            }
            
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    public void loadArchiv() {
    	table = paketmanager.Main.getForm().getTable();
        String search = paketmanager.Main.getForm().getSearchText();
        ((DefaultTableModel) table.getModel()).setRowCount(0);
        
        try {
        	Main.getForm().setLabels("Archiv", getCount("archiv"));
            Statement statement = connection.createStatement();
            ResultSet resultset;
            if (search.equals("")) {
                resultset = statement.executeQuery("SELECT * FROM archiv ORDER BY rückgabe DESC");
            } else {
                resultset = statement.executeQuery("SELECT * FROM archiv WHERE nachname LIKE '%" + search + "%' OR vorname LIKE '%" + search + "%' OR anschrift LIKE '%" + search + "%' OR nummer LIKE '%" + search + "%' ORDER BY rückgabe DESC");
            }

            while(resultset.next()) {

            	String abgeholt;
            	if (resultset.getInt("abgeholt") == 1) {
    				abgeholt = "ja";
    			} else {
    				abgeholt = "nein";
    			}
                
                String express;
            	if (resultset.getInt("express") == 1) {
    				express = "X";
    			} else {
    				express = "-";
    			}
            	
            	String nachname = resultset.getString("nachname");
            	if (nachname.equals("")) {
					nachname = "-";
				}
            	
            	String vorname = resultset.getString("vorname");
            	if (vorname.equals("")) {
					vorname = "-";
				}
            	
            	String anschrift = resultset.getString("anschrift");
            	if (anschrift.equals("")) {
					anschrift = "-";
				}
            	
            	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            	
            	Calendar zurück = Calendar.getInstance();
            	zurück.setTime(dateFormat.parse(resultset.getString("rückgabe")));
            	
            	Calendar ankunft = Calendar.getInstance();
            	ankunft.setTime(dateFormat.parse(resultset.getString("ankunft")));
            	
            	dateFormat = new SimpleDateFormat("dd.MM.yy");
            	
                Object[] data = {dateFormat.format(zurück.getTime()), nachname, vorname, anschrift, resultset.getInt("nummer"), express, abgeholt, dateFormat.format(ankunft.getTime())};
                ((DefaultTableModel) table.getModel()).addRow(data);
            }
            
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    public int getNextNumber() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery("SELECT nummer FROM aktuellepakete ORDER BY nummer ASC");
            
            int counter = 1;
            while(resultset.next()) {
                if (resultset.getInt("nummer") - counter != 0) {
                    return counter;
                }
                counter = counter + 1;
            }
            return counter;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return 0;
    }
    
    public String getDatenbankString() {
    	if (database) {
    		return "archiv";
		} else {
			return "aktuellepakete";
		}
    }
    
    public void changeDatabase() {

    	database = !database;
    }

    private int getCount(String table) {
		try {
			Statement statement = connection.createStatement();
	        ResultSet resultset = statement.executeQuery("SELECT Count(*) FROM " + table);
	        int anzahl = 0;
	        while (resultset.next()) {
	        	anzahl = resultset.getInt("Count(*)");
			}
	        return anzahl;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
    }
}

