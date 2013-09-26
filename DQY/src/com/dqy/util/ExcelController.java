/**
 *
 */
package com.dqy.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhenjia(wangzhenjia@ait.net.cn)
 * @Date 2006-4-21 10:06:05
 */
public class ExcelController {

    protected InputStream in;

    protected int sheetIndex = -1;

    protected String sheetName = "sheet1";

    protected int cellRow = -1;

    protected int cellCol = -1;

    protected File file;

    protected InputStream inputStream;

    public ExcelController() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * ��ȡ�ļ���IO��
     *
     * @param path
     */
    public void initializationCreate(String path) {
        file = new File(path);
    }

    public void initializationRead(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void dispose() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���ù��������
     *
     * @param index
     */
    public void setSheetIndex(int index) {
        this.sheetIndex = index;
    }

    /**
     * ���ù�������
     *
     * @param key
     */
    public void setSheetName(String key) {
        this.sheetName = key;
    }

    /**
     * ���õ�Ԫ����
     *
     * @param row
     */
    public void setCellRow(int row) {
        this.cellRow = row;
    }

    /**
     * ���õ�Ԫ����
     *
     * @param col
     */
    public void setCellCol(int col) {
        this.cellCol = col;
    }

}