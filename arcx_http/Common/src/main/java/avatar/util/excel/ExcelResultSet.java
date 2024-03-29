package avatar.util.excel;

import avatar.util.system.DateUtil;
import jxl.Cell;
import jxl.format.CellFormat;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;

public class ExcelResultSet {

	private ArrayList<RowValue> rowList;
	private int curIndex;
	private int colLength;

	public ExcelResultSet(int rowLength, int colLength) {
		this.colLength = colLength;
		rowList = new ArrayList<>(rowLength);
		for (int index = 0; index < rowLength; index++) {
			rowList.add(new RowValue(colLength));
		}
		curIndex = -1;
	}

	public int getColLength() {
		return colLength;
	}

	public void beforeFirst() {
		curIndex = -1;
	}

	public boolean first() {
		if (rowList.size() == 0) {
			curIndex = -1;
			return false;
		}
		curIndex = 0;
		return true;
	}

	public boolean next() {
		if (curIndex == rowList.size() - 1) {
			curIndex++;
			return false;
		}
		if (curIndex >= rowList.size()) {
			return false;
		}
		curIndex++;
		return true;
	}

	public void updateCellValue(int columnIndex, String columnLabel, Cell cell) {
		RowValue rowValue = rowList.get(curIndex);
		rowValue.setCellValue(cell, columnLabel, columnIndex);
	}

	/**
	 * @param columnIndex 从0开始，比如第一列是 columnIndex = 0
	 * @return
	 */
	public boolean getBoolean(int columnIndex) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnIndex).cell;
		return parseBoolean(cell.getContents().trim());
	}

	public boolean getBoolean(String columnLabel) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnLabel).cell;
		return parseBoolean(cell.getContents().trim());
	}

	public Boolean getBooleanEx(int columnIndex) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? null : parseBoolean(contents);
	}

	public Boolean getBooleanEx(String columnLabel) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? null : parseBoolean(contents);
	}

	public boolean getBooleanOrDefault(int columnIndex, boolean defaultValue) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? defaultValue : parseBoolean(contents);
	}

	public boolean getBooleanOrDefault(String columnLabel, boolean defaultValue) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? defaultValue : parseBoolean(contents);
	}

	public byte getByte(int columnIndex) {
		String contents = getStringEx(columnIndex);
		return parseByte(contents);
	}

	public byte getByte(String columnLabel) {
		String contents = getStringEx(columnLabel);
		return parseByte(contents);
	}

	public Byte getByteEx(int columnIndex) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? null : parseByte(contents);
	}

	public Byte getByteEx(String columnLabel) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? null : parseByte(contents);
	}

	public byte getByteOrDefault(int columnIndex, byte defaultValue) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? defaultValue : parseByte(contents);
	}

	public byte getByteOrDefault(String columnLabel, byte defaultValue) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? defaultValue : parseByte(contents);
	}

	public short getShort(int columnIndex) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnIndex).cell;
		return parseShort(cell.getContents().trim());
	}

	public short getShort(String columnLabel) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnLabel).cell;
		return parseShort(cell.getContents().trim());
	}

	public Short getShortEx(int columnIndex) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? null : parseShort(contents);
	}

	public Short getShortEx(String columnLabel) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? null : parseShort(contents);
	}

	public short getShortOrDefault(int columnIndex, short defaultValue) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? defaultValue : parseShort(contents);
	}

	public short getShortOrDefault(String columnLabel, short defaultValue) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? defaultValue : parseShort(contents);
	}

	public int getInt(int columnIndex) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnIndex).cell;
		return parseInt(cell.getContents().trim());
	}

	public int getInt(String columnLabel) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnLabel).cell;
		return parseInt(cell.getContents().trim());
	}

	public Integer getIntEx(int columnIndex) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? null : parseInt(contents);
	}

	public Integer getIntEx(String columnLabel) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? null : parseInt(contents);
	}

	public int getIntOrDefault(int columnIndex, int defaultValue) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? defaultValue : parseInt(contents);
	}

	public int getIntOrDefault(String columnLabel, int defaultValue) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? defaultValue : parseInt(contents);
	}

	public float getFloat(int columnIndex) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnIndex).cell;
		return parseFloat(cell.getContents().trim());
	}

	public float getFloat(String columnLabel) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnLabel).cell;
		return parseFloat(cell.getContents().trim());
	}

	public Float getFloatEx(int columnIndex) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? null : parseFloat(contents);
	}

	public Float getFloatEx(String columnLabel) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? null : parseFloat(contents);
	}

	public float getFloatOrDefault(int columnIndex, float defaultValue) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? defaultValue : parseFloat(contents);
	}

	public float getFloatOrDefault(String columnLabel, float defaultValue) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? defaultValue : parseFloat(contents);
	}

	public double getDouble(int columnIndex) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnIndex).cell;
		return parseDouble(cell.getContents().trim());
	}

	public double getDouble(String columnLabel) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnLabel).cell;
		return parseDouble(cell.getContents().trim());
	}

	public Double getDoubleEx(int columnIndex) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? null : parseDouble(contents);
	}

	public Double getDoubleEx(String columnLabel) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? null : parseDouble(contents);
	}

	public double getDoubleOrDefault(int columnIndex, double defaultValue) {
		String contents = getStringEx(columnIndex);
		return StringUtils.isEmpty(contents) ? defaultValue : parseDouble(contents);
	}

	public double getDoubleOrDefault(String columnLabel, double defaultValue) {
		String contents = getStringEx(columnLabel);
		return StringUtils.isEmpty(contents) ? defaultValue : parseDouble(contents);
	}

	public String getString(int columnIndex) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnIndex).cell;
		return cell.getContents();
	}

	public String getString(String columnLabel) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnLabel).cell;
		return cell.getContents();
	}

	public String getStringEx(int columnIndex) {
		return getString(columnIndex).trim();
	}

	public String getStringEx(String columnLabel) {
		return getString(columnLabel).trim();
	}

	public Date getDate(String columnIndex) {
		String str = getString(columnIndex);
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return DateUtil.parseYMDCHIN(str);
	}

	public Date getDateTime(String columnIndex) {
		String str = getString(columnIndex);
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return DateUtil.parseYMDHMS(str);
	}

	private boolean parseBoolean(String content) {
		boolean result;
		try {
			result = Boolean.parseBoolean(content);
		} catch (Throwable t) {
			throw new RuntimeException(String.format("[column index:%d]", curIndex), t);
		}
		return result;
	}

	private byte parseByte(String content) {
		byte result;
		try {
			result = Byte.parseByte(content);
		} catch (Throwable t) {
			throw new RuntimeException(String.format("[column index:%d]", curIndex), t);
		}
		return result;
	}

	private short parseShort(String content) {
		short result;
		try {
			result = Short.parseShort(content);
		} catch (Throwable t) {
			throw new RuntimeException(String.format("[column index:%d]", curIndex), t);
		}
		return result;
	}

	private int parseInt(String content) {
		int result;
		try {
			result = Integer.parseInt(content);
		} catch (Throwable t) {
			throw new RuntimeException(String.format("[column index:%d]", curIndex), t);
		}
		return result;
	}

	private double parseDouble(String content) {
		double result;
		try {
			result = Double.parseDouble(content);
		} catch (Throwable t) {
			throw new RuntimeException(String.format("[column index:%d]", curIndex), t);
		}
		return result;
	}

	private float parseFloat(String content) {
		float result;
		try {
			result = Float.parseFloat(content);
		} catch (Throwable t) {
			throw new RuntimeException(String.format("[column index:%d]", curIndex), t);
		}
		return result;
	}

	public CellFormat getCellFormat(String columnLabel) {
		RowValue row = rowList.get(curIndex);
		Cell cell = row.getCellValue(columnLabel).cell;
		return cell.getCellFormat();
	}

	public Cell getCell(String columnLabel) {
		RowValue row = rowList.get(curIndex);
		return row.getCellValue(columnLabel).cell;
	}

	public boolean contains(String columnLabel) {
		RowValue row = rowList.get(curIndex);
		return row.getCellValue(columnLabel) != null;
	}

	public boolean contains(int columnIndex) {
		RowValue row = rowList.get(curIndex);
		return row.getCellValue(columnIndex) != null;
	}

	private static class CellValue {

		final Cell cell;
		final String columnLabel;

		CellValue(Cell cell, String columnLabel) {
			this.cell = cell;
			this.columnLabel = columnLabel.trim();
		}

	}

	private static class RowValue {

		private final CellValue[] columnArray;

		RowValue(int colLength) {
			columnArray = new CellValue[colLength];
		}

		void setCellValue(Cell cell, String columnLabel, int columnIndex) {
			if (columnLabel == null)
				columnLabel = "";
			CellValue cellValue = new CellValue(cell, columnLabel/*, columnIndex*/);
			columnArray[columnIndex] = cellValue;
		}

		CellValue getCellValue(int columnIndex) {
			return columnArray[columnIndex];
		}

		CellValue getCellValue(String columnLabel) {
			columnLabel = columnLabel.trim();
			for (CellValue column : columnArray) {
				if (column.columnLabel.equalsIgnoreCase(columnLabel)) {
					return column;
				}
			}
			return null;
		}

	}

}
