
INSERT INTO `institution_type`(`institution_type_id`,`institution_type_name`) VALUES 
(1,'NIBSS'),
(2,'BANK'),
(3,'THIRD PARTY');
INSERT INTO `role` (`role_id`,`flag`,`role_name`,`description`,`institution_type`,`parent_role_id`) VALUES
 (1,'E','view-institution','Institution',1,NULL),
 (2,'E','create-institution','Create Institution',1,1),
 (3,'E','edit-institution','Edit Institution',1,1),
 (4,'E','delete-institution','Delete Institution',1,1),
 
 (5,'E','view-branch','Branch',NULL,NULL),
 (6,'E','create-branch','Create Branch',NULL,5),
 (7,'E','edit-branch','Edit Branch',NULL,5),
 (8,'E','delete-branch','Delete Branch',NULL,5),
 
 (9,'E','view-user-role','View User Role',1,NULL),
 (10,'E','create-user-role','Create User Role',1,9),
 (11,'E','edit-user-role','Edit User Role',1,9),
 (12,'E','delete-user-role','Delete User Role',1,9),
 
 (13,'E','view-user','User',NULL,NULL),
 (14,'E','create-user','Create User',NULL,13),
 (15,'E','edit-user','Edit',NULL,13),
 (16,'E','delete-user','Delete',NULL,13),
 
 (17,'E','view-transaction','Transaction',NULL,NULL),
 (18,'E','view-transaction-report','Transaction Report',NULL,NULL),
 (19,'E','settings','Settings',1,NULL),
 (20,'E','activity-log','Activity Log',NULL,NULL);


INSERT INTO `institution` (`institution_id`,`flag`,`institution_name`,`institution_code`,`institution_type_id`,`institution_type`) VALUES 
(1,'E','NIBSS','999',1,1),
(2,'E','UBA NIGERIA','UBA',2,2),
(3,'E','FIRST BANK PLC','FBN',2,2),
(4,'E','KEYSTONE BANK PLC','KYS',2,2),
(5,'E','UNION BANK PLC','UBN',2,2),
(6,'E','FIDELITY BANK','FDB',2,2),
(7,'E','ZENITH BANK','ZBN',2,2),
(8,'E','GT BANK','GTB',2,2),
(9,'E','INTERCONTINENTAL BANK','ICT',2,2),
(10,'E','ACCESS BANK','ACB',2,2),
(11,'E','WEMA BANK','WBN',2,2),
(12,'E','StanbicIBTC ','IBT',2,2),
(13,'E','DIAMOND BANK','DBN',2,2),
(14,'E','FIRSTINLAND BANK','FIB',2,2),
(15,'E','AFRI BANK','AFB',2,2),
(16,'E','EQUITORIAL BANK','ETB',2,2),
(17,'E','ECO BANK','ECB',2,2),
(18,'E','FIRST CITY MONUMENT BANK','FCB',2,2),
(19,'E','SPRING BANK','SPB',2,2),
(20,'E','CITI BANK','CTB',2,2),
(21,'E','OCEANIC BANK','OBN',2,2),
(22,'E','SKYE BANK','SKB',2,2),
(23,'E','STERLING BANK','STB',2,2),
(24,'E','STANDARD CHARTERED','SCB',2,2),
(25,'E','UNITY BANK','UTB',2,2);

INSERT INTO `bank` (`institution_id`,`bank_code`) VALUES 
(2,'033'),
(3,'011'),
(4,'082'),
(5,'032'),
(6,'070'),
(7,'057'),
(8,'058'),
(9,'069'),
(10,'044'),
(11,'035'),
(12,'221'),
(13,'063'),
(14,'085'),
(15,'014'),
(16,'040'),
(17,'050'),
(18,'214'),
(19,'084'),
(20,'023'),
(21,'056'),
(22,'076'),
(23,'232'),
(24,'068'),
(25,'215');


INSERT INTO `status` (`status_id`,`description`,`status_type`,`status_code`) VALUES
(1,'Login','A',1),
(2,'Logout','A',2),
(3,'Create','A',3),
(4,'Edit','A',4),
(5,'Delete','A',5);


INSERT INTO `user_role` (`user_role_id`,`flag`,`role_config` ,`user_role_name` ,`institution_type`) VALUES 
(1,'E','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20','NIBSS ADMIN',1),
(2,'E','5,6,7,8,13,14,15,16,17,18,19,20','BANK ADMIN',2);

INSERT INTO `branch` (`branch_id`,`branch_code`,`branch_name`,`created_date`,`flag`,`location`,`institution`) VALUES 
(1,'999','NIBSS','2014-02-25 00:41:19','E','Plot 1230, Ahmadu Bello Way',1);

INSERT INTO `user_identity` (`user_id`,`access_count`,`created_by`,`created_date`,`email`,`first_accessed_date`,`first_name`,
`flag`,`last_accessed_date`,`last_name`,`last_password_change_date`,`password`,`branch`,`user_role`) VALUES 
(1,0,NULL,'2014-02-25 00:41:19','seun.ogunjimi@gmail.com',NULL,'ADMIN','E',NULL,'NIBSS',NULL,'/VlEFP+mbWYQm4et+sSNtQ==',1,1);

INSERT INTO transaction_report_type (`DESCRIPTION`, `report_type_code`,`report_type_name`,`institution_type_config`,`flag`) 
	VALUES ('Destination (Inward) Bank Report', 1,'inwards report','2,3','E');
INSERT INTO transaction_report_type (`DESCRIPTION`, `report_type_code`,`report_type_name`,`institution_type_config`,`flag`) 
	VALUES ('Source (Outward) Bank Report', 2,'outwards report','2,3','E');
INSERT INTO transaction_report_type (`DESCRIPTION`, `report_type_code`,`report_type_name`,`institution_type_config`,`flag`) 
	VALUES ('Inwards Successful', 3,'inwards successful','2,3','E');
INSERT INTO transaction_report_type (`DESCRIPTION`, `report_type_code`,`report_type_name`,`institution_type_config`,`flag`) 
	VALUES ('Outwards Successful', 4,'outwards successful','2,3','E');
INSERT INTO transaction_report_type (`DESCRIPTION`, `report_type_code`,`report_type_name`,`institution_type_config`,`flag`) 
	VALUES ('Inwards Unsuccessful', 5,'inwards unsuccessful','2,3','E');
INSERT INTO transaction_report_type (`DESCRIPTION`, `report_type_code`,`report_type_name`,`institution_type_config`,`flag`) 
	VALUES ('Outwards Unsuccessful', 6,'outwards unsuccessful','2,3','E');
INSERT INTO transaction_report_type (`DESCRIPTION`, `report_type_code`,`report_type_name`,`institution_type_config`,`flag`) 
	VALUES ('Transaction Queue (Inwards)', 7,'inwards txnlog','2,3','E');
INSERT INTO transaction_report_type (`DESCRIPTION`, `report_type_code`,`report_type_name`,`institution_type_config`,`flag`) 
	VALUES ('Transaction Queue (Outwards)', 8,'outwards txnlog','2,3','E');
INSERT INTO transaction_report_type (`DESCRIPTION`, `report_type_code`,`report_type_name`,`institution_type_config`,`flag`) 
	VALUES ('Bank Daily Activity Report', 9,'daily activity','2,3','E');


INSERT INTO channel (`CHANNEL_Code`, `description`) 
	VALUES ('1', 'BANK TELLER');
INSERT INTO channel (`CHANNEL_Code`, `description`) 
	VALUES ('2', 'INTERNET BANKING');
INSERT INTO channel (`CHANNEL_Code`, `description`) 
	VALUES ('3', 'MOBILE PHONE');
INSERT INTO channel (`CHANNEL_Code`, `description`) 
	VALUES ('4', 'POS TERMINALS');
INSERT INTO channel (`CHANNEL_Code`, `description`) 
	VALUES ('5', 'ATM');
INSERT INTO channel (`CHANNEL_Code`, `description`) 
	VALUES ('6', 'VENDOR MERCHANT WEB PORTAL');
INSERT INTO channel (`CHANNEL_Code`, `description`) 
	VALUES ('7', 'THIRD-PARTY PAYMENT PLATFORM');
INSERT INTO channel (`CHANNEL_Code`, `description`) 
	VALUES ('8', 'ABC UNIT');


INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('00', 'Approved or Completed Successfully');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('03', 'Invalid Sender');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('05', 'Do not honor');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('06', 'Dormant Account');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('07', 'Invalid Account');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('08', 'Account Name Mismatch');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('09', 'Request Processing In Progress');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('12', 'Invalid Transaction');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('13', 'Invalid Amount');

INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('14', 'invalid Batch Number');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('15', 'Invalid Session ID');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('16', 'Invalid Bank Code');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('17', 'invalid Channel');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('18', 'Wrong Method Call');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('21', 'No Action Taken');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('25', 'Unable To Locate Record');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('26', 'Duplicate Record');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('30', 'Format Error');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('34', 'Suspected Error');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('35', 'Contact Sending Bank');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('51', 'No Sufficient Funds');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('57', 'Transaction Not Permitted To Sender');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('58', 'Transaction Not Permitted On Channel');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('61', 'Transfer Limit Exceeded');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('63', 'Security Violation');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('65', 'Exceeds Withdrawal Frequency');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('68', 'Response Received Too Late');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('91', 'Beneficiary Bank Not Available');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('92', 'Routing Error');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('94', 'Duplicate Transaction');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('96', 'System Malfunction');
INSERT INTO transaction_response (`RESPONSE_CODE`, `description`) 
	VALUES ('97', 'Timeout waiting for response from Destination');


INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Pagatech', 'E', '301', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Eartholeum (Qik Qik)', 'E', '302', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Kegow', 'E', '303', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Stanbic Mobile', 'E', '304', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Paycom', 'E', '305', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('E-Tranzact', 'E', '306', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('EcoMobile', 'E', '307', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Fortis Mobile', 'E', '308', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('FBN M-money', 'E', '309', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Corporetti', 'E', '310', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Parkway (Ready Cash)', 'E', '311', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Monetize', 'E', '312', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('M-Kudi', 'E', '313', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Fets (My Wallet)', 'E', '314', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('GT Mobile Money', 'E', '315', 3, 3);

INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('U-MO', 'E', '316', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Cellulant', 'E', '317', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('ASO Savings And Loans', 'E', '401', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Teasy Mobile', 'E', '319', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('VTNetwork', 'E', '320', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('AccessMobile', 'E', '323', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Hedonmark', 'E', '324', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Zenith Mobile', 'E', '322', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Fortis Microfinance Bank', 'E', '501', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Fidelity Mobile', 'E', '318', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Jubilee Life', 'E', '402', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Moneybox', 'E', '325', 3, 3);
INSERT INTO institution (`institution_name`, `flag`, `institution_code`, `institution_type`, `institution_type_id`) 
	VALUES ('Parrallex', 'E', '502', 3, 3);


INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (26,  '301',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (27,  '302',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (28,  '303',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (29,  '304',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (30,  '305',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (31,  '306',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (32,  '307',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (33,  '308', , 3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (34,  '309',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (35,  '310',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (36,  '311',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (37,  '312',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (38,  '313',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (39,  '314',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (40,  '315',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (41,  '316',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (42,  '317',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (43,  '401',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (44,  '319',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (45,  '320',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (46,  '323',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (47,  '324',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (48,  '322',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (49,  '501',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (50,  '318', 3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (51,  '402',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (52,  '325',  3);
INSERT INTO nip_report.third_party (institution_id,  third_party_code,  settlement_bank) 
	VALUES (53,  '502',  3);





