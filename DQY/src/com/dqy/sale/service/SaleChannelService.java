package com.dqy.sale.service;

import com.dqy.sale.entity.SaleChannel;
import com.dqy.sys.entity.SysOrg;
import com.google.inject.Singleton;
import org.guiceside.commons.Page;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.dao.hquery.HQuery;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;

import java.util.List;

/**
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Singleton
public class SaleChannelService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SaleChannel> getPageList(int start,
                                         int limit, List<Selector> selectorList) {
        return $(selectorList).page(SaleChannel.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SaleChannel> getList() {
        return $($eq("useYn","Y")).list(SaleChannel.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SaleChannel> getList(List<Selector> selectorList) {
        return $(selectorList).list(SaleChannel.class);
    }
    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SaleChannel getById(Long id) {
        return $(id).get(SaleChannel.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SaleChannel saleChannel) {
        $(saleChannel).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SaleChannel saleChannel) {
        $(saleChannel).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SaleChannel.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SaleChannel> channelList) {
        $(channelList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SaleChannel> channelList) {
        $(channelList).delete();
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(String channelName) {
        return $($count("id"), $eq("channelName", channelName)).value(SaleChannel.class, Integer.class);
    }


}
