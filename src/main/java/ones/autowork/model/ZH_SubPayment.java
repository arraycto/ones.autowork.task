package ones.autowork.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liYueYang on 2020/6/11.
 * 分包付款信息表
 */
@Data
public class ZH_SubPayment {
    private String payment_id; // 主键ID
    private String project_id; // 项目ID
    private String contract_id; // 分包合同ID
    private BigDecimal report_date; // 提交日期
    private BigDecimal pay_month; // 付款月份
    private BigDecimal pay_times; // 付款次数
    private BigDecimal amount_invoice; // 发票金额
    private BigDecimal amount_receivable; // 挂账金额
    private BigDecimal pay_date; // 付款时间
    private BigDecimal amount_payment; // 付款金额
    private String remark; // 备注信息
    private String handledby; // 经办人
    private String reason; // 付款原因
    private String contract_name; // 分包合同名称 TODO 备用字段,无实际业务意义

    public ZH_SubPayment(String payment_id, String project_id, String contract_id, BigDecimal report_date, BigDecimal pay_month, BigDecimal pay_times, BigDecimal amount_invoice, BigDecimal amount_receivable, BigDecimal pay_date, BigDecimal amount_payment, String remark, String handledby, String reason, String contract_name) {
        this.payment_id = payment_id;
        this.project_id = project_id;
        this.contract_id = contract_id;
        this.report_date = report_date;
        this.pay_month = pay_month;
        this.pay_times = pay_times;
        this.amount_invoice = amount_invoice;
        this.amount_receivable = amount_receivable;
        this.pay_date = pay_date;
        this.amount_payment = amount_payment;
        this.remark = remark;
        this.handledby = handledby;
        this.reason = reason;
        this.contract_name = contract_name;
    }
}