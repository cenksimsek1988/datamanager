package com.softactive.core.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.softactive.core.object.Pullable;

import okhttp3.Response;

public abstract class AbstractExcelHandler<P extends Pullable>
		extends AbstractHandler<P, Workbook, Sheet, Iterator<Cell>> {
	private static final long serialVersionUID = 1702240267667438881L;
	protected Map<Integer, Integer> map;
	protected int dateIndex;
	protected int regionCodeIndex;

	@Override
	public boolean handle(Response r, Map<String, Object> requestParams) {
		try {
			FileInputStream excelFile = getFile(r);
			handleFile(excelFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void handleFile(FileInputStream excelFile) throws IOException {
		Workbook workbook = new XSSFWorkbook(excelFile);
		List<P> list = getList(getArray(workbook), null);
		if(list == null || list.isEmpty()) {
			onError(null);
		} else {
			onListSuccesfullyParsed(list, null);
		}
		workbook.close();
	}

	public void handle(String fileName) throws IOException {
		try {
			FileInputStream excelFile = getFile(fileName);
			handleFile(excelFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FileInputStream getFile(Response r) throws FileNotFoundException {
		System.out.println("Response: " + r);
		String fileName = getFileName(r);
		return new FileInputStream(new File(fileName));

	}

	private FileInputStream getFile(String fileName) throws FileNotFoundException {
		return new FileInputStream(new File(fileName));

	}

	protected Double getDoubleValue(Cell c) {
		CellType type = c.getCellTypeEnum();
		switch (type) {
		case STRING:
			String s = c.getStringCellValue();
			if (s != null && s.length() > 0) {
				try {
					return Double.valueOf(s);
				} catch (NumberFormatException e) {
					return null;
				}
			} else {
				return null;
			}
		case NUMERIC:
			return c.getNumericCellValue();
		case BLANK:
			return null;
		default:
			System.out.println("Cell type is strange: " + type);
			return null;
		}
	}

	protected abstract Date resolveDate(String dateString);

	@Override
	protected Workbook onFormatResponse(Response r) {
		try {
			FileInputStream excelFile = getFile(r);
			return new XSSFWorkbook(excelFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected Workbook onFormatResponse(String fileName) {
		try {
			FileInputStream excelFile = getFile(fileName);
			return new XSSFWorkbook(excelFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected abstract String getFileName(Response r);

	protected abstract int getArrayStartIndex();

	protected abstract List<P> onEachRow(Row row);

	@Override
	protected Sheet getArray(Workbook r) {
		return r.getSheetAt(getSheetIndex());
	}

	protected abstract int getSheetIndex();

	@Override
	protected List<P> getList(Sheet array, Map<String, Object> requestParams) {
		List<P> list = new ArrayList<>();
		int startIndex = getArrayStartIndex();
		int endIndex = array.getLastRowNum();
		int count = endIndex - startIndex;
		for (int i = startIndex; i < endIndex; i++) {
			List<P> additional = onEachRow(array.getRow(i));
			if (additional != null) {
				list.addAll(additional);
			}
			setProgress(100 * i / count);
		}
		return list;
	}
}
