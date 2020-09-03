package ones.autowork.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liYueYang on 2020/6/11.
 */
@Data
public class ZH_ExpendMoney {
    private String money_id; // 主键ID
    private String expend_id; // 支出表ID
    private String expend_type; // 用款类型
    private BigDecimal amount_expend; // 金额
    private String expend_typename; // 用款类型名称

    public ZH_ExpendMoney(String money_id, String expend_id, String expend_type, BigDecimal amount_expend, String expend_typename) {
        this.money_id = money_id;
        this.expend_id = expend_id;
        this.expend_type = expend_type;
        this.amount_expend = amount_expend;
        this.expend_typename = expend_typename;
    }
}