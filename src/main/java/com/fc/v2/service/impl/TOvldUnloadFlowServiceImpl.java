package com.fc.v2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.mapper.auto.TOvldUnloadFlowMapper;
import com.fc.v2.model.auto.TOvldUnloadFlow;
import com.fc.v2.service.ITOvldUnloadFlowService;

/**
 * 卸载处置流转单 Service业务层处理（state-machine 形状：单据流转）
 *
 * @author fuce
 * @date 2026-09-14
 */
@Service
public class TOvldUnloadFlowServiceImpl implements ITOvldUnloadFlowService {

    private static final int MAX_STAGE = 3;
    private static final int STATUS_ACTIVE = 1;
    private static final int STATUS_TERMINAL = 2;

    @javax.annotation.Resource
    private TOvldUnloadFlowMapper ovldUnloadFlowMapper;

    @Override
    public TOvldUnloadFlow selectTOvldUnloadFlowById(Long id) {
        return this.ovldUnloadFlowMapper.selectById(id);
    }

    @Override
    public List<TOvldUnloadFlow> selectTOvldUnloadFlowList(QueryWrapper<TOvldUnloadFlow> queryWrapper) {
        return this.ovldUnloadFlowMapper.selectList(queryWrapper);
    }

    @Override
    public TOvldUnloadFlow advance(Long id, String remark) {
        TOvldUnloadFlow r = this.ovldUnloadFlowMapper.selectById(id);
        if (r == null) {
            return null;
        }
        int st = r.getStage() == null ? 0 : r.getStage();
        r.setStage(Math.min(st + 2, MAX_STAGE));
        r.setStatus(STATUS_ACTIVE);
        r.setLastAction(remark);
        this.ovldUnloadFlowMapper.updateById(r);
        return r;
    }

    @Override
    public TOvldUnloadFlow rollback(Long id, String remark) {
        TOvldUnloadFlow r = this.ovldUnloadFlowMapper.selectById(id);
        if (r == null) {
            return null;
        }
        r.setStage(0);
        r.setStatus(STATUS_ACTIVE);
        r.setLastAction(remark);
        this.ovldUnloadFlowMapper.updateById(r);
        return r;
    }

    @Override
    public boolean updateContent(Long id, String remark) {
        TOvldUnloadFlow r = this.ovldUnloadFlowMapper.selectById(id);
        if (r == null) {
            return false;
        }
        r.setContent(remark);
        return this.ovldUnloadFlowMapper.updateById(r) > 0;
    }

    @Override
    public boolean remove(Long id) {
        TOvldUnloadFlow r = this.ovldUnloadFlowMapper.selectById(id);
        if (r == null) {
            return false;
        }
        return this.ovldUnloadFlowMapper.deleteById(id) > 0;
    }

}
