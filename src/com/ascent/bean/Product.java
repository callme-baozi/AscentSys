package com.ascent.bean;

/**
 * ʵ����Product������������Ʒ����Ϣ��
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Product implements Comparable, java.io.Serializable {

	private String productname; // ҩƷ����

	private String cas; // ��ѧ��ժ�ǼǺ�

	private String structure; // �ṹͼ����

	private String formula; // ��ʽ

	private String price; // �۸�

	private String realstock; // ����

	private String category; // ���

	private int number;// 兼顾购物车的药品数量
	
	/**
	 * Ĭ�Ϲ��췽��
	 */
	public Product() {
	}

	/**
	 * �����в������췽��
	 * @param productName ҩƷ����
	 * @param cas ��ѧ��ժ�ǼǺ�
	 * @param structure �ṹͼ����
	 * @param formula ��ʽ
	 * @param price �۸�
	 * @param realstock ����
	 * @param category ���
	 */
	public Product(String productName, String cas, String structure,
			String formula, String price, String realstock, String category) {
		this.productname = productName;
		this.structure = structure;
		this.formula = formula;
		this.price = price;
		this.realstock = realstock;
		this.cas = cas;
		this.category = category;
	}

	/**
	 * @return the cas
	 */
	public String getCas() {
		return cas;
	}

	/**
	 * @param cas the cas to set
	 */
	public void setCas(String cas) {
		this.cas = cas;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the productname
	 */
	public String getProductname() {
		return productname;
	}

	/**
	 * @param productname the productname to set
	 */
	public void setProductname(String productname) {
		this.productname = productname;
	}

	/**
	 * @return the realstock
	 */
	public String getRealstock() {
		return realstock;
	}

	/**
	 * @param realstock the realstock to set
	 */
	public void setRealstock(String realstock) {
		this.realstock = realstock;
	}

	/**
	 * @return the structure
	 */
	public String getStructure() {
		return structure;
	}

	/**
	 * @param structure the structure to set
	 */
	public void setStructure(String structure) {
		this.structure = structure;
	}

	@Override
	public String toString() {
		return "Product{" +
				"productname='" + productname + '\'' +
				", category='" + category + '\'' +
				'}';
	}

	public int compareTo(Object o) {
		Product product = (Product) o;
		return this.getProductname().compareTo(product.getProductname());
	}

	public void setNumber(int number){
		this.number=number;
	}

	public int getNumber() {
		return number;
	}
}
