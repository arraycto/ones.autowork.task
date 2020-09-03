package ones.autowork.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liYueYang on 2020/6/11.
 * 项目月报信息表
 */
@Data
public class ZH_WorkInfo {
    private String work_id; // 主键ID
    private String project_id; // 项目ID
    private BigDecimal report_date; // 提交时间
    private BigDecimal report_month; // 提交月份
    private String problem; // 重点问题
    private String financial_status; // 资金情况
    private String work_status; // 生产情况
    private String work_plan; // 生产计划
    private String safety_status; // 安全生产
    private String safety_plan; // 安全计划
    private String remark; // 备注信息
    private String proposal; // 建议及要求
    private BigDecimal current_output_value; // 本月产值
    private BigDecimal year_output_value; // 本年产值
    private BigDecimal comulative_output_value; // 累计产值
    private BigDecimal next_plan_receipt; // 下月计划回款
    private BigDecimal current_progress; // 本月完成进度
    private BigDecimal next_progress; // 下月计划进度
    private String progress_status; // 进度情况
    private String reason_delay; // 延期原因
    private String measures; // 采取措施

    public ZH_WorkInfo(String work_id, String project_id, BigDecimal report_date, BigDecimal report_month, String problem, String financial_status, String work_status, String work_plan, String safety_status, String safety_plan, String remark, String proposal, BigDecimal current_output_value, BigDecimal year_output_value, BigDecimal comulative_output_value, BigDecimal next_plan_receipt, BigDecimal current_progress, BigDecimal next_progress, String progress_status, String reason_delay, String measures) {
        this.work_id = work_id;
        this.project_id = project_id;
        this.report_date = report_date;
        this.report_month = report_month;
        this.problem = problem;
        this.financial_status = financial_status;
        this.work_status = work_status;
        this.work_plan = work_plan;
        this.safety_status = safety_status;
        this.safety_plan = safety_plan;
        this.remark = remark;
        this.proposal = proposal;
        this.current_output_value = current_output_value;
        this.year_output_value = year_output_value;
        this.comulative_output_value = comulative_output_value;
        this.next_plan_receipt = next_plan_receipt;
        this.current_progress = current_progress;
        this.next_progress = next_progress;
        this.progress_status = progress_status;
        this.reason_delay = reason_delay;
        this.measures = measures;
    }
}