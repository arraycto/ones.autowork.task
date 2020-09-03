正元智慧城市 - 项目经营台账表定时任务

项目名称	项目表 - PROJECT_ID
业主单位	项目表 - OWNER_ID
签订时间	项目表 - SIGN_TIME
合同金额（万元）	项目表 - AMOUNT
累计回款	项目回款信息表 - AMOUNT_RETURNED(所有月份总和) ----不 做 统 计------
本月完成产值	项目月报信息表 - CURRENT_OUTPUT_VALUE--
本月实际回款	项目回款信息表 - AMOUNT_RETURNED(当月的)--
上月计划回款	来自上月的‘下月计划回款’
本月实际支出	（ 分包付款信息表 - AMOUNT_PAYMENT + 项目支出用款表 - AMOUNT_EXPEND（支出ID为本月实际支出的） ）多条--
上月计划支出	来自上月的‘下月计划支出’
下月预计回款	项目月报信息表 - NEXT_PLAN_RECEIPT--
下月预计支出	（ 用款申请计划表 - ESTIMATE_PAYMENT + 项目支出用款表 - AMOUNT_EXPEND（支出ID为下月预计支出的） ）--
备注	（ 分包付款信息表 + 项目支出用款表 + 用款申请计划表  ） --


项目回款信息表 zh_returnedinfo ：交易时间、回款月份？？（一个项目一个月只有一条数据）
项目月报信息表 zh_workinfo ：提交时间、提交月份？（一个项目一个月只有一条数据）
分包付款信息表 zh_subpayment ：提交日期、付款月份？
项目支出用款表 zh_expendmoney ：zh_expendinfo 表中的提交时间、支出月份？？
	（ 项目支出信息表 zh_expendinfo ：expend_id 外键关联）
	SELECT * FROM zh_expendmoney a,zh_expendinfo b where a.expend_id = b.expend_id and b.spend_type = 'current'
用款申请计划表 zh_disbursementplan ：提交日期、申请月份


mongoexport -d ZYDX -c Smart_Kylin_DayReport -o "F:/Smart_Kylin_DayReport.json"

--------  依据开始  --------
项目名称	项目表 - PROJECT_ID
业主单位	项目表 - OWNER_ID
签订时间	项目表 - SIGN_TIME
合同金额（万元）	项目表 - AMOUNT
累计回款	项目回款信息表 - AMOUNT_RETURNED(所有月份总和) ----不 做 统 计------
本月完成产值	项目月报信息表 - CURRENT_OUTPUT_VALUE--
本月实际回款	项目回款信息表 - AMOUNT_RETURNED(当月的)--
上月计划回款	来自上月的‘下月计划回款’
本月实际支出	（ 分包付款信息表 - AMOUNT_PAYMENT + 项目支出用款表 - AMOUNT_EXPEND（支出ID为本月实际支出的） ）多条--
上月计划支出	来自上月的‘下月计划支出’
下月预计回款	项目月报信息表 - NEXT_PLAN_RECEIPT--
下月预计支出	（ 用款申请计划表 - ESTIMATE_PAYMENT + 项目支出用款表 - AMOUNT_EXPEND（支出ID为下月预计支出的） ）--
备注	（ 分包付款信息表 + 项目支出用款表 + 用款申请计划表  ） -- 没有备注

项目回款信息表 zh_returnedinfo ：交易时间、回款月份？？（一个项目一个月只有一条数据）
项目月报信息表 zh_workinfo ：提交时间、提交月份？（一个项目一个月只有一条数据）
分包付款信息表 zh_subpayment ：提交日期、付款月份？
项目支出用款表 zh_expendmoney ：zh_expendinfo（月度支出） 表中的提交时间、支出月份？？
	（ 项目支出信息表 zh_expendinfo ：expend_id 外键关联）
	SELECT * FROM zh_expendmoney a,zh_expendinfo b where a.expend_id = b.expend_id and b.spend_type = 'current'
用款申请计划表 zh_disbursementplan ：提交日期、申请月份
--------  依据结束  --------

mongoexport -d ZYDX -c Smart_Kylin_DayReport -o "F:/Smart_Kylin_DayReport.json"

更新日志：
    2020-07-14 项目回款信息表 根据提交日期（report_date）查询
    2020-08-13 修改定时任务逻辑(V2)
        新增一个表，定时任务开始时，只扫描该表，有数据就继续执行，没有则直接结束。
        处理完每一条数据后就删除该数据（同一项目id、同一日期，算一条，删除时全部删除）
    2020-08-26 详情信息相关的金额处理（去掉小数点后多余的0）