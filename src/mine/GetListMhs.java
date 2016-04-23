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



class GetListMhs extends Maintenance {

	public static Vector getListMhsGiven(String kdpst) {
		/*
		 * smawlmsmhs di mysql harus akurat!!
		 * smawlmsmhs wajib diisi utk object mahasiswa
		 */
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			connectToMysql();
			stmt = con.prepareStatement("select * from CIVITAS where KDPSTMSMHS=? order by NPMHSMSMHS");
			stmt.setString(1,kdpst);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String npmhs = rs.getString("NPMHSMSMHS");
				String nimhs = rs.getString("NIMHSMSMHS");
				String nmmhs = rs.getString("NMMHSMSMHS");
				li.add(nimhs);
				li.add(nmmhs);
				//System.out.println(npmhs);
			}
			writeFile("list_mhs_"+kdpst+"_di_mysql",v);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return v;
	}

	public static void main(String[]args)throws Exception {
		System.out.println("yes");
		Vector v = getListMhsGiven("65101");
		ListIterator li = v.listIterator();
		while(li.hasNext()) {
			String brs = (String)li.next();
			System.out.println(brs);
		}
	}
	

}