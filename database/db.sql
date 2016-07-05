drop schema if exists goos cascade;
create schema goos;

-- question

drop table if exists goos.question;
create table goos.question (
	id		serial primary key,
	name		text,
	description 	text
);

-- paper

drop table if exists goos.paper;
create table goos.paper (
	id		serial primary key,
	title		text,
	description 	text
);

-- parent

drop table if exists goos.parent;
create table goos.parent (
	id		serial primary key,
	name		text
);

-- network

drop table if exists goos.network;
create table goos.network (
	id		serial primary key,
	name		text,
	description 	text,
	funding		text,
	ongoing		boolean
);

-- eov

drop table if exists goos.eov;
create table goos.eov (
	id		serial primary key,
	name		text,
	parent_id 	integer references goos.parent
);

-- eov_question

drop table if exists goos.eov_question;
create table goos.eov_question (
	id		serial primary key,
	eov_id		integer	references goos.eov on delete cascade,
	question_id	integer references goos.question on delete cascade
);

-- eov_paper

drop table if exists goos.eov_paper;
create table goos.eov_paper (
	id		serial primary key,
	eov_id		integer	references goos.eov,
	paper_id	integer references goos.paper
);

-- activity

drop table if exists goos.activity;
create table goos.activity (
	id		serial primary key,
	network_id	integer references goos.network on delete cascade,
	eov_id		integer references goos.eov on delete cascade,
	coverage	geometry,
	date_start	date,
	date_end	date,
	timescale	text
);

-- requirement

drop table if exists goos.requirement;
create table goos.requirement (
	id		serial primary key,
	name		text,
	type		text
);

-- requirement_eov

drop table if exists goos.requirement_eov;
create table goos.requirement_eov (
	id		serial primary key,
	requirement_id	integer	references goos.requirement,
	eov_id		integer references goos.eov
);

-- expert

drop table if exists goos.expert;
create table goos.expert (
	id		serial primary key,
	name		text,
	oceanexpertid	integer
);

-- tool

drop table if exists goos.tool;
create table goos.tool (
	id		serial primary key,
	name		text,
	description	text,
	type		text,
	automated	boolean
);

-- datasystem

drop table if exists goos.datasystem;
create table goos.datasystem (
	id		serial primary key,
	name		text,
	description	text,
	type		text,
	openaccess	boolean,
	funding		text
);

-- aichitarget

drop table if exists goos.aichitarget;
create table goos.aichitarget (
	id		serial primary key,
	name		text,
	description	text
);

-- dataproduct

drop table if exists goos.dataproduct;
create table goos.dataproduct (
	id		serial primary key,
	name		text,
	description	text,
	type		text,
	usergroup	text,
	aichitarget_id	integer references goos.aichitarget
);

-- activity_expert

drop table if exists goos.activity_expert;
create table goos.activity_expert (
	id		serial primary key,
	activity_id	integer	references goos.activity on delete cascade,
	expert_id	integer references goos.expert on delete cascade
);

-- activity_tool

drop table if exists goos.activity_tool;
create table goos.activity_tool (
	id		serial primary key,
	activity_id	integer	references goos.activity on delete cascade,
	tool_id		integer references goos.tool on delete cascade
);

-- activity_datasystem

drop table if exists goos.activity_datasystem;
create table goos.activity_datasystem (
	id		serial primary key,
	activity_id	integer	references goos.activity on delete cascade,
	datasystem_id	integer references goos.datasystem on delete cascade
);

-- activity_dataproduct

drop table if exists goos.activity_dataproduct;
create table goos.activity_dataproduct (
	id		serial primary key,
	activity_id	integer	references goos.activity on delete cascade,
	dataproduct_id	integer references goos.dataproduct on delete cascade
);


