package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TOvldEnforceBill;

import java.util.List;

/**
 * 治超处理单 Service接口
 *
 * @author fuce
 * @date 2026-09-12
 */
public interface ITOvldEnforceBillService {

    /** 按主键查询 */
    TOvldEnforceBill selectTOvldEnforceBillById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TOvldEnforceBill> selectTOvldEnforceBillList(Wrapper<TOvldEnforceBill> queryWrapper);

    /** 新增 */
    int insertTOvldEnforceBill(TOvldEnforceBill record);

    /**
     * 登记（重载）：罚款金额由系统按站点分档阈值折算（systemFine=true 时页面传值一律忽略），
     * 单号手工录入且全库唯一、站点须在用、超限吨位 0..100 吨，校验不收返回 null；
     * 登记成功返回落库后的单据（含系统折算结果），登记结果以返回为准。
     */
    TOvldEnforceBill insertTOvldEnforceBill(TOvldEnforceBill record, boolean systemFine);

    /** 修改 */
    int updateTOvldEnforceBill(TOvldEnforceBill record);

    /** 批量删除 */
    int deleteTOvldEnforceBillByIds(String ids);

    /** 按主键删除 */
    int deleteTOvldEnforceBillById(Long id);
}
