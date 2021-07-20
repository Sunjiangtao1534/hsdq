package login;

import java.util.List;

public class UserInfo {

    private int code;
    private String message;
    private DataDTO data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private int total;
        private List<RowsDTO> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RowsDTO> getRows() {
            return rows;
        }

        public void setRows(List<RowsDTO> rows) {
            this.rows = rows;
        }

        public static class RowsDTO {
            private int id;
            private int staff_id;
            private String userdesc;
            private String username;
            private String userpwd;
            private String status;
            private String create_time;
            private String update_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStaff_id() {
                return staff_id;
            }

            public void setStaff_id(int staff_id) {
                this.staff_id = staff_id;
            }

            public String getUserdesc() {
                return userdesc;
            }

            public void setUserdesc(String userdesc) {
                this.userdesc = userdesc;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUserpwd() {
                return userpwd;
            }

            public void setUserpwd(String userpwd) {
                this.userpwd = userpwd;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }
        }
    }
}
