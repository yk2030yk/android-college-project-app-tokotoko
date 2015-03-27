package com.example.client;

import java.io.Serializable;

public class LargeArea implements Serializable {
	private static final long serialVersionUID = 1L;

	public String name = "";
	public String id = "";

	public LargeArea(String name, String id) {
		this.name = name;
		this.id = id;
	}

}
