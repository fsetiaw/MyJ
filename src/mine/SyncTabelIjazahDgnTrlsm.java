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



class SyncTabelIjazahDgnTrlsm extends Maintenance {
	
	public static void bacaInfoTrlsm(String kdpst) {
		//baca hasil SyncLulusanEpsbedDgnMysql
		String nmfile = "NPM_lulusan_"+kdpst+"_EPSBED";
		try {
			//0423/Ij/MIP/USG/2002#AGUS YUDIANTORO#0002010194#2002-10-25#20022#6510100100165
			connectToMysql();
			stmt = con.prepareStatement("INSERT INTO IJAZAH(KDPST,NPMHS,TGLLS,NOIJA,NAMADIIJAZAH,NIMHSDIIJAZAH,GELARIJA,NOTE,EDITABLE,CETAKABLE)VALUES(?,?,?,?,?,?,?,?,?,?)");
			Vector v = bacaFileTxt(nmfile);
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				String baris = (String)li.next();
				//System.out.println(baris);
				StringTokenizer st = new StringTokenizer(baris,"#");
				String noija = st.nextToken();
				String nmmhs = st.nextToken();
				StringTokenizer stm = new StringTokenizer(nmmhs);
				nmmhs="";
				while(stm.hasMoreTokens()) {
					nmmhs = nmmhs + stm.nextToken();
					if(stm.hasMoreTokens()) {
						nmmhs = nmmhs + " ";
					}
				}
				String nimija = st.nextToken();
				String tgllsija = st.nextToken();
				String thsmsija = st.nextToken();
				String npmhs = st.nextToken();
				//KDPST,NPMHS,NOIJA,NAMADIIJAZAH,NIMHSDIIJAZAH,GELARIJA,NOTE,EDITABLE,CETAKABLE
				stmt.setString(1,kdpst);
				stmt.setString(2,npmhs);
				if(tgllsija.equalsIgnoreCase("null")) {
					stmt.setNull(3,java.sql.Types.DATE);
				}
				else {
					stmt.setDate(3,java.sql.Date.valueOf(tgllsija));
				}
				
				stmt.setString(4,noija);
				stmt.setString(5,nmmhs);
				stmt.setString(6,nimija);
				stmt.setString(7,"MAGISTER ILMU PEMERINTAHAN");
				stmt.setString(8,"data hasil migrasi");
				stmt.setBoolean(9,false);
				stmt.setBoolean(10,false);
				System.out.println(nimija+"/"+npmhs+"="+stmt.executeUpdate());
			}
		}
		catch(Exception e) {
			System.out.println("aneh = "+e);
		}

	}

	public static void updateNpmTabelIjazah(String kdpst) {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			//sync dgn tabel iijazah
			
			connectToMysql();
			stmt = con.prepareStatement("select distinct NIMHSDIIJAZAH from IJAZAH where KDPST=? ");
			stmt.setString(1,kdpst);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String nimhs = rs.getString("NIMHSDIIJAZAH");
				li.add(nimhs);
			}
			
			stmt = con.prepareStatement("select * from CIVITAS where KDPSTMSMHS=? and NIMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String nimhs = (String)li.next();
				if(nimhs.length()==17) {
					nimhs = nimhs.substring(2,17);
				}
				stmt.setString(1, kdpst);
				stmt.setString(2, nimhs);
				rs = stmt.executeQuery();
				if(rs.next()) {
					String npmhs = rs.getString("NPMHSMSMHS");
					li.set(npmhs+","+nimhs);
				}
				else {
					li.set("null,"+nimhs);
				}
			}
			stmt = con.prepareStatement("update IJAZAH set NPMHS=? where KDPST=? and (NIMHSDIIJAZAH=? OR NIMHSDIIJAZAH=?)");
			li = v.listIterator();
			while(li.hasNext()) {
				String baris = (String)li.next();
				StringTokenizer st = new StringTokenizer(baris,",");
				String npmhs = st.nextToken();
				String nimhs = st.nextToken();
				String nimhs1 = ""+nimhs;
				if(nimhs.length()==15) {
					nimhs1 = "20"+nimhs;
				}
				stmt.setString(1, npmhs);
				stmt.setString(2, kdpst);
				stmt.setString(3, nimhs);
				stmt.setString(4, nimhs1);
				System.out.println(baris+" "+stmt.executeUpdate());
			}
		}
		catch(Exception e) {
			System.out.println("aneh = "+e);
		}

	}
	
	public static void insertTrlsmDgnIjazah(String kdpst) {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			connectToMysql();
			stmt = con.prepareStatement("select distinct NPMHS from IJAZAH where KDPST=? ");
			stmt.setString(1,kdpst);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String npmhs = rs.getString("NPMHS");
				//String tglls = ""+rs.getDate("TGLLS");
				if(npmhs!=null && !npmhs.equalsIgnoreCase("null")) {
					li.add(npmhs);
					//li.add(tglls);
				}	
			}
			
			stmt = con.prepareStatement("select * from IJAZAH where KDPST=? and NPMHS=? ");
			li = v.listIterator();
			int i=0;
			while(li.hasNext()) {
				String npmhs = (String)li.next();
				stmt.setString(1, kdpst);
				stmt.setString(2, npmhs);
				rs = stmt.executeQuery();
				rs.next();
				i++;
				String tglls = ""+rs.getDate("TGLLS");
				System.out.println(i+"."+npmhs+","+tglls);
				li.set(npmhs+","+tglls);
			}
			
			stmt = con.prepareStatement("insert into TRLSM (THSMS,KDPST,NPMHS,STMHS)values(?,?,?,?)");
			li = v.listIterator();
			while(li.hasNext()) {
				String baris = (String)li.next();
				StringTokenizer st = new StringTokenizer(baris,",");
				String npmhs = st.nextToken();
				String tglls = st.nextToken();
				stmt.setString(1,"00000");
				stmt.setString(2,kdpst);
				stmt.setString(3,npmhs);
				stmt.setString(4,"L");
				System.out.println(baris+" = "+stmt.executeUpdate());
			}	
			stmt = con.prepareStatement("UPDATE CIVITAS SET STMHSMSMHS=?,TGLLSMSMHS=? where KDPSTMSMHS=? and NPMHSMSMHS=? ");
			li = v.listIterator();
			while(li.hasNext()) {
				String baris = (String)li.next();
				StringTokenizer st = new StringTokenizer(baris,",");
				String npmhs = st.nextToken();
				String tglls = st.nextToken();
				stmt.setString(1,"L");
				if(tglls.equalsIgnoreCase("null")) {
					stmt.setNull(2, java.sql.Types.DATE);
				}
				else {
					stmt.setDate(2, java.sql.Date.valueOf(tglls));
				}
				stmt.setString(3,kdpst);
				stmt.setString(4,npmhs);

				System.out.println(baris+" civitas = "+stmt.executeUpdate());
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[]args)throws Exception {
		insertTrlsmDgnIjazah("65101");
		//System.out.println("done");
	}
	

}