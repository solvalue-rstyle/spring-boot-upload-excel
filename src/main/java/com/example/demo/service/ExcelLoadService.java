package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

@Service
public class ExcelLoadService {

	private static final int KEY_CELL = 0;
	private static final int DATA_CELL = 1;

	public Map<String, String> read(InputStream in) throws EncryptedDocumentException, IOException {

		final Workbook wb = WorkbookFactory.create(in);
		final FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		try {
			final Map<String, String> loaded = new HashMap<>();
			final Sheet sheet = wb.getSheet("example");
			for (Row row : sheet) {
				if (sheet.getLastRowNum() == row.getRowNum()) {
					break;
				}
				final String key = this.readValue(row.getCell(KEY_CELL), evaluator);
				final String val = this.readValue(row.getCell(DATA_CELL), evaluator);
				loaded.put(key, val);
			}
			return loaded;

		} finally {
			wb.close();
		}
	}

	private String readValue(final Cell cell, final FormulaEvaluator evaluator) {
		
		final CellValue cellValue = evaluator.evaluate(cell);
		switch (cellValue.getCellType()) {
		case STRING:
			return cellValue.getStringValue();

		case NUMERIC:
			return String.valueOf(Double.valueOf(cellValue.getNumberValue()).intValue());

		case BOOLEAN:
			return String.valueOf(cellValue.getBooleanValue());

		case FORMULA:
		case BLANK:
		default:
			return "";
		}
	}
}
