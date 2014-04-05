package com.dqy.sale.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sale.entity.SaleSeries;
import com.dqy.sale.entity.SaleSystem;
import com.dqy.sale.service.SaleSeriesService;
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
@Action(name = "series", namespace = "/sale")
public class SaleSeriesAction extends ActionSupport<SaleSeries> {

    @Inject
    private SaleSeriesService saleSeriesService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SaleSeries saleSeries;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private String keyword;

    @ReqSet
    private List<SaleSeries> seriesList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sale/series/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sale");
            userInfo.setLeftMenu("saleSeries");
            pageObj = this.saleSeriesService.getPageList(getStart(), rows, searchModeCallback());
            if(pageObj!=null){
                seriesList=pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if(StringUtils.isNotBlank(keyword)){
                selectorList.add(SelectorUtils.$like("seriesName",keyword));
            }
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sale/series/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            saleSeries = this.saleSeriesService.getById(id);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && saleSeries != null) {
            if (saleSeries.getId() != null) {
                SaleSeries old = this.saleSeriesService.getById(saleSeries.getId());
                saleSeries = this.copy(saleSeries, old);
            }
            saleSeries.setSeriesName(saleSeries.getSeriesName().trim());
            this.bind(saleSeries);
            this.saleSeriesService.save(saleSeries);
        }
        return "saveSuccess";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sale/series.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            saleSeries = this.saleSeriesService.getById(id);
            if (saleSeries != null) {
                saleSeriesService.delete(saleSeries);
            }
        }
        return "success";
    }


    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (saleSeries != null) {
                if (StringUtils.isNotBlank(saleSeries.getSeriesName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(saleSeries.getSeriesName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.saleSeriesService.validateName( saleSeries.getSeriesName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.saleSeriesService.validateName( saleSeries.getSeriesName());
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

    @PageFlow(result = {@Result(name = "success", path = "/sale/series.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            saleSeries = this.saleSeriesService.getById(id);
            if (saleSeries != null) {
                saleSeries.setUseYn("N");
                saleSeriesService.save(saleSeries);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sale/series.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            saleSeries = this.saleSeriesService.getById(id);
            if (saleSeries != null) {
                saleSeries.setUseYn("Y");
                saleSeriesService.save(saleSeries);
            }
        }
        return "success";
    }

}
