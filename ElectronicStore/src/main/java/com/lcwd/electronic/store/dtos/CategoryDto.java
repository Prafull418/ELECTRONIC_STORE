package com.lcwd.electronic.store.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

	
		private String categoryId;
		
		@NotBlank(message = "Title required !!")
		@Size(min = 4, message = "Title must be minimum 4 characters !!")
		private String title;
		
		@NotBlank(message = "Description is required !!")
		private String description;
		
		@NotBlank(message = "Cover image is required !!")
		private String coverImage;
		
		
}
