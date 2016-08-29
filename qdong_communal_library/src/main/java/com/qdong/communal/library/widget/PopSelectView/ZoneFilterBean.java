package com.qdong.communal.library.widget.PopSelectView;

import java.io.Serializable;

public class ZoneFilterBean implements Serializable {

	private static final long serialVersionUID = 19840902L;

	private int id;
	private String name;
	private boolean isChecked;

	public ZoneFilterBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ZoneFilterBean [id=" + id + ", name=" + name + ", isChecked="
				+ isChecked + "]";
	}

}
