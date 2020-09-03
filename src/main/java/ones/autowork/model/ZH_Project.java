package ones.autowork.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liYueYang on 2020/6/9.
 * 项目表
 */
@Data
public class ZH_Project {
    private String project_id;// 主键ID
    private String project_name;// 项目名称
    private String project_code;// 项目编号
    private String owner_id;// 业主ID
    private BigDecimal sign_time;// 签订时间
    private BigDecimal amount;// 合同金额
    private String project_leader;// 项目负责人
    private String leader_id;// 项目负责人ID
    private BigDecimal leader_phone;// 负责人电话
    private String address;// 项目地址
    private BigDecimal citycode;// 区划编号
    private BigDecimal date_start; // 开始时间
    private BigDecimal date_end;// 结束时间
    private String department;// 部门
    private String department_id;// 部门ID
    private String type_project; // 项目类型
    private String type_work;// 业务分类
    private String stage;// 阶段
    private String contract_no; // 合同编号
    private String discription;// 描述
    private String marketer;// 市场负责人
    private String marketer_id; // 市场负责人ID
    private BigDecimal m_phone;// 市场负责人电话
    private String pre_saler;// 售前负责人
    private String pre_saler_id; // 售前负责人ID
    private BigDecimal p_phone;// 售前负责人电话
    private String remark;// 备注信息
    private BigDecimal winning_date; // 中标日期
    private BigDecimal start_date;// 实际开工日期
    private BigDecimal completion_date;// 合同竣工日期
    private BigDecimal acceptance_date;// 验收日期
    private BigDecimal finish_date;// 实际竣工日期
    private BigDecimal commence_date;// 合同开工日期
    private BigDecimal budget;// 预算成本

    public ZH_Project(String project_id, String project_name, String project_code, String owner_id, BigDecimal sign_time, BigDecimal amount, String project_leader, String leader_id, BigDecimal leader_phone, String address, BigDecimal citycode, BigDecimal date_start, BigDecimal date_end, String department, String department_id, String type_project, String type_work, String stage, String contract_no, String discription, String marketer, String marketer_id, BigDecimal m_phone, String pre_saler, String pre_saler_id, BigDecimal p_phone, String remark, BigDecimal winning_date, BigDecimal start_date, BigDecimal completion_date, BigDecimal acceptance_date, BigDecimal finish_date, BigDecimal commence_date, BigDecimal budget) {
        this.project_id = project_id;
        this.project_name = project_name;
        this.project_code = project_code;
        this.owner_id = owner_id;
        this.sign_time = sign_time;
        this.amount = amount;
        this.project_leader = project_leader;
        this.leader_id = leader_id;
        this.leader_phone = leader_phone;
        this.address = address;
        this.citycode = citycode;
        this.date_start = date_start;
        this.date_end = date_end;
        this.department = department;
        this.department_id = department_id;
        this.type_project = type_project;
        this.type_work = type_work;
        this.stage = stage;
        this.contract_no = contract_no;
        this.discription = discription;
        this.marketer = marketer;
        this.marketer_id = marketer_id;
        this.m_phone = m_phone;
        this.pre_saler = pre_saler;
        this.pre_saler_id = pre_saler_id;
        this.p_phone = p_phone;
        this.remark = remark;
        this.winning_date = winning_date;
        this.start_date = start_date;
        this.completion_date = completion_date;
        this.acceptance_date = acceptance_date;
        this.finish_date = finish_date;
        this.commence_date = commence_date;
        this.budget = budget;
    }
}
