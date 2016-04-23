package mine;
import java.util.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Collections;
import java.util.Vector;
import java.util.ListIterator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.StringTokenizer;
//poi staff
import org.apache.poi.ss.usermodel.*;



public class ExcelTool extends Maintenance {
    
	public static void bacaExcel(String fileName) {
		System.out.println("start");
    	String msg = "";
    	java.io.File file = null;
    	try {
    		file = new File("input_txt/"+fileName);
    	}
    	catch(Exception e) {
    		System.out.println(e);
    	}
    	System.out.println("start 1 file="+file);
    	if(file.exists()) {
    		try {
    			connectToOt();
    			stmt = con.prepareStatement("INSERT INTO SOAL (SOAL,TKN_MULTIPLE_CHOICE,KUNCI,PICT_FILE,AUDIO_FILE,VIDEO_FILE,SOURCE_SOAL,PAGE_SOURCE_SOAL) VALUES (?,?,?,?,?,?,?,?)");
    			InputStream inp = new FileInputStream(file);
    			msg = msg+" file "+fileName+" ditemukan";
    			System.out.println(msg);
        	    //InputStream inp = new FileInputStream("workbook.xlsx");
    			Workbook wb = WorkbookFactory.create(inp);
    			int tot_sheet = wb.getNumberOfSheets();
    			msg = msg+"/n total sheet = "+tot_sheet;
    			System.out.println(msg);
    			for(int k=1;k<tot_sheet;k++) {
    			//for(int k=1;k<12;k++) {
    				System.out.println("k="+k);
    				Sheet sheet = wb.getSheetAt(k);
    				String baris = "";
    				String picFile = null;
    				String audFile = null;
    				String vidFile = null;
    				String source = "TOEFL SECTION III READING 4";
    				int pageSrc = 0;
    				
    				//get soal
    				Row row = sheet.getRow(2);
					Cell cell = row.getCell(0);
					String soal = "";
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						soal = ""+cell.getRichStringCellValue().getString();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							soal = ""+cell.getDateCellValue();
						} else {
							soal = ""+cell.getNumericCellValue();
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						soal = ""+cell.getBooleanCellValue();
						break;
					case Cell.CELL_TYPE_FORMULA:
						soal = ""+cell.getCellFormula();
						break;
					default:
						soal = null;
					}
					System.out.println(soal);
					
					
					
					//get kunci jawaban
    				row = sheet.getRow(10);
					cell = row.getCell(2);
					String kunci = "";
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						kunci = ""+cell.getRichStringCellValue().getString();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							kunci = ""+cell.getDateCellValue();
						} else {
							kunci = ""+cell.getNumericCellValue();
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						kunci = ""+cell.getBooleanCellValue();
						break;
					case Cell.CELL_TYPE_FORMULA:
						kunci = ""+cell.getCellFormula();
						break;
					default:
						kunci = null;
					}
					
					if(kunci.equalsIgnoreCase("A")) {
						kunci = "A 1";
					}
					if(kunci.equalsIgnoreCase("B")) {
						kunci = "B 2";
					}
					if(kunci.equalsIgnoreCase("C")) {
						kunci = "C 3";
					}
					if(kunci.equalsIgnoreCase("D")) {
						kunci = "D 4";
					}
					if(kunci.equalsIgnoreCase("E")) {
						kunci = "E 5";
					}
					if(kunci.equalsIgnoreCase("F")) {
						kunci = "F 6";
					}
					if(kunci.equalsIgnoreCase("G")) {
						kunci = "G 7";
					}
					if(kunci.equalsIgnoreCase("H")) {
						kunci = "H 8";
					}
					if(kunci.equalsIgnoreCase("I")) {
						kunci = "I 9";
					}
					if(kunci.equalsIgnoreCase("J")) {
						kunci = "J 10";
					}
					System.out.println(kunci);
					
					//get pilihan jawaban
					boolean first = true;
					int j=10;
					String tknJawaban = "";
					for(int i=0;i<10;i++) {
						j=j+2;
						row = sheet.getRow(j);
						if(row==null) {
							tknJawaban = tknJawaban+"||"+null;
						}
						else {
							cell = row.getCell(1);
							if(cell==null) {
								tknJawaban = tknJawaban+"||"+null;
							}
							else {
								switch (cell.getCellType()) {
									case Cell.CELL_TYPE_STRING:
										tknJawaban = tknJawaban+"||"+cell.getRichStringCellValue().getString();
										break;
									case Cell.CELL_TYPE_NUMERIC:
										if (DateUtil.isCellDateFormatted(cell)) {
											tknJawaban = tknJawaban+"||"+cell.getDateCellValue();
										} else {
											tknJawaban = tknJawaban+"||"+cell.getNumericCellValue();
										}
										break;
									case Cell.CELL_TYPE_BOOLEAN:
										tknJawaban = tknJawaban+"||"+cell.getBooleanCellValue();
										break;
									case Cell.CELL_TYPE_FORMULA:
										tknJawaban = tknJawaban+"||"+cell.getCellFormula();
										break;
									default:
										tknJawaban = tknJawaban+"||"+null;
								}
							}
						}
						//System.out.println(tknJawaban);
					}	
					tknJawaban=tknJawaban.replace("||null","");
					StringTokenizer st = new StringTokenizer(tknJawaban,"||");
					tknJawaban = "";
					while(st.hasMoreTokens()) {
						String tmp = st.nextToken();
						tknJawaban = tknJawaban+tmp;
						if(st.hasMoreTokens()) {
							tknJawaban=tknJawaban+"||";
						}
					}
					System.out.println(tknJawaban);
					
					//insert into dbsoal
					//SOAL,TKN_MULTIPLE_CHOICE,KUNCI,PICT_FILE,AUDIO_FILE,VIDEO_FILE,SOURCE_SOAL,PAGE_SOURCE_SOAL
					stmt.setString(1, soal);
					stmt.setString(2, tknJawaban);
					st = new StringTokenizer(kunci);
					st.nextToken();
					stmt.setInt(3,Integer.valueOf(st.nextToken()).intValue());
					if(picFile==null) {
						stmt.setNull(4, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(4, picFile);
					}
					if(audFile==null) {
						stmt.setNull(5, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(5, audFile);
					}
					if(vidFile==null) {
						stmt.setNull(6, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(6, vidFile);
					}
					if(source==null) {
						stmt.setNull(7, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(7, source);
					}
					stmt.setInt(8, pageSrc);
					System.out.println(k+". insert = "+stmt.executeUpdate());
    			}	
    		}catch (Exception e) {
		   		System.out.println("poi err "+e);
		    	msg = msg+"<br /> ada error ngga jelas";
		    }
    	}
    	else {
    		System.out.println("no fined");
    	}
    }	

		
	public static void main(String[]args) throws Exception {
		//bacaExcel("TPA NUMERIK hal 241.xlsx");
		//bacaExcel("TPA PENALARAN HAL 263 - 271.xlsx");
		//bacaExcel("TPA VERBAL HAL 228 - 235.xlsx");
		bacaExcel("TOEFL Section 3 Question 41 - 50.xlsx");
	}

}
