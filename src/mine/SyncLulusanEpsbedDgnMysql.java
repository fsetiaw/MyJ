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



class SyncLulusanEpsbedDgnMysql extends Maintenance {
	
	public static void bacaInfoLulusan(String kdpst) {
		String nmfile = "lulusan_"+kdpst+"_sort_by_noija";
		try {
			connectToMysql();
			stmt = con.prepareStatement("select * from CIVITAS where KDPSTMSMHS=? and NMMHSMSMHS like ?");
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
				String nimhs = st.nextToken();
				String tglls = st.nextToken();
				String thsms = st.nextToken();
				stmt.setString(1, kdpst);
				stmt.setString(2, "%"+nmmhs+"%");
				rs = stmt.executeQuery();
				if(rs.next()) {
					String npmhs = rs.getString("NPMHSMSMHS");
					li.set(baris+"#"+npmhs);
				}
				else {
					System.out.println(nimhs+" - "+nmmhs);
				}
			}
			writeFile("NPM_lulusan_"+kdpst+"_EPSBED",v);
		}
		catch(Exception e) {
			System.out.println("aneh = "+e);
		}

	}

	public static void main(String[]args)throws Exception {
		bacaInfoLulusan("65101");
		System.out.println("done");
	}
	

}