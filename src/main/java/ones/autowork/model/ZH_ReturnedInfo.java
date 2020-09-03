package ones.autowork.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liYueYang on 2020/6/11.
 * 项目回款信息表
 */
@Data
public class ZH_ReturnedInfo {
    private String returned_id; // 主键ID
    private String project_id; // 项目ID
    private BigDecimal trade_date; // 交易时间
    private BigDecimal returned_month; // 回款月份
    private BigDecimal amount_returned; // 回款金额
    private String use; // 用途
    private String nature_payment; // 款项性质
    private String postscript; // 交易附言
    private String remark; // 备注信息
    private String returned_unit; // 回款单位

    public ZH_ReturnedInfo(String returned_id, String project_id, BigDecimal trade_date, BigDecimal returned_month, BigDecimal amount_returned, String use, String nature_payment, String postscript, String remark, String returned_unit) {
        this.returned_id = returned_id;
        this.project_id = project_id;
        this.trade_date = trade_date;
        this.returned_month = returned_month;
        this.amount_returned = amount_returned;
        this.use = use;
        this.nature_payment = nature_payment;
        this.postscript = postscript;
        this.remark = remark;
        this.returned_unit = returned_unit;
    }
}
