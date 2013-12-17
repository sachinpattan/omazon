/**
 * 
 */
package com.omazan.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.omazan.hbm.Order;
import com.omazan.util.HibernateUtil;

/**
 * @author Banashri
 *
 */
public class OrderManagedBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String SUCCESS = "success";
	private static final String ERROR = "error";
	
	private List<Order> ordersList = new ArrayList<Order>();
	
	/**
	 * @return the ordersList
	 */
	public List<Order> getOrdersList() {
		return ordersList;
	}

	/**
	 * @param ordersList the ordersList to set
	 */
	public void setOrdersList(List<Order> ordersList) {
		this.ordersList = ordersList;
	}

	public String getAllOrders() {
		
		System.out.println("Called: getAllOrders()");
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Query q = session.createQuery("from Order order where order.status='pending'");
		List<Order> result = (List<Order>)q.list(); 
		
		setOrdersList(result);
		
		System.out.println(result);
		return SUCCESS;
	}
	
	public List getOrdersOfUser(String userId) {
		
		return ordersList;
	}
	
	public List getOrdersWithStatus(String status) {
		return ordersList;
	}
}
