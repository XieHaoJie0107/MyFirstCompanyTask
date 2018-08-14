package xhj.zime.com.mymaptest.bean;

import java.util.List;

public class DataBean {
    private List<TaskBeansBean> taskBeans;
    private List<TaskPointBeansBean> taskPointBeans;
    private List<?> flawBeans;
    private List<?> adjunctBeans;

    public List<TaskBeansBean> getTaskBeans() {
        return taskBeans;
    }

    public void setTaskBeans(List<TaskBeansBean> taskBeans) {
        this.taskBeans = taskBeans;
    }

    public List<TaskPointBeansBean> getTaskPointBeans() {
        return taskPointBeans;
    }

    public void setTaskPointBeans(List<TaskPointBeansBean> taskPointBeans) {
        this.taskPointBeans = taskPointBeans;
    }

    public List<?> getFlawBeans() {
        return flawBeans;
    }

    public void setFlawBeans(List<?> flawBeans) {
        this.flawBeans = flawBeans;
    }

    public List<?> getAdjunctBeans() {
        return adjunctBeans;
    }

    public void setAdjunctBeans(List<?> adjunctBeans) {
        this.adjunctBeans = adjunctBeans;
    }
}
