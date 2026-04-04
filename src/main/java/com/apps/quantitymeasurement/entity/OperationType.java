package com.apps.quantitymeasurement.entity;

public enum OperationType {
	ADD,
	SUBTRACT,
	MULTIPLY,
	DIVIDE,
	COMPARISON,
	CONVERT;
	
	public String getDisplayName() {
		return this.name().toLowerCase();
	}
}
