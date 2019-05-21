/*
 * Alters the Beneficiaries History table to add foreign key to Beneficiary 
 * 
 * See:
 * * https://jira.cms.gov/browse/BLUEBUTTON-620
 */


alter table "Beneficiaries" add column "beneficiaryHistories" varchar(255)
alter table "Beneficiaries" add column "medicareBeneficiaryIdHistories" varchar(255)

alter table "BeneficiariesHistory" add column "parentBeneficiary" varchar(255);

alter table "BeneficiariesHistory" 
   add constraint "BeneficiariesHistory_beneHistoryParentBeneficiary_to_Beneficiary" 
   foreign key ("beneHistoryParentBeneficiary") 
   references "Beneficiaries";
   
alter table "MedicareBeneficiaryIdHistory" add column "parentBeneficiary" varchar(255);
   
alter table "MedicareBeneficiaryIdHistory" 
   add constraint "MedicareBeneficiaryIdHistory_mbiParentBeneficiary_to_Beneficiary" 
   foreign key ("parentBeneficiary") 
   references "Beneficiaries";

