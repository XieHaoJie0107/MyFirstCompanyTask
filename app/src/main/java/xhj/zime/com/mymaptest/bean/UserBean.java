package xhj.zime.com.mymaptest.bean;

public class UserBean {

    /**
     * code : 1
     * msg : 请求成功
     * data : {"userId":26,"userName":"李玉","loginName":"liyu","password":"123456","org":"设备运检室","group":"设备巡检组","groupName":"三班"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : 26
         * userName : 李玉
         * loginName : liyu
         * password : 123456
         * org : 设备运检室
         * group : 设备巡检组
         * groupName : 三班
         */

        private int userId;
        private String userName;
        private String loginName;
        private String password;
        private String org;
        private String group;
        private String groupName;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getOrg() {
            return org;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
    }
}
