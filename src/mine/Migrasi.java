package mine;


import java.text.*;
import java.io.*;
import java.sql.*;
import java.net.*;
import java.util.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.awt.Color;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.tomcat.jdbc.pool.DataSource;




//import beans.setting.Constants;
//import beans.tools.Checker;



class Migrasi extends Maintenance {

	public static void setStpid() {
		//bila ada di trnlp maka = P else B
		try{
			connectToMysql();
			stmt=con.prepareStatement("update CIVITAS set STPIDMSMHS='B'");
			System.out.println("set stpid=B "+stmt.executeUpdate());
			stmt = con.prepareStatement("select * from TRNLP");
			rs = stmt.executeQuery();
			Vector v = new Vector();
			ListIterator li = v.listIterator();
			while(rs.next()) {
				String kdpst = ""+rs.getString("KDPSTTRNLP");
				String npmhs = ""+rs.getString("NPMHSTRNLP");
				li.add(kdpst+","+npmhs);
			}
			v=hapusDuplicateRecordFromVector(v);
			
			stmt=con.prepareStatement("update CIVITAS set STPIDMSMHS='P' where KDPSTMSMHS=? and NPMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,",");
				String kdpst = st.nextToken();
				String npmhs = st.nextToken();
				stmt.setString(1,kdpst);
				stmt.setString(2,npmhs);
				System.out.println(brs+"="+stmt.executeUpdate());
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void migrateDataTrnlpPartI(String filename) {
		Vector v = null;
		Vector v1 = null;
		Vector v2 = new Vector();
		ListIterator li2 = v2.listIterator();
		try {
			v1 = bacaFileTxt("list_makul_filtered");
			v = bacaFileTxt(filename);
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				String kdjen = st.nextToken();
				String kdpst = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				boolean match = false;
				ListIterator li1 = v1.listIterator();
				while(li1.hasNext()&&!match) {
					//20201#TE3218#STATISTIK DAN PROBABILITAS#3.0#TE 2183
					String brs1 = (String)li1.next();
					st = new StringTokenizer(brs1,"#");
					String kdpst1 = st.nextToken();
					String askmk1 = st.nextToken();
					String nakmk1 = st.nextToken();
					String sksmk1 = st.nextToken();
					String nukmk1 = st.nextToken();
					if(kdpst.equalsIgnoreCase(kdpst1)&&kdkmk.equalsIgnoreCase(askmk1)) {
						match = true;
						li2.add(brs+"#"+askmk1+"#"+nakmk1+"#"+sksmk1+"#"+nukmk1);
					}
				}
				if(!match) {
					li2.add(brs+"#"+kdkmk+"#LOOK UP TBKMK#0#"+kdkmk);
				}
			}
			writeFile(filename+"_upd",v2);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public static void migrateDataTrnlpPartIa(String filename) {
		Vector v = null;
		try {
			v = bacaFileTxt(filename+"_upd");
			
			System.out.println(v.size());
			connectToMysql();
			stmt = con.prepareStatement("select * from CIVITAS where KDPSTMSMHS=? and NIMHSMSMHS=?");
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				System.out.println(brs);
				StringTokenizer st = new StringTokenizer(brs,"#");
				String kdjen = st.nextToken();
				String kdpst = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				String askmk1 = st.nextToken();
				String nakmk1 = st.nextToken();
				String sksmk1 = st.nextToken();
				String nukmk1 = st.nextToken();
				stmt.setString(1,kdpst);
				//System.out.println(kdpst);
				stmt.setString(2,nimhs);
				//System.out.println(nimhs);
				
				rs = stmt.executeQuery();
				System.out.println(nimhs);
				rs.next();
				System.out.println("pass");
				String npmhs = rs.getString("NPMHSMSMHS");
				System.out.println(npmhs);
				li.set(brs+"#"+npmhs);
				
			}
			writeFile(filename+"_upd2",v);
		}
		catch(Exception e) {
			System.out.println();
		}		
	}
	
	public static void migrateDataTrnlpPartII(String filename) {
		Vector v = null;
		try {
			connectToMysql();
			stmt = con.prepareStatement("INSERT INTO TRNLP(THSMSTRNLP,KDPSTTRNLP,NPMHSTRNLP,KDKMKTRNLP,NLAKHTRNLP,BOBOTTRNLP,SKSMKTRNLP,KDKMKASALP,NAKMKASALP,SKSMKASAL) VALUES (?,?,?,?,?,?,?,?,?,?)");
			
			

			v = bacaFileTxt(filename+"_upd2");
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				String kdjen = st.nextToken();
				String kdpst = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				String askmk1 = st.nextToken();
				String nakmk1 = st.nextToken();
				String sksmk1 = st.nextToken();
				sksmk1 = sksmk1.substring(0,1);
				String nukmk1 = st.nextToken();
				String npmhs = st.nextToken();
				stmt.setString(1,"00000");
				stmt.setString(2, kdpst);
				stmt.setString(3, npmhs);
				stmt.setString(4, nukmk1);
				stmt.setString(5, nlakh);
				stmt.setFloat(6, Float.valueOf(bobot).floatValue());
				stmt.setInt(7, Integer.valueOf(sksmk1).intValue());
				stmt.setString(8,askmk1);
				stmt.setString(9,nakmk1);
				
				stmt.setInt(10,Integer.valueOf(sksmk1).intValue());
				
				System.out.println(npmhs+" = "+stmt.executeUpdate());
			}

		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public static void updDataMhsPindahan() {
		try {
			connectToMysql();
			stmt = con.prepareStatement("update CIVITAS set SKSDIMSMHS=?,ASNIMMSMHS=?,ASPTIMSMHS=?,ASJENMSMHS=?,ASPSTMSMHS=? where KDPSTMSMHS=? and NIMHSMSMHS=?");
			Vector v = bacaFileTxt("data_mhs_pindahan2");
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				String kdpst = st.nextToken();
				String nimhs = st.nextToken();
				String sksdi = st.nextToken();
				if(sksdi.equalsIgnoreCase("null")) {
					stmt.setNull(1,java.sql.Types.DOUBLE);
				}
				else {
					stmt.setDouble(1, Double.valueOf(sksdi).doubleValue());
				}
				String asnim = st.nextToken();
				if(asnim.equalsIgnoreCase("null")) {
					stmt.setNull(2, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(2,asnim);
				}
				String aspti = st.nextToken();
				if(aspti.equalsIgnoreCase("null")) {
					stmt.setNull(3, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(3,aspti);
				}
				String asjen = st.nextToken();
				if(asjen.equalsIgnoreCase("null")) {
					stmt.setNull(4, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(4,asjen);
				}	
				
				String aspst = st.nextToken();
				if(aspst.equalsIgnoreCase("null")) {
					stmt.setNull(5, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(5,aspst);
				}	
				stmt.setString(6, kdpst);
				stmt.setString(7, nimhs);
				System.out.println(kdpst+"-"+nimhs+" = "+stmt.executeUpdate());
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void visitasi(String filename) {
		Vector v = null;
		Vector v1 = null;
		String kdpst = "65101";
		try {
			v = bacaFileTxt(filename);
			v1 = bacaFileTxt("nakmk");
			System.out.println("v size = "+v.size());
			connectToMysql();
			stmt = con.prepareStatement("select * from CIVITAS where NIMHSMSMHS=?");
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				ListIterator li1 = v1.listIterator();
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"|");
				String thsms = st.nextToken();
				String npmhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				boolean match = false;
				while(li1.hasNext()&&!match) {
					String brs1 = (String)li1.next();
					StringTokenizer st1 = new StringTokenizer(brs1,"-");
					if(brs1.contains(kdkmk)) {
						
						st1.nextToken();
						String nakmk = st1.nextToken();
						//System.out.println(brs+" = "+nakmk);
						match = true;
						li.set(brs+"|"+nakmk);
					}
				}

			}
			
			li = v.listIterator();
			while(li.hasNext()) {
				
				String brs = (String)li.next();
				System.out.println(brs);
				StringTokenizer st = new StringTokenizer(brs,"|");
				
				String thsms = st.nextToken();
				String npmhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				String nakmk = st.nextToken();
			}	
			writeFile("krs_mip2015",v);
			/*
			li = v.listIterator();
			
			while(li.hasNext()) {
			
				String npmhs = null;
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"|");
				String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String nmmhs = st.nextToken();
				String tplhr = st.nextToken();
				String tglhr = st.nextToken();
				String kdjek = st.nextToken();
				String smawl = st.nextToken();
				String stmhs = st.nextToken();
				String stpid = st.nextToken();
				stmt = con.prepareStatement("SELECT * FROM CIVITAS where NPMHSMSMHS like ? order by NPMHSMSMHS desc");
		
    			npmhs = kdpst+thsms.substring(2,5);
    		
    			stmt.setString(1,npmhs+"%");
    			rs = stmt.executeQuery();
    			if(rs.next()) {
    				npmhs = rs.getString("NPMHSMSMHS");
    				String first8dig = npmhs.substring(0,8);
    				String tmp = npmhs.substring(npmhs.length()-5,npmhs.length());
    			//System.out.println("norut ="+tmp);
    				String norut = ""+(Long.valueOf(tmp).longValue()+1);
    				for(int i=norut.length();i<5;i++) {
    					norut = "0"+norut;
    				}
    				npmhs = first8dig+norut;
    			}
    			else {
    				npmhs = npmhs+"00001";
    			}
    			stmt = con.prepareStatement("INSERT INTO CIVITAS(ID_OBJ,KDPTIMSMHS,KDJENMSMHS,KDPSTMSMHS,NPMHSMSMHS,NIMHSMSMHS,NMMHSMSMHS,TPLHRMSMHS,TGLHRMSMHS,KDJEKMSMHS,TAHUNMSMHS,SMAWLMSMHS,STPIDMSMHS)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
				stmt.setLong(1, Long.valueOf("151").longValue());
	    		stmt.setString(2,"031031");
	    		//stmt.setString(3, kdjen);
	    		stmt.setString(3, "B");
	    		stmt.setString(4, "65101");
	    		stmt.setString(5,npmhs);
	    		stmt.setString(6,nimhs);
	    		stmt.setString(7, nmmhs.toUpperCase());
	    		stmt.setString(8, tplhr.toUpperCase());
	    		stmt.setDate(9, java.sql.Date.valueOf(tglhr));
	    		stmt.setString(10, kdjek.toUpperCase());
	    		stmt.setString(11,thsms.substring(0,4));
	    		stmt.setString(12,thsms);
	    		stmt.setString(13, stpid.toUpperCase());
	    		System.out.println(npmhs+" "+nmmhs+" ="+stmt.executeUpdate());
	    		stmt = con.prepareStatement("INSERT INTO EXT_CIVITAS(KDPSTMSMHS,NPMHSMSMHS,EMAILMSMHS,NEGLHMSMHS,NOHPEMSMHS,AGAMAMSMHS)VALUES(?,?,?,?,?,?)");
	    		stmt.setString(1, "65101");
	    		stmt.setString(2, npmhs);
	    		stmt.setString(3, "a@a.com");
	    		stmt.setString(4, "INDONESIA");
	    		stmt.setString(5, "0811");
	    		stmt.setString(6, "ISLAM");
	    		System.out.println(npmhs+" "+nmmhs+" ="+stmt.executeUpdate());
			}	
*/
			System.out.println("v size 2= "+v.size());
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void visitasi2(String filename) {
		Vector v = null;
		//Vector v1 = null;
		String kdpst = "65101";
		try {
			v = bacaFileTxt(filename);
			//v1 = bacaFileTxt("nakmk");
			System.out.println("v size = "+v.size());
			connectToMysql();
			/*
			stmt = con.prepareStatement("select * from CIVITAS where KDPSTMSMHS=? and NIMHSMSMHS=?");
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				
				String brs = (String)li.next();
				//System.out.println(brs);
				StringTokenizer st = new StringTokenizer(brs,"|");
				
				String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				String nakmk = st.nextToken();
				String idkmk = st.nextToken();
				String sksmk = st.nextToken();
				stmt.setString(1,"65101");
				stmt.setString(2, nimhs);
				
				rs = stmt.executeQuery();
				rs.next();
				
				String npmhs= rs.getString("NPMHSMSMHS");
				li.set(brs+"|"+npmhs);
			}
			writeFile("krs_mip2015_v2",v);
			*/
			
			
					
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				
				String brs = (String)li.next();
				stmt = con.prepareStatement("INSERT INTO TRNLM(THSMSTRNLM,KDPTITRNLM,KDJENTRNLM,KDPSTTRNLM,NPMHSTRNLM,KDKMKTRNLM,NILAITRNLM,NLAKHTRNLM,BOBOTTRNLM,SKSMKTRNLM,KELASTRNLM,KRSDONWLOADED,KHSDONWLOADED,BAK_APPROVAL,SHIFTTRNLM,PA_APPROVAL,LOCKTRNLM,BAUK_APPROVAL,IDKMKTRNLM,ADD_REQ,DRP_REQ,BAA_APPROVAL,KTU_APPROVAL,DEKAN_APPROVAL,LOCKMHS,KODE_KAMPUS)VALUES(?,'031031','B','65101',?,?,0,'T',3,3,'00',false,false,false,'EKSEKUTIF PASCA',false,false,false,?,false,false,false,false,false,false,'JST')");
				System.out.println(brs);
				StringTokenizer st = new StringTokenizer(brs,"|");
				
				String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				String nakmk = st.nextToken();
				String idkmk = st.nextToken();
				String sksmk = st.nextToken();
				String npmhs = st.nextToken();
				stmt.setString(1,thsms);
				stmt.setString(2,npmhs);
				stmt.setString(3,kdkmk);
				stmt.setInt(4, Integer.parseInt(idkmk));
				System.out.println("1="+stmt.executeUpdate());	
				stmt = con.prepareStatement("delete from TRAKM where THSMSTRAKM=? and KDPSTTRAKM=? and NPMHSTRAKM=?");
        		stmt.setString(1, thsms);
        		stmt.setString(2, kdpst);
        		stmt.setString(3, npmhs);
        		System.out.println("2="+stmt.executeUpdate());	
        		//insert fresh record
        		stmt = con.prepareStatement("INSERT into TRAKM(THSMSTRAKM,KDPTITRAKM,KDJENTRAKM,KDPSTTRAKM,NPMHSTRAKM,SKSEMTRAKM,NLIPSTRAKM,SKSTTTRAKM,NLIPKTRAKM)values(?,?,?,?,?,?,?,?,?)");
        		stmt.setString(1, thsms);
        		stmt.setString(2, "031031");
        		stmt.setString(3,"B");
        		stmt.setString(4, "65101");
        		stmt.setString(5, npmhs);
        		stmt.setInt(6, 0);
        		stmt.setDouble(7, 0);
        		stmt.setInt(8, 0);
        		stmt.setDouble(9, 0);
        		System.out.println("3="+stmt.executeUpdate());	
			}
					
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void getNilai() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		//Vector v1 = null;
		String kdpst = "65101";
		try {
			
			connectToMysql();
			
			stmt = con.prepareStatement("SELECT * FROM USG.TRNLM where KDPSTTRNLM='65101' and BOBOTTRNLM>0");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String nlakh = rs.getString("NLAKHTRNLM");
				String bobot = ""+rs.getFloat("BOBOTTRNLM");
				li.add(nlakh+" "+bobot);
			}
			writeFile("nilai",v);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	
	public static void kasihNilai(String filename) {
		Vector v = null;
		Vector v1 = null;
		//Vector v1 = null;
		String kdpst = "65101";
		try {
			v = bacaFileTxt(filename);
			v1 = bacaFileTxt("nilai");
			//v1 = bacaFileTxt("nakmk");
			System.out.println("v size = "+v.size());
			connectToMysql();
			stmt = con.prepareStatement("select * from TRNLM where KDPSTTRNLM='65101'");
			rs = stmt.executeQuery();
			v = new Vector();
			ListIterator li = v.listIterator();
			while(rs.next()) {
				String thsms = ""+rs.getString("THSMSTRNLM");
				String npmhs = ""+rs.getString("NPMHSTRNLM");
				String kdkmk = ""+rs.getString("KDKMKTRNLM");
				li.add(thsms);
				li.add(npmhs);
				li.add(kdkmk);
			}
			li = v.listIterator();
			stmt = con.prepareStatement("update TRNLM set NILAITRNLM=?,NLAKHTRNLM=?,BOBOTTRNLM=? where THSMSTRNLM=? and NPMHSTRNLM=? and KDKMKTRNLM=?");
			
			ListIterator li1 = v1.listIterator();
			while(li.hasNext()) {
			
				String thsms = (String)li.next();
				String npmhs = (String)li.next();
				String kdkmk = (String)li.next();
				/*
				String brs = (String)li.next();
				
				//System.out.println(brs);
				
				
				String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				String nakmk = st.nextToken();
				String idkmk = st.nextToken();
				String sksmk = st.nextToken();
				String npmhs = st.nextToken();
				*/
				String brs1 = "";
				String nilai1 = "";
				String bobot1 = "";
				StringTokenizer st = null;
				if(li1.hasNext()) {
					brs1 = (String)li1.next();
					st = new StringTokenizer(brs1);
					nilai1 = st.nextToken();
					bobot1 = st.nextToken();
				}
				else {
					li1 = v1.listIterator();
					brs1 = (String)li1.next();
					st = new StringTokenizer(brs1);
					nilai1 = st.nextToken();
					bobot1 = st.nextToken();
				}
				stmt.setFloat(1,Float.parseFloat(nilai1));
				stmt.setString(2,nilai1);
				stmt.setFloat(3,Float.parseFloat(bobot1));
				stmt.setString(4,thsms);
				stmt.setString(5,npmhs);
				stmt.setString(6,kdkmk);
				System.out.println(thsms+" "+npmhs+" "+kdkmk+" "+brs1+" = "+stmt.executeUpdate());
			}
					
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[]args)throws Exception {
		//migrateDataTrnlpPartI("data_trnlp");
		//migrateDataTrnlpPartIa("data_trnlp");
		//updDataMhsPindahan();
		//setStpid();
		//visitasi("all_krs_mip2015");
		//visitasi2("krs_mip2015_v2");
		kasihNilai("krs_mip2015_v2");
		
		//getNilai();
		System.out.println("done");
	}
	

}