package mine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;
import java.util.ListIterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.StringTokenizer;
/**
 * Session Bean implementation class Checker
 */

public class Checker extends Maintenance {

    public static boolean isStringNullOrEmpty(String word) {
    	boolean empty = true;
    	if(word!=null) {
    		StringTokenizer st = new StringTokenizer(word);
    		if(st.countTokens()>0) {
    			String tkn = st.nextToken();
    			if(!tkn.equalsIgnoreCase("null")) {
    				empty = false;
    			}
    		}
    	}
    	//System.out.println("checker word="+word+" "+empty);
    	return empty;
    }

    public static String pnn(String word) {//printnotnull
    	String tmp="";
    	if(word!=null) {
    		StringTokenizer st = new StringTokenizer(word);
    		if(st.countTokens()>0) {
    			while(st.hasMoreTokens()) {
    				String tkn = st.nextToken();
    				if(tkn.equalsIgnoreCase("null")) {
    					tmp = tmp+"";
    				}
    				else {
    					tmp=tmp+tkn;
    				}
    				if(st.hasMoreTokens()) {
    					tmp = tmp+" ";
    				}
    			}
    			if(isStringNullOrEmpty(tmp)) {
    				word="";
    			}
    		}
    		else {
    			word="";
    		}
    	}
    	return word;
    }
    
    public static String getThsmsPmb() {
    	String thsms =null;
    	String url=null;     

    	try {
    		connectToMysql();
    		//cretae NPM auto increment
    		stmt = con.prepareStatement("SELECT * FROM CALENDAR where AKTIF=?");
    		stmt.setBoolean(1,true);
    		rs = stmt.executeQuery();
    		if(rs.next()) {
    			thsms = rs.getString("THSMS_PMB");
    		}
    	} 
        catch (SQLException ex) {
        	ex.printStackTrace();
        } 
        finally {
        	if (con!=null) {
        		try {
        			con.close();
        		}
        		catch (Exception ignore) {
            		System.out.println(ignore);
        		}
        	}
        }
    	return thsms;	
    }
    
    public static boolean isUsrNameAvailable(String usrnm, String npm) {
    	//cek apa udah ada yg punya, kalaupun ada apa punya sendiri
    	boolean avail = false;
    	String id_usr = null;

    	try {
    		connectToMysql();
    		stmt = con.prepareStatement("SELECT * FROM USR_DAT where USR_NAME=?");
    		stmt.setString(1, usrnm);
    		ResultSet rs = stmt.executeQuery();
    		if(rs.next()) {
    			id_usr = ""+rs.getInt("ID");
    		} 	
    		else {
    			avail = true;
    		}
    		if(id_usr!=null) {
    			String nonpm = "";
        		stmt = con.prepareStatement("select * from CIVITAS where ID=?");
        		stmt.setInt(1, Integer.valueOf(id_usr).intValue());
        		rs = stmt.executeQuery();
        		if(rs.next()) {
        			nonpm = rs.getString("NPMHSMSMHS");
        			if(nonpm.equalsIgnoreCase(npm)) {
        				avail=true;
        			}
        		}
    		}
    	}
    	catch (SQLException ex) {
    		ex.printStackTrace();
    	} 
    	finally {
    		if (con!=null) {
    			try {
    				con.close();
    			}
    			catch (Exception ignore) {
    				System.out.println(ignore);
    			}
    		}
    	}
    	return avail;
    }
    
    public static String getThsmsNow() {
    	String thsms =null;
    	String url=null;     

    	try {
    		connectToMysql();
    		//cretae NPM auto increment
    		stmt = con.prepareStatement("SELECT * FROM CALENDAR where AKTIF=?");
    		stmt.setBoolean(1,true);
    		rs = stmt.executeQuery();
    		if(rs.next()) {
    			thsms = rs.getString("THSMS");
    		}
    	} 
        catch (SQLException ex) {
        	ex.printStackTrace();
        } 
        finally {
        	if (con!=null) {
        		try {
        			con.close();
        		}
        		catch (Exception ignore) {
            		System.out.println(ignore);
        		}
        	}
        }
    	return thsms;	
    }

}
