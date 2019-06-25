# RFC Proposal
[RFC Proposal]: #rfc-proposal

* RFC Proposal ID: `0000-postgresql-on-rds`
* Start Date: 2019-06-11
* RFC PR: [rust-lang/rfcs#0000](https://github.com/rust-lang/rfcs/pull/0000)
* JIRA Ticket(s):
    * [BLUEBUTTON-952](https://jira.cms.gov/browse/BLUEBUTTON-925)
    * [BLUEBUTTON-1014](https://jira.cms.gov/browse/BLUEBUTTON-1014)

This RFC proposes that our team switch away from running PostgreSQL ourselves on EC2 to a managed service offerring, specifically AWS' PostgreSQL-compatible RDS Aurora service.
This switch would best be accomplished as part of our migration into the CMS Cloud Services environment, either prior to or during.

## Table of Contents
[Table of Contents]: #table-of-contents

* [RFC Proposal](#rfc-proposal)
* [Table of Contents](#table-of-contents)
* [Motivation](#motivation)
* [Proposed Solution](#proposed-solution)
    * [Proposed Solution: Detailed Design](#proposed-solution-detailed-design)
    * [Proposed Solution: Unresolved Questions](#proposed-solution-unresolved-questions)
    * [Proposed Solution: Drawbacks](#proposed-solution-drawbacks)
    * [Proposed Solution: Notable Alternatives](#proposed-solution-notable-alternatives)
* [Prior Art](#prior-art)
* [Future Possibilities](#future-possibilities)
* [Addendums](#addendums)

## Motivation
[Motivation]: #motivation

This change would increase our system's reliability and agility.
Most importantly, this would free up operator/staff time to focus more on higher value work.
Given our teams' small size and lack of PostgreSQL expertise, it would also likely improve our services' overall reliability.
In addition, RDS is much more amenable to automation -- and thus likely to be deployed far more consistently,
  which reduces our chances of creating unintended environmental differences and related bugs.
Ultimately, I think the argument for switching to RDS is less "why should we" and more "why _wouldn't_ we?"
Given appropriate testing and verification before making the switch, I can't think of any reasons not to.

As an end result of this change, we can expect comparable or improved performance, with significantly less operator overhead, and dramatically simpler configuration management.

## Proposed Solution
[Proposed Solution]: #proposed-solution

[Amazon's Relational Database Service (RDS)](https://aws.amazon.com/rds/) offerings are basically,
  "click a button and get a PostgreSQL server" -- they're managed services wherein Amazon runs and mostly manages RDS for you.
RDS is offered in several different "flavours": PostgreSQL, MySQL, their "Aurora" product, and others.
These services remove many, though not all, of the hassles of running a database reliably: they manage the OS, patching, and backups.
That leaves us still on the hook to manage: major upgrades, maintenance processes like vaccuuming, and tuning.
For a team as small as ours, this counts as a rather significant win:
  we're freeing our time and energy up to focus on more important things.

One of the most important considerations here is cost: RDS isn't free.
For details, please see the attached
  [RFC postgresql-on-rds Addendum: AWS Pricing](https://docs.google.com/spreadsheets/d/1eeMVqy9xdctcJO80M1JGVqd6apUaza-hfKGtIKLhIdE/edit?usp=sharing)
  spreadsheet, but at a high-level:

* RDS Aurora charges about twice as much for compute.
* RDS Aurora does not charge for extra storage for each compute instance; all instances within a region share the same storage.
    * As storage-per-instance is currently project to be our largest and fastest-growing AWS expense,
      this would provide us with very large savings.
* RDS Aurora also introduces a new charge of $0.20 per 1M I/O operations (both read & write).
  With our projected number of DB queries, this will not be a major cost driver.
    * It could _become_ a major cost driver if our usage wildly exceeds projections.
      But, in that scenario, having the more reliable managed DB-as-a-service backing us becomes an even bigger concern that justifies the cost.

Overall, RDS Aurora will likely save us anywhere from 27% to 71% on our AWS costs, through 2020-06: it's a major win on cost control.

It's worth mentioning that we're actually already leveraging RDS for several things:

* RDS has powered the "old sandbox" all along.
* RDS was also used as the database for the initial load development and benchmarking that was done

This change is an "under the hood" thing only; it does not directly impact external users of our systems.

### Proposed Solution: Detailed Design
[Proposed Solution: Detailed Design]: #proposed-solution-detailed-design

For the most important pieces of our systems, the Data Pipeline and Data Server,
  RDS Aurora is a fully-compatible switch: all they need to change are their JDBC URLs.
For our infrastructure automation, RDS will be a major win, as we'll go from having to orchestrate an EC2 instance,
  operating system, _and_ PostgreSQL configuration to just asking AWS to, "give us an RDS instance, please."

Once ready, The deployment/transition to RDS Aurora should be simple and smooth:

1. Update our infrastructure playbooks to deploy and configure the desired RDS Aurora instances.
    * Should also include duplicate Data Pipeline and Data Server instances,
      connected to the new RDS Aurora instances.
2. Pause all ETL (i.e. the RIF export and the Data Pipeline service) in the environment being transitioned.
3. Fork the RIF export into a second S3 location, for the new Data Pipeline service.
4. Restore a current backup of the environment's DB to the primary RDS Aurora instance.
   Wait for the restore to complete and for it to replicate to all secondary instances.
5. Resume all ETL.
6. Verify that the infrastructure components are working as expected, using non-PII/PHI records.
    * This must include end-to-end integration testing.
    * This must include end-to end performance and load testing.
7. Slowly transition load balancer traffic away from the old Data Servers to the new ones.
8. Once all of the old Data Servers are out of service, decommission all of the old infrastructure.

Proceeding through the transition one environment at a time, in order of: TEST, DPR, then PROD.

### Proposed Solution: Unresolved Questions
[Proposed Solution: Unresolved Questions]: #proposed-solution-unresolved-questions

The following questions need to be resolved prior to merging this RFC:

1. Does our 60M random sample data set load as expected, in comparable or better amounts of time, as compared to our existing configuration?
2. Do our load tests achieve comparable or better levels of performance, as compared to our existing configuration?
3. Does database replication across AZs work as expected?
4. Does our disaster recovery process work as expected?
5. Are there any important differences in the PostgreSQL "vaccuum" process?
6. Given Aurora's different memeory/cache management strategies, do our analytical queries still run, or do they fail with memory errors?
7. Can we successfully drop and recreate all of our indexes?
8. Can we migrate _away_ from Aurora back to standard PostgreSQL -- just in case we ever need to?
9. Should we perform the actual switch during the migration to the CCS, or prior to it?

These issues can be prototyped and vetted first in a non-production environment, such as our existing `TEST` environment or the `hhsdevcloud` environment.

In addition, we may want to investigate other database architectures in the future, to see if any offer significant cost savings over RDS.

### Proposed Solution: Drawbacks
[Proposed Solution: Drawbacks]: #proposed-solution-drawbacks

One area of concern that has been raised is with RDS' space limitations.
Further research and analysis indicates that these will not be a concern:
  the maximum table size for Aurora PostgreSQL DB cluster is 32 TiB.
Right now, our largest table is less than 3 TB.
Furthermore, partitioning tables removes this limit (the limit applies to each of the partitions, of course).
Documentation on this limit is here:
  [Limits for Amazon RDS (Aurora)](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/CHAP_Limits.html).

The other major potential concern is pricing, which was addressed above.

It's worth noting that we have not heard of any RDS Aurora customers running a cluster as large as ours would be (in terms of storage).
However, we haven't heard of any specific PostgreSQL-on-EC2 users doing that, either.

### Proposed Solution: Notable Alternatives
[Proposed Solution: Notable Alternatives]: #proposed-solution-notable-alternatives

This is very specifically *not* being presented as the best-possible option for our database needs.
Instead, it's being presented as a low-cost incremental improvement.
Compared to our current PostgreSQL-on-EC2 architecture, moving to RDS Aurora will be a big win.
That's enough for now.

Many, many other possible database architectures exist and can be evaluated if our testing and verification
  of RDS Aurora indicate that it won't be "good enough" in the medium or long term.

This switch should really be made as soon as possible:

* The flexibility it provides will make research and testing of other architectural changes much simpler.
* Its cost improvements will give us much-needed "breathing room" over the course of 2020.
* Its reliability and "hands-off" management will lower our operators stress levels as usage ramps up dramatically over 2020.

## Prior Art
[Prior Art]: #prior-art

As mentioned above, our project already has a reasonable amount of experience with RDS,
  though with the "standard" PostgreSQL version, not Aurora:

* The "old sandbox" uses it.
* Our ETL benchmark tests use it.

Those experiences have both been very positive, which is a good sign.

Many other CMS and OEDA projects are also successfully using "standard" RDS PostgreSQL.

The internet has a wealth of reported experiences with RDS Aurora. Some selections:

* <https://www.reddit.com/r/aws/comments/9so3rt/will_aurora_postgresql_ever_be_anything_but_a/>
    * Critical of Arurora, but Largely doesn't apply to us, as the cost is fine and we can easily workaround the problems with major version upgrades.
* <https://www.linkedin.com/pulse/amazon-aurora-postgres-first-thoughts-eric-green/>
    * Critical of Aurora and has informed some of the testing we'll perform prior to migrating.
* <https://www.dbbest.com/blog/migrating-postgresql-to-amazon-rds-aurora/>
    * Very positive performance reviews for Aurora PostgreSQL.
    * Presents several useful strategies for migrating to Aurora PostgreSQL.

## Future Possibilities
[Future Possibilities]: #future-possibilities

No future possibilities are being seriously considered at this time.
That said, obvious areas for future investigation include other DB products and paradigms, such as Redshift.

## Addendums
[Addendums]: #addendums

The following addendums are required reading before voting on this proposal:

* [RFC postgresql-on-rds Addendum: AWS Pricing](https://docs.google.com/spreadsheets/d/1eeMVqy9xdctcJO80M1JGVqd6apUaza-hfKGtIKLhIdE/edit?usp=sharing)

Please note that some of these addendums may be encrypted. If you are unable to decrypt the files, you are not authorized to vote on this proposal.
