package com.dqy.repot.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.service.SysBudgetAmountService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.util.ExcelCreateController;
import com.dqy.web.support.ActionSupport;
import com.dqy.wf.entity.*;
import com.dqy.wf.service.*;
import com.google.inject.Inject;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.TokenUtils;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.NumberUtils;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.support.file.FileManager;
import org.guiceside.support.properties.PropertiesConfig;
import org.guiceside.web.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "req", namespace = "/report")
public class ReportReqAction extends ActionSupport<WfReq> {

    @Inject
    private WfReqService wfReqService;


    @Inject
    private WfReqRePaymentService wfReqRePaymentService;

    @Inject WfReqAdvanceAccountService wfReqAdvanceAccountService;


    @Inject
    private WfReqRePaymentDetailService wfReqRePaymentDetailService;

    @Inject
    private WfReqDailyDetailService wfReqDailyDetailService;

    @Inject
    private WfReqDailyService wfReqDailyService;

    @Inject
    private WfReqSaleService wfReqSaleService;

    @Inject
    private WfReqSaleDetailService wfReqSaleDetailService;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long applyMyId;

    @ReqGet
    @ReqSet
    private Long targetId;

    @ReqGet
    @ReqSet
    private String keyword;

    @ReqGet
    @ReqSet
    private String applyId;

    @ReqGet
    @ModelDriver
    @ReqSet
    private WfReq wfReq;


    @ReqSet
    private List<WfReq> reqList;


    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/apply/my/index.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    private List<Selector> searchModeCallbackSaleList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("report");
            userInfo.setLeftMenu("sale");
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("applyState", 2));
                selectorList.add(SelectorUtils.$eq("applyResult", 1));
                selectorList.add(SelectorUtils.$eq("complete", 1));
                selectorList.add(SelectorUtils.$eq("applyId", "SALE"));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqNo", keyword), SelectorUtils.$like("subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/report/sale/list.ftl", type = Dispatcher.FreeMarker)})
    public String saleList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqService.getPageList(getStart(), 10, searchModeCallbackSaleList());
            if (pageObj != null) {
                reqList = pageObj.getResultList();
            }
        }
        return "success";
    }

    public String saleExport() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            List<Selector> selectorList = new ArrayList<Selector>();
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                selectorList.add(SelectorUtils.$eq("reqId.orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("reqId.applyState", 2));
                selectorList.add(SelectorUtils.$eq("reqId.applyResult", 1));
                selectorList.add(SelectorUtils.$eq("reqId.complete", 1));
                selectorList.add(SelectorUtils.$eq("reqId.applyId", "SALE"));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqId.reqNo", keyword), SelectorUtils.$like("reqId.subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
            List<WfReqSale> reqSaleList=this.wfReqSaleService.getByList(selectorList);
            if (reqSaleList != null&&!reqSaleList.isEmpty()) {
                WritableFont blackBoldFont = new WritableFont(WritableFont.ARIAL, 10,
                        WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        jxl.format.Colour.BLACK);//黑色加粗
                WritableFont blackFont = new WritableFont(WritableFont.ARIAL, 11,
                        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        Colour.BLACK);// 黑色普通


                WritableCellFormat blackCenterAll = new WritableCellFormat(blackFont);
                blackCenterAll.setAlignment(Alignment.CENTRE);
                blackCenterAll.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackCenterAll
                        .setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAll.setBorder(Border.LEFT, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAll.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAll.setBorder(Border.RIGHT, BorderLineStyle.THIN,
                        Colour.BLACK);// 黑色普通居中全边框

                WritableCellFormat blackLeftAll = new WritableCellFormat(blackFont);
                blackLeftAll.setAlignment(Alignment.LEFT);
                blackLeftAll.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackLeftAll.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAll.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAll.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackLeftAll
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);// 黑色普通居左全边框

                WritableCellFormat blackLeftAllB = new WritableCellFormat(blackBoldFont);
                blackLeftAllB.setAlignment(Alignment.LEFT);
                blackLeftAllB.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackLeftAllB.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAllB.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAllB.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackLeftAllB
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);// 黑色普通居左全边框

                WritableCellFormat blackCenterAllB = new WritableCellFormat(blackBoldFont);
                blackCenterAllB.setAlignment(Alignment.CENTRE);
                blackCenterAllB.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackCenterAllB.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAllB.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAllB.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAllB
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);

                ExcelCreateController createExcel = new ExcelCreateController();
                String path = getRootPath() + "upload/excelTemp/";
                path += userInfo.getOrgId() + System.currentTimeMillis() + TokenUtils.getToken(userInfo.getSessionId()) + ".xls";
                PropertiesConfig propertiesConfig = (PropertiesConfig) getServletContext().getAttribute("webConfig");
                if (propertiesConfig.getProperties() != null) {
                    String platform = propertiesConfig.getString("platform");
                    if (platform.equals("windows")) {
                        path = path.replaceAll("\\/", "\\\\");
                    }
                }
                createExcel.initializationCreate(path);
                int row = 0;
                int col = 0;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("OA编号", blackCenterAllB);
                createExcel.setColumnView(20);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("申请人", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("申请时间", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);


                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("报销金额", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("支付方式", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("收款单位", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("开户行", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("帐号", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("开始日期", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("结束日期", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("核销日期", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);


                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用类别", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用项目", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("渠道", blackCenterAllB);
                createExcel.setColumnView(20);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("业态/部门", blackCenterAllB);
                createExcel.setColumnView(20);
                createExcel.setRowView(500);


                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("系统", blackCenterAllB);
                createExcel.setColumnView(20);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("门店", blackCenterAllB);
                createExcel.setColumnView(20);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("单品金额", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("品类/系类", blackCenterAllB);
                createExcel.setColumnView(20);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("单品", blackCenterAllB);
                createExcel.setColumnView(40);
                createExcel.setRowView(500);

                row = 1;
                for(WfReqSale reqSale:reqSaleList){
                    List<WfReqSaleDetail> details=this.wfReqSaleDetailService.getBySaleId(reqSale.getId());
                    if(details!=null&&!details.isEmpty()){
                        int j=0;
                        for(WfReqSaleDetail detail:details){
                            col = 0;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "reqId.reqNo"), blackCenterAll);
                            createExcel.setColumnView(20);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "reqId.userId.userName"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(getDate(reqSale, "reqId.created",DateFormatUtil.YEAR_MONTH_DAY_PATTERN), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }



                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setNumber(get(reqSale, "amount",Double.class), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            String payMethod="";
                            if(reqSale.getPayMethod().intValue()==1){
                                payMethod="现金";
                            }else if(reqSale.getPayMethod().intValue()==2){
                                payMethod="银行转账";
                            }else if(reqSale.getPayMethod().intValue()==3){
                                payMethod="支票";
                            }else if(reqSale.getPayMethod().intValue()==4){
                                payMethod="帐扣";
                            }
                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(payMethod, blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "payee"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "bank"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "bankAccount"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(getDate(reqSale, "startDate",DateFormatUtil.YEAR_MONTH_DAY_PATTERN), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(getDate(reqSale, "endDate",DateFormatUtil.YEAR_MONTH_DAY_PATTERN), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(getDate(reqSale, "payDate",DateFormatUtil.YEAR_MONTH_DAY_PATTERN), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }



                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "expenseType.expenseType"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "expenseTitle.titleName"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "channelId.channelName"), blackCenterAll);
                            createExcel.setColumnView(20);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "deptId.deptName"), blackCenterAll);
                            createExcel.setColumnView(20);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "systemId.systemName"), blackCenterAll);
                            createExcel.setColumnView(20);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqSale, "customerId.customerName"), blackCenterAll);
                            createExcel.setColumnView(20);
                            createExcel.setRowView(500);


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setNumber(get(detail, "amount",Double.class), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(detail, "seriesId.seriesName"), blackCenterAll);
                            createExcel.setColumnView(20);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent("("+get(detail, "productId.productCode")+") "+get(detail, "productId.productName"), blackCenterAll);
                            createExcel.setColumnView(40);
                            createExcel.setRowView(500);

                            row ++;
                            j++;
                        }
                    }
                }
                createExcel.write();
                createExcel.closeWorkBook();
                String  fileName = "销售费用报销明细统计表.xls";
                fileName = StringUtils.toUtf8String(fileName);
                File file = FileManager.getFile(path, false);
                download(file, fileName, true);
            }
        }
        return null;
    }


    private List<Selector> searchModeCallbackAdvanceAccountList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("report");
            userInfo.setLeftMenu("advanceAccount");
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("applyState", 2));
                selectorList.add(SelectorUtils.$eq("applyResult", 1));
                selectorList.add(SelectorUtils.$eq("complete", 1));
                selectorList.add(SelectorUtils.$eq("applyId", "ADVANCE_ACCOUNT"));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqNo", keyword), SelectorUtils.$like("subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/report/advanceAccount/list.ftl", type = Dispatcher.FreeMarker)})
    public String advanceAccountList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqService.getPageList(getStart(), 10, searchModeCallbackAdvanceAccountList());
            if (pageObj != null) {
                reqList = pageObj.getResultList();
            }
        }
        return "success";
    }


    public String advanceAccountExport() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            List<Selector> selectorList = new ArrayList<Selector>();
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                selectorList.add(SelectorUtils.$eq("reqId.orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("reqId.applyState", 2));
                selectorList.add(SelectorUtils.$eq("reqId.applyResult", 1));
                selectorList.add(SelectorUtils.$eq("reqId.complete", 1));
                selectorList.add(SelectorUtils.$eq("reqId.applyId", "ADVANCE_ACCOUNT"));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqId.reqNo", keyword), SelectorUtils.$like("reqId.subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
            List<WfReqAdvanceAccount> reqAdvanceAccountList=this.wfReqAdvanceAccountService.getByList(selectorList);
            if (reqAdvanceAccountList != null&&!reqAdvanceAccountList.isEmpty()) {
                WritableFont blackBoldFont = new WritableFont(WritableFont.ARIAL, 10,
                        WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        jxl.format.Colour.BLACK);//黑色加粗
                WritableFont blackFont = new WritableFont(WritableFont.ARIAL, 11,
                        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        Colour.BLACK);// 黑色普通


                WritableCellFormat blackCenterAll = new WritableCellFormat(blackFont);
                blackCenterAll.setAlignment(Alignment.CENTRE);
                blackCenterAll.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackCenterAll
                        .setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAll.setBorder(Border.LEFT, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAll.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAll.setBorder(Border.RIGHT, BorderLineStyle.THIN,
                        Colour.BLACK);// 黑色普通居中全边框

                WritableCellFormat blackLeftAll = new WritableCellFormat(blackFont);
                blackLeftAll.setAlignment(Alignment.LEFT);
                blackLeftAll.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackLeftAll.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAll.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAll.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackLeftAll
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);// 黑色普通居左全边框

                WritableCellFormat blackLeftAllB = new WritableCellFormat(blackBoldFont);
                blackLeftAllB.setAlignment(Alignment.LEFT);
                blackLeftAllB.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackLeftAllB.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAllB.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAllB.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackLeftAllB
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);// 黑色普通居左全边框

                WritableCellFormat blackCenterAllB = new WritableCellFormat(blackBoldFont);
                blackCenterAllB.setAlignment(Alignment.CENTRE);
                blackCenterAllB.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackCenterAllB.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAllB.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAllB.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAllB
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);

                ExcelCreateController createExcel = new ExcelCreateController();
                String path = getRootPath() + "upload/excelTemp/";
                path += userInfo.getOrgId() + System.currentTimeMillis() + TokenUtils.getToken(userInfo.getSessionId()) + ".xls";
                PropertiesConfig propertiesConfig = (PropertiesConfig) getServletContext().getAttribute("webConfig");
                if (propertiesConfig.getProperties() != null) {
                    String platform = propertiesConfig.getString("platform");
                    if (platform.equals("windows")) {
                        path = path.replaceAll("\\/", "\\\\");
                    }
                }
                createExcel.initializationCreate(path);
                int row = 0;
                int col = 0;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("OA编号", blackCenterAllB);
                createExcel.setColumnView(20);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("申请人", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("申请时间", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);


                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("预支金额", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("支付方式", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("收款单位", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("开户行", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("帐号", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);



                row = 1;
                for(WfReqAdvanceAccount reqAdvanceAccount:reqAdvanceAccountList){
                    col = 0;
                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setContent(get(reqAdvanceAccount, "reqId.reqNo"), blackCenterAll);
                    createExcel.setColumnView(20);
                    createExcel.setRowView(500);

                    col++;
                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setContent(get(reqAdvanceAccount, "reqId.userId.userName"), blackCenterAll);
                    createExcel.setColumnView(15);
                    createExcel.setRowView(500);

                    col++;
                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setContent(getDate(reqAdvanceAccount, "reqId.created",DateFormatUtil.YEAR_MONTH_DAY_PATTERN), blackCenterAll);
                    createExcel.setColumnView(15);
                    createExcel.setRowView(500);


                    col++;
                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setNumber(get(reqAdvanceAccount, "amount",Double.class), blackCenterAll);
                    createExcel.setColumnView(15);
                    createExcel.setRowView(500);

                    String payMethod="";
                    if(reqAdvanceAccount.getPayMethod().intValue()==1){
                        payMethod="现金";
                    }else if(reqAdvanceAccount.getPayMethod().intValue()==2){
                        payMethod="银行转账";
                    }else if(reqAdvanceAccount.getPayMethod().intValue()==3){
                        payMethod="支票";
                    }else if(reqAdvanceAccount.getPayMethod().intValue()==4){
                        payMethod="帐扣";
                    }
                    col++;
                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setContent(payMethod, blackCenterAll);
                    createExcel.setColumnView(15);
                    createExcel.setRowView(500);

                    col++;
                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setContent(get(reqAdvanceAccount, "payee"), blackCenterAll);
                    createExcel.setColumnView(25);
                    createExcel.setRowView(500);

                    col++;
                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setContent(get(reqAdvanceAccount, "bank"), blackCenterAll);
                    createExcel.setColumnView(25);
                    createExcel.setRowView(500);

                    col++;
                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setContent(get(reqAdvanceAccount, "bankAccount"), blackCenterAll);
                    createExcel.setColumnView(25);
                    createExcel.setRowView(500);

                    row ++;
                }
                createExcel.write();
                createExcel.closeWorkBook();
                String  fileName = "预支申请单统计表.xls";
                fileName = StringUtils.toUtf8String(fileName);
                File file = FileManager.getFile(path, false);
                download(file, fileName, true);
            }
        }
        return null;
    }

    private List<Selector> searchModeCallbackDailyList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("report");
            userInfo.setLeftMenu("daily");
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("applyState", 2));
                selectorList.add(SelectorUtils.$eq("applyResult", 1));
                selectorList.add(SelectorUtils.$eq("complete", 1));
                selectorList.add(SelectorUtils.$eq("applyId", "DAILY"));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqNo", keyword), SelectorUtils.$like("subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/report/daily/list.ftl", type = Dispatcher.FreeMarker)})
    public String dailyList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqService.getPageList(getStart(), 10, searchModeCallbackDailyList());
            if (pageObj != null) {
                reqList = pageObj.getResultList();
            }
        }
        return "success";
    }

    public String dailyExport() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            List<Selector> selectorList = new ArrayList<Selector>();
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                selectorList.add(SelectorUtils.$eq("reqId.orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("reqId.applyState", 2));
                selectorList.add(SelectorUtils.$eq("reqId.applyResult", 1));
                selectorList.add(SelectorUtils.$eq("reqId.complete", 1));
                selectorList.add(SelectorUtils.$eq("reqId.applyId", "DAILY"));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqId.reqNo", keyword), SelectorUtils.$like("reqId.subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
            List<WfReqDaily> reqDailyList=this.wfReqDailyService.getByList(selectorList);
            if (reqDailyList != null&&!reqDailyList.isEmpty()) {
                WritableFont blackBoldFont = new WritableFont(WritableFont.ARIAL, 10,
                        WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        jxl.format.Colour.BLACK);//黑色加粗
                WritableFont blackFont = new WritableFont(WritableFont.ARIAL, 11,
                        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        Colour.BLACK);// 黑色普通


                WritableCellFormat blackCenterAll = new WritableCellFormat(blackFont);
                blackCenterAll.setAlignment(Alignment.CENTRE);
                blackCenterAll.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackCenterAll
                        .setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAll.setBorder(Border.LEFT, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAll.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAll.setBorder(Border.RIGHT, BorderLineStyle.THIN,
                        Colour.BLACK);// 黑色普通居中全边框

                WritableCellFormat blackLeftAll = new WritableCellFormat(blackFont);
                blackLeftAll.setAlignment(Alignment.LEFT);
                blackLeftAll.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackLeftAll.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAll.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAll.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackLeftAll
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);// 黑色普通居左全边框

                WritableCellFormat blackLeftAllB = new WritableCellFormat(blackBoldFont);
                blackLeftAllB.setAlignment(Alignment.LEFT);
                blackLeftAllB.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackLeftAllB.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAllB.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAllB.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackLeftAllB
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);// 黑色普通居左全边框

                WritableCellFormat blackCenterAllB = new WritableCellFormat(blackBoldFont);
                blackCenterAllB.setAlignment(Alignment.CENTRE);
                blackCenterAllB.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackCenterAllB.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAllB.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAllB.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAllB
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);

                ExcelCreateController createExcel = new ExcelCreateController();
                String path = getRootPath() + "upload/excelTemp/";
                path += userInfo.getOrgId() + System.currentTimeMillis() + TokenUtils.getToken(userInfo.getSessionId()) + ".xls";
                PropertiesConfig propertiesConfig = (PropertiesConfig) getServletContext().getAttribute("webConfig");
                if (propertiesConfig.getProperties() != null) {
                    String platform = propertiesConfig.getString("platform");
                    if (platform.equals("windows")) {
                        path = path.replaceAll("\\/", "\\\\");
                    }
                }
                createExcel.initializationCreate(path);
                int row = 0;
                int col = 0;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("OA编号", blackCenterAllB);
                createExcel.setColumnView(20);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("申请人", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("申请时间", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);


                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("报销金额金额", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("支付方式", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("收款单位", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("开户行", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("帐号", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用部门", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用类别", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用项目", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用日期", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用金额", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("备注", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                row = 1;
                for(WfReqDaily reqDaily:reqDailyList){
                    List<WfReqDailyDetail> details=this.wfReqDailyDetailService.getDetailListByDailyId(reqDaily.getId());
                    if(details!=null&&!details.isEmpty()){
                        int j=0;
                        for(WfReqDailyDetail detail:details){
                            col = 0;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqDaily, "reqId.reqNo"), blackCenterAll);
                            createExcel.setColumnView(20);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqDaily, "reqId.userId.userName"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(getDate(reqDaily, "reqId.created",DateFormatUtil.YEAR_MONTH_DAY_PATTERN), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }



                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setNumber(get(reqDaily, "amount",Double.class), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            String payMethod="";
                            if(reqDaily.getPayMethod().intValue()==1){
                                payMethod="现金";
                            }else if(reqDaily.getPayMethod().intValue()==2){
                                payMethod="银行转账";
                            }else if(reqDaily.getPayMethod().intValue()==3){
                                payMethod="支票";
                            }else if(reqDaily.getPayMethod().intValue()==4){
                                payMethod="帐扣";
                            }
                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(payMethod, blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqDaily, "payee"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                             if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqDaily, "bank"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(reqDaily, "bankAccount"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(detail, "expenseDept.deptName"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(detail, "expenseType.expenseType"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(detail, "expenseTitle.titleName"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(getDate(detail, "amountDate",DateFormatUtil.YEAR_MONTH_DAY_PATTERN), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setNumber(get(detail, "amount",Double.class), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(detail, "remarks"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);

                            row ++;
                            j++;
                        }
                    }
                }
                createExcel.write();
                createExcel.closeWorkBook();
                String  fileName = "费用报销明细统计表.xls";
                fileName = StringUtils.toUtf8String(fileName);
                File file = FileManager.getFile(path, false);
                download(file, fileName, true);
            }
        }
        return null;
    }

    private List<Selector> searchModeCallbackRePaymentList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("report");
            userInfo.setLeftMenu("rePayment");
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("applyState", 2));
                selectorList.add(SelectorUtils.$eq("applyResult", 1));
                selectorList.add(SelectorUtils.$eq("complete", 1));
                selectorList.add(SelectorUtils.$eq("applyId", "REPAYMENT"));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqNo", keyword), SelectorUtils.$like("subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/report/rePayment/list.ftl", type = Dispatcher.FreeMarker)})
    public String rePaymentList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqService.getPageList(getStart(), 10, searchModeCallbackRePaymentList());
            if (pageObj != null) {
                reqList = pageObj.getResultList();
            }
        }
        return "success";
    }

    public String rePaymentExport() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            List<Selector> selectorList = new ArrayList<Selector>();
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                selectorList.add(SelectorUtils.$eq("reqId.orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("reqId.applyState", 2));
                selectorList.add(SelectorUtils.$eq("reqId.applyResult", 1));
                selectorList.add(SelectorUtils.$eq("reqId.complete", 1));
                selectorList.add(SelectorUtils.$eq("reqId.applyId", "REPAYMENT"));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqId.reqNo", keyword), SelectorUtils.$like("reqId.subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
            List<WfReqRePayment> reqRePaymentList=this.wfReqRePaymentService.getByList(selectorList);
            if (reqRePaymentList != null&&!reqRePaymentList.isEmpty()) {
                WritableFont blackBoldFont = new WritableFont(WritableFont.ARIAL, 10,
                        WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        jxl.format.Colour.BLACK);//黑色加粗
                WritableFont blackFont = new WritableFont(WritableFont.ARIAL, 11,
                        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        Colour.BLACK);// 黑色普通


                WritableCellFormat blackCenterAll = new WritableCellFormat(blackFont);
                blackCenterAll.setAlignment(Alignment.CENTRE);
                blackCenterAll.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackCenterAll
                        .setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAll.setBorder(Border.LEFT, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAll.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAll.setBorder(Border.RIGHT, BorderLineStyle.THIN,
                        Colour.BLACK);// 黑色普通居中全边框

                WritableCellFormat blackLeftAll = new WritableCellFormat(blackFont);
                blackLeftAll.setAlignment(Alignment.LEFT);
                blackLeftAll.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackLeftAll.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAll.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAll.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackLeftAll
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);// 黑色普通居左全边框

                WritableCellFormat blackLeftAllB = new WritableCellFormat(blackBoldFont);
                blackLeftAllB.setAlignment(Alignment.LEFT);
                blackLeftAllB.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackLeftAllB.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAllB.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackLeftAllB.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackLeftAllB
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);// 黑色普通居左全边框

                WritableCellFormat blackCenterAllB = new WritableCellFormat(blackBoldFont);
                blackCenterAllB.setAlignment(Alignment.CENTRE);
                blackCenterAllB.setVerticalAlignment(VerticalAlignment.CENTRE);
                blackCenterAllB.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAllB.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                blackCenterAllB.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                        Colour.BLACK);
                blackCenterAllB
                        .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);

                ExcelCreateController createExcel = new ExcelCreateController();
                String path = getRootPath() + "upload/excelTemp/";
                path += userInfo.getOrgId() + System.currentTimeMillis() + TokenUtils.getToken(userInfo.getSessionId()) + ".xls";
                PropertiesConfig propertiesConfig = (PropertiesConfig) getServletContext().getAttribute("webConfig");
                if (propertiesConfig.getProperties() != null) {
                    String platform = propertiesConfig.getString("platform");
                    if (platform.equals("windows")) {
                        path = path.replaceAll("\\/", "\\\\");
                    }
                }
                createExcel.initializationCreate(path);
                int row = 0;
                int col = 0;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("OA编号", blackCenterAllB);
                createExcel.setColumnView(20);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("申请人", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("申请时间", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("预支申请", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("还款金额", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("支付方式", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("收款单位", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("开户行", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("帐号", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用部门", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用类别", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用项目", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用日期", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("费用金额", blackCenterAllB);
                createExcel.setColumnView(15);
                createExcel.setRowView(500);

                col++;
                createExcel.setCellRow(row);
                createExcel.setCellCol(col);
                createExcel.setContent("备注", blackCenterAllB);
                createExcel.setColumnView(25);
                createExcel.setRowView(500);

                row = 1;
                for(WfReqRePayment rePayment:reqRePaymentList){
                    List<WfReqRePaymentDetail> details=this.wfReqRePaymentDetailService.getDetailListByRePaymentId(rePayment.getId());
                    if(details!=null&&!details.isEmpty()){
                        int j=0;
                        for(WfReqRePaymentDetail detail:details){
                            col = 0;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(rePayment, "reqId.reqNo"), blackCenterAll);
                            createExcel.setColumnView(20);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(rePayment, "reqId.userId.userName"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(getDate(rePayment, "reqId.created",DateFormatUtil.YEAR_MONTH_DAY_PATTERN), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(rePayment, "advanceId.reqId.subject"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setNumber(get(rePayment, "amount",Double.class), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            String payMethod="";
                            if(rePayment.getPayMethod().intValue()==1){
                                payMethod="现金";
                            }else if(rePayment.getPayMethod().intValue()==2){
                                payMethod="银行转账";
                            }else if(rePayment.getPayMethod().intValue()==3){
                                payMethod="支票";
                            }else if(rePayment.getPayMethod().intValue()==4){
                                payMethod="帐扣";
                            }
                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(payMethod, blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(rePayment, "payee"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(rePayment, "bank"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(rePayment, "bankAccount"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);
                            if(j==details.size()-1){
                                createExcel.mergeCells(col,row-details.size()+1,col,row);
                            }

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(detail, "expenseDept.deptName"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(detail, "expenseType.expenseType"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(detail, "expenseTitle.titleName"), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(getDate(detail, "amountDate",DateFormatUtil.YEAR_MONTH_DAY_PATTERN), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);


                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setNumber(get(detail, "amount",Double.class), blackCenterAll);
                            createExcel.setColumnView(15);
                            createExcel.setRowView(500);

                            col++;
                            createExcel.setCellRow(row);
                            createExcel.setCellCol(col);
                            createExcel.setContent(get(detail, "remarks"), blackCenterAll);
                            createExcel.setColumnView(25);
                            createExcel.setRowView(500);

                            row ++;

                            j++;
                        }
                    }
                }
                createExcel.write();
                createExcel.closeWorkBook();
                String  fileName = "预支还款明细统计表.xls";
                fileName = StringUtils.toUtf8String(fileName);
                File file = FileManager.getFile(path, false);
                download(file, fileName, true);
            }
        }
        return null;
    }

}
