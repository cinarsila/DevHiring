CREATE TABLE product
(
    content_id bigint NOT NULL,
    brand_name character varying COLLATE pg_catalog."default",
    boutique_id character varying COLLATE pg_catalog."default",
    name character varying COLLATE pg_catalog."default",
    merchant_id character varying COLLATE pg_catalog."default",
    CONSTRAINT product_pkey PRIMARY KEY (content_id)
)
TABLESPACE pg_default;

CREATE TABLE link_collection
(
    id bigint NOT NULL,
    url character varying(255) COLLATE pg_catalog."default",
    deep_link character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT link_collection_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;