/**
 *
 */
package com.dqy.util;

import jxl.*;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhenjia(wangzhenjia@ait.net.cn)
 * @Date 2006-4-21 10:08:42
 */
public class ExcelReadController extends ExcelController {

    private Workbook wb;

    private Sheet st;

    public ExcelReadController() {
    }

    public void clearSheet() {
        st = null;
    }

    /**
     * 得到读取excel对象
     *
     * @return
     */

    /**
     * 读取一个工作薄
     *
     * @return
     */
    public void getWorkBook() {
        try {
            wb = Workbook.getWorkbook(this.inputStream);
        } catch (BiffException e) {
            wb.close();
            e.printStackTrace();
        } catch (IOException e) {
            wb.close();
            e.printStackTrace();
        }
    }

    public void closeWorkBook() {
        if (wb != null) {
            wb.close();
        }
    }

    /**
     * 获得工作表的总数
     *
     * @return
     */
    public int getSheetCount() {
        if (wb == null) {
            getWorkBook();
        }
        int count = wb.getNumberOfSheets();
        return count;
    }

    /**
     * 获得工作表的数组
     *
     * @return
     */
    public Sheet[] getSheets() {
        if (wb == null) {
            getWorkBook();
        }
        Sheet[] sheets = wb.getSheets();
        return sheets;
    }

    /**
     * 获得工作表的名称
     *
     * @return
     */
    public String getSheetName() {
        if (st == null) {
            getSheet();
        }
        return st.getName();
    }

    /**
     * 获得工作表所包含的总列数
     *
     * @return
     */
    public int getSheetColumns() {
        if (st == null) {
            getSheet();
        }
        return st.getColumns();
    }

    /**
     * 获得工作表所包含的总行数
     *
     * @return
     */
    public int getSheetRows() {
        if (st == null) {
            getSheet();
        }
        return st.getRows();
    }

    /**
     * 获取某一列的所有单元格
     *
     * @return
     */
    public Cell[] getCellColumn() {
        if (st == null) {
            getSheet();
        }
        if (cellCol != -1) {
            return st.getColumn(cellCol);
        } else {
            return null;
        }
    }

    /**
     * 获取某一行的所有单元格
     *
     * @return
     */
    public Cell[] getCellRow() {
        if (st == null) {
            getSheet();
        }
        if (cellRow != -1) {
            return st.getRow(cellRow);
        } else {
            return null;
        }
    }

    /**
     * 读取一个工作表
     *
     * @return
     */
    public void getSheet() {
        if (wb == null) {
            getWorkBook();
        }
        if (sheetIndex != -1) {
            st = wb.getSheet(sheetIndex);
        } else if (sheetName != null) {
            st = wb.getSheet(sheetName);
        }
    }

    /**
     * 读取一个单元格
     *
     * @return
     */
    public Cell getCell() {
        if (st == null) {
            getSheet();
        }
        if (cellRow != -1 && cellCol != -1) {
            Cell cell = st.getCell(cellCol, cellRow);
            return cell;
        }
        return null;
    }

    public Object getValue(Cell cell) {
        if (cell.getType() == CellType.LABEL) {
            return getString() == null ? "" : getString();
        } else if (cell.getType() == CellType.NUMBER) {
            return getDouble();
        } else if (cell.getType() == CellType.DATE) {
            return getDate();
        } else {
            return getString() == null ? "" : getString();
        }
    }

    /**
     * 获取String型数据
     *
     * @return
     */
    public String getString() {
        String str = "";
        Cell cell = getCell();
        if (cell.getType() == CellType.LABEL) {
            LabelCell label = (LabelCell) cell;
            str = label.getString();
        }
        return str;
    }

    /**
     * 或得某一单元集合的值
     *
     * @param cells
     * @return
     */
    @SuppressWarnings("unchecked")
    public List getResult(Cell[] cells) {
        List<Object> resultList = new ArrayList<Object>();
        if (cells.length > 0) {
            Cell cell = null;
            double d = -1;
            Date date = null;
            String str = "";
            for (int i = 0; i < cells.length; i++) {
                cell = cells[i];
                if (cell.getType() == CellType.LABEL) {
                    LabelCell label = (LabelCell) cell;
                    str = label.getString();
                    resultList.add(str);
                }
                if (cell.getType() == CellType.NUMBER) {
                    NumberCell number = (NumberCell) cell;
                    d = number.getValue();
                    resultList.add(new Double(d));
                }
                if (cell.getType() == CellType.DATE) {
                    DateCell da = (DateCell) cell;
                    date = da.getDate();
                    resultList.add(date);
                }
            }
        }
        return resultList;
    }

    /**
     * 获取double数据
     *
     * @return
     */
    public double getDouble() {
        Double d = null;
        Cell cell = getCell();
        if (cell.getType() == CellType.NUMBER) {
            NumberCell number = (NumberCell) cell;
            d = Double.valueOf(number.getValue());
            String dos = String.valueOf(d);
            d = Double.parseDouble(dos);
        }
        return d;
    }

    public long getLong() {
        Long d = null;
        Cell cell = getCell();
        if (cell.getType() == CellType.NUMBER) {
            NumberCell number = (NumberCell) cell;
            Double dd = Double.valueOf(number.getValue());
            String dos = String.valueOf(dd);
            d = Long.parseLong(dos);
        }
        return d;
    }

    /**
     * 获得Int型数据
     *
     * @return
     */
    public int getInt() {
        int i = -1;
        Cell cell = getCell();
        if (cell.getType() == CellType.NUMBER) {
            NumberCell number = (NumberCell) cell;
            i = Double.valueOf(number.getValue()).intValue();
        }
        return i;
    }

    /**
     * 获得float数据
     *
     * @return
     */
    public float getFloat() {
        float f = -1;
        Cell cell = getCell();
        if (cell.getType() == CellType.NUMBER) {
            NumberCell number = (NumberCell) cell;
            f = Float.parseFloat(Double.toString(number.getValue()));
        }
        return f;
    }

    /**
     * 获得日期型数据
     *
     * @return
     */
    public Date getDate() {
        Date date = null;
        Cell cell = getCell();
        if (cell.getType() == CellType.DATE) {
            DateCell d = (DateCell) cell;
            date = d.getDate();
        }
        return date;
    }

}