CREATE TABLE CATCH_PHRASE (
    ID VARCHAR(100) PRIMARY KEY,
    IMAGE_PATH VARCHAR(500) NOT NULL
);

INSERT INTO CATCH_PHRASE (ID, IMAGE_PATH)
    VALUES ('TEST001', '/images/cp_test.jpg');

--//@UNDO

DROP TABLE CATCH_PHRASE;