package cn.safe6.core.jobs;

import java.util.concurrent.Callable;

/**
 *  demo 请勿删除
 */

public class Job implements Callable<String> {
    private String target;
    private String cve;

    public Job(String target, String cve) {
        this.target = target;
        this.cve = cve;
    }



    @Override
    public String call() throws Exception {
        String isVul = "";
//        System.out.println("线程:" + this.target + " -> 运行...");
/*        if (this.checkAllExp()) {
            isVul = "存在";
        } else {
            isVul = "不存在";
        }*/
//        System.out.println("线程:" + this.target + " -> 结束.");

        return isVul;
    }
}
