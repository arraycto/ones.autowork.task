package ones.autowork.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liYueYang on 2020/6/9.
 * 项目经营台账表
 */
@Data
public class ZH_Management {
    private String management_id; // 主键ID
    private String project_id; // 项目ID
    private BigDecimal report_date; // 提交日期 todo 实际是回款月份含义
    private BigDecimal current_actual_receipt; // 本月实际回款
    private BigDecimal current_output_value; // 本月实际完成产值
    private BigDecimal current_expenditure; // 本月实际支出
    private BigDecimal last_plan_return; // 上月计划回款
    private BigDecimal last_plan_expenditure; // 上月计划支出
    private BigDecimal next_plan_receipt; // 下月计划回款
    private BigDecimal next_plan_expenditure; // 下月计划支出
    private String info_current; // 本月实际支出详情
    private String info_next; // 下月计划支出详情
    private String remark; // 备注信息

    public ZH_Management(String management_id, String project_id, BigDecimal report_date, BigDecimal current_actual_receipt, BigDecimal current_output_value, BigDecimal current_expenditure, BigDecimal last_plan_return, BigDecimal last_plan_expenditure, BigDecimal next_plan_receipt, BigDecimal next_plan_expenditure, String info_current, String info_next, String remark) {
        this.management_id = management_id;
        this.project_id = project_id;
        this.report_date = report_date;
        this.current_actual_receipt = current_actual_receipt;
        this.current_output_value = current_output_value;
        this.current_expenditure = current_expenditure;
        this.last_plan_return = last_plan_return;
        this.last_plan_expenditure = last_plan_expenditure;
        this.next_plan_receipt = next_plan_receipt;
        this.next_plan_expenditure = next_plan_expenditure;
        this.info_current = info_current;
        this.info_next = info_next;
        this.remark = remark;
    }

    public ZH_Management() {
    }
}
