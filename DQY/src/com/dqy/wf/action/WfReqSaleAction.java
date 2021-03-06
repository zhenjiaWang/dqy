package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.common.service.TempAttService;
import com.dqy.sale.entity.*;
import com.dqy.sale.service.*;
import com.dqy.sys.entity.SysBudgetTitle;
import com.dqy.sys.entity.SysBudgetType;
import com.dqy.sys.service.SysBudgetTitleService;
import com.dqy.sys.service.SysBudgetTypeService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.wf.entity.*;
import com.dqy.wf.service.*;
import com.google.inject.Inject;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.NumberUtils;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.web.annotation.*;

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
@Action(name = "sale", namespace = "/wf")
public class WfReqSaleAction extends WfReqSupportAction<WfReqSale> {

    @Inject
    private WfVariableGlobalService wfVariableGlobalService;

    @Inject
    private WfReqExecuteService wfReqExecuteService;

    @Inject
    private WfReqService wfReqService;

    @Inject
    private WfReqSaleService wfReqSaleService;

    @Inject
    private WfReqSaleDetailService wfReqSaleDetailService;

    @Inject
    private WfReqSaleDetailTrueService wfReqSaleDetailTrueService;

    @Inject
    private WfReqCommentsService wfReqCommentsService;

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private WfReqTaskService wfReqTaskService;

    @Inject
    private SysBudgetTitleService sysBudgetTitleService;

    @Inject
    private SysBudgetTypeService sysBudgetTypeService;

    @Inject
    private SaleChannelService saleChannelService;

    @Inject
    private SaleDeptService saleDeptService;

    @Inject
    private SaleSystemService saleSystemService;

    @Inject
    private SaleDeptSystemService saleDeptSystemService;

    @Inject
    private SaleCustomerService saleCustomerService;

    @Inject
    private SaleSeriesService saleSeriesService;

    @Inject
    private SaleProductService saleProductService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private WfReqSale wfReqSale;

    @Inject
    private WfReqAttService wfReqAttService;

    @Inject
    private TempAttService tempAttService;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long reqId;

    @ReqGet
    @ReqSet
    private Long taskId;

    @ReqGet
    @ReqSet
    private Long executeId;

    @ReqSet
    public final String applyId = "SALE";


    @ReqSet
    private WfReqTask wfReqTask;

    @ReqSet
    private WfReqExecute wfReqExecute;

    @ReqSet
    private List<WfReqComments> reqCommentsList;


    @ReqSet
    private Date sendDate;


    @ReqSet
    private String applyName;


    @ReqGet
    @ReqSet
    private String attToken;

    @ReqSet
    private List<SysBudgetType> typeList;

    @ReqSet
    private List<SysBudgetTitle> titleList;

    @ReqSet
    private List<SaleChannel> channelList;

    @ReqSet
    private List<SaleDept> deptList;

    @ReqSet
    private List<SaleDeptSystem> deptSystemList;

    @ReqSet
    private List<SaleCustomer> customerList;

    @ReqSet
    private List<SaleSeries> seriesList;

    @ReqSet
    private List<SaleProduct> productList;

    @ReqSet
    private List<Integer> yearList;

    @ReqSet
    private Integer currentYear;

    @ReqGet
    private Integer detailCount;

    @ReqGet
    private Integer detailCount1;

    @ReqSet
    private List<WfReqSaleDetail> reqSaleDetailList;

    @ReqSet
    private List<WfReqSaleDetailTrue> reqSaleDetailTrueList;

    @ReqGet
    @ReqSet
    private String trueAmount;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/sale/input.ftl", type = Dispatcher.FreeMarker),
            @Result(name = "true", path = "/view/wf/sale/inputTrue.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sale");
            userInfo.setLeftMenu("saleApply");
            userInfo.setChildMenu(null);
            sendDate = DateFormatUtil.getCurrentDate(true);

            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("expenseType"));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            typeList = this.sysBudgetTypeService.getAllList(selectorList);

            selectorList.clear();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("titleName"));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            titleList = this.sysBudgetTitleService.getAllList(selectorList);

            selectorList.clear();
            selectorList.add(SelectorUtils.$order("channelName"));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            channelList = this.saleChannelService.getList(selectorList);


            if (channelList != null && !channelList.isEmpty()) {
                SaleChannel saleChannel = channelList.get(0);
                if (saleChannel != null) {
                    selectorList.clear();
                    selectorList.add(SelectorUtils.$eq("channelId.id", saleChannel.getId()));
                    selectorList.add(SelectorUtils.$order("deptName"));
                    selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                    deptList = this.saleDeptService.getList(selectorList);
                    if (deptList != null && !deptList.isEmpty()) {
                        SaleDept saleDept = deptList.get(0);
                        if (saleDept != null) {
                            selectorList.clear();
                            selectorList.add(SelectorUtils.$alias("systemId", "systemId"));
                            selectorList.add(SelectorUtils.$eq("deptId.id", saleDept.getId()));
                            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                            selectorList.add(SelectorUtils.$eq("systemId.useYn", "Y"));
                            selectorList.add(SelectorUtils.$order("systemId.systemName"));
                            deptSystemList = this.saleDeptSystemService.getList(selectorList);
                            if (deptSystemList != null && !deptSystemList.isEmpty()) {
                                SaleDeptSystem saleDeptSystem = deptSystemList.get(0);
                                if (saleDeptSystem != null) {
                                    selectorList.clear();
                                    selectorList.add(SelectorUtils.$eq("systemId.id", saleDeptSystem.getSystemId().getId()));
                                    selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                                    selectorList.add(SelectorUtils.$order("customerName"));
                                    customerList = this.saleCustomerService.getList(selectorList);
                                }
                            }
                        }
                    }
                }
            }

            selectorList.clear();
            selectorList.add(SelectorUtils.$order("seriesName"));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            seriesList = this.saleSeriesService.getList(selectorList);

            if (seriesList != null && !seriesList.isEmpty()) {
                SaleSeries saleSeries = seriesList.get(0);
                if (saleSeries != null) {
                    selectorList.clear();
                    selectorList.add(SelectorUtils.$eq("seriesId.id", saleSeries.getId()));
                    selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                    selectorList.add(SelectorUtils.$order("productCode"));
                    productList=this.saleProductService.getList(selectorList);
                }
            }
            Date currentDate = DateFormatUtil.getCurrentDate(false);
            if (currentYear == null) {
                currentYear = DateFormatUtil.getDayInYear(currentDate);
            }
            yearList = new ArrayList<Integer>();
            yearList.add(currentYear - 1);
            yearList.add(currentYear);
            yearList.add(currentYear + 1);
        }
        if(StringUtils.isNotBlank(trueAmount)){
            return "true";
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/req!ingList.dhtml", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && wfReqSale != null) {
            initReq();
            wfReqSale.setReqId(wfReq);
            wfReqSale.setUseYn("Y");
            wfReqSale.setTrueAmount(0.00d);
            bind(wfReqSale);
            Double totalAmount = 0.00d;

            if (detailCount != null) {
                reqSaleDetailList = new ArrayList<WfReqSaleDetail>();
                for (int i = 1; i <= detailCount; i++) {
                    Long productId = getParameter("productId" + i, Long.class);
                    Long seriesId = getParameter("seriesId" + i, Long.class);
                    Double productAmount = getParameter("productAmount" + i, Double.class);
                    if (seriesId != null && productId != null && productAmount != null) {
                        if (productAmount == null) {
                            productAmount = 0.00d;
                        }
                        SaleSeries saleSeries = this.saleSeriesService.getById(seriesId);
                        SaleProduct saleProduct = this.saleProductService.getById(productId);
                        if (saleSeries!=null&&saleProduct != null) {
                            WfReqSaleDetail saleDetail = new WfReqSaleDetail();
                            saleDetail.setSeriesId(saleSeries);
                            saleDetail.setProductId(saleProduct);
                            saleDetail.setSaleId(wfReqSale);
                            saleDetail.setAmount(productAmount);
                            bind(saleDetail);
                            saleDetail.setUseYn("Y");
                            totalAmount += productAmount;
                            reqSaleDetailList.add(saleDetail);
                        }
                    }
                }
            }
            wfReqSale.setAmount(NumberUtils.multiply(totalAmount, 1, 2));
            this.wfReqSaleService.save(wfReqSale,reqSaleDetailList, wfReq, wfReqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast, reqAttList);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/req!ingList.dhtml", type = Dispatcher.Redirect)})
    public String saveTrue() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && wfReqSale != null) {
            initReq();
            wfReqSale.setReqId(wfReq);
            wfReqSale.setUseYn("Y");
            bind(wfReqSale);
            Double totalAmount = 0.00d;
            Double trueTotalAmount = 0.00d;
            if (detailCount != null) {
                reqSaleDetailList = new ArrayList<WfReqSaleDetail>();
                for (int i = 1; i <= detailCount; i++) {
                    Long productId = getParameter("productId" + i, Long.class);
                    Long seriesId = getParameter("seriesId" + i, Long.class);
                    Double productAmount = getParameter("productAmount" + i, Double.class);
                    if (seriesId != null && productId != null && productAmount != null) {
                        if (productAmount == null) {
                            productAmount = 0.00d;
                        }
                        SaleSeries saleSeries = this.saleSeriesService.getById(seriesId);
                        SaleProduct saleProduct = this.saleProductService.getById(productId);
                        if (saleSeries!=null&&saleProduct != null) {
                            WfReqSaleDetail saleDetail = new WfReqSaleDetail();
                            saleDetail.setSeriesId(saleSeries);
                            saleDetail.setProductId(saleProduct);
                            saleDetail.setSaleId(wfReqSale);
                            saleDetail.setAmount(productAmount);
                            bind(saleDetail);
                            saleDetail.setUseYn("Y");
                            totalAmount += productAmount;
                            reqSaleDetailList.add(saleDetail);
                        }
                    }
                }
            }

            if (detailCount1 != null) {
                reqSaleDetailTrueList = new ArrayList<WfReqSaleDetailTrue>();
                for (int i = 1; i <= detailCount1; i++) {
                    Long typeId = getParameter("expType" + i, Long.class);
                    Long titleId = getParameter("expTitle" + i, Long.class);
                    Double productAmount = getParameter("productAmount1" + i, Double.class);
                    if (typeId != null && titleId != null && productAmount != null) {
                        if (productAmount == null) {
                            productAmount = 0.00d;
                        }
                        SysBudgetType sysBudgetType = this.sysBudgetTypeService.getById(typeId);
                        SysBudgetTitle sysBudgetTitle = this.sysBudgetTitleService.getById(titleId);
                        if (sysBudgetType!=null&&sysBudgetTitle != null) {
                            WfReqSaleDetailTrue saleDetailTrue = new WfReqSaleDetailTrue();
                            saleDetailTrue.setExpenseType(sysBudgetType);
                            saleDetailTrue.setExpenseTitle(sysBudgetTitle);
                            saleDetailTrue.setSaleId(wfReqSale);
                            saleDetailTrue.setAmount(productAmount);
                            bind(saleDetailTrue);
                            saleDetailTrue.setUseYn("Y");
                            trueTotalAmount += productAmount;
                            reqSaleDetailTrueList.add(saleDetailTrue);
                        }
                    }
                }
            }
            wfReqSale.setAmount(NumberUtils.multiply(totalAmount, 1, 2));
            wfReqSale.setTrueAmount(NumberUtils.multiply(trueTotalAmount, 1, 2));
            this.wfReqSaleService.saveTrue(wfReqSale,reqSaleDetailList,reqSaleDetailTrueList, wfReq, wfReqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast, reqAttList);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/sale/print.ftl", type = Dispatcher.FreeMarker)})
    public String print() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && reqId != null) {
            wfReqSale = this.wfReqSaleService.getByReqId(reqId);
            if (wfReqSale != null) {
                wfReq = wfReqSale.getReqId();
                if (wfReq != null) {
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqSaleDetailList = this.wfReqSaleDetailService.getBySaleId(wfReqSale.getId());
                    reqSaleDetailTrueList=this.wfReqSaleDetailTrueService.getBySaleId(wfReqSale.getId());
                }
                if (StringUtils.isNotBlank(applyId)) {
                    if (applyId.equals("ADVANCE_ACCOUNT")) {
                        applyName = "预支申请";
                    } else if (applyId.equals("REPAYMENT")) {
                        applyName = "还款申请";
                    } else if (applyId.equals("DAILY")) {
                        applyName = "费用报销";
                    } else if (applyId.equals("BUSINESS")) {
                        applyName = "事务申请";
                    } else if (applyId.equals("SALE")) {
                        applyName = "销售费用报销";
                    }
                }
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/sale/view.ftl", type = Dispatcher.FreeMarker)})
    public String view() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && reqId != null) {
            wfReqSale = this.wfReqSaleService.getByReqId(reqId);
            if (wfReqSale != null) {
                wfReq = wfReqSale.getReqId();
                if (wfReq != null) {
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList = wfReqAttService.getByReqId(wfReq.getId());
                    reqSaleDetailList = this.wfReqSaleDetailService.getBySaleId(wfReqSale.getId());
                    reqSaleDetailTrueList=this.wfReqSaleDetailTrueService.getBySaleId(wfReqSale.getId());
                }
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/sale/financial.ftl", type = Dispatcher.FreeMarker)})
    public String financial() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && reqId != null) {
            wfReqSale = this.wfReqSaleService.getByReqId(reqId);
            if (wfReqSale != null) {
                wfReq = wfReqSale.getReqId();
                if (wfReq != null) {
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList = wfReqAttService.getByReqId(wfReq.getId());
                    reqSaleDetailList = this.wfReqSaleDetailService.getBySaleId(wfReqSale.getId());
                    reqSaleDetailTrueList=this.wfReqSaleDetailTrueService.getBySaleId(wfReqSale.getId());
                }
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/sale/process.ftl", type = Dispatcher.FreeMarker)})
    public String process() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && taskId != null) {
            wfReqTask = this.wfReqTaskService.getById(taskId);
            if (wfReqTask != null) {
                wfReq = wfReqTask.getReqId();
                if (wfReq != null) {
                    wfReqSale = this.wfReqSaleService.getByReqId(wfReq.getId());
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList = wfReqAttService.getByReqId(wfReq.getId());
                    reqSaleDetailList = this.wfReqSaleDetailService.getBySaleId(wfReqSale.getId());
                    reqSaleDetailTrueList=this.wfReqSaleDetailTrueService.getBySaleId(wfReqSale.getId());
                }
            }
        }
        return "success";
    }


}
