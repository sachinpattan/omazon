package com.omazan.hbm;

public class Shipment {
	private int shipmentId;
	private int orderId;
	private String shipmentPosition;
	
	public int getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(int shipmentId) {
		this.shipmentId = shipmentId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getShipmentPosition() {
		return shipmentPosition;
	}
	public void setShipmentPosition(String shipmentPosition) {
		this.shipmentPosition = shipmentPosition;
	}
	
	
}
