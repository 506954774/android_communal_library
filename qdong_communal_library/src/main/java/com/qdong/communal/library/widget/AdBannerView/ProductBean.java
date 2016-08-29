package com.qdong.communal.library.widget.AdBannerView;

import java.io.Serializable;
/**
 *
 * @author Chuck
 *
 */

public class ProductBean implements Serializable{

    private static final long serialVersionUID = 19840902L;
	
	public ProductBean(){}
	

    private int id;
    
    private String name;
	
	private String image_url;
	
	private String link_url;
	
    private int status_code = -1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getLink_url() {
		return link_url;
	}

	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

    
	
}
