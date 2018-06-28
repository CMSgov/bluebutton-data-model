/*
 * Creates the dataload history table required by the application. This
 * schema is derived from the JPA metadata, via the 
 * gov.hhs.cms.bluebutton.data.pipeline.rif.schema.HibernateSchemaPrinter 
 * utility.
 */

${logic.tablespaces-escape} SET default_tablespace = fhirdb_ts2;

create table "RifDataloadHistory" (
  "recordType" varchar(25) not null,
  "createUpdateTimestamp" timestamp not null,
  "sequenceId" numeric not null,
  "dataloadFilename" varchar(64) not null,
  constraint "RifDataload_History_pkey" primary key ("recordType", "createUpdateTimestamp", "sequenceId")
)
${logic.tablespaces-escape} tablespace "rifdataloadhistory_ts"
;
