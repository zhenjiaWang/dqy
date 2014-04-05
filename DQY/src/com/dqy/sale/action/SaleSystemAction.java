package com.dqy.sale.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sale.entity.SaleChannel;
import com.dqy.sale.entity.SaleSystem;
import com.dqy.sale.service.SaleChannelService;
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
@Action(name = "system", namespace = "/sale")
public class SaleSystemAction extends ActionSupport<SaleSystem> {

    @Inject
    private SaleSystemService saleSystemService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SaleSystem saleSystem;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private String keyword;

    @ReqSet
    private List<SaleSystem> systemList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sale/system/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sale");
            userInfo.setLeftMenu("saleSystem");
            pageObj = this.saleSystemService.getPageList(getStart(), rows, searchModeCallback());
            if(pageObj!=null){
                systemList=pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if(StringUtils.isNotBlank(keyword)){
                selectorList.add(SelectorUtils.$like("systemName",keyword));
            }
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sale/system/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            saleSystem = this.saleSystemService.getById(id);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && saleSystem != null) {
            if (saleSystem.getId() != null) {
                SaleSystem old = this.saleSystemService.getById(saleSystem.getId());
                saleSystem = this.copy(saleSystem, old);
            }
            saleSystem.setSystemName(saleSystem.getSystemName().trim());
            this.bind(saleSystem);
            this.saleSystemService.save(saleSystem);
        }
        return "saveSuccess";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sale/system.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            saleSystem = this.saleSystemService.getById(id);
            if (saleSystem != null) {
                saleSystemService.delete(saleSystem);
            }
        }
        return "success";
    }


    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (saleSystem != null) {
                if (StringUtils.isNotBlank(saleSystem.getSystemName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(saleSystem.getSystemName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.saleSystemService.validateName( saleSystem.getSystemName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.saleSystemService.validateName( saleSystem.getSystemName());
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

    @PageFlow(result = {@Result(name = "success", path = "/sale/system.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            saleSystem = this.saleSystemService.getById(id);
            if (saleSystem != null) {
                saleSystem.setUseYn("N");
                saleSystemService.save(saleSystem);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sale/system.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            saleSystem = this.saleSystemService.getById(id);
            if (saleSystem != null) {
                saleSystem.setUseYn("Y");
                saleSystemService.save(saleSystem);
            }
        }
        return "success";
    }

}
