package com.dqy.sale.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sale.entity.SaleChannel;
import com.dqy.sale.service.SaleChannelService;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysOrgGroupService;
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
@Action(name = "channel", namespace = "/sale")
public class SaleChannelAction extends ActionSupport<SaleChannel> {

    @Inject
    private SaleChannelService saleChannelService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SaleChannel saleChannel;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private String keyword;

    @ReqSet
    private List<SaleChannel> channelList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sale/channel/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sale");
            userInfo.setLeftMenu("saleChannel");
            pageObj = this.saleChannelService.getPageList(getStart(), rows, searchModeCallback());
            if(pageObj!=null){
                channelList=pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if(StringUtils.isNotBlank(keyword)){
                selectorList.add(SelectorUtils.$like("channelName",keyword));
            }
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sale/channel/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            saleChannel = this.saleChannelService.getById(id);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && saleChannel != null) {
            if (saleChannel.getId() != null) {
                SaleChannel old = this.saleChannelService.getById(saleChannel.getId());
                saleChannel = this.copy(saleChannel, old);
            }
            saleChannel.setChannelName(saleChannel.getChannelName().trim());
            this.bind(saleChannel);
            this.saleChannelService.save(saleChannel);
        }
        return "saveSuccess";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sale/channel.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            saleChannel = this.saleChannelService.getById(id);
            if (saleChannel != null) {
                saleChannelService.delete(saleChannel);
            }
        }
        return "success";
    }


    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (saleChannel != null) {
                if (StringUtils.isNotBlank(saleChannel.getChannelName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(saleChannel.getChannelName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.saleChannelService.validateName( saleChannel.getChannelName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.saleChannelService.validateName( saleChannel.getChannelName());
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

    @PageFlow(result = {@Result(name = "success", path = "/sale/channel.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            saleChannel = this.saleChannelService.getById(id);
            if (saleChannel != null) {
                saleChannel.setUseYn("N");
                saleChannelService.save(saleChannel);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sale/channel.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            saleChannel = this.saleChannelService.getById(id);
            if (saleChannel != null) {
                saleChannel.setUseYn("Y");
                saleChannelService.save(saleChannel);
            }
        }
        return "success";
    }

}
