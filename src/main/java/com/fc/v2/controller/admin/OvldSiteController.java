package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TOvldSite;
import com.fc.v2.service.ITOvldSiteService;
import com.fc.v2.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 治超站点档案 Controller
 *
 * @author fuce
 * @date 2026-09-19
 */
@Api(value = "治超站点档案")
@Controller
@RequestMapping("/OvldSiteController")
public class OvldSiteController extends BaseController {

    private final String prefix = "admin/ovldSite";

    @Autowired
    private ITOvldSiteService ovldSiteService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("ovld:ovldSite:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "治超站点档案集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("ovld:ovldSite:list")
    @ResponseBody
    public ResultTable list(TOvldSite record) {
        QueryWrapper<TOvldSite> queryWrapper = new QueryWrapper<TOvldSite>();
        queryWrapper.eq("del_flag", 0);
        queryWrapper.like(StringUtils.isNotEmpty(record.getSiteNo()), "site_no", record.getSiteNo());
        queryWrapper.like(StringUtils.isNotEmpty(record.getSiteName()), "site_name", record.getSiteName());
        queryWrapper.eq(record.getStatus() != null, "status", record.getStatus());
        queryWrapper.orderByDesc("id");
        startPage();
        com.github.pagehelper.PageInfo<TOvldSite> page =
                new com.github.pagehelper.PageInfo<TOvldSite>(ovldSiteService.selectTOvldSiteList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @ApiOperation(value = "新增跳转", notes = "新增跳转")
    @GetMapping("/add")
    public String add(ModelMap modelMap) {
        return prefix + "/add";
    }

    @Log(title = "治超站点档案新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("ovld:ovldSite:add")
    @ResponseBody
    public AjaxResult add(TOvldSite record) {
        return toAjax(ovldSiteService.insertTOvldSite(record));
    }

    @ApiOperation(value = "修改跳转", notes = "修改跳转")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        mmap.put("TOvldSite", ovldSiteService.selectTOvldSiteById(id));
        return prefix + "/edit";
    }

    @Log(title = "治超站点档案修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("ovld:ovldSite:edit")
    @ResponseBody
    public AjaxResult editSave(TOvldSite record) {
        return toAjax(ovldSiteService.updateTOvldSite(record));
    }

    @Log(title = "治超站点档案删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("ovld:ovldSite:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(ovldSiteService.deleteTOvldSiteByIds(ids));
    }
}
