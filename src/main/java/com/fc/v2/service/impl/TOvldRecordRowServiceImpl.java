package com.fc.v2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.mapper.auto.TOvldRecordRowMapper;
import com.fc.v2.model.auto.TOvldRecordRow;
import com.fc.v2.service.ITOvldRecordRowService;

/**
 * 执法记录明细 Service业务层处理（batch-process 形状：整批提交）
 *
 * @author fuce
 * @date 2026-09-14
 */
@Service
public class TOvldRecordRowServiceImpl implements ITOvldRecordRowService {

    private static final int MAX_ROWS = 500;
    private static final int STATUS_OK = 1;
    private static final int STATUS_FAIL = 2;

    @javax.annotation.Resource
    private TOvldRecordRowMapper ovldRecordRowMapper;

    @Override
    public TOvldRecordRow selectTOvldRecordRowById(Long id) {
        return this.ovldRecordRowMapper.selectById(id);
    }

    @Override
    public int submitBatch(String batchNo, List<TOvldRecordRow> rows) {
        String no = rows.get(0).getBatchNo();
        java.util.List<TOvldRecordRow> errors = new java.util.ArrayList<TOvldRecordRow>();
        int seq = 0;
        for (TOvldRecordRow r : rows) {
            if (r.getItemCode() == null || r.getItemCode().trim().isEmpty()
                    || r.getQty() == null
                    || r.getQty().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                seq++;
                r.setRowNo(Integer.valueOf(seq));
                r.setBatchNo(no);
                r.setStatus(STATUS_FAIL);
                this.ovldRecordRowMapper.insert(r);
                errors.add(r);
            }
        }
        if (!errors.isEmpty()) {
            return 0;
        }
        int ok = 0;
        for (TOvldRecordRow r : rows) {
            r.setBatchNo(no);
            r.setStatus(STATUS_OK);
            this.ovldRecordRowMapper.insert(r);
            ok++;
        }
        return ok;
    }

    @Override
    public List<TOvldRecordRow> listErrors(String batchNo) {
        return this.ovldRecordRowMapper.selectList(new QueryWrapper<TOvldRecordRow>()
                .eq("batch_no", batchNo).eq("status", STATUS_FAIL));
    }
}
