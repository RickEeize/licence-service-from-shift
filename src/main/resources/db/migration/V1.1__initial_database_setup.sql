create table customers
(
id bigserial not null
constraint pk_user
primary key,
type varchar(50) not null,
name varchar(50) not null,
registration_date date not null
);

create table licences
(
id uuid not null
constraint pk_licences
primary key,
create_date date not null,
end_date date not null,
private_key varchar(8000) not null,
customer_id bigint not null
constraint fk_40
references customers,
type varchar(255) not null,
number_of_licences bigint,
licence_key varchar(8000),
product_type varchar(255),
product_version varchar(255)
);

create table contacts
(
id bigserial not null
constraint pk_emails
primary key,
email varchar(50) not null,
customer_id bigint not null
constraint fk_43
references customers
);

insert into customers( type, name, registration_date) values
    ('company', 'FTC', '2020-12-12'),
    ('user', 'Vitya', '2020-01-20'),
    ('company', 'NSU', '2019-08-01') ;

