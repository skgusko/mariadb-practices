package bookmall.vo;

public class OrderBookVo {
	private Long orderNo;
	private Long bookNo;
	private int quantity;
	private int price;
	private String bookTitle;
	
	public Long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	public Long getBookNo() {
		return bookNo;
	}
	public void setBookNo(Long bookNo) {
		this.bookNo = bookNo;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getBookTitle() {
		return bookTitle;
	}
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	
	@Override
	public String toString() {
		return "OrderBookVo [orderNo=" + orderNo + ", bookNo=" + bookNo + ", quantity=" + quantity + ", price=" + price
				+ ", bookTitle=" + bookTitle + "]";
	}
	
	
	
}