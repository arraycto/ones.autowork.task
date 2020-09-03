package ones.autowork.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liYueYang on 2020/8/13.
 * 被监听的表
 */
@Data
public class ZH_UpdateStatus {
    private String status_id;
    private String project_id;
    private BigDecimal month;

    public ZH_UpdateStatus(String status_id, String project_id, BigDecimal month) {
        this.status_id = status_id;
        this.project_id = project_id;
        this.month = month;
    }
}
