package com.shuishou.retailer.common.models;

import java.io.Serializable;
import java.util.List;

public interface IDiscountTemplateDataAccessor {

	List<DiscountTemplate> queryDiscountTemplates();
	
	DiscountTemplate getDiscountTemplateById(int id);
	
	Serializable insertDiscountTemplate(DiscountTemplate discountTemplate);
	
	void updateDiscountTemplate(DiscountTemplate discountTemplate);
	
	void deleteDiscountTemplate(DiscountTemplate discountTemplate);
}
