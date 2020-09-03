package ones.autowork.util;

import ones.autowork.model.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ones.autowork.util.postgresql.PgJdbcConn.getConn;

/**
 * Created by liYueYang on 2020/6/9.
 */
public class JdbcOptPgUtilsV2 {
    /**
     * 查询所涉及的表今天是否有数据更新
     *
     * @param reportDate
     * @return
     */
    public static int getUpdateCount(String reportDate) {
        Connection c = getConn();
        Statement stmt = null;
        int count = 0;
        try {
            stmt = c.createStatement();
            String sql = "SELECT (" +
                    "(SELECT count(*) FROM zh_returnedinfo a where a.report_date = '" + reportDate + "')" +
                    "+ (SELECT count(*) FROM zh_workinfo b where b.report_date = '" + reportDate + "')" +
                    "+(SELECT count(*) FROM zh_subpayment c where c.report_date = '" + reportDate + "')" +
                    "+(SELECT count(*) FROM zh_expendinfo d where d.report_date = '" + reportDate + "')" +
                    "+(SELECT count(*) FROM zh_disbursementplan e where e.report_date = '" + reportDate + "')" +
                    ") as count";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                count = rs.getInt("count");
            }
            rs.close();
            stmt.close();
            c.close();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // 获取所有项目列表
    public static List<ZH_Project> getAllProject() {
        Connection c = getConn();
        Statement stmt = null;
        List<ZH_Project> zh_projects = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM zh_project";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_projects.add(new ZH_Project(rs.getString("project_id"), rs.getString("project_name"),
                        rs.getString("project_code"), rs.getString("owner_id"),
                        rs.getBigDecimal("sign_time"), rs.getBigDecimal("amount"),
                        rs.getString("project_leader"), rs.getString("leader_id"),
                        rs.getBigDecimal("leader_phone"), rs.getString("address"),
                        rs.getBigDecimal("citycode"), rs.getBigDecimal("date_start"),
                        rs.getBigDecimal("date_end"), rs.getString("department"),
                        rs.getString("department_id"), rs.getString("type_project"),
                        rs.getString("type_work"), rs.getString("stage"),
                        rs.getString("contract_no"), rs.getString("discription"),
                        rs.getString("marketer"), rs.getString("marketer_id"),
                        rs.getBigDecimal("m_phone"), rs.getString("pre_saler"),
                        rs.getString("pre_saler_id"), rs.getBigDecimal("p_phone"),
                        rs.getString("remark"), rs.getBigDecimal("winning_date"),
                        rs.getBigDecimal("start_date"), rs.getBigDecimal("completion_date"),
                        rs.getBigDecimal("acceptance_date"), rs.getBigDecimal("finish_date"),
                        rs.getBigDecimal("commence_date"), rs.getBigDecimal("budget")));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_projects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_projects;
    }

    // 项目回款信息表
    public static List<ZH_ReturnedInfo> getReturnedInfo(String nowDate, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        List<ZH_ReturnedInfo> zh_returnedInfos = new ArrayList<>();
        try {
            stmt = c.createStatement();
            // todo 项目回款信息表 根据提交日期（report_date）查询
            String sql = "SELECT * FROM zh_returnedinfo where returned_month = '" + nowDate + "' and project_id = '" + projectId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_returnedInfos.add(new ZH_ReturnedInfo(rs.getString("returned_id"), rs.getString("project_id"),
                        rs.getBigDecimal("trade_date"), rs.getBigDecimal("returned_month"),
                        rs.getBigDecimal("amount_returned"), rs.getString("use"),
                        rs.getString("nature_payment"), rs.getString("postscript"),
                        rs.getString("remark"), rs.getString("returned_unit")));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_returnedInfos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_returnedInfos;
    }

    // 项目月报信息表
    public static List<ZH_WorkInfo> getWorkInfo(String nowDate, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        List<ZH_WorkInfo> zh_workInfos = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM zh_workinfo where report_month = '" + nowDate + "' and project_id = '" + projectId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_workInfos.add(new ZH_WorkInfo(rs.getString("work_id"), rs.getString("project_id"),
                        rs.getBigDecimal("report_date"), rs.getBigDecimal("report_month"),
                        rs.getString("problem"), rs.getString("financial_status"),
                        rs.getString("work_status"), rs.getString("work_plan"),
                        rs.getString("safety_status"), rs.getString("safety_plan"),
                        rs.getString("remark"), rs.getString("proposal"),
                        rs.getBigDecimal("current_output_value"), rs.getBigDecimal("year_output_value"),
                        rs.getBigDecimal("comulative_output_value"), rs.getBigDecimal("next_plan_receipt"),
                        rs.getBigDecimal("current_progress"), rs.getBigDecimal("next_progress"),
                        rs.getString("progress_status"), rs.getString("reason_delay"),
                        rs.getString("measures")));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_workInfos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_workInfos;
    }

    // 分包付款信息表通过申请日期
    public static List<ZH_SubPayment> getSubPaymentByReportDate(String nowDate, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        List<ZH_SubPayment> zh_subPayments = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM zh_subpayment where pay_month = '" + nowDate + "' and project_id = '" + projectId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_subPayments.add(new ZH_SubPayment(rs.getString("payment_id"), rs.getString("project_id"),
                        rs.getString("contract_id"), rs.getBigDecimal("report_date"),
                        rs.getBigDecimal("pay_month"), rs.getBigDecimal("pay_times"),
                        rs.getBigDecimal("amount_invoice"), rs.getBigDecimal("amount_receivable"),
                        rs.getBigDecimal("pay_date"), rs.getBigDecimal("amount_payment"),
                        rs.getString("remark"), rs.getString("handledby"),
                        rs.getString("reason"), ""));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_subPayments;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_subPayments;
    }

    // 分包付款信息表通过付款月份
    public static List<ZH_SubPayment> getSubPaymentByPayMonth(BigDecimal payMonth, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        List<ZH_SubPayment> zh_subPayments = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT a.*,b.contract_name FROM zh_subpayment a, zh_subcontract b " +
                    "where a.contract_id = b.contract_id and a.pay_month = '" + payMonth + "' and a.project_id = '" + projectId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_subPayments.add(new ZH_SubPayment(rs.getString("payment_id"), rs.getString("project_id"),
                        rs.getString("contract_id"), rs.getBigDecimal("report_date"),
                        rs.getBigDecimal("pay_month"), rs.getBigDecimal("pay_times"),
                        rs.getBigDecimal("amount_invoice"), rs.getBigDecimal("amount_receivable"),
                        rs.getBigDecimal("pay_date"), rs.getBigDecimal("amount_payment"),
                        rs.getString("remark"), rs.getString("handledby"),
                        rs.getString("reason"), rs.getString("contract_name")));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_subPayments;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_subPayments;
    }

    // 分包付款 - 下月预计支出
    public static List<ZH_DisBursementPlan> getDisBursementPlanByPayMonth(BigDecimal applyMonth, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        List<ZH_DisBursementPlan> zh_disBursementPlans = new ArrayList<>();

        try {
            stmt = c.createStatement();
            String sql = "SELECT a.*,b.contract_name FROM zh_disbursementplan a,zh_subcontract b " +
                    "where a.contract_id = b.contract_id and a.apply_month = '" + applyMonth + "' and a.project_id = '" + projectId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_disBursementPlans.add(new ZH_DisBursementPlan(rs.getString("plan_id"), rs.getString("project_id"),
                        rs.getString("contract_id"), rs.getBigDecimal("report_date"),
                        rs.getBigDecimal("apply_month"), rs.getBigDecimal("estimate_payment"),
                        rs.getString("remark"), rs.getString("proportion"),
                        rs.getBigDecimal("paid_amount"), rs.getString("contract_name")));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_disBursementPlans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_disBursementPlans;
    }

    // 项目支出用款表，通过上报日期
    public static List<ZH_ExpendInfo> getExpendInfoByReportDate(String nowDate, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        List<ZH_ExpendInfo> zh_expendInfos = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM zh_expendinfo where spend_month = '" + nowDate + "' and project_id = '" + projectId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_expendInfos.add(new ZH_ExpendInfo(rs.getString("expend_id"), rs.getString("project_id"),
                        rs.getBigDecimal("report_date"), rs.getBigDecimal("spend_month"),
                        rs.getString("spend_type"), rs.getString("remark")));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_expendInfos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_expendInfos;
    }

    // 项目支出用款表，通过支出月份
    public static List<ZH_ExpendMoney> getExpendInfoBySpeedMonth(BigDecimal nowDate, String projectId, String speedType) {
        Connection c = getConn();
        Statement stmt = null;
        List<ZH_ExpendMoney> zh_expendMoneyList = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * from zh_expendmoney where expend_id in" +
                    "(SELECT expend_id FROM zh_expendinfo " +
                    "where spend_type = '" + speedType + "' and spend_month = '"
                    + nowDate + "' and project_id = '" + projectId + "')";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_expendMoneyList.add(new ZH_ExpendMoney(rs.getString("money_id"), rs.getString("expend_id"),
                        rs.getString("expend_type"), rs.getBigDecimal("amount_expend"),
                        rs.getString("expend_typename")));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_expendMoneyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_expendMoneyList;
    }

    // 用款申请计划表
    public static List<ZH_DisBursementPlan> getDisBurseMentPlan(String nowDate, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        List<ZH_DisBursementPlan> zh_disBursementPlans = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM zh_disbursementplan where apply_month = '" + nowDate + "' and project_id = '" + projectId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_disBursementPlans.add(new ZH_DisBursementPlan(rs.getString("plan_id"), rs.getString("project_id"),
                        rs.getString("contract_id"), rs.getBigDecimal("report_date"),
                        rs.getBigDecimal("apply_month"), rs.getBigDecimal("estimate_payment"),
                        rs.getString("remark"), rs.getString("proportion"),
                        rs.getBigDecimal("paid_amount"), ""));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_disBursementPlans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_disBursementPlans;
    }

    // 根据项目id、回款月份查询项目经营台账表
    public static ZH_Management getManagementByProIdAndMonth(String projectId, BigDecimal returnedMonth) {
        Connection c = getConn();
        Statement stmt = null;
        ZH_Management zh_management = null;
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM zh_management where project_id = '" + projectId + "' and report_date = '" + returnedMonth + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_management = new ZH_Management(rs.getString("management_id"), rs.getString("project_id"),
                        rs.getBigDecimal("report_date"), rs.getBigDecimal("current_actual_receipt"),
                        rs.getBigDecimal("current_output_value"), rs.getBigDecimal("current_expenditure"),
                        rs.getBigDecimal("last_plan_return"), rs.getBigDecimal("last_plan_expenditure"),
                        rs.getBigDecimal("next_plan_receipt"), rs.getBigDecimal("next_plan_expenditure"),
                        rs.getString("info_current"), rs.getString("info_next"),
                        rs.getString("remark"));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_management;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_management;
    }

    // 分包付款信息表 - AMOUNT_PAYMENT + 项目支出用款表 - AMOUNT_EXPEND（支出ID为本月实际支出的）
    public static BigDecimal getCurrentExpenditureSum(BigDecimal spendMonth, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        BigDecimal amountReturnSum = null;
        try {
            stmt = c.createStatement();
            String sql = "SELECT sum(COALESCE((SELECT sum(amount_payment) FROM zh_subpayment  where " +
                    " pay_month = '" + spendMonth + "'" +
                    "and project_id = '" + projectId + "'),0) + COALESCE((SELECT sum(a.amount_expend) " +
                    "FROM zh_expendmoney a,zh_expendinfo b where " +
                    "a.expend_id = b.expend_id and b.spend_type = 'current' " +
                    "and b.project_id = '" + projectId + "'" +
                    "and b.spend_month = '" + spendMonth + "'),0)) AS sum";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                amountReturnSum = rs.getBigDecimal("sum");
            }
            rs.close();
            stmt.close();
            c.close();
            return amountReturnSum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amountReturnSum;
    }

    // 本月完成产值
    public static BigDecimal getCurrentOutPutValueThisMonth(BigDecimal reportMonth, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        BigDecimal amountReturnSum = null;
        try {
            stmt = c.createStatement();
            String sql = "SELECT current_output_value " +
                    "from zh_workinfo where report_month = '" + reportMonth + "' and  project_id = '" + projectId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                amountReturnSum = rs.getBigDecimal("current_output_value");
            }
            rs.close();
            stmt.close();
            c.close();
            return amountReturnSum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amountReturnSum;
    }

    // 下月预计回款
    public static BigDecimal getNextPlanReceipt(BigDecimal reportMonth, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        BigDecimal amountReturnSum = null;
        try {
            stmt = c.createStatement();
            String sql = "SELECT next_plan_receipt " +
                    "from zh_workinfo where report_month = '" + reportMonth + "' and  project_id = '" + projectId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                amountReturnSum = rs.getBigDecimal("next_plan_receipt");
            }
            rs.close();
            stmt.close();
            c.close();
            return amountReturnSum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amountReturnSum;
    }

    // 本月实际回款
    public static BigDecimal getCurrentActualReceipt(BigDecimal returnedMonth, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        BigDecimal amountReturnSum = null;
        try {
            stmt = c.createStatement();
            String sql = "SELECT amount_returned from zh_returnedinfo " +
                    "WHERE project_id = '" + projectId + "' and returned_month = '" + returnedMonth + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                amountReturnSum = rs.getBigDecimal("amount_returned");
            }
            rs.close();
            stmt.close();
            c.close();
            return amountReturnSum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amountReturnSum;
    }

    // 下月预计支出 - 分包付款
    public static BigDecimal getNextPlanExpenditureFenBaoSum(BigDecimal spendMonth, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        BigDecimal amountReturnSum = null;
        try {
            stmt = c.createStatement();
            String sql = "SELECT sum(estimate_payment) as estimatePaymentSum FROM zh_disbursementplan where " +
                    " project_id = '" + projectId + "' and apply_month = '" + spendMonth + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                amountReturnSum = rs.getBigDecimal("estimatePaymentSum");
            }
            rs.close();
            stmt.close();
            c.close();
            return amountReturnSum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amountReturnSum;
    }

    // 下月预计支出 - 其他支出
    public static BigDecimal getNextPlanExpenditureOtherSum(BigDecimal spendMonth, String projectId) {
        Connection c = getConn();
        Statement stmt = null;
        BigDecimal amountReturnSum = null;
        try {
            stmt = c.createStatement();
            String sql = "SELECT sum(a.amount_expend) as amountExpendSum FROM zh_expendmoney a,zh_expendinfo b where " +
                    " a.expend_id = b.expend_id and b.spend_type = 'next' " +
                    " and b.project_id = '" + projectId + "'" +
                    " and b.spend_month = '" + spendMonth + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                amountReturnSum = rs.getBigDecimal("amountExpendSum");
            }
            rs.close();
            stmt.close();
            c.close();
            return amountReturnSum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amountReturnSum;
    }

    // 台账表：更新‘累计回款’、‘本月实际回款’字段
    public static void updateManagementByReturnInfo(String managementId, BigDecimal currentActualReceiptSum, BigDecimal NextPlanReceipt, BigDecimal NextPlanExpenditure) {
        Connection c = getConn();
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "UPDATE zh_management SET " +
                    "current_actual_receipt = '" + currentActualReceiptSum + "'" +
                    ", last_plan_return = '" + NextPlanReceipt + "'" +
                    ", last_plan_expenditure = '" + NextPlanExpenditure + "'" +
                    " WHERE management_id = '" + managementId + "'";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 台账表：更新‘本月完成产值’、‘下月预计回款’字段
    public static void updateManagementByWorkInfo(String managementId, BigDecimal currentOutPutValueThisSum,
                                                  BigDecimal nextPlanReceiptSum, BigDecimal NextPlanReceipt, BigDecimal NextPlanExpenditure) {
        Connection c = getConn();
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "UPDATE zh_management SET " +
                    "current_output_value = '" + currentOutPutValueThisSum + "'" +
                    ", next_plan_receipt = '" + nextPlanReceiptSum + "'" +
                    ", last_plan_return = '" + NextPlanReceipt + "'" +
                    ", last_plan_expenditure = '" + NextPlanExpenditure + "'" +
                    " WHERE management_id = '" + managementId + "'";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 台账表：更新‘本月实际支出’、‘备注’字段
    public static void updateManagementBySubPayMent(String managementId, BigDecimal currentOutPutValueThisSum,
                                                    String infoCurrent, BigDecimal NextPlanReceipt, BigDecimal NextPlanExpenditure) {
        Connection c = getConn();
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "UPDATE zh_management SET " +
                    "current_expenditure = '" + currentOutPutValueThisSum + "'" +
                    ", info_current = '" + infoCurrent + "'" +
                    ", last_plan_return = '" + NextPlanReceipt + "'" +
                    ", last_plan_expenditure = '" + NextPlanExpenditure + "'" +
                    " WHERE management_id = '" + managementId + "'";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 台账表：更新‘下月预计支出’、'本月实际支出'字段
    public static void updateManagementByExpendInfo(String managementId, BigDecimal nextPlanExpenditureSum,
                                                    BigDecimal currentOutPutValueThisSum, String infoCurrent,
                                                    String infoNext, BigDecimal NextPlanReceipt, BigDecimal NextPlanExpenditure) {
        Connection c = getConn();
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "UPDATE zh_management SET " +
                    "next_plan_expenditure = '" + nextPlanExpenditureSum +
                    "',current_expenditure = '" + currentOutPutValueThisSum +
                    "',info_current = '" + infoCurrent +
                    "', info_next = '" + infoNext +
                    "', last_plan_return = '" + NextPlanReceipt +
                    "', last_plan_expenditure = '" + NextPlanExpenditure +
                    "' WHERE management_id = '" + managementId + "'";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 台账表：更新‘下月预计支出’字段
    public static void updateManagementByDisBursementPlan(String managementId, BigDecimal nextPlanExpenditureSum,
                                                          String infoNext, BigDecimal NextPlanReceipt, BigDecimal NextPlanExpenditure) {
        Connection c = getConn();
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "UPDATE zh_management SET " +
                    "next_plan_expenditure = '" + nextPlanExpenditureSum +
                    "', info_next = '" + infoNext +
                    "', last_plan_return = '" + NextPlanReceipt +
                    "', last_plan_expenditure = '" + NextPlanExpenditure +
                    "' WHERE management_id = '" + managementId + "'";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 新增台账记录
    public static void insertManagement(ZH_Management zh_management) {
        Connection c = getConn();
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "INSERT INTO zh_management(management_id,project_id,report_date,current_actual_receipt,current_output_value" +
                    ",current_expenditure,last_plan_return,last_plan_expenditure,next_plan_receipt" +
                    ",next_plan_expenditure,info_current,info_next,remark) VALUES(" +
                    "'" + UUID.randomUUID().toString() + "'," +
                    "'" + zh_management.getProject_id() + "'," +
                    "'" + zh_management.getReport_date() + "'," +
                    "'" + zh_management.getCurrent_actual_receipt() + "'," +
                    "'" + zh_management.getCurrent_output_value() + "'," +
                    "'" + zh_management.getCurrent_expenditure() + "'," +
                    "'" + zh_management.getLast_plan_return() + "'," +
                    "'" + zh_management.getLast_plan_expenditure() + "'," +
                    "'" + zh_management.getNext_plan_receipt() + "'," +
                    "'" + zh_management.getNext_plan_expenditure() + "'," +
                    "'" + zh_management.getInfo_current() + "'," +
                    "'" + zh_management.getInfo_next() + "'," +
                    "'" + zh_management.getRemark() + "')";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<ZH_UpdateStatus> getUpdateCountNew() {
        Connection c = getConn();
        Statement stmt = null;
        List<ZH_UpdateStatus> zh_updateStatuses = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM zh_update_status";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                zh_updateStatuses.add(new ZH_UpdateStatus(rs.getString("status_id"), rs.getString("project_id"),
                        rs.getBigDecimal("month")));
            }
            rs.close();
            stmt.close();
            c.close();
            return zh_updateStatuses;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zh_updateStatuses;
    }

    // 删除一条记录
    public static void deleteIt(String statusId) {
        Connection c = getConn();
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "DELETE from zh_update_status where status_id = '" + statusId + "'";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
