/*
 * Alters the Beneficiaries History table to add foreign key to Beneficiary 
 * 
 * See:
 * * https://jira.cms.gov/browse/BLUEBUTTON-620
 */

alter table "BeneficiariesHistory" add column "parentBeneficiary" varchar(255);

alter table "BeneficiariesHistory" 
   add constraint "BeneficiariesHistory_parentBeneficiary_to_Beneficiary" 
   foreign key ("parentBeneficiary") 
   references "Beneficiaries";

