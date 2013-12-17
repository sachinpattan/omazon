package com.omazan.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.omazan.hbm.Address;
import com.omazan.hbm.Order;
import com.omazan.hbm.Orderedproducts;
import com.omazan.hbm.Product;
import com.omazan.hbm.User;
import com.omazan.util.EmailFormatValidator;
import com.omazan.util.HibernateUtil;
import com.omazan.util.PasswordEncryption;

/**
 * @author Sachin Pattan
 * @since 30 Oct 2013
 * @version 1.0.0
 * 
 */
@SessionScoped
public class UserManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String SUCCESS = "success";
	private static final String ERROR = "error";
	private String errorMessage;
	private String title;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String type;
	private int mobile;

	private String message;
	private Address address = null;
	private List<Product> products;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String save() {
		return saveUsers();
	}

	public String login() {
		return SUCCESS;
	}

	@SuppressWarnings("unused")
	public String updateUsers() {

		String result = null;
		User user = (User) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("user");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(user);
			tx.commit();
			result = SUCCESS;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();

				// Message error = new MessageImpl(1,"Same Email Address",null);
			}

			result = ERROR;
			e.printStackTrace();

		} finally {
			session.close();
		}
		return result;
	}

	private String saveUsers() {
		String result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();

		address = new Address();
		User user = new User();
		user.setTitle(this.title);
		user.setFirstName(this.getFirstName());
		user.setLastName(this.getLastName());
		if (EmailFormatValidator.validate(this.getEmail())) {
			result = ERROR;
			errorMessage="Invalid Email Format! please enter a valid email ";
			return result;

		}
		user.setEmail(this.getEmail());
		user.setPassword(PasswordEncryption.encrypt(this.getPassword()));
		user.setType("C");
		user.setMobile(this.getMobile());
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			result = SUCCESS;
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put("user", user);
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();

				// Message error = new MessageImpl(1,"Same Email Address",null);
			}

			result = ERROR;
			e.printStackTrace();

		} finally {
			session.close();
		}
		return result;
	}

	public List<User> getUsers() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		@SuppressWarnings("unchecked")
		List<User> userList = session.createCriteria(User.class).list();
		return userList;
	}

	public String reset() {
		return null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMobile() {
		return mobile;
	}

	public void setMobile(int mobile) {
		this.mobile = mobile;
	}

	@SuppressWarnings("unchecked")
	public List<Product> getProducts() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria productCriteria = session.createCriteria(Product.class);
		products = productCriteria.list();
		return products;

	}

	public void setProducts(List<Product> products) {

		this.products = products;
	}

	public String placeOrder() {
		String result = null;
		User currentUser = (User) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("user");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		Order order = new Order();
		Address address = getAddress();
		address.setAddressType("UserAddress");
		address.setUserId(currentUser.getUserId());
		order.setUserId(currentUser.getUserId());
		order.setStatus("pending");
		order.setAddressId(address.getAddressId());
		Transaction tx = session.beginTransaction();
		session.save(address);
		order.setAddressId(address.getAddressId());
		session.save(order);
		// List<Orderedproducts> orderedProductsList = new
		// ArrayList<Orderedproducts>();
		for (Product product : this.products) {
			if (product.isProductOrdered()) {

				Orderedproducts orderedProducts = new Orderedproducts();
				orderedProducts.setOrderId(order.getOrderId());
				orderedProducts.setProductId(product.getProductId());
				session.save(orderedProducts);
			}
		}

		try {
			tx.commit();
			result = SUCCESS;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				result = ERROR;
				e.printStackTrace();
			}
		} finally {
			session.close();
		}
		return result;

	}

	public Address getAddress() {

		if (address == null) {
			Session session = HibernateUtil.getSessionFactory().openSession();

			address = new Address();
			session.close();
		}
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getOrderDetails() {

		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		int productId = Integer.parseInt(params.get("productId"));

		return "Success";

	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}