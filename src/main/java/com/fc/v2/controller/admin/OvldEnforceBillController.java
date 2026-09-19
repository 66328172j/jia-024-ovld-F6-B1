package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TOvldEnforceBill;
import com.fc.v2.model.auto.TOvldSite;
import com.fc.v2.service.ITOvldEnforceBillService;
import com.fc.v2.service.ITOvldSiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 治超处理单 Controller
 *
 * @author fuce
 * @date 2026-09-12
 */
@Api(value = "治超处理单")
@Controller
@RequestMapping("/OvldEnforceBillController")
public class OvldEnforceBillController extends BaseController {

    private final String prefix = "admin/ovldEnforceBill";

    @Autowired
    private ITOvldEnforceBillService ovldEnforceBillService;

    @Autowired
    private ITOvldSiteService ovldSiteService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("ovld:ovldEnforceBill:view")
    public String view(ModelMap model) {
        //站点筛选项给全量档案（含停用），历史单据仍可按停用站点筛出
        model.put("siteList", ovldSiteService.selectTOvldSiteList(
                new QueryWrapper<TOvldSite>().eq("del_flag", 0).orderByAsc("id")));
        return prefix + "/list";
    }

    @Log(title = "治超处理单集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("ovld:ovldEnforceBill:list")
    @ResponseBody
    public ResultTable list(TOvldEnforceBill record) {
        QueryWrapper<TOvldEnforceBill> queryWrapper = new QueryWrapper<TOvldEnforceBill>();
        queryWrapper.eq("del_flag", 0);
        queryWrapper.eq(record.getSiteId() != null, "site_id", record.getSiteId());
        queryWrapper.eq(record.getStatus() != null, "status", record.getStatus());
        queryWrapper.orderByDesc("id");
        startPage();
        com.github.pagehelper.PageInfo<TOvldEnforceBill> page =
                new com.github.pagehelper.PageInfo<TOvldEnforceBill>(ovldEnforceBillService.selectTOvldEnforceBillList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @ApiOperation(value = "新增跳转", notes = "新增跳转")
    @GetMapping("/add")
    public String add(ModelMap modelMap) {
        //登记时下拉只给在用站点，停用站点不再开新单
        modelMap.put("siteList", ovldSiteService.selectTOvldSiteList(
                new QueryWrapper<TOvldSite>().eq("del_flag", 0).eq("status", 0).orderByAsc("id")));
        return prefix + "/add";
    }

    @Log(title = "治超处理单新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("ovld:ovldEnforceBill:add")
    @ResponseBody
    public AjaxResult add(TOvldEnforceBill record) {
        return toAjax(ovldEnforceBillService.insertTOvldEnforceBill(record));
    }

    @Log(title = "治超处理单登记", action = "register")
    @ApiOperation(value = "登记", notes = "登记")
    @PostMapping("/register")
    @RequiresPermissions("ovld:ovldEnforceBill:register")
    @ResponseBody
    public AjaxResult register(TOvldEnforceBill record) {
        TOvldEnforceBill saved = ovldEnforceBillService.insertTOvldEnforceBill(record, true);
        if (saved == null) {
            return error("登记失败：单号重复、站点不可用或吨位越界");
        }
        return success(200, "登记成功", saved);
    }

    @ApiOperation(value = "修改跳转", notes = "修改跳转")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        mmap.put("TOvldEnforceBill", ovldEnforceBillService.selectTOvldEnforceBillById(id));
        return prefix + "/edit";
    }

    @Log(title = "治超处理单修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("ovld:ovldEnforceBill:edit")
    @ResponseBody
    public AjaxResult editSave(TOvldEnforceBill record) {
        return toAjax(ovldEnforceBillService.updateTOvldEnforceBill(record));
    }

    @Log(title = "治超处理单删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("ovld:ovldEnforceBill:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(ovldEnforceBillService.deleteTOvldEnforceBillByIds(ids));
    }
}
