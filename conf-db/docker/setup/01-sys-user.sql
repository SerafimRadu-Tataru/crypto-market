ALTER SESSION SET CONTAINER=PDB;
CREATE USER crypto_market IDENTIFIED BY crypto_market;
GRANT CREATE SESSION TO crypto_market;
GRANT CREATE TABLE, CREATE VIEW, CREATE SEQUENCE, CREATE SYNONYM TO crypto_market;
GRANT CREATE PROCEDURE TO crypto_market;
GRANT CREATE TRIGGER TO crypto_market;
GRANT UNLIMITED TABLESPACE TO crypto_market;