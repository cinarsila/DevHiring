package com.trendyol.testcasews.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "link_collection")
public class LinkCollection {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "url")
	private String url;

	@Column(name = "deep_link")
	private String deepLink;

	public LinkCollection() {
	}

	public LinkCollection(String url, String deepLink) {
		this.url = url;
		this.deepLink = deepLink;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDeepLink() {
		return deepLink;
	}

	public void setDeepLink(String deepLink) {
		this.deepLink = deepLink;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LinkCollection that = (LinkCollection) o;
		return id.equals(that.id) &&
				Objects.equals(url, that.url) &&
				Objects.equals(deepLink, that.deepLink);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, url, deepLink);
	}

	@Override
	public String toString() {
		return String.format("LinkCollection{id=%d, url='%s', deep-link=%s}", id, url, deepLink);
	}
}
