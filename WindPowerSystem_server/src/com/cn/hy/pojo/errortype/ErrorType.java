package com.cn.hy.pojo.errortype;

public class ErrorType {

		private int id;
		private String errorname;
		private int parent_id;
		private String createtime;
		private int create_user;
		private String updatetime;
		private int update_user;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getErrorname() {
			return errorname;
		}
		public void setErrorname(String errorname) {
			this.errorname = errorname;
		}
		public int getParent_id() {
			return parent_id;
		}
		public void setParent_id(int parent_id) {
			this.parent_id = parent_id;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public int getCreate_user() {
			return create_user;
		}
		public void setCreate_user(int create_user) {
			this.create_user = create_user;
		}
		public String getUpdatetime() {
			return updatetime;
		}
		public void setUpdatetime(String updatetime) {
			this.updatetime = updatetime;
		}
		public int getUpdate_user() {
			return update_user;
		}
		public void setUpdate_user(int update_user) {
			this.update_user = update_user;
		}
		
		
}
