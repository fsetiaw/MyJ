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



class Ijazah extends Maintenance {


	public static void main(String[]args)throws Exception {
		String kdpst = "65101";
		try {
			connectToMysql();

			stmt = con.prepareStatement("INSERT INTO IJAZAH(KDPST,NONIRL,NOIJA,NAMADIIJAZAH,NIMHSDIIJAZAH,TPTGLHRDIIJAZAH,TGLCETAKSTR)VALUES(?,?,?,?,?,?,?)");
			Vector v = bacaFileTxt("ijazah_mip");
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				String noija = (String)li.next();
				String nmmhs = (String)li.next();
				String tplhr = (String)li.next();
				String nimhs = (String)li.next();
				String nirl = (String)li.next();
				String terbit = (String)li.next();
				stmt.setString(1,kdpst);
				stmt.setString(2,nirl);
				stmt.setString(3,noija);
				stmt.setString(4,nmmhs);
				stmt.setString(5,nimhs);
				stmt.setString(6,tplhr);
				stmt.setString(7, terbit);
				System.out.println(nimhs+" "+nmmhs+" "+stmt.executeUpdate());
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	

}