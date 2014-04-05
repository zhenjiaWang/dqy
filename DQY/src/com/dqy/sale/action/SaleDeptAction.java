package com.dqy.sale.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sale.entity.SaleChannel;
import com.dqy.sale.entity.SaleDept;
import com.dqy.sale.service.SaleChannelService;
import com.dqy.sale.service.SaleDeptService;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysOrgGroupService;
import com.dqy.sys.service.SysOrgService;
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
@Action(name = "dept", namespace = "/sale")
public class SaleDeptAction extends ActionSupport<SaleDept> {

    @Inject
    private SaleDeptService saleDeptService;


    @Inject
    private SaleChannelService saleChannelService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SaleDept saleDept;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long channelId;


    @ReqSet
    private List<SaleChannel> channelList;


    @ReqGet
    @ReqSet
    private String keyword;

    @ReqSet
    private List<SaleDept> deptList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sale/dept/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sale");
            userInfo.setLeftMenu("saleDept");
            pageObj = this.saleDeptService.getPageList(getStart(), rows, searchModeCallback());
            if(pageObj!=null){
                deptList=pageObj.getResultList();
            }

        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            selectorList.add(SelectorUtils.$order("channelId.id"));
            selectorList.add(SelectorUtils.$order("deptName"));
            if(StringUtils.isNotBlank(keyword)){
                selectorList.add(SelectorUtils.$like("deptName",keyword));
            }
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sale/dept/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            saleDept = this.saleDeptService.getById(id);
            channelId=saleDept.getChannelId().getId();
        }
        List<Selector> selectorList=new ArrayList<Selector>();
        selectorList.add(SelectorUtils.$eq("useYn","Y"));
        channelList=this.saleChannelService.getList(selectorList);
        if(channelId==null){
            if(channelList!=null&&!channelList.isEmpty()){
                channelId=channelList.get(0).getId();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && saleDept != null) {
            if (saleDept.getId() != null) {
                SaleDept old = this.saleDeptService.getById(saleDept.getId());
                saleDept = this.copy(saleDept, old);
            }
            if(saleDept.getChannelId()!=null){
                if(saleDept.getChannelId().getId()==null){
                    saleDept.setChannelId(null);
                }
            }
            this.bind(saleDept);
            this.saleDeptService.save(saleDept);
        }
        return "saveSuccess";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sale/dept.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            saleDept = this.saleDeptService.getById(id);
            if (saleDept != null) {
                saleDeptService.delete(saleDept);
            }
        }
        return "success";
    }


    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (saleDept != null) {
                if (StringUtils.isNotBlank(saleDept.getDeptName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(saleDept.getDeptName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.saleDeptService.validateName(saleDept.getDeptName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.saleDeptService.validateName(saleDept.getDeptName());
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



    @PageFlow(result = {@Result(name = "success", path = "/sale/dept.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            saleDept = this.saleDeptService.getById(id);
            if (saleDept != null) {
                saleDept.setUseYn("N");
                saleDeptService.save(saleDept);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sale/dept.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            saleDept = this.saleDeptService.getById(id);
            if (saleDept != null) {
                saleDept.setUseYn("Y");
                saleDeptService.save(saleDept);
            }
        }
        return "success";
    }

}
