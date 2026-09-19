package com.fc.v2.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fc.v2.common.support.ConvertUtil;
import com.fc.v2.mapper.auto.TOvldEnforceBillMapper;
import com.fc.v2.mapper.auto.TOvldSiteMapper;
import com.fc.v2.model.auto.TOvldEnforceBill;
import com.fc.v2.model.auto.TOvldSite;
import com.fc.v2.service.ITOvldEnforceBillService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 治超处理单Service业务层处理
 *
 * @author fuce
 * @date 2026-09-12
 */
@Service
public class TOvldEnforceBillServiceImpl extends ServiceImpl<TOvldEnforceBillMapper, TOvldEnforceBill> implements ITOvldEnforceBillService {

    /** 超限吨位越界上限：与 TOvldGradeRuleServiceImpl 判档口径一致，0..100 吨之外一律不收 */
    private static final BigDecimal QTY_MAX = new BigDecimal("100");

    /** 分档折算单价（元/吨）：一档500、二档800、三档1000、超三档1500 */
    private static final BigDecimal RATE_T1 = new BigDecimal("500");
    private static final BigDecimal RATE_T2 = new BigDecimal("800");
    private static final BigDecimal RATE_T3 = new BigDecimal("1000");
    private static final BigDecimal RATE_T4 = new BigDecimal("1500");

    /** 处理状态：待处理 */
    private static final int STATUS_PENDING = 0;

    /** 站点档案状态：停用 */
    private static final int SITE_STATUS_OFF = 1;

    @Autowired
    private TOvldSiteMapper ovldSiteMapper;

    @Override
    public TOvldEnforceBill selectTOvldEnforceBillById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TOvldEnforceBill>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TOvldEnforceBill> selectTOvldEnforceBillList(Wrapper<TOvldEnforceBill> queryWrapper) {
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public int insertTOvldEnforceBill(TOvldEnforceBill record) {
        TOvldEnforceBill saved = insertTOvldEnforceBill(record, true);
        return saved == null ? 0 : 1;
    }

    @Override
    public TOvldEnforceBill insertTOvldEnforceBill(TOvldEnforceBill record, boolean systemFine) {
        if (record == null) {
            return null;
        }
        //处理单号手工录入：必填且全库唯一，填重了不收
        if (StringUtils.isEmpty(record.getBillNo())) {
            return null;
        }
        Integer dupCnt = this.baseMapper.selectCount(new QueryWrapper<TOvldEnforceBill>()
                .eq("bill_no", record.getBillNo()).eq("del_flag", 0));
        if (dupCnt != null && dupCnt > 0) {
            return null;
        }
        //所属站点：必须存在且在在用；停用的站不再开新单，历史单据上的站点快照不受影响
        if (record.getSiteId() == null) {
            return null;
        }
        TOvldSite site = ovldSiteMapper.selectOne(new QueryWrapper<TOvldSite>()
                .eq("id", record.getSiteId()).eq("del_flag", 0));
        if (site == null || (site.getStatus() != null && site.getStatus() == SITE_STATUS_OFF)) {
            return null;
        }
        record.setSiteNo(site.getSiteNo());
        //超限吨位：负数或明显越界（>100 吨）直接不收
        BigDecimal qty = record.getQty();
        if (qty == null || qty.compareTo(BigDecimal.ZERO) < 0 || qty.compareTo(QTY_MAX) > 0) {
            return null;
        }
        //罚款金额由系统按吨位折算，人工传值一律忽略
        if (systemFine) {
            record.setFineAmt(fineOf(site, qty));
        }
        record.setStatus(STATUS_PENDING);
        record.setDelFlag(0);
        return this.baseMapper.insert(record) > 0 ? record : null;
    }

    @Override
    public int updateTOvldEnforceBill(TOvldEnforceBill record) {
        if (record == null || record.getId() == null) {
            return 0;
        }

        if (StringUtils.isNotEmpty(record.getBillNo())) {
            Integer dupCnt = this.baseMapper.selectCount(new QueryWrapper<TOvldEnforceBill>()
                    .eq("bill_no", record.getBillNo()).ne("id", record.getId()).eq("del_flag", 0));
            if (dupCnt != null && dupCnt > 0) {
                return 0;
            }
        }

        //换站点：站点须在用，并同步刷新站点编号快照
        if (record.getSiteId() != null) {
            TOvldSite site = ovldSiteMapper.selectOne(new QueryWrapper<TOvldSite>()
                    .eq("id", record.getSiteId()).eq("del_flag", 0));
            if (site == null || (site.getStatus() != null && site.getStatus() == SITE_STATUS_OFF)) {
                return 0;
            }
            record.setSiteNo(site.getSiteNo());
        }

        //罚款人工不许填：修改入口一律不落人工罚款；吨位变更时按站点档位重新折算
        BigDecimal qty = record.getQty();
        record.setFineAmt(null);
        if (qty != null) {
            if (qty.compareTo(BigDecimal.ZERO) < 0 || qty.compareTo(QTY_MAX) > 0) {
                return 0;
            }
            Long siteId = record.getSiteId() != null ? record.getSiteId().longValue() : null;
            if (siteId == null) {
                TOvldEnforceBill db = this.baseMapper.selectById(record.getId());
                siteId = db != null && db.getSiteId() != null ? db.getSiteId().longValue() : null;
            }
            TOvldSite site = siteId == null ? null : ovldSiteMapper.selectById(siteId);
            if (site != null) {
                record.setFineAmt(fineOf(site, qty));
            }
        }

        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TOvldEnforceBill>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTOvldEnforceBillByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTOvldEnforceBillById(Long id) {
        return this.baseMapper.deleteById(id);
    }

    /**
     * 罚款折算：按站点档案维护的分档上限定档，档位单价 500/800/1000/1500 元/吨；
     * 档案未维护任何档位上限时按一档单价折算。结果保留两位小数。
     */
    private BigDecimal fineOf(TOvldSite site, BigDecimal qty) {
        BigDecimal rate = RATE_T1;
        if (site.getTh1Max() != null && qty.compareTo(site.getTh1Max()) <= 0) {
            rate = RATE_T1;
        } else if (site.getTh2Max() != null && qty.compareTo(site.getTh2Max()) <= 0) {
            rate = RATE_T2;
        } else if (site.getTh3Max() != null && qty.compareTo(site.getTh3Max()) <= 0) {
            rate = RATE_T3;
        } else if (site.getTh1Max() != null || site.getTh2Max() != null || site.getTh3Max() != null) {
            rate = RATE_T4;
        }
        return qty.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
