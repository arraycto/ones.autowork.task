package ones.autowork.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liYueYang on 2020/6/11.
 * 用款申请计划表
 */
@Data
public class ZH_DisBursementPlan {
    private String plan_id; // 主键ID
    private String project_id; // 项目ID
    private String contract_id; // 合同ID
    private BigDecimal report_date; // 提交日期
    private BigDecimal apply_month; // 申请月份
    private BigDecimal estimate_payment; // 预计支付
    private String remark; // 备注信息
    private String proportion; // 预付后比例
    private BigDecimal paid_amount; // 已付金额
    private String contract_name; // 分包合同名称 TODO 备用字段,无实际业务意义

    public ZH_DisBursementPlan(String plan_id, String project_id, String contract_id, BigDecimal report_date, BigDecimal apply_month, BigDecimal estimate_payment, String remark, String proportion, BigDecimal paid_amount, String contract_name) {
        this.plan_id = plan_id;
        this.project_id = project_id;
        this.contract_id = contract_id;
        this.report_date = report_date;
        this.apply_month = apply_month;
        this.estimate_payment = estimate_payment;
        this.remark = remark;
        this.proportion = proportion;
        this.paid_amount = paid_amount;
        this.contract_name = contract_name;
    }
}