package HCHomeServer.model.db;

public class PostPicture {
	private int pictureId;
	private String url;
	private int postId;
	private int order;
	public int getPictureId() {
		return pictureId;
	}
	public void setPictureId(int pictureId) {
		this.pictureId = pictureId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public static PostPicture creatPostPicture(String url, int postId, int order) {
		PostPicture postPicture = new PostPicture();
		postPicture.setUrl(url);
		postPicture.setPostId(postId);
		postPicture.setOrder(order);
		return postPicture;
	}
	
}
