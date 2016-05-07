package rating_model;

public class Review {
	private String reviewContent;
	private String id = "";
	private int polarity = -2;	//-2 means unset
	private String source = "weibo";
	
	public Review(){}
	public Review(String id,String reviewContent) {
		this.reviewContent = reviewContent;
		this.id = id;
	}

	public void setContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setPolarity(int polarity) {
		this.polarity = polarity;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	
	public String getReviewContent() {
		return reviewContent;
	}
	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}

	public String toXML() {
		return "\t<"+ source + " id=\"" + id +
				"\" polarity=\"" + polarity + "\">" +
				reviewContent + "</" + source + ">\n";
	}
}
