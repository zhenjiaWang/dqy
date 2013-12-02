package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.common.entity.TempBudgetAmount;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.sys.entity.*;
import com.dqy.sys.service.*;
import com.dqy.util.ExcelCreateController;
import com.dqy.web.support.ActionSupport;
import com.dqy.wf.entity.WfReqDaily;
import com.dqy.wf.entity.WfReqDailyDetail;
import com.dqy.wf.entity.WfReqRePaymentDetail;
import com.dqy.wf.service.WfReqDailyDetailService;
import com.dqy.wf.service.WfReqRePaymentDetailService;
import com.google.inject.Inject;
import jxl.format.*;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.guiceside.commons.TokenUtils;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.support.file.FileManager;
import org.guiceside.support.properties.PropertiesConfig;
import org.guiceside.web.annotation.*;

import java.io.File;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "budgetAmount", namespace = "/sys")
public class SysBudgetAmountAction extends ActionSupport<SysBudgetAmount> {

    @Inject
    private SysBudgetAmountService sysBudgetAmountService;

    @Inject
    private SysBudgetTypeService sysBudgetTypeService;

    @Inject
    private WfReqDailyDetailService wfReqDailyDetailService;

    @Inject
    private WfReqRePaymentDetailService wfReqRePaymentDetailService;

    @Inject
    private SysFinancialTitleService sysFinancialTitleService;

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private HrDepartmentService hrDepartmentService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SysBudgetAmount sysBudgetAmount;

    @ReqSet
    private HrDepartment hrDepartment;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    private String approveBudget;

    @ReqGet
    @ReqSet
    private Long deptId;

    @ReqGet
    @ReqSet
    private Integer rows;

    @ReqGet
    @ReqSet
    private Integer currentYear;

    @ReqSet
    private Double totalAmount;

    @ReqSet
    private Double totalPassAmount;

    @ReqSet
    private Double totalIngAmount;

    @ReqSet
    private Double remnantAmount;
    @ReqSet
    private List<Integer> yearList;

    @ReqSet
    private List<SysBudgetType> budgetTypeList;

    @ReqSet
    private List<SysBudgetAmount> sysBudgetAmountList;

    @ReqSet
    private List<SysFinancialTitle> titleList;

    @ReqSet
    private List<SysBudgetType> typeList;
    @ReqSet
    private List<HrDepartment> departmentList;

    @ReqSet
    private List<TempBudgetAmount> tempBudgetAmountList;

    @ReqSet
    private Set<String> idSets;


    @ReqSet
    private Map<String, List<SysBudgetTitle>> budgetTitleMap;

    @ReqSet
    private Map<String, Double> budgetAmountMap;

    private Map<Integer, String> engMap;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetAmount/index.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            totalAmount = 0.00d;
            userInfo.setTopMenu("budget");
            userInfo.setLeftMenu("budgetAmount");
            Date currentDate = DateFormatUtil.getCurrentDate(false);
            if (currentYear == null) {
                currentYear = DateFormatUtil.getDayInYear(currentDate);
            }
            yearList = new ArrayList<Integer>();
            yearList.add(currentYear - 1);
            yearList.add(currentYear);
            yearList.add(currentYear + 1);
            yearList.add(currentYear + 2);

            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("expenseType"));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            typeList = this.sysBudgetTypeService.getAllList(selectorList);


            hrDepartment = this.hrDepartmentService.getById(userInfo.getDepartmentId());

            rows = 0;
            sysBudgetAmountList = this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(), currentYear, hrDepartment.getId());
            if (sysBudgetAmountList != null && !sysBudgetAmountList.isEmpty()) {
                if (sysBudgetAmount == null) {
                    sysBudgetAmount = sysBudgetAmountList.get(0);
                }
                idSets = new HashSet<String>();
                budgetAmountMap = new HashMap<String, Double>();
                for (SysBudgetAmount budgetAmount : sysBudgetAmountList) {
                    idSets.add(get(budgetAmount, "typeId.id") + "_" + get(budgetAmount, "titleId.id") + "_" + get(budgetAmount, "titleId.titleName"));
                    budgetAmountMap.put(get(budgetAmount, "typeId.id") + "_" + get(budgetAmount, "titleId.id") + "_" + budgetAmount.getMonth(),
                            budgetAmount.getAmount());
                }
                if (idSets != null && !idSets.isEmpty()) {
                    rows = idSets.size();
                }
            }
            totalAmount = sysBudgetAmountService.geTotalAmount(userInfo.getOrgId(), currentYear, hrDepartment.getId());
            if (totalAmount == null) {
                totalAmount = 0.00d;
            }

        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String export() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (currentYear != null) {
                if (deptId == null) {
                    deptId = userInfo.getDepartmentId();
                }
                hrDepartment = this.hrDepartmentService.getById(userInfo.getDepartmentId());
                if (hrDepartment != null) {
                    intEngMap();
                    WritableFont blackBoldFont = new WritableFont(WritableFont.ARIAL, 10,
                            WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                            jxl.format.Colour.BLACK);// ��ɫ����
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

                    WritableCellFormat blackCenterAllB = new WritableCellFormat(blackBoldFont);
                    blackCenterAllB.setAlignment(Alignment.CENTRE);
                    blackCenterAllB.setVerticalAlignment(VerticalAlignment.CENTRE);
                    blackCenterAllB.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                    blackCenterAllB.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                    blackCenterAllB.setBorder(Border.BOTTOM, BorderLineStyle.THIN,
                            Colour.BLACK);
                    blackCenterAllB
                            .setBorder(Border.RIGHT, BorderLineStyle.THIN, Colour.BLACK);

                    ExcelCreateController<SysBudgetAmount> createExcel = new ExcelCreateController<SysBudgetAmount>();
                    String path = getRootPath() + "upload/excelTemp/";
                    path += System.currentTimeMillis() + TokenUtils.getToken(userInfo.getSessionId()) + ".xls";
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
                    createExcel.setContent("预算部门", blackCenterAllB);
                    createExcel.setColumnView(15);
                    col++;
                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setContent("费用类别", blackCenterAllB);
                    createExcel.setColumnView(15);
                    col++;

                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setContent("费用名称", blackCenterAllB);
                    createExcel.setColumnView(15);
                    col++;

                    createExcel.setCellRow(row);
                    createExcel.setCellCol(col);
                    createExcel.setContent("年预算", blackCenterAllB);
                    createExcel.setColumnView(10);
                    col++;
                    for (int i = 1; i <= 12; i++) {
                        createExcel.setCellRow(row);
                        createExcel.setCellCol(col);
                        createExcel.setContent(i + "月", blackCenterAllB);
                        createExcel.setColumnView(10);
                        col++;
                    }
                    sysBudgetAmountList = this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(), currentYear, hrDepartment.getId());
                    if (sysBudgetAmountList != null && !sysBudgetAmountList.isEmpty()) {
                        budgetAmountMap = new HashMap<String, Double>();
                        idSets = new HashSet<String>();
                        for (SysBudgetAmount budgetAmount : sysBudgetAmountList) {
                            idSets.add(get(budgetAmount, "typeId.id") + "_" + get(budgetAmount, "titleId.id"));
                        }
                        if (idSets != null && !idSets.isEmpty()) {
                            for (String idStr : idSets) {
                                String[] idStrs = idStr.split("_");
                                Long typeID = BeanUtils.convertValue(idStrs[0], Long.class);
                                Long titleID = BeanUtils.convertValue(idStrs[1], Long.class);
                                if (typeID != null && titleID != null) {
                                    SysBudgetType budgetType = this.sysBudgetTypeService.getById(typeID);
                                    SysFinancialTitle financialTitle = this.sysFinancialTitleService.getById(titleID);
                                    if (budgetType != null && financialTitle != null) {
                                        sysBudgetAmountList = this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(), currentYear, hrDepartment.getId(), budgetType.getId(), financialTitle.getId());
                                        if (sysBudgetAmountList != null && !sysBudgetAmountList.isEmpty()) {
                                            col = 0;
                                            row++;
                                            createExcel.setCellRow(row);
                                            createExcel.setCellCol(col);
                                            createExcel.setContent(hrDepartment.getDeptName(), blackCenterAll);
                                            col++;

                                            createExcel.setCellRow(row);
                                            createExcel.setCellCol(col);
                                            createExcel.setContent(budgetType.getExpenseType(), blackCenterAll);
                                            col++;

                                            createExcel.setCellRow(row);
                                            createExcel.setCellCol(col);
                                            createExcel.setContent(financialTitle.getTitleName(), blackCenterAll);
                                            col++;

                                            createExcel.setCellRow(row);
                                            createExcel.setCellCol(col);
                                            createExcel.setFormula(formatSum(col+1, row, col+12, row),blackCenterAll);
                                            col++;

                                            for (SysBudgetAmount budgetAmount : sysBudgetAmountList) {
                                                createExcel.setCellRow(row);
                                                createExcel.setCellCol(col);
                                                createExcel.setNumber(budgetAmount.getAmount(), blackCenterAll);
                                                col++;
                                            }
                                        }
                                    }
                                }
                            }
                            createExcel.write();
                            createExcel.closeWorkBook();
                            String attchName = org.guiceside.commons.lang.StringUtils.toUtf8String(hrDepartment.getDeptName() + " 第" + currentYear + "年预算.xls");
                            File file = FileManager.getFile(path, false);
                            download(file, attchName);
                            file.delete();
                        }
                    }
                }
            }
        }
        return null;
    }

    private void intEngMap() {
        engMap = new HashMap<Integer, String>();
        engMap.put(0, "A");
        engMap.put(1, "B");
        engMap.put(2, "C");
        engMap.put(3, "D");
        engMap.put(4, "E");
        engMap.put(5, "F");
        engMap.put(6, "G");
        engMap.put(7, "H");
        engMap.put(8, "I");
        engMap.put(9, "J");
        engMap.put(10, "K");
        engMap.put(11, "L");
        engMap.put(12, "M");
        engMap.put(13, "N");
        engMap.put(14, "O");
        engMap.put(15, "P");
        engMap.put(16, "Q");
        engMap.put(17, "R");
        engMap.put(18, "S");
        engMap.put(19, "T");
        engMap.put(20, "U");
        engMap.put(21, "V");
        engMap.put(22, "W");
        engMap.put(23, "X");
        engMap.put(24, "Y");
        engMap.put(25, "Z");//
    }

    private String getPlace(int col) throws Exception {
        String place = null;
        int basic = 25;
        int j = 0;
        int t = 0;
        int k = 0;
        int n = 0;
        int e = 0;
        int begin = 0;
        int end = 0;
        if (col > basic) {
            j = col / basic;
            k = j - 1;
            t = j * basic + j;
            if (col >= t) {
                begin = j - 1;
            } else {
                begin = k - 1;
            }
            if (k == 0) {
                k = 1;
            }
            n = j * basic;
            e = col - n;
            if (e >= j) {
                end = e - j;
            } else {
                end = (basic + 1) - (j - e);
            }
            place = engMap.get(begin) + engMap.get(end);
        } else {
            place = engMap.get(col);
        }
        return place;
    }

    private String formatSum(int scol, int srow, int ecol, int erow)
            throws Exception {
        String startCell = getPlace(scol).concat(String.valueOf(srow + 1));
        String endCell = getPlace(ecol).concat(String.valueOf(erow + 1));
        String formula = "SUM(" + startCell + ":" + endCell + ")";
        return formula;
    }
    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetAmount!approve.dhtml?currentYear=${currentYear}&deptId=${deptId}", type = Dispatcher.Redirect)})
    public String lock() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && deptId != null && currentYear != null) {
            List<SysBudgetAmount> budgetAmountList = this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(), currentYear, deptId);
            if (budgetAmountList != null && !budgetAmountList.isEmpty()) {
                for (SysBudgetAmount budgetAmount : budgetAmountList) {
                    budgetAmount.setLockYn("Y");
                    bind(budgetAmount);
                }
                this.sysBudgetAmountService.save(budgetAmountList);
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetAmount!approve.dhtml?currentYear=${currentYear}&deptId=${deptId}", type = Dispatcher.Redirect)})
    public String unLock() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && deptId != null && currentYear != null) {
            List<SysBudgetAmount> budgetAmountList = this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(), currentYear, deptId);
            if (budgetAmountList != null && !budgetAmountList.isEmpty()) {
                for (SysBudgetAmount budgetAmount : budgetAmountList) {
                    budgetAmount.setLockYn("N");
                    bind(budgetAmount);
                }
                this.sysBudgetAmountService.save(budgetAmountList);
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetAmount/approve.ftl", type = Dispatcher.FreeMarker)})
    public String approve() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            totalAmount = 0.00d;
            userInfo.setTopMenu("budget");
            userInfo.setLeftMenu("budgetAmountApprove");
            Date currentDate = DateFormatUtil.getCurrentDate(false);
            if (currentYear == null) {
                currentYear = DateFormatUtil.getDayInYear(currentDate);
            }
            yearList = new ArrayList<Integer>();
            yearList.add(currentYear - 1);
            yearList.add(currentYear);
            yearList.add(currentYear + 1);
            yearList.add(currentYear + 2);

            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("expenseType"));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            typeList = this.sysBudgetTypeService.getAllList(selectorList);

            if (deptId == null) {
                deptId = userInfo.getDepartmentId();
            }

            selectorList.clear();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("deptNo"));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            departmentList = this.hrDepartmentService.getByList(selectorList);
            if (departmentList != null && !departmentList.isEmpty()) {
                for (HrDepartment department : departmentList) {
                    totalAmount = sysBudgetAmountService.geTotalAmount(userInfo.getOrgId(), currentYear, department.getId());
                    if (totalAmount == null) {
                        totalAmount = 0.00d;
                    }
                    department.setBudgetAmount(totalAmount);
                }
            }
            hrDepartment = this.hrDepartmentService.getById(deptId);

            rows = 0;
            sysBudgetAmountList = this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(), currentYear, hrDepartment.getId());
            if (sysBudgetAmountList != null && !sysBudgetAmountList.isEmpty()) {
                if (sysBudgetAmount == null) {
                    sysBudgetAmount = sysBudgetAmountList.get(0);
                }
                idSets = new HashSet<String>();
                budgetAmountMap = new HashMap<String, Double>();
                for (SysBudgetAmount budgetAmount : sysBudgetAmountList) {
                    idSets.add(get(budgetAmount, "typeId.id") + "_" + get(budgetAmount, "titleId.id") + "_" + get(budgetAmount, "titleId.titleName"));
                    budgetAmountMap.put(get(budgetAmount, "typeId.id") + "_" + get(budgetAmount, "titleId.id") + "_" + budgetAmount.getMonth(),
                            budgetAmount.getAmount());
                }
                if (idSets != null && !idSets.isEmpty()) {
                    rows = idSets.size();
                }
            }
            totalAmount = sysBudgetAmountService.geTotalAmount(userInfo.getOrgId(), currentYear, hrDepartment.getId());
            if (totalAmount == null) {
                totalAmount = 0.00d;
            }
            String startDateStr = currentYear + "-01-01 00:00:01";
            String endDateStr = currentYear + "-12-31 23:23:59";
            Date startDate = DateFormatUtil.parse(startDateStr, DateFormatUtil.YMDHMS_PATTERN);
            Date endDate = DateFormatUtil.parse(endDateStr, DateFormatUtil.YMDHMS_PATTERN);
            Double dailyIng = this.wfReqDailyDetailService.getSumAmountByIng(userInfo.getOrgId(), hrDepartment.getId(), startDate, endDate);
            Double dailyPass = this.wfReqDailyDetailService.getSumAmountByPass(userInfo.getOrgId(), hrDepartment.getId(), startDate, endDate);

            Double rePaymentIng = this.wfReqRePaymentDetailService.getSumAmountByIng(userInfo.getOrgId(), hrDepartment.getId(), startDate, endDate);
            Double rePaymentPass = this.wfReqRePaymentDetailService.getSumAmountByPass(userInfo.getOrgId(), hrDepartment.getId(), startDate, endDate);
            if (dailyIng == null) {
                dailyIng = 0.00d;
            }
            if (dailyPass == null) {
                dailyPass = 0.00d;
            }
            if (rePaymentIng == null) {
                rePaymentIng = 0.00d;
            }
            if (rePaymentPass == null) {
                rePaymentPass = 0.00d;
            }
            totalIngAmount = dailyIng + rePaymentIng;

            totalPassAmount = dailyPass + rePaymentPass;

            remnantAmount = totalPassAmount - totalAmount;
            if (remnantAmount.doubleValue() < 0) {
                remnantAmount = 0.00d;
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetAmount.dhtml?currentYear=${currentYear}", type = Dispatcher.Redirect),
            @Result(name = "successApprove", path = "/sys/budgetAmount!approve.dhtml?currentYear=${currentYear}&deptId=${deptId}", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (currentYear != null && deptId != null && rows != null && rows.intValue() > 0) {
                hrDepartment = this.hrDepartmentService.getById(deptId);
                SysOrg sysOrg = this.sysOrgService.getById(userInfo.getOrgId());
                List<SysBudgetAmount> delBudgeAmountList = null;
                Set<String> setStr = new HashSet<String>();
                if (hrDepartment != null) {
                    delBudgeAmountList = this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(), currentYear, hrDepartment.getId());
                    sysBudgetAmountList = new ArrayList<SysBudgetAmount>();
                    SysBudgetType budgetType = null;
                    SysFinancialTitle sysFinancialTitle = null;
                    for (int index = 1; index <= rows; index++) {
                        budgetType = null;
                        sysFinancialTitle = null;
                        Long typeId = getParameter("typeId" + index, Long.class);
                        if (typeId != null) {
                            budgetType = this.sysBudgetTypeService.getById(typeId);
                        }
                        Long titleId = getParameter("titleId" + index, Long.class);
                        if (titleId != null) {
                            sysFinancialTitle = this.sysFinancialTitleService.getById(titleId);
                        }
                        String str = hrDepartment.getId() + "_" + typeId + "_" + titleId;
                        if (!setStr.contains(str)) {
                            setStr.add(str);
                            for (int month = 1; month <= 12; month++) {
                                Double amount = getParameter("amount" + index + "_" + month, Double.class);
                                if (amount == null) {
                                    amount = 0.00d;
                                }
                                sysBudgetAmount = new SysBudgetAmount();
                                sysBudgetAmount.setOrgId(sysOrg);
                                sysBudgetAmount.setTypeId(budgetType);
                                sysBudgetAmount.setTitleId(sysFinancialTitle);
                                sysBudgetAmount.setYear(currentYear);
                                sysBudgetAmount.setDeptId(hrDepartment);
                                sysBudgetAmount.setMonth(month);
                                sysBudgetAmount.setAmount(amount);
                                sysBudgetAmount.setUseYn("Y");
                                sysBudgetAmount.setLockYn("N");
                                bind(sysBudgetAmount);
                                sysBudgetAmountList.add(sysBudgetAmount);
                            }
                        }
                    }
                    if ((sysBudgetAmountList != null && !sysBudgetAmountList.isEmpty()) ||
                            (delBudgeAmountList != null && !delBudgeAmountList.isEmpty())) {
                        this.sysBudgetAmountService.save(delBudgeAmountList, sysBudgetAmountList);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(approveBudget)) {
            return "successApprove";
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetAmount/monitor.ftl", type = Dispatcher.FreeMarker)})
    public String monitor() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            totalAmount = 0.00d;
            userInfo.setTopMenu("budget");
            userInfo.setLeftMenu("budgetMonitor");
            Date currentDate = DateFormatUtil.getCurrentDate(false);
            if (currentYear == null) {
                currentYear = DateFormatUtil.getDayInYear(currentDate);
            }
            yearList = new ArrayList<Integer>();
            yearList.add(currentYear - 1);
            yearList.add(currentYear);
            yearList.add(currentYear + 1);
            yearList.add(currentYear + 2);

            hrDepartment = this.hrDepartmentService.getById(userInfo.getDepartmentId());

            rows = 0;
            sysBudgetAmountList = this.sysBudgetAmountService.getAmountListByTitleLv2(userInfo.getOrgId(), currentYear, hrDepartment.getId());
            if (sysBudgetAmountList != null && !sysBudgetAmountList.isEmpty()) {
                if (sysBudgetAmount == null) {
                    sysBudgetAmount = sysBudgetAmountList.get(0);
                }
                idSets = new HashSet<String>();
                for (SysBudgetAmount budgetAmount : sysBudgetAmountList) {
                    idSets.add(get(budgetAmount, "typeId.id") + "_" + get(budgetAmount, "titleId.id"));
                }
                if (idSets != null && !idSets.isEmpty()) {
                    rows = idSets.size();
                    for(String idStr:idSets){
                        String[] idStrs = idStr.split("_");
                        Long typeID = BeanUtils.convertValue(idStrs[0], Long.class);
                        Long titleID = BeanUtils.convertValue(idStrs[1], Long.class);
                        if (typeID != null && titleID != null) {
                            tempBudgetAmountList=new ArrayList<TempBudgetAmount>();
                            SysBudgetType budgetType = this.sysBudgetTypeService.getById(typeID);
                            SysFinancialTitle financialTitle = this.sysFinancialTitleService.getById(titleID);
                            if (budgetType != null && financialTitle != null) {
                                TempBudgetAmount tempBudgetAmount=new TempBudgetAmount();
                                tempBudgetAmount.setHrDepartment(hrDepartment);
                                tempBudgetAmount.setSysBudgetType(budgetType);
                                tempBudgetAmount.setSysFinancialTitle(financialTitle);
                                for(int i=1;i<=12;i++){
                                    Double amountTotalMonth=this.sysBudgetAmountService.countAmountListByTitleNo(userInfo.getOrgId(), currentYear, hrDepartment.getId(), budgetType.getId(), financialTitle.getTitleNo(),i);
                                    if(amountTotalMonth==null){
                                        amountTotalMonth=0.0d;
                                    }
                                    BeanUtils.setValue(tempBudgetAmount,"monthAmount"+i,amountTotalMonth);
                                }
                                tempBudgetAmountList.add(tempBudgetAmount);
                            }
                        }
                    }
                }
            }
            totalAmount = sysBudgetAmountService.geTotalAmount(userInfo.getOrgId(), currentYear, hrDepartment.getId());
            if (totalAmount == null) {
                totalAmount = 0.00d;
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

}
