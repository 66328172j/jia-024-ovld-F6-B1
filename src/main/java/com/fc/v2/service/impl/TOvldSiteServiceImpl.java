package com.fc.v2.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fc.v2.common.support.ConvertUtil;
import com.fc.v2.mapper.auto.TOvldSiteMapper;
import com.fc.v2.model.auto.TOvldSite;
import com.fc.v2.service.ITOvldSiteService;
import com.fc.v2.util.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 治超站点档案Service业务层处理
 *
 * @author fuce
 * @date 2026-09-19
 */
@Service
public class TOvldSiteServiceImpl extends ServiceImpl<TOvldSiteMapper, TOvldSite> implements ITOvldSiteService {

    /** 档案状态：在用 */
    private static final int STATUS_ON = 0;

    @Override
    public TOvldSite selectTOvldSiteById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TOvldSite>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TOvldSite> selectTOvldSiteList(Wrapper<TOvldSite> queryWrapper) {
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public int insertTOvldSite(TOvldSite record) {
        if (record == null) {
            return 0;
        }
        //站点编号人工填写，必填且全库唯一：填重了不收
        if (StringUtils.isEmpty(record.getSiteNo()) || StringUtils.isEmpty(record.getSiteName())) {
            return 0;
        }
        Integer dupCnt = this.baseMapper.selectCount(new QueryWrapper<TOvldSite>()
                .eq("site_no", record.getSiteNo()).eq("del_flag", 0));
        if (dupCnt != null && dupCnt > 0) {
            return 0;
        }
        if (record.getStatus() == null) {
            record.setStatus(STATUS_ON);
        }
        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTOvldSite(TOvldSite record) {
        if (record == null || record.getId() == null) {
            return 0;
        }
        if (StringUtils.isNotEmpty(record.getSiteNo())) {
            Integer dupCnt = this.baseMapper.selectCount(new QueryWrapper<TOvldSite>()
                    .eq("site_no", record.getSiteNo()).ne("id", record.getId()).eq("del_flag", 0));
            if (dupCnt != null && dupCnt > 0) {
                return 0;
            }
        }
        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TOvldSite>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTOvldSiteByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTOvldSiteById(Long id) {
        return this.baseMapper.deleteById(id);
    }
}
