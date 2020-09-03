package ones.autowork.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liYueYang on 2020/6/11.
 * 项目支出用款表
 */
@Data
public class ZH_ExpendInfo {
    private String expend_id; // 主键ID
    private String project_id; // 项目ID
    private BigDecimal report_date; // 提交时间
    private BigDecimal spend_month; // 支出月份
    private String spend_type; // 支出类型
    private String remark; // 备注信息

    public ZH_ExpendInfo(String expend_id, String project_id, BigDecimal report_date, BigDecimal spend_month, String spend_type, String remark) {
        this.expend_id = expend_id;
        this.project_id = project_id;
        this.report_date = report_date;
        this.spend_month = spend_month;
        this.spend_type = spend_type;
        this.remark = remark;
    }
}