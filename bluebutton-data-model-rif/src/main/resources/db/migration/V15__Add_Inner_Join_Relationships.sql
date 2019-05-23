/*
 * Alters the Beneficiaries History table to add foreign key to Beneficiary 
 * 
 * See:
 * * https://jira.cms.gov/browse/BLUEBUTTON-620
 */

alter table "BeneficiariesHistory" add column "beneHistoryParentBeneficiary" varchar(15);

alter table "BeneficiariesHistory" 
   add constraint "BeneficiariesHistory_beneHistoryParentBeneficiary_to_Beneficiary" 
   foreign key ("beneHistoryParentBeneficiary") 
   references "Beneficiaries";
   
alter table "MedicareBeneficiaryIdHistory" add column "mbiParentBeneficiary" varchar(15);
   
alter table "MedicareBeneficiaryIdHistory" 
   add constraint "MedicareBeneficiaryIdHistory_mbiParentBeneficiary_to_Beneficiary" 
   foreign key ("mbiParentBeneficiary") 
   references "Beneficiaries";

