ALTER TABLE `retailer`.`discounttemplate` DROP COLUMN `value`;
ALTER TABLE `retailer`.`discounttemplate` CHANGE COLUMN `rate` `value` DOUBLE NOT NULL ;
update retailer.discounttemplate set type = 1 where id > 0;