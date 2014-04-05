package com.dqy.sale.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sale.entity.SaleChannel;
import com.dqy.sale.entity.SaleCustomer;
import com.dqy.sale.entity.SaleDept;
import com.dqy.sale.entity.SaleSystem;
import com.dqy.sale.service.SaleChannelService;
import com.dqy.sale.service.SaleCustomerService;
import com.dqy.sale.service.SaleDeptService;
import com.dqy.sale.service.SaleSystemService;
import com.dqy.web.support.ActionSupport;
import com.google.inject.Inject;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.web.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "customer", namespace = "/sale")
public class SaleCustomerAction extends ActionSupport<SaleCustomer> {

    @Inject
    private SaleCustomerService saleCustomerService;


    @Inject
    private SaleSystemService saleSystemService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SaleCustomer saleCustomer;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long systemId;


    @ReqSet
    private List<SaleSystem> systemList;


    @ReqGet
    @ReqSet
    private String keyword;

    @ReqSet
    private List<SaleCustomer> customerList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sale/customer/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sale");
            userInfo.setLeftMenu("saleCustomer");
            pageObj = this.saleCustomerService.getPageList(getStart(), rows, searchModeCallback());
            if(pageObj!=null){
                customerList=pageObj.getResultList();
            }

        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            selectorList.add(SelectorUtils.$order("systemId.id"));
            selectorList.add(SelectorUtils.$order("customerName"));
            if(StringUtils.isNotBlank(keyword)){
                selectorList.add(SelectorUtils.$like("customerName", keyword));
            }
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sale/customer/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            saleCustomer = this.saleCustomerService.getById(id);
            systemId=saleCustomer.getSystemId().getId();
        }
        List<Selector> selectorList=new ArrayList<Selector>();
        selectorList.add(SelectorUtils.$eq("useYn","Y"));
        systemList=this.saleSystemService.getList(selectorList);
        if(systemId==null){
            if(systemList!=null&&!systemList.isEmpty()){
                systemId=systemList.get(0).getId();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && saleCustomer != null) {
            if (saleCustomer.getId() != null) {
                SaleCustomer old = this.saleCustomerService.getById(saleCustomer.getId());
                saleCustomer = this.copy(saleCustomer, old);
            }
            if(saleCustomer.getSystemId()!=null){
                if(saleCustomer.getSystemId().getId()==null){
                    saleCustomer.setSystemId(null);
                }
            }
            this.bind(saleCustomer);
            this.saleCustomerService.save(saleCustomer);
        }
        return "saveSuccess";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sale/customer.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            saleCustomer = this.saleCustomerService.getById(id);
            if (saleCustomer != null) {
                saleCustomerService.delete(saleCustomer);
            }
        }
        return "success";
    }


    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (saleCustomer != null) {
                if (StringUtils.isNotBlank(saleCustomer.getCustomerName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(saleCustomer.getCustomerName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.saleCustomerService.validateName(saleCustomer.getCustomerName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.saleCustomerService.validateName(saleCustomer.getCustomerName());
                        if (row.intValue() == 0) {
                            item.put("result", true);
                        }
                    }
                }
            }
        }
        writeJsonByAction(item.toString());
        return null;
    }



    @PageFlow(result = {@Result(name = "success", path = "/sale/customer.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            saleCustomer = this.saleCustomerService.getById(id);
            if (saleCustomer != null) {
                saleCustomer.setUseYn("N");
                saleCustomerService.save(saleCustomer);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sale/customer.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            saleCustomer = this.saleCustomerService.getById(id);
            if (saleCustomer != null) {
                saleCustomer.setUseYn("Y");
                saleCustomerService.save(saleCustomer);
            }
        }
        return "success";
    }

}
