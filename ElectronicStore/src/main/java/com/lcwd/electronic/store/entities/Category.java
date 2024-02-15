package com.lcwd.electronic.store.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

	@Id
	@Column(name = "id")
	private String categoryId;
	
	@Column(name = "category_title",length = 60)
	private String title;
	
	@Column(name = "category_desc",length = 500)
	private String description;
	
	
	private String coverImage;
	
	/*
	 * @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch =
	 * FetchType.LAZY) private List<Product> products = new ArrayList<>();
	 */
	
}
