package com.lv.testlv.model;

public class LeanCloudModel {
	public String login_id;
	public String conversation_id;
	public String conversation_name;
	public String conversation_date;
	public String conversation_type;
	public String conversation_content;
	public String conversation_send_id;

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}

	public String getConversation_id() {
		return conversation_id;
	}

	public void setConversation_id(String conversation_id) {
		this.conversation_id = conversation_id;
	}

	public String getConversation_name() {
		return conversation_name;
	}

	public void setConversation_name(String conversation_name) {
		this.conversation_name = conversation_name;
	}

	public String getConversation_date() {
		return conversation_date;
	}

	public void setConversation_date(String conversation_date) {
		this.conversation_date = conversation_date;
	}

	public String getConversation_type() {
		return conversation_type;
	}

	public void setConversation_type(String conversation_type) {
		this.conversation_type = conversation_type;
	}

	public String getConversation_content() {
		return conversation_content;
	}

	public void setConversation_content(String conversation_content) {
		this.conversation_content = conversation_content;
	}

	public String getConversation_send_id() {
		return conversation_send_id;
	}

	public void setConversation_send_id(String conversation_send_id) {
		this.conversation_send_id = conversation_send_id;
	}

	@Override
	public String toString() {
		return "LeanCloudModel{" +
				"login_id='" + login_id + '\'' +
				", conversation_id='" + conversation_id + '\'' +
				", conversation_name='" + conversation_name + '\'' +
				", conversation_date='" + conversation_date + '\'' +
				", conversation_type='" + conversation_type + '\'' +
				", conversation_content='" + conversation_content + '\'' +
				", conversation_send_id='" + conversation_send_id + '\'' +
				'}';
	}
}
