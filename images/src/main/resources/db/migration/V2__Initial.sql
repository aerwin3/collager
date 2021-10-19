
CREATE TABLE IF NOT EXISTS images (
    id uuid NOT NULL,
    account character varying(255) COLLATE pg_catalog."default" NOT NULL,
    label character varying(255) COLLATE pg_catalog."default" NOT NULL,
    objects character varying(255) COLLATE pg_catalog."default",
    url character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT images_pkey PRIMARY KEY (id)
);