package com.heima;

import io.mycat.config.model.rule.RuleAlgorithm;
import io.mycat.route.function.AbstractPartitionAlgorithm;

/**
 *自定义多字段算法计算
 */
public class HeiMaBurstRuleAlgorithm  extends AbstractPartitionAlgorithm implements RuleAlgorithm {

    private Long volume;
    private Integer step;
    private Integer mod;


    /**
     *
     * @param columnValue   3-2 1-2
     * @return
     * 分片id = （dataId/volume） * step +分表ID/mod
     */
    @Override
    public Integer calculate(String columnValue) {
        if(columnValue != null){
            String[] split = columnValue.split("-");
            if(split.length == 2){
                try {
                    Long dataId = Long.valueOf(split[0]);
                    Long burstId = Long.valueOf(split[1]);
                    int group = (int) ((dataId/volume)*step);
                    int pos = (int) (group+(burstId/mod));
                    System.out.println("这是 columnValue" +columnValue+"---"+pos);
                    return pos;
                }catch(Exception e){
                    System.out.println("这是 columnValue" +columnValue+"---"+e.getMessage());
                }
            }
        }
        return new Integer(0);
    }

    /**
     * 范围计算
     * @param beginValue
     * @param endValue
     * @return
     */
    @Override
    public Integer[] calculateRange(String beginValue, String endValue) {
        if(beginValue!=null&&endValue!=null){
            Integer begin = calculate(beginValue);
            Integer end = calculate(endValue);
            if(begin == null || end == null){
                return new Integer[0];
            }
            if (end >= begin) {
                int len = end - begin + 1;
                Integer[] re = new Integer[len];
                for (int i = 0; i < len; i++) {
                    re[i] = begin + i;
                }
                return re;
            }
        }
        return new Integer[0];
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public void setMod(Integer mod) {
        this.mod = mod;
    }
}
