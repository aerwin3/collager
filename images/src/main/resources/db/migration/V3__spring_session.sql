CREATE TABLE IF NOT EXISTS public.spring_session
(
    primary_id character(36) COLLATE pg_catalog."default" NOT NULL,
    session_id character(36) COLLATE pg_catalog."default" NOT NULL,
    creation_time bigint NOT NULL,
    last_access_time bigint NOT NULL,
    max_inactive_interval integer NOT NULL,
    expiry_time bigint NOT NULL,
    principal_name character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT spring_session_pk PRIMARY KEY (primary_id)
) TABLESPACE pg_default;

CREATE UNIQUE INDEX IF NOT EXISTS spring_session_ix1
    ON public.spring_session USING btree
        (session_id COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS spring_session_ix2
    ON public.spring_session USING btree
        (expiry_time ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS spring_session_ix3
    ON public.spring_session USING btree
        (principal_name COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

