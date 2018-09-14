/*
 * Creates the beneficiary ID history table required by the application. This
 * schema is derived from the JPA metadata, via the 
 * gov.hhs.cms.bluebutton.data.pipeline.rif.schema.HibernateSchemaPrinter 
 * utility.
 */

${logic.tablespaces-escape} SET default_tablespace = fhirdb_ts2;

--create table "Claims" (
--  "claimId" varchar(15) not null,
--  "finalAction" char(1) not null,
--  constraint "Claims_pkey" primary key ("claimId")
--);

create table "FakeSupers" (
  "id" integer not null,
  "group" varchar(64) not null,
  constraint "FakeSupers_pkey" primary key ("id")
);

create table "FakeChildA" (
  "id" integer not null,
  "data" varchar(64) not null,
  constraint "FakeChildA_pkey" primary key ("id")
);

create table "FakeChildB" (
  "id" integer not null,
  "data" varchar(64) not null,
  constraint "FakeChildB_pkey" primary key ("id")
);