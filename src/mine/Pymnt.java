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



class Pymnt extends Maintenance {
	public static void ubahNpm(String npm_lama, String npm_baru)throws Exception {
		try {
			connectToMysql();
			stmt = con.prepareStatement("update PYMNT set NPMHSPYMNT=? where NPMHSPYMNT=?");
			stmt.setString(1, npm_baru);
			stmt.setString(2, npm_lama);
			System.out.println("ubah "+npm_lama+" menjadi "+npm_baru+" = "+stmt.executeUpdate());
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}


	public static void main(String[]args)throws Exception {

		ubahNpm("6510100000053", "6510111100002");
	}
	

}