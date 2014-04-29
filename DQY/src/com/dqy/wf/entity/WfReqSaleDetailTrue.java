package com.dqy.wf.entity;

import com.dqy.sale.entity.SaleProduct;
import com.dqy.sale.entity.SaleSeries;
import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.entity.Tracker;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-8-15
 * Time: 下午4:26
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "WF_REQ_SALE_DETAIL_TRUE")
public class WfReqSaleDetailTrue extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private WfReqSale saleId;

    private Double amount;

    private SaleSeries seriesId;

    private SaleProduct productId;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;


    @Id
    @GeneratedValue(generator="WF_REQ_SALE_DETAIL_TRUE")
    @GenericGenerator(name="WF_REQ_SALE_DETAIL_TRUE",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_SALE_DETAIL_TRUE")})
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "CREATED",updatable = false)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDATED")
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Column(name = "UPDATED_BY")
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "USE_YN")
    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }




    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERIES_ID")
    public SaleSeries getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(SaleSeries seriesId) {
        this.seriesId = seriesId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    public SaleProduct getProductId() {
        return productId;
    }

    public void setProductId(SaleProduct productId) {
        this.productId = productId;
    }

    @Column(name = "AMOUNT")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SALE_ID")
    public WfReqSale getSaleId() {
        return saleId;
    }

    public void setSaleId(WfReqSale saleId) {
        this.saleId = saleId;
    }
}
