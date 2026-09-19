-- ovld 站点档案 / 治超处理单 菜单与权限点（照 auto_code/sql/menu.sql.vm 口径）
-- 用法：先把 @pid 调成要挂载的上级菜单 id（t_sys_permission.id），再整段执行。
-- 类型列：1=菜单 2=按钮；visible：0=显示。

set @pid = 0;

-- 治超站点档案
INSERT INTO `t_sys_permission` VALUES
 (1926000000000000101, '治超站点档案管理', '治超站点档案展示', '/OvldSiteController/view', '0', @pid, 'ovld:ovldSite:view', 1, 'layui-icon layui-icon-location', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL)
,(1926000000000000102, '治超站点档案集合', '治超站点档案集合', '/OvldSiteController/list', '0', 1926000000000000101, 'ovld:ovldSite:list', 2, '', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL)
,(1926000000000000103, '治超站点档案添加', '治超站点档案添加', '/OvldSiteController/add', '0', 1926000000000000101, 'ovld:ovldSite:add', 2, 'layui-icon layui-icon-add-1', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL)
,(1926000000000000104, '治超站点档案删除', '治超站点档案删除', '/OvldSiteController/remove', '0', 1926000000000000101, 'ovld:ovldSite:remove', 2, 'layui-icon layui-icon-delete', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL)
,(1926000000000000105, '治超站点档案修改', '治超站点档案修改', '/OvldSiteController/edit', '0', 1926000000000000101, 'ovld:ovldSite:edit', 2, 'layui-icon layui-icon-edit', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL);

-- 治超处理单
INSERT INTO `t_sys_permission` VALUES
 (1926000000000000201, '治超处理单管理', '治超处理单展示', '/OvldEnforceBillController/view', '0', @pid, 'ovld:ovldEnforceBill:view', 1, 'layui-icon layui-icon-file-b', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL)
,(1926000000000000202, '治超处理单集合', '治超处理单集合', '/OvldEnforceBillController/list', '0', 1926000000000000201, 'ovld:ovldEnforceBill:list', 2, '', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL)
,(1926000000000000203, '治超处理单添加', '治超处理单添加', '/OvldEnforceBillController/add', '0', 1926000000000000201, 'ovld:ovldEnforceBill:add', 2, 'layui-icon layui-icon-add-1', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL)
,(1926000000000000204, '治超处理单登记', '治超处理单登记', '/OvldEnforceBillController/register', '0', 1926000000000000201, 'ovld:ovldEnforceBill:register', 2, 'layui-icon layui-icon-edit', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL)
,(1926000000000000205, '治超处理单删除', '治超处理单删除', '/OvldEnforceBillController/remove', '0', 1926000000000000201, 'ovld:ovldEnforceBill:remove', 2, 'layui-icon layui-icon-delete', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL)
,(1926000000000000206, '治超处理单修改', '治超处理单修改', '/OvldEnforceBillController/edit', '0', 1926000000000000201, 'ovld:ovldEnforceBill:edit', 2, 'layui-icon layui-icon-edit', NULL, 0, 'admin', sysdate(), NULL, NULL, NULL);
