package ones.autowork.task;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import ones.autowork.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ones.autowork.util.JdbcOptPgUtils.*;

/**
 * Created by liYueYang on 2020/6/11.
 * 智慧报调度定时任务
 */
@Component
public class AutoWorkTask {
    private static Logger logger = LoggerFactory.getLogger(AutoWorkTask.class);

    //    @Scheduled(cron = "0 0/10 * * * ?") // 十分钟执行一次
//    @XxlJob("autoWorkTask")
    public ReturnT<String> autoWorkTask(String param) {
        logger.info("-------- 智慧报调度定时任务执行开始 --------");
        // todo java8 方式获取当前年月日
        LocalDate localDate = LocalDate.now();
        String nowDate = localDate.toString().replace("-", "");
        // 查询那几个表，看有没有今天提交的数据
        int count = getUpdateCount(nowDate);
        if (count > 0) {
            // 获取所有项目
            List<ZH_Project> zh_projectList = getAllProject();
            if (zh_projectList.size() > 0) {
                generateManagement(zh_projectList, nowDate);
            } else {
                logger.info("项目列表为空");
            }
        } else {
            logger.info("没数据更新");
        }
        logger.info("-------- 智慧报调度定时任务执行结束 --------");
        return ReturnT.SUCCESS;
    }

    /**
     * 经营合同台账数据生成
     */
    private void generateManagement(List<ZH_Project> zh_projects, String nowDate) {
        // 遍历项目列表
        zh_projects.forEach(project -> {
            // 项目ID
            String projectId = project.getProject_id();

            // 项目回款信息表 zh_returnedinfo
            List<ZH_ReturnedInfo> returnedInfoList = getReturnedInfo(nowDate, projectId);
            if (returnedInfoList.size() > 0) {
                // 以回款月份进行分组
                returnedInfoList.stream().collect(Collectors.groupingBy(ZH_ReturnedInfo::getReturned_month));
                // 转 map
                Map<BigDecimal, List<ZH_ReturnedInfo>> returnedInfoMap =
                        returnedInfoList.stream().collect(Collectors.toMap(ZH_ReturnedInfo::getReturned_month, s -> {
                            List<ZH_ReturnedInfo> l = new ArrayList<>();
                            l.add(s);
                            return l;
                        }, (List<ZH_ReturnedInfo> s1, List<ZH_ReturnedInfo> s2) -> {
                            s1.addAll(s2);
                            return s1;
                        }));
                returnedInfoMap.forEach((k, v) -> { // k:回款月份 v:项目回款信息表数据
                    // 根据项目id、回款月份查询项目经营台账表
                    ZH_Management zh_management = getManagementByProIdAndMonth(projectId, k);
                    // 本月实际回款
                    BigDecimal currentActualReceiptSum = getCurrentActualReceipt(k, projectId);

                    // 查询该项目上个月的数据
                    ZH_Management zh_management2 = getManagementByProIdAndMonth(projectId, changeMonth(k, -1));
                    // 下月计划回款 作为 上月计划回款
                    BigDecimal NextPlanReceipt = (zh_management2 == null ? new BigDecimal("0") : zh_management2.getNext_plan_receipt());
                    // 下月计划支出 作为 上月计划支出
                    BigDecimal NextPlanExpenditure = (zh_management2 == null ? new BigDecimal("0") : zh_management2.getNext_plan_expenditure());
                    if (zh_management != null) {
                        String managementId = zh_management.getManagement_id();
                        updateManagementByReturnInfo(managementId, (currentActualReceiptSum == null ? new BigDecimal("0") : currentActualReceiptSum),
                                NextPlanReceipt, NextPlanExpenditure);
                    } else { // 新增一条
                        ZH_Management zh_management1 = new ZH_Management();
                        zh_management1.setProject_id(projectId);
                        zh_management1.setReport_date(k);
                        zh_management1.setCurrent_actual_receipt(currentActualReceiptSum == null ? new BigDecimal("0") : currentActualReceiptSum);
                        zh_management1.setCurrent_output_value(new BigDecimal("0"));
                        zh_management1.setCurrent_expenditure(new BigDecimal("0"));
                        zh_management1.setLast_plan_return(NextPlanReceipt);
                        zh_management1.setLast_plan_expenditure(NextPlanExpenditure);
                        zh_management1.setNext_plan_receipt(new BigDecimal("0"));
                        zh_management1.setNext_plan_expenditure(new BigDecimal("0"));
                        zh_management1.setInfo_current("");
                        zh_management1.setInfo_next("");
                        zh_management1.setRemark("");
                        insertManagement(zh_management1);
                    }
                });
            }

            // 项目月报信息表 zh_workinfo
            List<ZH_WorkInfo> workInfoList = getWorkInfo(nowDate, projectId);
            if (workInfoList.size() > 0) {
                // 以提交月份进行分组
                workInfoList.stream().collect(Collectors.groupingBy(ZH_WorkInfo::getReport_month));
                // 转 map
                Map<BigDecimal, List<ZH_WorkInfo>> workInfoMap =
                        workInfoList.stream().collect(Collectors.toMap(ZH_WorkInfo::getReport_month, s -> {
                            List<ZH_WorkInfo> l = new ArrayList<>();
                            l.add(s);
                            return l;
                        }, (List<ZH_WorkInfo> s1, List<ZH_WorkInfo> s2) -> {
                            s1.addAll(s2);
                            return s1;
                        }));
                workInfoMap.forEach((k, v) -> {
                    // 本月完成产值
                    BigDecimal currentOutPutValueSum = getCurrentOutPutValueThisMonth(k, projectId);
                    // 下月预计回款
                    BigDecimal nextPlanReceiptSum = getNextPlanReceipt(k, projectId);

                    // 查询该项目上个月的数据
                    ZH_Management zh_management2 = getManagementByProIdAndMonth(projectId, changeMonth(k, -1));
                    // 下月计划回款
                    BigDecimal NextPlanReceipt = (zh_management2 == null ? new BigDecimal("0") : zh_management2.getNext_plan_receipt());
                    // 下月计划支出
                    BigDecimal NextPlanExpenditure = (zh_management2 == null ? new BigDecimal("0") : zh_management2.getNext_plan_expenditure());

                    // 根据项目id、回款月份查询项目经营台账表
                    ZH_Management zh_management = getManagementByProIdAndMonth(projectId, k);
                    if (zh_management != null) {
                        String managementId = zh_management.getManagement_id();
                        updateManagementByWorkInfo(managementId,
                                (currentOutPutValueSum == null ? new BigDecimal("0") : currentOutPutValueSum),
                                (nextPlanReceiptSum == null ? new BigDecimal("0") : nextPlanReceiptSum),
                                NextPlanReceipt, NextPlanExpenditure);
                    } else { // 新增一条
                        ZH_Management zh_management1 = new ZH_Management();
                        zh_management1.setProject_id(projectId);
                        zh_management1.setReport_date(k);
                        zh_management1.setCurrent_actual_receipt(new BigDecimal("0"));
                        zh_management1.setCurrent_output_value(currentOutPutValueSum == null ? new BigDecimal("0") : currentOutPutValueSum);
                        zh_management1.setCurrent_expenditure(new BigDecimal("0"));
                        zh_management1.setLast_plan_return(NextPlanReceipt);
                        zh_management1.setLast_plan_expenditure(NextPlanExpenditure);
                        zh_management1.setNext_plan_receipt(nextPlanReceiptSum == null ? new BigDecimal("0") : nextPlanReceiptSum);
                        zh_management1.setNext_plan_expenditure(new BigDecimal("0"));
                        zh_management1.setInfo_current("");
                        zh_management1.setInfo_next("");
                        zh_management1.setRemark("");
                        insertManagement(zh_management1);
                    }
                });
            }

            // 分包付款信息表 zh_subpayment
            List<ZH_SubPayment> subPaymentList = getSubPaymentByReportDate(nowDate, projectId);
            if (subPaymentList.size() > 0) {
                // 以付款月份进行分组
                subPaymentList.stream().collect(Collectors.groupingBy(ZH_SubPayment::getPay_month));
                // 转 map
                Map<BigDecimal, List<ZH_SubPayment>> subPaymentMap =
                        subPaymentList.stream().collect(Collectors.toMap(ZH_SubPayment::getPay_month, s -> {
                            List<ZH_SubPayment> l = new ArrayList<>();
                            l.add(s);
                            return l;
                        }, (List<ZH_SubPayment> s1, List<ZH_SubPayment> s2) -> {
                            s1.addAll(s2);
                            return s1;
                        }));
                subPaymentMap.forEach((k, v) -> {
                    // 本月实际支出 current_expenditure
                    BigDecimal currentExpenditureSum = getCurrentExpenditureSum(k, projectId);
                    // 本月实际支出详情
                    String infoCurrent = infoCurrentDetial((currentExpenditureSum == null ? new BigDecimal("0") : currentExpenditureSum), k, projectId);

                    // 查询该项目上个月的数据
                    ZH_Management zh_management2 = getManagementByProIdAndMonth(projectId, changeMonth(k, -1));
                    // 下月计划回款
                    BigDecimal NextPlanReceipt = (zh_management2 == null ? new BigDecimal("0") : zh_management2.getNext_plan_receipt());
                    // 下月计划支出
                    BigDecimal NextPlanExpenditure = (zh_management2 == null ? new BigDecimal("0") : zh_management2.getNext_plan_expenditure());
                    // 根据项目id、回款月份查询项目经营台账表
                    ZH_Management zh_management = getManagementByProIdAndMonth(projectId, k);
                    if (zh_management != null) {
                        String managementId = zh_management.getManagement_id();
                        updateManagementBySubPayMent(managementId, (currentExpenditureSum == null ? new BigDecimal("0") : currentExpenditureSum),
                                infoCurrent, NextPlanReceipt, NextPlanExpenditure);
                    } else { // 新增一条
                        ZH_Management zh_management1 = new ZH_Management();
                        zh_management1.setProject_id(projectId);
                        zh_management1.setReport_date(k);
                        zh_management1.setCurrent_actual_receipt(new BigDecimal("0"));
                        zh_management1.setCurrent_output_value(new BigDecimal("0"));
                        zh_management1.setCurrent_expenditure(currentExpenditureSum == null ? new BigDecimal("0") : currentExpenditureSum);
                        zh_management1.setLast_plan_return(NextPlanReceipt);
                        zh_management1.setLast_plan_expenditure(NextPlanExpenditure);
                        zh_management1.setNext_plan_receipt(new BigDecimal("0"));
                        zh_management1.setNext_plan_expenditure(new BigDecimal("0"));
                        zh_management1.setInfo_current(infoCurrent);
                        zh_management1.setInfo_next("");
                        zh_management1.setRemark("");
                        insertManagement(zh_management1);
                    }
                });
            }

            // 项目支出用款表 zh_expendmoney ：zh_expendinfo
            List<ZH_ExpendInfo> expendInfoList = getExpendInfoByReportDate(nowDate, projectId);
            if (expendInfoList.size() > 0) {
                // 以支出月份进行分组
                expendInfoList.stream().collect(Collectors.groupingBy(ZH_ExpendInfo::getSpend_month));
                // 转 map
                Map<BigDecimal, List<ZH_ExpendInfo>> expendInfoMap =
                        expendInfoList.stream().collect(Collectors.toMap(ZH_ExpendInfo::getSpend_month, s -> {
                            List<ZH_ExpendInfo> l = new ArrayList<>();
                            l.add(s);
                            return l;
                        }, (List<ZH_ExpendInfo> s1, List<ZH_ExpendInfo> s2) -> {
                            s1.addAll(s2);
                            return s1;
                        }));
                expendInfoMap.forEach((k, v) -> {
                    // 本月实际支出
                    BigDecimal currentExpenditureSum = getCurrentExpenditureSum(k, projectId);
                    // 本月实际支出详情
                    String infoCurrent = infoCurrentDetial((currentExpenditureSum == null ? new BigDecimal("0") : currentExpenditureSum), k, projectId);

                    // 下月计划支出
                    BigDecimal nextPlanExpenditureSum = getNextPlanExpenditureSum(k, projectId);
                    // 下月计划支出详情
                    String infoNext = infoNextDetail((nextPlanExpenditureSum == null ? new BigDecimal("0") : nextPlanExpenditureSum), k, projectId);

                    // 查询该项目上个月的数据
                    ZH_Management zh_management2 = getManagementByProIdAndMonth(projectId, changeMonth(k, -1));
                    // 下月计划回款
                    BigDecimal NextPlanReceipt = (zh_management2 == null ? new BigDecimal("0") : zh_management2.getNext_plan_receipt());
                    // 下月计划支出
                    BigDecimal NextPlanExpenditure = (zh_management2 == null ? new BigDecimal("0") : zh_management2.getNext_plan_expenditure());
                    // 根据项目id、回款月份查询项目经营台账表
                    ZH_Management zh_management = getManagementByProIdAndMonth(projectId, k);
                    if (zh_management != null) {
                        String managementId = zh_management.getManagement_id();
                        updateManagementByExpendInfo(managementId,
                                (nextPlanExpenditureSum == null ? new BigDecimal("0") : nextPlanExpenditureSum),
                                (currentExpenditureSum == null ? new BigDecimal("0") : currentExpenditureSum),
                                infoCurrent, infoNext, NextPlanReceipt, NextPlanExpenditure);
                    } else { // 新增一条 nextPlanExpenditure
                        ZH_Management zh_management1 = new ZH_Management();
                        zh_management1.setProject_id(projectId);
                        zh_management1.setReport_date(k);
                        zh_management1.setCurrent_actual_receipt(new BigDecimal("0"));
                        zh_management1.setCurrent_output_value(new BigDecimal("0"));
                        zh_management1.setCurrent_expenditure(currentExpenditureSum == null ? new BigDecimal("0") : currentExpenditureSum);
                        zh_management1.setLast_plan_return(NextPlanReceipt);
                        zh_management1.setLast_plan_expenditure(NextPlanExpenditure);
                        zh_management1.setNext_plan_receipt(new BigDecimal("0"));
                        zh_management1.setNext_plan_expenditure(nextPlanExpenditureSum == null ? new BigDecimal("0") : nextPlanExpenditureSum);
                        zh_management1.setInfo_current(infoCurrent);
                        zh_management1.setInfo_next(infoNext);
                        zh_management1.setRemark("");
                        insertManagement(zh_management1);
                    }
                });
            }

            // 用款申请计划表 zh_disbursementplan
            List<ZH_DisBursementPlan> disBursementPlanList = getDisBurseMentPlan(nowDate, projectId);
            if (disBursementPlanList.size() > 0) {
                // 以申请月份进行分组
                disBursementPlanList.stream().collect(Collectors.groupingBy(ZH_DisBursementPlan::getApply_month));
                // 转 map
                Map<BigDecimal, List<ZH_DisBursementPlan>> disBursementPlanMap =
                        disBursementPlanList.stream().collect(Collectors.toMap(ZH_DisBursementPlan::getApply_month, s -> {
                            List<ZH_DisBursementPlan> l = new ArrayList<>();
                            l.add(s);
                            return l;
                        }, (List<ZH_DisBursementPlan> s1, List<ZH_DisBursementPlan> s2) -> {
                            s1.addAll(s2);
                            return s1;
                        }));
                disBursementPlanMap.forEach((k, v) -> {
                    // 下月计划支出
                    BigDecimal nextPlanExpenditureSum = getNextPlanExpenditureSum2(k, projectId);
                    // 下月计划支出详情
                    String infoNext = infoNextDetail2((nextPlanExpenditureSum == null ? new BigDecimal("0") : nextPlanExpenditureSum), k, projectId);

                    // 查询该项目上个月的数据
                    ZH_Management zh_management2 = getManagementByProIdAndMonth(projectId, changeMonth(k, -2));
                    // 上月计划回款
                    BigDecimal NextPlanReceipt = (zh_management2 == null ? new BigDecimal("0") : zh_management2.getNext_plan_receipt());
                    // 上月计划支出
                    BigDecimal NextPlanExpenditure = (zh_management2 == null ? new BigDecimal("0") : zh_management2.getNext_plan_expenditure());
                    // 根据项目id、回款月份查询项目经营台账表
                    ZH_Management zh_management = getManagementByProIdAndMonth(projectId, changeMonth(k, -1));
                    if (zh_management != null) {
                        String managementId = zh_management.getManagement_id();
                        updateManagementByDisBursementPlan(managementId, (nextPlanExpenditureSum == null ? new BigDecimal("0") : nextPlanExpenditureSum),
                                infoNext, NextPlanReceipt, NextPlanExpenditure);
                    } else { // 新增一条
                        ZH_Management zh_management1 = new ZH_Management();
                        zh_management1.setProject_id(projectId);
                        zh_management1.setReport_date(changeMonth(k, -1));
                        zh_management1.setCurrent_actual_receipt(new BigDecimal("0"));
                        zh_management1.setCurrent_output_value(new BigDecimal("0"));
                        zh_management1.setCurrent_expenditure(new BigDecimal("0"));
                        zh_management1.setLast_plan_return(NextPlanReceipt);
                        zh_management1.setLast_plan_expenditure(NextPlanExpenditure);
                        zh_management1.setNext_plan_receipt(new BigDecimal("0"));
                        zh_management1.setNext_plan_expenditure(nextPlanExpenditureSum == null ? new BigDecimal("0") : nextPlanExpenditureSum);
                        zh_management1.setInfo_current("");
                        zh_management1.setInfo_next(infoNext);
                        zh_management1.setRemark("");
                        insertManagement(zh_management1);
                    }
                });
            }
        });
    }

    /**
     * 月份处理
     *
     * @param k
     * @param num
     * @return
     */
    public BigDecimal changeMonth(BigDecimal k, int num) {
        DateFormat df = new SimpleDateFormat("yyyyMM");
        String aaa = k.toString();
        Calendar ct = Calendar.getInstance();
        try {
            ct.setTime(df.parse(aaa));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ct.add(Calendar.MONTH, num);
        return new BigDecimal(df.format(ct.getTime()));
    }

    // 下月预计支出总和（分包付款 + 其他支出）
    private BigDecimal getNextPlanExpenditureSum(BigDecimal k, String projectId) {
        BigDecimal fenBao = getNextPlanExpenditureFenBaoSum(k, projectId) == null ? new BigDecimal("0") : getNextPlanExpenditureFenBaoSum(k, projectId);
        BigDecimal other = getNextPlanExpenditureOtherSum(k, projectId) == null ? new BigDecimal("0") : getNextPlanExpenditureOtherSum(k, projectId);
        return fenBao.add(other);
    }

    // 下月预计支出总和（分包付款 + 其他支出）
    private BigDecimal getNextPlanExpenditureSum2(BigDecimal k, String projectId) {
        BigDecimal fenBao = getNextPlanExpenditureFenBaoSum(changeMonth(k, +1), projectId) == null ? new BigDecimal("0") : getNextPlanExpenditureFenBaoSum(changeMonth(k, +1), projectId);
        BigDecimal other = getNextPlanExpenditureOtherSum(k, projectId) == null ? new BigDecimal("0") : getNextPlanExpenditureOtherSum(k, projectId);
        return fenBao.add(other);
    }

    // 本月实际支出详情
    private String infoCurrentDetial(BigDecimal currentOutPutValueThisSum, BigDecimal k, String projectId) {
        StringBuilder infoCurrent = new StringBuilder();
        infoCurrent.append("本月实际支出：" + removeZero(currentOutPutValueThisSum.toString()) + "万元。\n");
        // 查询该项目该回款月份有哪些分包合同数据
        List<ZH_SubPayment> zh_subPaymentList = getSubPaymentByPayMonth(k, projectId);
        if (zh_subPaymentList.size() > 0) {
            Map map = new HashMap();
            zh_subPaymentList.forEach(zh_subPayment -> {
                map.put(zh_subPayment.getContract_name(), removeZero(zh_subPayment.getAmount_payment().toString()) + "万元；");
            });
            infoCurrent.append("分包付款：" + JSON.toJSONString(map).substring(2, JSON.toJSONString(map).length() - 2).replace("\",\"", "").replace("\"", "") + "\n");
        }
        // 其他支出
        List<ZH_ExpendMoney> zh_expendMoneyList = getExpendInfoBySpeedMonth(k, projectId, "current");
        if (zh_expendMoneyList.size() > 0) {
            Map map = new HashMap();
            zh_expendMoneyList.forEach(zh_expendMoney -> {
                map.put(zh_expendMoney.getExpend_type(), removeZero(zh_expendMoney.getAmount_expend().toString()) + "万元；");
            });
            infoCurrent.append("其他支出：" + JSON.toJSONString(map).substring(2, JSON.toJSONString(map).length() - 2).replace("\",\"", "").replace("\"", ""));
        }
        return infoCurrent.toString();
    }

    // 下月计划支出详情
    private String infoNextDetail(BigDecimal nextPlanExpenditureSum, BigDecimal k, String projectId) {
        StringBuilder infoNext = new StringBuilder();
        // 下月预计支出
        infoNext.append("下月计划支出：" + removeZero(nextPlanExpenditureSum.toString()) + "万元。\n");
        // 分包付款
        List<ZH_DisBursementPlan> zh_disBursementPlanList = getDisBursementPlanByPayMonth(k, projectId);
        if (zh_disBursementPlanList.size() > 0) {
            Map map = new HashMap();
            zh_disBursementPlanList.forEach(zh_disBursementPlan -> {
                map.put(zh_disBursementPlan.getContract_name(), removeZero(zh_disBursementPlan.getEstimate_payment().toString()) + "万元；");
            });
            infoNext.append("分包付款：" + JSON.toJSONString(map).substring(2, JSON.toJSONString(map).length() - 2).replace("\",\"", "").replace("\"", "") + "\n");
        }
        // 其他支出
        List<ZH_ExpendMoney> zh_expendMoneyList1 = getExpendInfoBySpeedMonth(k, projectId, "next");
        if (zh_expendMoneyList1.size() > 0) {
            Map map = new HashMap();
            zh_expendMoneyList1.forEach(zh_expendMoney -> {
                map.put(zh_expendMoney.getExpend_type(), removeZero(zh_expendMoney.getAmount_expend().toString()) + "万元；");
            });
            infoNext.append("其他支出：" + JSON.toJSONString(map).substring(2, JSON.toJSONString(map).length() - 2).replace("\",\"", "").replace("\"", ""));
        }
        return infoNext.toString();
    }

    // 下月计划支出详情
    private String infoNextDetail2(BigDecimal nextPlanExpenditureSum, BigDecimal k, String projectId) {
        StringBuilder infoNext = new StringBuilder();
        // 下月预计支出
        infoNext.append("下月计划支出：" + removeZero(nextPlanExpenditureSum.toString()) + "万元。\n");
        // 分包付款
        List<ZH_DisBursementPlan> zh_disBursementPlanList = getDisBursementPlanByPayMonth(changeMonth(k, +1), projectId);
        if (zh_disBursementPlanList.size() > 0) {
            Map map = new HashMap();
            zh_disBursementPlanList.forEach(zh_disBursementPlan -> {
                map.put(zh_disBursementPlan.getContract_name(), removeZero(zh_disBursementPlan.getEstimate_payment().toString()) + "万元；");
            });
            infoNext.append("分包付款：" + JSON.toJSONString(map).substring(2, JSON.toJSONString(map).length() - 2).replace("\",\"", "").replace("\"", "") + "\n");
        }
        // 其他支出
        List<ZH_ExpendMoney> zh_expendMoneyList1 = getExpendInfoBySpeedMonth(k, projectId, "next");
        if (zh_expendMoneyList1.size() > 0) {
            Map map = new HashMap();
            zh_expendMoneyList1.forEach(zh_expendMoney -> {
                map.put(zh_expendMoney.getExpend_type(), removeZero(zh_expendMoney.getAmount_expend().toString()) + "万元；");
            });
            infoNext.append("其他支出：" + JSON.toJSONString(map).substring(2, JSON.toJSONString(map).length() - 2).replace("\",\"", "").replace("\"", ""));
        }
        return infoNext.toString();
    }

    /**
     * 去掉小数点后无意义的 0
     */
    public static String removeZero(String param) {
        BigDecimal value = new BigDecimal(param);
        BigDecimal noZeros = value.stripTrailingZeros();
        return noZeros.toPlainString();
    }
}