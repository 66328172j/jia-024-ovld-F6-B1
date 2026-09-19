package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TOvldSite;

import java.util.List;

/**
 * 治超站点档案 Service接口
 *
 * @author fuce
 * @date 2026-09-19
 */
public interface ITOvldSiteService {

    /** 按主键查询 */
    TOvldSite selectTOvldSiteById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TOvldSite> selectTOvldSiteList(Wrapper<TOvldSite> queryWrapper);

    /**
     * 新增档案：站点编号人工填写，全库唯一（del_flag=0 范围内），填重了不收返回 0；
     * 档案状态默认 0在用
     */
    int insertTOvldSite(TOvldSite record);

    /** 修改（站点编号唯一性校验排除自身） */
    int updateTOvldSite(TOvldSite record);

    /** 批量删除 */
    int deleteTOvldSiteByIds(String ids);

    /** 按主键删除 */
    int deleteTOvldSiteById(Long id);
}
