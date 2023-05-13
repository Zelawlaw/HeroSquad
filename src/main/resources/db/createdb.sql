


-- object: Squads | type: TABLE --
-- DROP TABLE IF EXISTS Squads CASCADE;
CREATE TABLE IF NOT EXISTS Squads (
	id serial NOT NULL,
	name varchar(100) NOT NULL,
	cause varchar(200),
	CONSTRAINT table_b_pk PRIMARY KEY (id)
);
-- ddl-end --
ALTER TABLE Squads OWNER TO root;
-- ddl-end --

-- object: Powers | type: TABLE --
-- DROP TABLE IF EXISTS Powers CASCADE;
CREATE TABLE IF NOT EXISTS Powers (
	id serial NOT NULL,
	name varchar(50),
	description varchar(200),
	CONSTRAINT table_c_pk PRIMARY KEY (id)
);

-- ddl-end --
ALTER TABLE Powers OWNER TO root;
-- ddl-end --

-- object: Weaknesses | type: TABLE --
-- DROP TABLE IF EXISTS Weaknesses CASCADE;
CREATE TABLE IF NOT EXISTS Weaknesses (
	id serial NOT NULL,
	name varchar(200),
	description varchar(50),
	CONSTRAINT table_d_pk PRIMARY KEY (id)
);

-- ddl-end --
ALTER TABLE Weaknesses OWNER TO root;
-- ddl-end --

-- object: Heros | type: TABLE --
-- DROP TABLE IF EXISTS Heros CASCADE;
CREATE TABLE IF NOT EXISTS Heros (
	id serial NOT NULL,
	name varchar(50),
	age integer,
	squadId integer,
	powerId integer,
	weaknessId integer,
	CONSTRAINT table_a_pk PRIMARY KEY (id),
	CONSTRAINT table_a_uq UNIQUE (name)
);
-- ddl-end --
ALTER TABLE Heros OWNER TO root;
-- ddl-end --

-- object: "PowerFK" | type: CONSTRAINT --
ALTER TABLE Heros DROP CONSTRAINT IF EXISTS PowerFK CASCADE;
ALTER TABLE Heros ADD CONSTRAINT PowerFK FOREIGN KEY (powerId)
REFERENCES Powers (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: "WeaknessFK" | type: CONSTRAINT --
 ALTER TABLE Heros DROP CONSTRAINT IF EXISTS WeaknessFK CASCADE;
ALTER TABLE Heros ADD CONSTRAINT WeaknessFK FOREIGN KEY (weaknessId)
REFERENCES Weaknesses (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --
