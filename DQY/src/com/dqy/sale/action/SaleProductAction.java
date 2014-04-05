package com.dqy.sale.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sale.entity.SaleCustomer;
import com.dqy.sale.entity.SaleProduct;
import com.dqy.sale.entity.SaleSeries;
import com.dqy.sale.entity.SaleSystem;
import com.dqy.sale.service.SaleCustomerService;
import com.dqy.sale.service.SaleProductService;
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
@Action(name = "product", namespace = "/sale")
public class SaleProductAction extends ActionSupport<SaleProduct> {

    @Inject
    private SaleProductService saleProductService;


    @Inject
    private SaleSeriesService saleSeriesService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SaleProduct saleProduct;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long seriesId;


    @ReqSet
    private List<SaleSeries> seriesList;


    @ReqGet
    @ReqSet
    private String keyword;

    @ReqSet
    private List<SaleProduct> productList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sale/product/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sale");
            userInfo.setLeftMenu("saleCustomer");
            pageObj = this.saleProductService.getPageList(getStart(), rows, searchModeCallback());
            if (pageObj != null) {
                productList = pageObj.getResultList();
            }

        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            selectorList.add(SelectorUtils.$order("seriesId.id"));
            selectorList.add(SelectorUtils.$order("productCode"));
            if (StringUtils.isNotBlank(keyword)) {
                selectorList.add(SelectorUtils.$or(SelectorUtils.$like("productCode", keyword), SelectorUtils.$like("productName", keyword)));
            }
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sale/product/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            saleProduct = this.saleProductService.getById(id);
            seriesId = saleProduct.getSeriesId().getId();
        }
        List<Selector> selectorList = new ArrayList<Selector>();
        selectorList.add(SelectorUtils.$eq("useYn", "Y"));
        seriesList = this.saleSeriesService.getList(selectorList);
        if (seriesId == null) {
            if (seriesList != null && !seriesList.isEmpty()) {
                seriesId = seriesList.get(0).getId();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && saleProduct != null) {
            if (saleProduct.getId() != null) {
                SaleProduct old = this.saleProductService.getById(saleProduct.getId());
                saleProduct = this.copy(saleProduct, old);
            }
            if (saleProduct.getSeriesId() != null) {
                if (saleProduct.getSeriesId().getId() == null) {
                    saleProduct.setSeriesId(null);
                }
            }
            this.bind(saleProduct);
            this.saleProductService.save(saleProduct);
        }
        return "saveSuccess";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sale/product.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            saleProduct = this.saleProductService.getById(id);
            if (saleProduct != null) {
                saleProductService.delete(saleProduct);
            }
        }
        return "success";
    }

    public String validateCode() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (saleProduct != null) {
                if (StringUtils.isNotBlank(saleProduct.getProductCode())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(saleProduct.getProductCode())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.saleProductService.validateCode(saleProduct.getProductCode());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.saleProductService.validateCode(saleProduct.getProductCode());
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

    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (saleProduct != null) {
                if (StringUtils.isNotBlank(saleProduct.getProductName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(saleProduct.getProductName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.saleProductService.validateName(saleProduct.getProductName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.saleProductService.validateName(saleProduct.getProductName());
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


    @PageFlow(result = {@Result(name = "success", path = "/sale/product.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            saleProduct = this.saleProductService.getById(id);
            if (saleProduct != null) {
                saleProduct.setUseYn("N");
                saleProductService.save(saleProduct);
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sale/product.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            saleProduct = this.saleProductService.getById(id);
            if (saleProduct != null) {
                saleProduct.setUseYn("Y");
                saleProductService.save(saleProduct);
            }
        }
        return "success";
    }

}
