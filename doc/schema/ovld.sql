-- ovld 公路超限运输检测与卸载处置管理 -- schema (jia-024)
-- 列名与基线实体契约（@TableName/@TableField）逐列对齐，改列必须同步实体。
-- 库：jia_024

CREATE TABLE IF NOT EXISTS t_ovld_case (
  id bigint NOT NULL COMMENT '主键',
  bill_no varchar(64) DEFAULT NULL COMMENT '案件编号',
  node_no int DEFAULT NULL COMMENT '当前环节 0..2',
  sign_mode int DEFAULT NULL COMMENT '签批模式 0或签 1会签',
  need_count int DEFAULT NULL COMMENT '本环节应签人数',
  sign_count int DEFAULT NULL COMMENT '本环节已签票数',
  status int DEFAULT NULL COMMENT '单据状态 0审批中 1已通过 2已否决',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='超限运输案件';

CREATE TABLE IF NOT EXISTS t_ovld_check_task (
  id bigint NOT NULL COMMENT '主键',
  item_no varchar(64) DEFAULT NULL COMMENT '条目编号',
  due_at datetime DEFAULT NULL COMMENT '到期时刻',
  amount decimal(12,2) DEFAULT NULL COMMENT '计量值',
  status int DEFAULT NULL COMMENT '状态 0待处理 1已处理 2失败',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='超限检测任务条目';

CREATE TABLE IF NOT EXISTS t_ovld_enforce_bill (
  id bigint NOT NULL COMMENT '主键',
  bill_no varchar(64) DEFAULT NULL COMMENT '处理单号',
  site_id int DEFAULT NULL COMMENT '所属站点',
  site_no varchar(64) DEFAULT NULL COMMENT '站点编号',
  qty decimal(12,2) DEFAULT NULL COMMENT '超限吨位',
  fine_amt decimal(12,2) DEFAULT NULL COMMENT '罚款金额',
  status int DEFAULT NULL COMMENT '状态 0待处理 1已处理 2已办结',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='治超处理单';

CREATE TABLE IF NOT EXISTS t_ovld_grade_rule (
  id bigint NOT NULL COMMENT '主键',
  rule_code varchar(64) DEFAULT NULL COMMENT '规则编号',
  rule_name varchar(128) DEFAULT NULL COMMENT '规则名称',
  th1_max decimal(12,2) DEFAULT NULL COMMENT '第一档上限',
  th2_max decimal(12,2) DEFAULT NULL COMMENT '第二档上限',
  th3_max decimal(12,2) DEFAULT NULL COMMENT '第三档上限',
  eff_start datetime DEFAULT NULL COMMENT '生效起始时刻',
  eff_end datetime DEFAULT NULL COMMENT '生效截止时刻(不含)',
  priority int DEFAULT NULL COMMENT '优先级(数值越大越优先)',
  status int DEFAULT NULL COMMENT '规则状态 0启用 1停用',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='超限吨位等级判定规则';

CREATE TABLE IF NOT EXISTS t_ovld_record_row (
  id bigint NOT NULL COMMENT '主键',
  batch_no varchar(64) DEFAULT NULL COMMENT '批次号',
  row_no int DEFAULT NULL COMMENT '原始行号',
  item_code varchar(64) DEFAULT NULL COMMENT '明细编码',
  qty decimal(12,2) DEFAULT NULL COMMENT '超限吨位',
  status int DEFAULT NULL COMMENT '行状态 0待处理 1成功 2失败',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='执法记录明细';

CREATE TABLE IF NOT EXISTS t_ovld_site (
  id bigint NOT NULL COMMENT '主键',
  site_no varchar(64) DEFAULT NULL COMMENT '站点编号',
  site_name varchar(128) DEFAULT NULL COMMENT '站点名称',
  site_type varchar(32) DEFAULT NULL COMMENT '站点类型',
  road_name varchar(128) DEFAULT NULL COMMENT '所属路段',
  th1_max decimal(12,2) DEFAULT NULL COMMENT '第一档上限',
  th2_max decimal(12,2) DEFAULT NULL COMMENT '第二档上限',
  th3_max decimal(12,2) DEFAULT NULL COMMENT '第三档上限',
  status int DEFAULT NULL COMMENT '档案状态 0在用 1停用',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='治超站点档案';

CREATE TABLE IF NOT EXISTS t_ovld_unload_flow (
  id bigint NOT NULL COMMENT '主键',
  biz_no varchar(64) DEFAULT NULL COMMENT '流转单号',
  stage int DEFAULT NULL COMMENT '当前环节 0..3',
  status int DEFAULT NULL COMMENT '流转状态 0待发起 1在办 2已办结',
  content varchar(255) DEFAULT NULL COMMENT '备注',
  last_action varchar(64) DEFAULT NULL COMMENT '最近一次流转动作',
  del_flag int DEFAULT '0' COMMENT '删除标记 0正常 1删除',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='卸载处置流转单';
