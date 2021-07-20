package djjy.filemange;

import java.util.List;

public class File {

    private int code;
    private String message;
    private djjy.filemange.File.DataDTO data;

    public File(String savePath, String filePath) {

    }


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

    public djjy.filemange.File.DataDTO getData() {
        return data;
    }

    public void setData(djjy.filemange.File.DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private int total;
        private List<djjy.filemange.File.DataDTO.RowsDTO> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<djjy.filemange.File.DataDTO.RowsDTO> getRows() {
            return rows;
        }

        public void setRows(List<djjy.filemange.File.DataDTO.RowsDTO> rows) {
            this.rows = rows;
        }

        public static class RowsDTO {
            private int id;
            private String file_name;
            private String file_type;
            private String file_size;
            private int user_id;
            private int file_score;
            private String file_path;
            private String file_content;
            private String create_time;
            private String update_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setFile_name(String file_name) {
                this.file_name = file_name;
            }

            public String getFile_name() {
                return file_name;
            }

            public void setFile_type(String file_type) {
                this.file_type = file_type;
            }

            public String getFile_type() {
                return file_type;
            }

            public void setFile_size(String file_size) {
                this.file_size = file_size;
            }

            public String getFile_size() {
                return file_size;
            }

            public void setFile_path(String file_path) {
                this.file_path = file_path;
            }

            public String getFile_path() {
                return file_path;
            }
        }
    }
}

